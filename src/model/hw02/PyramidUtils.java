package cs3500.pyramidsolitaire.model.hw02;

import java.util.Collections;
import java.util.List;

/**
 * A utility class for the PyramidSolitaireModel and Pyramid implementations. Provides various
 * utility methods for parameter and state checks.
 *
 * @param <K> the type of cards the relevant game of pyramid solitaire uses
 */
public class PyramidUtils<K> {

  /**
   * Determines if a valid pyramid game can be started with the given parameters.
   *
   * @param deck        the user-given deck of cards for the given game
   * @param perfectDeck the perfect deck of cards for the given game
   * @param numRows     the number of rows in the pyramid
   * @param numPyr      the number of sub-pyramids in the pyramid
   * @param numDraw     the number of cards in the draw pile
   * @throws IllegalArgumentException if any of the parameters are invalid
   */
  public void checkPyramidParams(List<K> deck, List<K> perfectDeck, int numRows,
      int numPyr,
      int numDraw) throws IllegalArgumentException {
    // numRows must be positive, numDraw must be non-negative, deck must be non-null.
    if (numRows < 1 || numDraw < 0 || deck == null) {
      throw new IllegalArgumentException("Illegal game parameters entered.");
    }

    // Cards cannot be null.
    for (K card : deck) {
      if (card == null) {
        throw new IllegalArgumentException("Cards cannot have null values.");
      }
    }

    // Deck must have the right number of cards.
    if (deck.size() != perfectDeck.size()) {
      throw new IllegalArgumentException("Deck is not the right size.");
    }

    // Does the deck contain all the same cards as this implementation's getDeck?
    for (K card : perfectDeck) {
      if (Collections.frequency(deck, card) != Collections.frequency(perfectDeck, card)) {
        throw new IllegalArgumentException("Deck has improper card distribution.");
      }
    }

    // Can a full pyramid and draw pile be dealt? (Use sum formula for first n digits)

    // Cards in row 0 + blanks in row 0
    int firstWidth = numPyr + ((numPyr - 1) * (numRows / 2 - 1));

    // Basic 7R: sum(7) - sum(0) - sum(0)
    // Multi 3P7R: sum(13) - sum(6) - 2*sum(2) <- blanks (inverted pyramid)
    int pyrSize = this.sum(numRows + firstWidth - 1) - this.sum(firstWidth - 1) - 2 * this
        .sum((firstWidth - numPyr) / 2);

    if (deck.size() < pyrSize + numDraw) {
      throw new IllegalArgumentException(
          "Full pyramid and draw pile cannot be dealt with the given deck.");
    }
  }

  /**
   * Sums the first n natural numbers.
   *
   * @param n the number of natural numbers to sum (1, 2, ..., n)
   * @return the sum of the first n natural numbers
   */
  private int sum(int n) {
    return (n * (n + 1)) / 2;
  }

  /**
   * Checks that a card is non-null.
   *
   * @param card the card to check
   * @return the card
   * @throws IllegalArgumentException if the card is null
   */
  public K checkNull(K card) throws IllegalArgumentException {
    if (card == null) {
      throw new IllegalArgumentException("There's no card here.");
    }
    return card;
  }

  /**
   * Checks if an illegal request has been made during pregame.
   *
   * @param arg the message to show, specific to the error
   * @throws IllegalStateException if in pregame
   */
  public void checkPregame(GameState gs, String arg) throws IllegalStateException {
    // If in pregame, throw exception.
    if (gs == GameState.PREGAME) {
      throw new IllegalStateException(arg + ", because the game hasn't started.");
    }
  }

  /**
   * Checks if an illegal request has been made during post-game.
   *
   * @param arg the message to show, specific to the error
   * @throws IllegalStateException if in game-over
   */
  public void checkGameOver(GameState gs, String arg) throws IllegalStateException {
    // If in game-over, throw exception.
    if (gs == GameState.OVER) {
      throw new IllegalStateException(arg + ", because the game is over.");
    }
  }
}