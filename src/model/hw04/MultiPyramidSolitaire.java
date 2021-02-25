package cs3500.pyramidsolitaire.model.hw04;

import cs3500.pyramidsolitaire.model.hw02.BasicPyramidSolitaire;
import cs3500.pyramidsolitaire.model.hw02.Card;
import java.util.ArrayList;
import java.util.List;

/**
 * A model for playing a game of multi-pyramid solitaire that maintains the state and enforces the
 * rules of gameplay. Uses the Card class to represent 104 playing cards (2 of each). This model
 * uses exactly 3 pyramids, which overlap for half their height, rounding up.
 */
public class MultiPyramidSolitaire extends BasicPyramidSolitaire {

  protected static final int NUM_PYR = 3;

  /**
   * Constructs a MultiPyramidSolitaire model.
   */
  public MultiPyramidSolitaire() {
    super();
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numRows, int numDraw)
      throws IllegalArgumentException {
    this.util.checkPyramidParams(deck, this.getDeck(), numRows, NUM_PYR, numDraw);

    List<Card> copyDeck = this.getInitDeck(deck, shuffle);

    super.initializeGameAttributes(copyDeck, numDraw, new MultiPyramid(numRows));
  }

  /**
   * Doubles the standard 52-card deck for the 3-pyramid game.
   *
   * @return the 104-card deck to use in this game
   */
  @Override
  public List<Card> getDeck() {
    List<Card> deck = new ArrayList<Card>();
    // Add the basic game's deck, twice.
    List<Card> halfDeck = super.getDeck();
    deck.addAll(halfDeck);
    deck.addAll(halfDeck.size(), halfDeck); // Add the other half at the halfway point.
    return deck;
  }
}