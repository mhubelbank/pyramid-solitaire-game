package cs3500.pyramidsolitaire.model.hw04;

import cs3500.pyramidsolitaire.model.hw02.BasicPyramid;
import cs3500.pyramidsolitaire.model.hw02.Card;

/**
 * A relaxed pyramid to be used by a RelaxedPyramidSolitaire model. This pyramid permits a
 * remove-two if one card uncovers the other.
 */
public class RelaxedPyramid extends BasicPyramid {

  /**
   * Constructs a RelaxedPyramid with the given number of rows.
   *
   * @param numRows the number of rows in the pyramid
   */
  public RelaxedPyramid(int numRows) {
    super(numRows);
  }

  /**
   * Determines if the two given cards are exposed by the "relaxed" definition; that is, whether one
   * given card is the only card covering the other given card.
   *
   * @param row1  the row of the first card
   * @param card1 the column of the first card
   * @param row2  the row of the second card
   * @param card2 the column of the second card
   * @return whether the two cards are exposed by the strict or relaxed rules
   */
  @Override
  public boolean cardsExposed(int row1, int card1, int row2, int card2) {
    // Are the two cards exposed by the stricter definition?
    boolean strictExposed = super.cardsExposed(row1, card1, row2, card2);
    if (strictExposed) {
      return true;
    }

    int rowDiff = row1 - row2;
    int cardDiff = card1 - card2;

    // False if the two cards are not exactly one row or more than 1 column apart.
    if (Math.abs(rowDiff) != 1 || Math.abs(cardDiff) > 1) {
      return false;
    }

    // We have 2/3 cards of a mini-pyramid; get the third card.
    // Row 1 on top ? Other row = row2 : Other row = row1
    int otherRow = (rowDiff < 0) ? row2 : row1;
    // Given bottom card on left ? Other card on right : Other card on left
    int otherCol = (cardDiff == 0) ? card1 + 1 : Math.min(card1, card2);

    // Get the last card on the bottom row (can be on left or right).
    Card otherCard = this.getCardAt(otherRow, otherCol);

    // If it's null, then the given cards can be removed together.
    return otherCard == null;
  }
}