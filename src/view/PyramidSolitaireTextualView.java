package cs3500.pyramidsolitaire.view;

import cs3500.pyramidsolitaire.model.hw02.PyramidSolitaireModel;
import java.io.IOException;
import java.util.List;

/**
 * To render a pyramid solitaire game.
 */
public class PyramidSolitaireTextualView implements PyramidSolitaireView {

  private final PyramidSolitaireModel<?> model;
  private final Appendable ap;
  private static final String EMPTY_CARD = ".  ";

  /**
   * Constructs a textual view of a Pyramid Solitaire game, given only a model instance.
   *
   * @param model the pyramid solitaire model to render
   */
  public PyramidSolitaireTextualView(PyramidSolitaireModel<?> model) {
    this.model = model;
    this.ap = null;
  }

  /**
   * Constructs a textual view of a Pyramid Solitaire game, given both a model instance and an
   * Appendable instance on which to add the current textual output.
   *
   * @param model the pyramid solitaire model to render
   * @param ap    the Appendable object to append onto
   */
  public PyramidSolitaireTextualView(PyramidSolitaireModel<?> model, Appendable ap) {
    this.model = model;
    this.ap = ap;
  }

  @Override
  public String toString() {
    if (model.getNumRows() == -1) { // Pregame
      return "";
    } else if (model.getScore() == 0) { // Won
      return "You win!";
    } else if (model.isGameOver()) { // Game over
      return "Game over. Score: " + model.getScore();
    } else { // In-game
      String pyramid = "";
      // Render each row in the pyramid.
      for (int rowIndex = 0; rowIndex < model.getNumRows(); rowIndex++) {
        pyramid += this.renderRow(rowIndex) + "\n";
      }
      // Render the draw pile.
      pyramid += this.renderDraw();
      return pyramid;
    }
  }

  /**
   * Render the given pyramid row as a string with spaces and card representations.
   *
   * @param rowIndex the row of the pyramid
   * @return the string representation of the given row
   */
  private String renderRow(int rowIndex) {
    String row = "";

    // Add the spaces to the beginning of the row.
    for (int spaceNum = 1; spaceNum < model.getNumRows() - rowIndex; spaceNum++) {
      row += "  ";
    }

    int nonNulls = 0;

    // Add the first card to the row.
    Object firstCard = model.getCardAt(rowIndex, 0);
    row += this.renderCard(firstCard, true);
    nonNulls += (firstCard != null) ? 1 : 0;

    // Add the rest of the cards to the row.
    for (int colIndex = 1; colIndex < model.getRowWidth(rowIndex); colIndex++) {
      Object card = model.getCardAt(rowIndex, colIndex);
      row += " " + this.renderCard(card, true);
      nonNulls += (card != null) ? 1 : 0;
    }

    // Remove trailing space.
    row = row.stripTrailing();

    return (nonNulls > 0) ? row : "";
  }

  /**
   * Renders the draw pile as a string.
   *
   * @return the string representation of the draw pile
   */
  private String renderDraw() {
    List<?> drawPile = model.getDrawCards();
    String draw = "Draw:";
    int nonNulls = 0;
    String cards = ""; // The cards in this row, or periods/spaces if none.

    // If no cards, return "Draw:" only.
    if (model.getNumDraw() == 0) {
      return draw;
    }

    // Add the first card to the draw pile.
    Object firstCard = drawPile.get(0);
    cards += this.renderCard(firstCard, false);
    nonNulls += (firstCard == null) ? 0 : 1;

    // Iterate over each subsequent card in the draw pile.
    for (int drawIndex = 1; drawIndex < model.getNumDraw(); drawIndex++) {
      cards += ", "; // Leading comma and space
      Object drawCard = drawPile.get(drawIndex);
      if (drawCard == null) {
        cards += EMPTY_CARD;
      } else {
        cards += this.renderCard(drawCard, false);
        nonNulls += 1;
      }
    }

    // Only add the card-list component if there is at least one card in the draw pile.
    if (nonNulls > 0) {
      draw += " " + cards;
    }

    // Remove trailing space.
    draw = draw.stripTrailing();

    return draw;
  }

  /**
   * Renders the given card as a string.
   *
   * @param card the card to represent
   * @return the string representation of the card
   */
  private String renderCard(Object card, boolean pad) {
    if (card == null) {
      return EMPTY_CARD;
    } else {
      String cardString = card.toString();
      // If the value of the card isn't two characters long, pad it with a space if needed.
      if (pad && cardString.length() < 3) {
        cardString += " ";
      }
      return cardString;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PyramidSolitaireTextualView)) {
      return false;
    }
    PyramidSolitaireTextualView that = (PyramidSolitaireTextualView) o;
    return this.toString().equals(that.toString());
  }

  @Override
  public int hashCode() {
    return this.toString().hashCode();
  }

  @Override
  public void render() throws IOException {
    this.ap.append(this.toString());
  }
}