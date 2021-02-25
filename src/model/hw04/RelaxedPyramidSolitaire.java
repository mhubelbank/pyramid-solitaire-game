package cs3500.pyramidsolitaire.model.hw04;

import cs3500.pyramidsolitaire.model.hw02.BasicPyramidSolitaire;
import cs3500.pyramidsolitaire.model.hw02.Card;
import java.util.List;

/**
 * A model for playing a game of relaxed pyramid solitaire. Maintains the state and enforces the
 * rules of gameplay, and uses the Card class to represent 52 playing cards. It is a "relaxed"
 * version of basic pyramid solitaire, as it permits a remove-two if one card uncovers the other.
 */
public class RelaxedPyramidSolitaire extends BasicPyramidSolitaire {

  /**
   * Constructs a RelaxedPyramidSolitaire model.
   */
  public RelaxedPyramidSolitaire() {
    super();
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numRows, int numDraw)
      throws IllegalArgumentException {
    this.util.checkPyramidParams(deck, this.getDeck(), numRows, 1, numDraw);

    List<Card> copyDeck = this.getInitDeck(deck, shuffle);

    this.initializeGameAttributes(copyDeck, numDraw, new RelaxedPyramid(numRows));
  }

  /**
   * Determines if the game is over by the strict and relaxed rules.
   *
   * @return true iff the game is over by both sets of rules, false otherwise
   * @throws IllegalStateException if the game hasn't been started yet
   */
  @Override
  public boolean isGameOver() throws IllegalStateException {
    // Is the game over using stricter BasicPyramidSolitaire rules? (also throw error if needed)
    boolean overStrict = super.isGameOver();

    // If not over then we don't need to look for minis; pyramids of size 1 can't have minis.
    if (!overStrict || this.getNumRows() < 2) {
      return overStrict;
    }

    // Iterate over every mini-pyramid (size 3) and check exposed for top-bot1 and top-bot2 pairs
    for (int rowIndex = 0; rowIndex < this.getNumRows() - 1; rowIndex++) {
      for (int colIndex = 0; colIndex < this.getRowWidth(rowIndex); colIndex++) {
        if (this.pairRelaxed(rowIndex, colIndex, colIndex) || this
            .pairRelaxed(rowIndex, colIndex, colIndex + 1)) {
          return false;
        }
      }
    }

    // If we reach here, then there is no relaxed-removable pair and the game is strictly over.
    return true;
  }

  /**
   * A helper to determine if the given top-bottom mini-pyramid pair is relaxed-removable. Both
   * cards must be non-null, relaxed-exposed, and sum to 13.
   *
   * @param rowTop the row index of the top card
   * @param colTop the column index of the top card
   * @param colBot the column index of the bottom card
   * @return whether the pair can be removed together
   */
  private boolean pairRelaxed(int rowTop, int colTop, int colBot) {
    Card top = this.getCardAt(rowTop, colTop);
    Card bot = this.getCardAt(rowTop + 1, colBot);

    return (top != null && bot != null
        && this.pyramid.cardsExposed(rowTop, colTop, rowTop + 1, colBot)
        && top.getVal() + bot.getVal() == 13);
  }
}