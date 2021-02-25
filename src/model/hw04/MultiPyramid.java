package cs3500.pyramidsolitaire.model.hw04;

import cs3500.pyramidsolitaire.model.hw02.BasicPyramid;
import cs3500.pyramidsolitaire.model.hw02.Card;
import java.util.List;

/**
 * A multi-pyramid to be used by a MultiPyramidSolitaire model. This MP has 3 pyramids which overlap
 * for half their height, rounding up. The spaces left by overlap are initialized as empty cards.
 */
public class MultiPyramid extends BasicPyramid {

  /**
   * Constructs a MultiPyramid with the given number of rows.
   *
   * @param numRows the number of rows in the pyramid
   */
  public MultiPyramid(int numRows) {
    super(numRows);
  }

  /**
   * Deals cards in the manner specified by {@code PyramidSolitaireModel}, and adds spaces in
   * between the pyramids in the top half's rows.
   *
   * @param deck the deck to use to deal cards
   * @return the stock to start off the game with, after dealing the draw cards
   */
  @Override
  public List<Card> dealCards(List<Card> deck) {

    // Add the empty rows to the pyramid.
    for (int rowIndex = 0; rowIndex < this.numRows; rowIndex++) {
      // In the 2nd row (row index 1), there are 2 cards (col indexes 0 and 1).
      Card[] row = new Card[this.getRowWidth(rowIndex)];
      this.pyramid.add(row);
    }

    int spacedRows = Math.max(0, this.numRows / 2 - 1); // Number of rows with spaces
    int skip = this.numRows / 2; // Number of indexes to skip by in each row with spaces

    // Add cards to the rows with spaces (each row: 3 x rowIndex + 1 cards)
    for (int sRowIndex = 0; sRowIndex < spacedRows; sRowIndex++) {
      // Iterate through each of the 3 pyramids
      for (int sColIndex = 0; sColIndex < this.getRowWidth(sRowIndex); sColIndex += skip) {
        // Iterate through each of the spots in the pyramid's row
        for (int subColIndex = sColIndex; subColIndex < sRowIndex + sColIndex + 1; subColIndex++) {
          this.pyramid.get(sRowIndex)[subColIndex] = deck.remove(0);
        }
      }
    }

    // Add the cards to the rows without spaces.
    for (int rowIndex = spacedRows; rowIndex < this.numRows; rowIndex++) {
      // Remove the play cards from the stock and deal to this row.
      for (int colIndex = 0; colIndex < this.getRowWidth(rowIndex); colIndex++) {
        this.pyramid.get(rowIndex)[colIndex] = deck.remove(0);
      }
    }

    return deck;
  }

  @Override
  public int getRowWidth(int row) throws IllegalArgumentException {
    // Return width if valid index, else throw exception.
    if (row > -1 && row < this.numRows) {
      // If 2 rows, first row has 3 cards, not 2.
      if (this.numRows == 2) {
        return row + MultiPyramidSolitaire.NUM_PYR;
      }
      // Otherwise row + numRows; if even rows add 1
      return row + this.numRows + ((this.numRows % 2 == 0) ? 1 : 0);
    } else {
      throw new IllegalArgumentException("The given row is invalid.");
    }
  }
}