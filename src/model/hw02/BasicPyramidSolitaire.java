package cs3500.pyramidsolitaire.model.hw02;

import cs3500.pyramidsolitaire.view.PyramidSolitaireTextualView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * A basic model for playing a game of pyramid solitaire that maintains the state and enforces the
 * rules of gameplay. Uses the Card class to represent 52 playing cards.
 */
public class BasicPyramidSolitaire implements PyramidSolitaireModel<Card> {

  protected GameState gameState;
  protected BasicPyramid pyramid;
  private List<Card> stock;
  private Card[] drawCards;
  private int numDraw;
  protected PyramidUtils<Card> util;

  public static final int MOVE_VAL = 13;

  /**
   * Constructs a basic pyramid solitaire game in pre-game state.
   */
  public BasicPyramidSolitaire() {
    gameState = GameState.PREGAME;
    this.numDraw = -1;
    util = new PyramidUtils<Card>();
  }

  // ---------------------------- GAME START ----------------------------

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numRows, int numDraw)
      throws IllegalArgumentException {
    this.util.checkPyramidParams(deck, this.getDeck(), numRows, 1, numDraw);

    List<Card> copyDeck = this.getInitDeck(deck, shuffle);

    this.initializeGameAttributes(copyDeck, numDraw, new BasicPyramid(numRows));
  }

  /**
   * Makes a copy of the given deck to be used in this game, and shuffles it if needed.
   *
   * @param deck    the user-given deck to copy and maybe shuffle
   * @param shuffle whether to shuffle the deck
   * @return the deck to use in this game
   */
  protected List<Card> getInitDeck(List<Card> deck, boolean shuffle) {
    // Make a copy of the given deck.
    List<Card> copyDeck = new ArrayList<>();
    copyDeck.addAll(deck);

    // The entire deck is in the stock to start. Shuffle deck if needed.
    if (shuffle) {
      Collections.shuffle(copyDeck);
    }

    return copyDeck;
  }

  /**
   * Initializes this BPS game's fields when the game is started, and deals the cards.
   *
   * @param copyDeck the starting stock card pile (whole deck)
   * @param numDraw  the number of draw cards available at a time
   * @param pyramid  the initialized pyramid to use in this game
   */
  protected void initializeGameAttributes(List<Card> copyDeck, int numDraw,
      BasicPyramid pyramid) {
    this.gameState = GameState.INGAME;
    this.pyramid = pyramid;
    this.numDraw = numDraw;
    this.drawCards = new Card[this.numDraw];
    this.stock = this.pyramid.dealCards(copyDeck);
    this.dealDeck();
  }

  /**
   * Initializes the draw pile. All draw cards are non-null to start.
   */
  protected void dealDeck() {
    for (int drawIndex = 0; drawIndex < this.numDraw; drawIndex++) {
      Card drawCard = this.stock.remove(0);
      this.drawCards[drawIndex] = drawCard;
    }
  }

  // ------------------------------ MOVES ------------------------------

  @Override
  public void remove(int row1, int card1, int row2, int card2)
      throws IllegalArgumentException, IllegalStateException {
    this.util.checkPregame(this.gameState, "Cannot remove anything yet");
    this.util.checkGameOver(this.gameState, "Cannot remove anything");

    // Get the pyramid cards and check that they're non-null.
    Card thisCard = this.util.checkNull(getCardAt(row1, card1));
    Card thatCard = this.util.checkNull(getCardAt(row2, card2));

    // Make sure both cards are exposed (can vary based on the strictness of the pyramid).
    if (!this.pyramid.cardsExposed(row1, card1, row2, card2)) {
      throw new IllegalArgumentException("Cards must be exposed.");
    }

    // Check that this is a valid move (must sum to 13, in this case).
    if (thisCard.getVal() + thatCard.getVal() != MOVE_VAL) {
      throw new IllegalArgumentException("Cards must sum to 13.");
    }

    // Discard the pyramid cards.
    this.pyramid.discard(row1, card1);
    this.pyramid.discard(row2, card2);

    this.postMove();
  }

  @Override
  public void remove(int row, int card) throws IllegalArgumentException, IllegalStateException {
    this.util.checkPregame(this.gameState, "Cannot remove anything yet");
    this.util.checkGameOver(this.gameState, "Cannot remove anything");

    // Get the pyramid card and check that it's non-null.
    Card thisCard = this.util.checkNull(getCardAt(row, card));

    // Make sure this pyramid card is exposed.
    if (!this.pyramid.cardExposed(row, card)) {
      throw new IllegalArgumentException("Card must be exposed.");
    }

    // To remove only one card, its value must equal 13 (King).
    if (thisCard.getVal() != MOVE_VAL) {
      throw new IllegalArgumentException("Card must have value 13.");
    }

    // Discard the pyramid card.
    this.pyramid.discard(row, card);

    this.postMove();
  }

  @Override
  public void removeUsingDraw(int drawIndex, int row, int card)
      throws IllegalArgumentException, IllegalStateException {
    this.util.checkPregame(this.gameState, "Cannot remove anything yet");
    this.util.checkGameOver(this.gameState, "Cannot remove anything");

    if (this.numDraw == 0) {
      throw new IllegalArgumentException("There's nothing in the draw pile.");
    }

    // Get the draw card and pyramid card and check if they're null.
    Card drawnCard = this.util.checkNull(getDrawAt(drawIndex));
    Card thisCard = this.util.checkNull(getCardAt(row, card));

    // Make sure the pyramid card is exposed.
    if (!this.pyramid.cardExposed(row, card)) {
      throw new IllegalArgumentException("Card must be exposed.");
    }

    // Check that this is a valid move (must sum to 13, in this case).
    if (thisCard.getVal() + drawnCard.getVal() != MOVE_VAL) {
      throw new IllegalArgumentException("Cards must sum to 13.");
    }

    // Discard the pyramid card and draw card.
    this.pyramid.discard(row, card);
    this.discard(drawIndex);

    this.postMove();
  }

  @Override
  public void discardDraw(int drawIndex) throws IllegalArgumentException, IllegalStateException {
    this.util.checkPregame(this.gameState, "Cannot discard yet");
    this.util.checkGameOver(this.gameState, "Cannot discard");

    if (this.numDraw == 0) {
      throw new IllegalArgumentException("There's nothing in the draw pile.");
    }

    // Get the draw card and check that it's non-null.
    this.util.checkNull(getDrawAt(drawIndex));

    // Discard the draw card.
    this.discard(drawIndex);

    this.postMove();
  }

  // --------------------------- MOVE HELPERS ---------------------------

  /**
   * Replaces the draw card at the given index with the next stock card/null if stock is empty.
   *
   * @param index the draw card to remove
   */
  private void discard(int index) {
    // Replace the draw card if possible.
    if (stock.size() != 0) {
      Card newDrawCard = this.stock.remove(0);
      this.drawCards[index] = newDrawCard;
    } else { // Set its position to null.
      this.drawCards[index] = null;
    }
  }

  /**
   * Checks if the game is over and, if so, updates the game state accordingly.
   */
  private void postMove() {
    if (this.isGameOver()) {
      this.gameState = GameState.OVER;
    }
  }

  // ----------------------------- GETTERS -----------------------------

  @Override
  public List<Card> getDeck() {
    List<Card> deck = new ArrayList<Card>();

    // Iterate over the 52 combinations of 4 Suits x 13 Values
    for (Suit suit : Suit.values()) {
      for (Value value : Value.values()) {
        deck.add(new Card(suit, value));
      }
    }

    ArrayList<Card> copy = new ArrayList<>();
    copy.addAll(deck);
    return copy;
  }

  @Override
  public int getNumRows() {
    // -1 if startGame not called yet, else ask pyramid
    return (this.gameState == GameState.PREGAME) ? -1 : this.pyramid.getNumRows();
  }

  @Override
  public int getNumDraw() {
    return this.numDraw; // -1 if startGame not called yet
  }

  @Override
  public int getRowWidth(int row) throws IllegalArgumentException, IllegalStateException {
    this.util.checkPregame(this.gameState, "There are no rows yet");
    return this.pyramid.getRowWidth(row);
  }

  @Override
  public int getScore() throws IllegalStateException {
    this.util.checkPregame(this.gameState, "There is no score yet");
    return this.pyramid.getScore();
  }

  @Override
  public Card getCardAt(int row, int card)
      throws IllegalArgumentException, IllegalStateException {
    this.util.checkPregame(this.gameState, "There are no cards yet");
    return this.pyramid.getCardAt(row, card);
  }

  /**
   * Returns the draw card at the specified index.
   *
   * @param index index of the desired card (0-indexed from the top)
   * @return the card at the given position, or <code>null</code> if no card is there
   * @throws IllegalArgumentException if the index is invalid
   * @throws IllegalStateException    if the game hasn't been started yet
   */
  public Card getDrawAt(int index) throws IllegalArgumentException, IllegalStateException {
    this.util.checkPregame(this.gameState, "There is no draw pile yet");

    // Check that the given drawn card request is valid.
    if (index < 0 || index > this.numDraw - 1 || this.numDraw == 0) {
      throw new IllegalArgumentException("There's no draw card here.");
    }

    return this.drawCards[index];
  }

  @Override
  public List<Card> getDrawCards() throws IllegalStateException {
    this.util.checkPregame(this.gameState, "There are no draw cards yet");

    ArrayList<Card> drawCopy = new ArrayList<>();
    for (Card card : this.drawCards) {
      drawCopy.add(card);
    }

    return drawCopy;
  }

  // --------------------------- STATE CHECKS ---------------------------

  @Override
  public boolean isGameOver() throws IllegalStateException {
    this.util.checkPregame(this.gameState, "The game cannot be over");

    // To check move availability.
    HashSet<Integer> pyrVals = new HashSet<>();

    boolean pyramidEmpty = true;
    // 1. If the pyramid is empty, the game is over.
    for (int rowIndex = 0; rowIndex < this.getNumRows(); rowIndex++) {
      for (int colIndex = 0; colIndex < this.getRowWidth(rowIndex); colIndex++) {
        Card pyrCard = this.getCardAt(rowIndex, colIndex);
        if (pyrCard != null && this.pyramid.cardExposed(rowIndex, colIndex)) {
          pyramidEmpty = false;
          pyrVals.add(pyrCard.getVal()); // Add card value to set.
        }
      }
    }
    if (pyramidEmpty) {
      return true;
    }

    // 2. If there are cards in the draw pile, the game is not over.
    if (this.getNumDraw() > 0) {
      for (Card drawCard : this.getDrawCards()) {
        if (drawCard != null) {
          return false;
        }
      }
    }

    // 3. If a move exists, the game is not over.
    // Step 2 returned true if a non-null draw card exists => don't need to check the draw cards.

    boolean moveExists = false;
    // For each pyramid card value, see if its 13-complement exists in the pyramid.
    for (Integer val : pyrVals) {
      Integer complement = MOVE_VAL - val;
      if (pyrVals.contains(complement) || val == 13) {
        moveExists = true;
      }
    }
    // Move exists => game is not over.
    if (moveExists) {
      return false;
    }

    // 4. If the stockpile is not empty and they can draw cards, the game is not over.
    // If none of these three conditions are met, the game is over.
    return !(this.stock.size() > 0 && this.getNumDraw() > 0);
  }

  // ---------------------- GOOD JAVA CITIZENSHIP ----------------------

  @Override
  public String toString() {
    PyramidSolitaireTextualView view = new PyramidSolitaireTextualView(this);
    return view.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BasicPyramidSolitaire)) {
      return false;
    }
    BasicPyramidSolitaire that = (BasicPyramidSolitaire) o;
    return this.getNumRows() == that.getNumRows()
        && numDraw == that.numDraw
        && gameState == that.gameState
        && Objects.equals(pyramid, that.pyramid)
        && Objects.equals(stock, that.stock)
        && Arrays.equals(drawCards, that.drawCards);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(gameState, pyramid, stock, this.getNumRows(), numDraw);
    result = 31 * result + Arrays.hashCode(drawCards);
    return result;
  }
}