import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import cs3500.pyramidsolitaire.model.hw02.BasicPyramidSolitaire;
import cs3500.pyramidsolitaire.model.hw02.Card;
import cs3500.pyramidsolitaire.model.hw02.PyramidSolitaireModel;
import cs3500.pyramidsolitaire.model.hw02.Suit;
import cs3500.pyramidsolitaire.model.hw02.Value;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for the {@code BasicPyramidSolitaire} class.
 */
public class BasicPyramidSolitaireTest {

  /* Full, non-shuffled pyramid:
                A♣
              2♣  3♣
            4♣  5♣  6♣
          7♣  8♣  9♣  10♣
        J♣  Q♣  K♣  A♦  2♦
      3♦  4♦  5♦  6♦  7♦  8♦
    9♦  10♦ J♦  Q♦  K♦  A♥  2♥
   */

  protected PyramidSolitaireModel model;
  protected List<Card> fullDeck;
  protected List<Card> fullDeckOrd; // Full deck w/ diff order
  protected List<Card> dupDeck; // Deck with duplicates
  protected List<Card> nullDeck; // Deck with a null card
  protected List<Card> incDeck; // Incomplete deck

  // initialize a basic model
  @Before
  public void initialize() {
    this.model = new BasicPyramidSolitaire();
  }

  // check that getDeck forms the correct deck
  @Test
  public void getDeckAttr() {
    List<Card> deck = model.getDeck();
    HashSet<Card> deckSet = new HashSet<>(deck);

    assertFalse(deck == null); // Not null
    assertEquals(52, deck.size()); // 52 cards
    assertEquals(deck.size(), deckSet.size()); // No duplicates
    assertFalse(deck.contains(null)); // No null cards
  }

  // test that a new game is in pregame through remove
  @Test(expected = IllegalStateException.class)
  public void pregameRemove() {
    model.remove(0, 0);
  }

  // test that a new game is in pregame through getRowWidth
  @Test(expected = IllegalStateException.class)
  public void pregameWidth() {
    model.getRowWidth(0);
  }

  // test that a new game is in pregame through getScore
  @Test(expected = IllegalStateException.class)
  public void pregameScore() {
    model.getScore();
  }

  // test that a new game is in pregame through getCardAt
  @Test(expected = IllegalStateException.class)
  public void pregameCardAt() {
    model.getCardAt(0, 0);
  }

  // test that a new game is in pregame through getDrawCards
  @Test(expected = IllegalStateException.class)
  public void pregameDraw() {
    model.getDrawCards();
  }

  // test that a new game is in pregame through isGameOver
  @Test(expected = IllegalStateException.class)
  public void pregameOver() {
    model.isGameOver();
  }

  // test that a new game is in pregame through non-exception getter methods
  @Test
  public void pregameVals() {
    assertEquals(-1, model.getNumRows());
    assertEquals(-1, model.getNumDraw());
  }

  // helper for testing different types of decks
  @Before
  public void initializeDecks() {
    fullDeck = model.getDeck();
    fullDeckOrd = model.getDeck();
    dupDeck = model.getDeck();
    nullDeck = model.getDeck();
    incDeck = Arrays.asList(new Card(Suit.SPADES, Value.ACE));

    // Swap the first and third cards.
    Card first = new Card(Suit.CLUBS, Value.ACE);
    Card third = new Card(Suit.CLUBS, Value.THREE);
    fullDeckOrd.set(0, third);
    fullDeckOrd.set(2, first);

    // Add some duplicates to the duplicates deck.
    dupDeck.set(0, new Card(Suit.SPADES, Value.ACE));
    dupDeck.set(1, new Card(Suit.SPADES, Value.ACE));

    // Add a null card to the null deck.
    nullDeck.set(10, null);
  }

  // test that startGame throws exceptions with an invalid num of rows
  @Test(expected = IllegalArgumentException.class)
  public void startInvalidRows() {
    model.startGame(fullDeck, false, 0, 5);
  }

  // test that startGame throws exceptions with an invalid num of draw
  @Test(expected = IllegalArgumentException.class)
  public void startInvalidDraw() {
    model.startGame(fullDeck, false, 5, -1);
  }

  // test that startGame throws exceptions with a null deck
  @Test(expected = IllegalArgumentException.class)
  public void startNull() {
    model.startGame(null, false, 5, 5);
  }

  // test that startGame throws exceptions with a deck with null card
  @Test(expected = IllegalArgumentException.class)
  public void startNullCard() {
    model.startGame(nullDeck, false, 5, 5);
  }

  // test that startGame throws exceptions with a deck with duplicates
  @Test(expected = IllegalArgumentException.class)
  public void startDupDeck() {
    model.startGame(dupDeck, false, 5, 5);
  }

  // test that startGame throws exceptions with an incomplete deck
  @Test(expected = IllegalArgumentException.class)
  public void startIncDeck() {
    model.startGame(incDeck, false, 5, 5);
  }

  // test that startGame throws exceptions when pyramid and draw pile cant be formed
  @Test(expected = IllegalArgumentException.class)
  public void startFullDeal() {
    model.startGame(fullDeck, false, 100, 5);
  }

  // test that 9 is the max row num of a basic pyramid
  @Test (expected = IllegalArgumentException.class)
  public void maxRows() {
    model.startGame(fullDeck,false,10,0);
  }

  // test that a game is started and a deck is dealt correctly
  @Test
  public void dealCards() {
    model.startGame(fullDeck, false, 7, 3);

    assertEquals(7, model.getNumRows());
    assertEquals(3, model.getNumDraw());
    assertEquals(1, model.getRowWidth(0));
    assertEquals(7, model.getRowWidth(6));

    // sum(1,7) = 28 cards
    // first 28 cards: 2 * sum(1,13) + 1 + 2 = 91 + 91 + 3 = 185
    assertEquals(185, model.getScore());
    assertEquals(new Card(Suit.CLUBS, Value.ACE), model.getCardAt(0, 0));
    assertEquals(new Card(Suit.CLUBS, Value.TWO), model.getCardAt(1, 0));

    assertEquals(3, model.getDrawCards().size());
    assertFalse(model.getDrawCards().contains(null));
  }

  // test that a deck is shuffled, dealt appropriately; test no error with differently ordered deck
  @Test
  public void shuffle() {
    model.startGame(fullDeck, true, 7, 3);

    assertTrue(this.shuffleTest(fullDeck, model));
    assertEquals(3, model.getDrawCards().size());
    assertFalse(model.getDrawCards().contains(null));
  }

  // tester helper to make sure that the given deck has been shuffled
  private boolean shuffleTest(List<Card> fullDeck, PyramidSolitaireModel bpsShuff) {
    int deckIndex = 0;
    for (int rowIndex = 0; rowIndex < bpsShuff.getNumRows(); rowIndex++) {
      for (int colIndex = 0; colIndex < bpsShuff.getRowWidth(rowIndex); colIndex++) {
        if (!fullDeck.get(deckIndex).equals(bpsShuff.getCardAt(rowIndex, colIndex))) {
          return true; // If even one card is different, the deck has been shuffled.
        }
        deckIndex++;
      }
    }
    return false;
  }

  // test illegal arg exception for getRowWidth
  @Test(expected = IllegalArgumentException.class)
  public void widthOut() {
    model.startGame(fullDeck, false, 7, 3);
    model.getRowWidth(7);
  }

  // test illegal arg exception for getCardAt
  @Test(expected = IllegalArgumentException.class)
  public void cardAtOut() {
    model.startGame(fullDeck, false, 7, 3);
    model.getCardAt(-1, 0);
  }

  // test illegal arg exceptions for remove out of bounds
  @Test(expected = IllegalArgumentException.class)
  public void removeOut() {
    model.startGame(fullDeck, false, 7, 3);
    model.remove(0, 0, 7, 0);
  }

  // test illegal arg exceptions for remove covered card
  @Test(expected = IllegalArgumentException.class)
  public void removeCovered() {
    model.startGame(fullDeck, false, 7, 3);
    model.remove(0, 0, 1, 1); // Covered
  }

  // test illegal arg exceptions for remove sum != 13
  @Test(expected = IllegalArgumentException.class)
  public void removeBadSum() {
    model.startGame(fullDeck, false, 7, 3);
    model.remove(6, 0, 6, 1); // Not sum to 13
  }

  // test illegal arg exceptions for remove-draw with no draw pile
  @Test(expected = IllegalArgumentException.class)
  public void drawNoPile() {
    model.startGame(fullDeck, false, 7, 0);
    model.removeUsingDraw(0, 0, 0); // No draw pile
  }

  // test illegal arg exceptions for discarding draw card out of bounds
  @Test(expected = IllegalArgumentException.class)
  public void drawOut() {
    model.startGame(fullDeck, false, 7, 3);
    model.discardDraw(8);
  }

  // test the remove 2-param and 1-param methods and their side effects
  @Test
  public void remove() {
    model.startGame(fullDeck, false, 7, 3);

    model.remove(6, 3, 6, 5); // 12 + 1 = 13
    assertEquals(null, model.getCardAt(6, 3));
    assertEquals(null, model.getCardAt(6, 5));
    model.remove(6, 2, 6, 6); // 11 + 2 = 13
    model.remove(6, 4); // King = 13
    assertEquals(null, model.getCardAt(6, 4));
    model.remove(5, 3, 5, 4); // Uncovered now: 6 + 7 = 13
  }

  // test the remove with draw move
  @Test
  public void removeWithDraw() {
    model.startGame(fullDeck, false, 7, 3);

    model.removeUsingDraw(1, 6, 0);
    assertEquals(null, model.getCardAt(6, 0));
  }

  // test that discard draw discards and replaces draw cards
  @Test(expected = IllegalArgumentException.class)
  public void discardDraw() {
    model.startGame(fullDeck, false, 7, 3);
    model.discardDraw(1); // First draw card was 3, now it's 6
    model.removeUsingDraw(1, 6, 2); // This is invalid now :(
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  // check that an exception is thrown when a null card is removed
  @Test
  public void removeOneNullCard() {
    model.startGame(fullDeck, false, 7, 3);

    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("There's no card here.");

    model.remove(6, 4);
    model.remove(6, 4); // Now this card is null.
  }

  // check that an exception is thrown when two null cards are removed
  @Test
  public void removeTwoNullCards() {
    model.startGame(fullDeck, false, 7, 3);

    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("There's no card here.");

    model.remove(6, 2, 6, 6);
    model.remove(6, 2, 6, 6); // Now these cards are null.
  }

  // check that an exception is thrown when a null card is discarded
  @Test
  public void discardNull() {
    model.startGame(fullDeck, false, 7, 3);

    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("There's no card here.");

    // Discard the whole stock pile.
    int i = 0;
    while (i < 22) {
      model.discardDraw(0);
      i++;
    }

    model.discardDraw(0); // Now this draw card is null.
  }

  // test that the game is over when it should be
  @Test
  public void gameOver() {
    BasicPyramidSolitaire twoRows = new BasicPyramidSolitaire();
    twoRows.startGame(fullDeck, false, 2, 1);

    /*
            1♣
          2♣  J♣ <- Wait until end of game to perform this move.
     */
    BasicPyramidSolitaire oneMoveLeft = new BasicPyramidSolitaire();
    List<Card> oneTwoEleven = new ArrayList<>();
    oneTwoEleven.addAll(fullDeck);
    oneTwoEleven.set(2, new Card(Suit.CLUBS, Value.JACK));
    oneTwoEleven.set(10, new Card(Suit.CLUBS, Value.THREE));
    oneMoveLeft.startGame(oneTwoEleven, false, 2, 1);

    // Not over; cards left in the draw pile.
    assertFalse(twoRows.isGameOver());
    assertFalse(oneMoveLeft.isGameOver());

    // No errors here means the game is never wrongly over during gameplay.
    int i = 0;
    while (i < 48) {
      twoRows.discardDraw(0);
      oneMoveLeft.discardDraw(0);
      i++;
    }

    // Still one card left in the draw pile.
    assertFalse(twoRows.isGameOver());
    assertFalse(oneMoveLeft.isGameOver());

    twoRows.discardDraw(0); // Discard the last card.
    // The stock pile is empty and no remaining moves -> over.
    assertTrue(twoRows.isGameOver());
    assertEquals(6, twoRows.getScore());

    oneMoveLeft.discardDraw(0); // Discard the last card.
    // The stock pile is empty, but there's one move left -> not over.
    assertFalse(oneMoveLeft.isGameOver());

    // Perform the one move you can.
    oneMoveLeft.remove(1, 0, 1, 1);
    assertTrue(oneMoveLeft.isGameOver()); // Now the game is over.
    assertEquals(1, oneMoveLeft.getScore());
  }

  // regression testing
  @Test(expected = IllegalArgumentException.class)
  public void strictRulesApply() {
    /* removing the king then the ace-queen pair would work in relaxed, but not basic
         A♣
       Q♣  K♣
   */
    List<Card> deckQueen = new ArrayList<>(fullDeck);
    deckQueen.set(1, new Card(Suit.CLUBS, Value.QUEEN));
    deckQueen.set(11, new Card(Suit.CLUBS, Value.TWO)); // Swap queen and 2
    deckQueen.set(2, new Card(Suit.CLUBS, Value.KING));
    deckQueen.set(12, new Card(Suit.CLUBS, Value.THREE)); // Swap king and 3
    model.startGame(deckQueen, false, 2, 3);
    assertFalse(model.isGameOver());

    model.remove(1, 1); // get rid of king
    assertTrue(model.isGameOver()); // now the game is over
    model.remove(0, 0, 1, 0); // can't remove the relaxed-removable pair
  }
}