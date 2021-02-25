package cs3500.pyramidsolitaire.model.hw02;

import java.util.List;

/**
 * A pyramid of cards to be used by a pyramid solitaire model. Provides a wrapper for the 2D pyramid
 * structure, as well as various necessary operations for the pyramid.
 */
public interface Pyramid<K> {

  /**
   * Deals the given (valid) deck of cards to the pyramid based on its own internal structure.
   *
   * @param deck the valid deck of cards to deal
   * @return the deck of cards post-deal; this will be used as the initial stock in the game
   */
  List<K> dealCards(List<K> deck);

  /**
   * Return the current score, which is the sum of the values of the cards remaining in the
   * pyramid.
   *
   * @return the score
   */
  int getScore();

  /**
   * Returns the number of rows originally in the pyramid, or -1 if the game hasn't been started.
   *
   * @return the height of the pyramid, or -1
   */
  int getNumRows();

  /**
   * Returns the width of the requested row, measured from the leftmost card to the rightmost card
   * (inclusive) as the game is initially dealt.
   *
   * @param row the desired row (0-indexed)
   * @return the number of spaces needed to deal out that row
   * @throws IllegalArgumentException if the row is invalid
   */
  int getRowWidth(int row);

  /**
   * Returns the card at the specified coordinates.
   *
   * @param row  row of the desired card (0-indexed from the top)
   * @param card column of the desired card (0-indexed from the left)
   * @return the card at the given position, or <code>null</code> if no card is there
   */
  K getCardAt(int row, int card);

  /**
   * Determines if the card at the given row and card column position is exposed.
   *
   * @param row  the row to access
   * @param card the card column to access
   * @return whether the given card is exposed
   */
  boolean cardExposed(int row, int card);

  /**
   * Determines if the cards at the given row and card column positions are exposed.
   *
   * @param row1  the row to access for the first card
   * @param card1 the card column to access for the first card
   * @param row2  the row to access for the second card
   * @param card2 the card column to access for the second card
   * @return whether the two given cards are exposed
   */
  boolean cardsExposed(int row1, int card1, int row2, int card2);

  /**
   * Removes the card at a given spot from the pyramid. This is only called after state and null
   * checks have been performed.
   *
   * @param row  the row to access
   * @param card the card column to access
   */
  void discard(int row, int card);
}