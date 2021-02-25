package cs3500.pyramidsolitaire.model.hw02;

import java.util.ArrayList;
import java.util.List;

/**
 * A basic pyramid to be used by a BasicPyramidSolitaire model. Contains 52 playing cards.
 */
public class BasicPyramid implements Pyramid<Card> {

  protected final int numRows;
  protected final List<Card[]> pyramid;

  /**
   * Constructs a BasicPyramid with the given number of rows.
   *
   * @param numRows the number of rows in the pyramid
   */
  public BasicPyramid(int numRows) {
    this.numRows = numRows;
    this.pyramid = new ArrayList<Card[]>(this.numRows);
  }

  @Override
  public List<Card> dealCards(List<Card> deck) {
    // A pyramid with 7 rows has row indexes 0-6.
    for (int rowIndex = 0; rowIndex < this.numRows; rowIndex++) {
      // In the 2nd row (row index 1), there are 2 cards (col indexes 0 and 1).
      Card[] row = new Card[this.getRowWidth(rowIndex)];

      // Remove the play cards from the stock and deal to this row.
      for (int colIndex = 0; colIndex < this.getRowWidth(rowIndex); colIndex++) {
        row[colIndex] = deck.remove(0);
      }

      // Add this row to the pyramid.
      this.pyramid.add(row);
    }
    return deck;
  }

  @Override
  public int getScore() {
    int score = 0;
    // Sum the pyramid card values.
    for (Card[] row : this.pyramid) {
      for (Card pyrCard : row) {
        if (pyrCard != null) {
          score += pyrCard.getVal();
        }
      }
    }
    return score;
  }

  @Override
  public int getNumRows() {
    return this.numRows;
  }

  @Override
  public Card getCardAt(int row, int card) {
    if (row < 0 || row > this.numRows - 1) {
      throw new IllegalArgumentException("Invalid row.");
    }

    if (card < 0 || card > this.getRowWidth(row) - 1) {
      throw new IllegalArgumentException("Invalid card column.");
    }

    return this.pyramid.get(row)[card]; // No exceptions; get card (even if null).
  }

  @Override
  public boolean cardExposed(int row, int card) {
    // The bottommost row is always exposed, so we only check the others.
    if (row < this.numRows - 1) {

      // Get card directly below and one below, one to the right.
      Card thisCardCoverLeft = this.getCardAt(row + 1, card);
      Card thisCardCoverRight = this.getCardAt(row + 1, card + 1);

      // Both card spots must be null for this card to be "exposed".
      if (thisCardCoverLeft != null || thisCardCoverRight != null) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean cardsExposed(int row1, int card1, int row2, int card2) {
    return this.cardExposed(row1, card1) && this.cardExposed(row2, card2);
  }

  @Override
  public void discard(int row, int card) {
    this.pyramid.get(row)[card] = null;
  }

  @Override
  public int getRowWidth(int row) throws IllegalArgumentException {
    // Return width if valid index, else throw exception.
    if (row > -1 && row < this.numRows) {
      return row + 1; // Row 0 has 1 card
    } else {
      throw new IllegalArgumentException("The given row is invalid.");
    }
  }
}