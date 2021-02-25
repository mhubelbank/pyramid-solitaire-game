import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import cs3500.pyramidsolitaire.model.hw02.Card;
import cs3500.pyramidsolitaire.model.hw02.Suit;
import cs3500.pyramidsolitaire.model.hw02.Value;
import cs3500.pyramidsolitaire.model.hw04.MultiPyramidSolitaire;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

/**
 * Tests for the {@code MultiPyramidSolitaire} class; adds onto the Basic tests.
 */
public class MultiPyramidSolitaireTest extends BasicPyramidSolitaireTest {

  // Override the initialization method that is called before each test method
  @Override
  public void initialize() {
    this.model = new MultiPyramidSolitaire();
  }

  // check that getDeck forms the correct deck
  @Override
  public void getDeckAttr() {
    List<Card> deck = model.getDeck();

    assertFalse(deck == null); // Not null
    assertEquals(104, deck.size()); // 104 cards
    assertFalse(deck.contains(null)); // No null cards
    for (Card card : deck) {
      assertEquals(Collections.frequency(deck, card), 2);
    }
  }

  // test that 8 is the max row num of a multi-pyramid
  @Override
  public void maxRows() {
    model.startGame(fullDeck, false, 9, 3);
  }

  // MP7R3D: test that a game is started and deck is dealt correctly
  @Override
  public void dealCards() {
    model.startGame(fullDeck, false, 7, 3);

    assertEquals(7, model.getNumRows());
    assertEquals(7, model.getRowWidth(0));
    assertEquals(442, model.getScore());
    assertEquals(new Card(Suit.CLUBS, Value.ACE), model.getCardAt(0, 0));
    assertEquals(new Card(Suit.CLUBS, Value.TWO), model.getCardAt(0, 3));
    assertEquals(new Card(Suit.CLUBS, Value.THREE), model.getCardAt(0, 6));
    assertEquals(null, model.getCardAt(0, 1)); // Haha spaces go brrr
    assertEquals(null, model.getCardAt(0, 2));
    assertEquals(null, model.getCardAt(0, 4));
    assertEquals(null, model.getCardAt(0, 5));
    assertEquals(null, model.getCardAt(1, 2));
    assertEquals(null, model.getCardAt(1, 5));

    assertEquals(3, model.getDrawCards().size());
    assertFalse(model.getDrawCards().contains(null));
  }

  // MP1R3D: test that a deck is dealt correctly
  @Test
  public void dealCards1R3D() {
    model.startGame(fullDeck, false, 1, 3);

    assertEquals(1, model.getNumRows());
    assertEquals(1, model.getRowWidth(0));
    assertEquals(1, model.getScore()); // Ace only
    assertEquals(new Card(Suit.CLUBS, Value.ACE), model.getCardAt(0, 0));

    assertEquals(3, model.getDrawCards().size());
    assertFalse(model.getDrawCards().contains(null));
  }

  // MP2R3D: test that a deck is dealt correctly
  @Test
  public void dealCards2R3D() {
    model.startGame(fullDeck, false, 2, 3);

    assertEquals(2, model.getNumRows());
    assertEquals(3, model.getRowWidth(0));
    assertEquals(4, model.getRowWidth(1));
    assertEquals(28, model.getScore());
    assertEquals(new Card(Suit.CLUBS, Value.ACE), model.getCardAt(0, 0));
    assertEquals(new Card(Suit.CLUBS, Value.FOUR), model.getCardAt(1, 0));

    assertEquals(3, model.getDrawCards().size());
    assertFalse(model.getDrawCards().contains(null));
  }

  // MP3R3D: test that a deck is dealt correctly
  @Test
  public void dealCards3R3D() {
    model.startGame(fullDeck, false, 3, 3);

    assertEquals(3, model.getNumRows());
    assertEquals(3, model.getRowWidth(0));
    assertEquals(5, model.getRowWidth(2));
    assertEquals(new Card(Suit.CLUBS, Value.ACE), model.getCardAt(0, 0));
    assertEquals(new Card(Suit.CLUBS, Value.EIGHT), model.getCardAt(2, 0));

    assertEquals(3, model.getDrawCards().size());
    assertFalse(model.getDrawCards().contains(null));
  }

  // MP4R3D: test that a deck is dealt correctly
  @Test
  public void dealCards4R3D() {
    model.startGame(fullDeck, false, 4, 3);

    assertEquals(4, model.getNumRows());
    assertEquals(5, model.getRowWidth(0));
    assertEquals(8, model.getRowWidth(3));
    assertEquals(new Card(Suit.CLUBS, Value.ACE), model.getCardAt(0, 0));
    assertEquals(null, model.getCardAt(0, 1)); // Spaces
    assertEquals(null, model.getCardAt(0, 3));

    assertEquals(3, model.getDrawCards().size());
    assertFalse(model.getDrawCards().contains(null));
  }

  // MP4R0D: test that an empty deck is instantiated correctly
  @Test
  public void dealCards4R0D() {
    model.startGame(fullDeck, false, 4, 0);

    assertEquals(new ArrayList<Card>(), model.getDrawCards());
  }

  // MP7R3D: test the remove 2-param and 1-param methods
  @Override
  public void remove() {
    model.startGame(fullDeck, false, 7, 3);

    model.remove(6, 1, 6, 12); // 1 + 12 = 13
    assertEquals(null, model.getCardAt(6, 1));
    assertEquals(null, model.getCardAt(6, 12));
    model.remove(6, 0); // King = 13
    assertEquals(null, model.getCardAt(6, 1));
  }

  // MP7R3D: test the remove with draw move
  @Override
  public void removeWithDraw() {
    model.startGame(fullDeck, false, 7, 3);

    model.removeUsingDraw(1, 6, 12);
    assertEquals(null, model.getCardAt(6, 12));
  }

  // MP7R3D: test that discard draw discards and replaces draw cards
  @Override
  public void discardDraw() {
    model.startGame(fullDeck, false, 7, 3);
    model.discardDraw(1); // Second draw card was A, now it's 3
    model.removeUsingDraw(1, 6, 13); // This is now invalid (exception thrown)
  }

  // check that an exception is thrown when a null card is removed
  @Override
  public void removeOneNullCard() {
    model.startGame(fullDeck, false, 7, 3);

    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("There's no card here.");

    model.remove(6, 0); // King = 13
    model.remove(6, 0); // Null now :(
  }

  // check that an exception is thrown when two null cards are removed
  @Override
  public void removeTwoNullCards() {
    model.startGame(fullDeck, false, 7, 3);

    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("There's no card here.");

    model.remove(6, 1, 6, 12); // 1 + 12 = 13
    model.remove(6, 1, 6, 12); // Null now :(
  }

  // check that an exception is thrown when a null card is drawn
  @Override
  public void discardNull() {
    model.startGame(fullDeck, false, 8, 16);

    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("There's no card here.");

    model.discardDraw(0);
    model.discardDraw(0); // Now this draw card is null.
  }

  // test a game-over losing state
  @Override
  public void gameOver() {
    model.startGame(fullDeck, false, 1, 103);

    assertFalse(model.isGameOver()); // Not over yet -- still draw cards in pile
    // Get rid of all the draw cards.
    int i = 0;
    while (i < 103) {
      model.discardDraw(i);
      i++;
    }
    assertTrue(model.isGameOver()); // Now it is over
  }

  // test a game-over winning state
  @Test
  public void gameOverWin() {
    List<Card> deckKing = new ArrayList<Card>(fullDeck);
    deckKing.set(0, new Card(Suit.CLUBS, Value.KING));
    deckKing.set(12, new Card(Suit.CLUBS, Value.ACE)); // Swap so that the only pyr card is a King
    model.startGame(deckKing, false, 1, 103);

    assertFalse(model.isGameOver()); // Not over yet -- still draw cards in pile
    // Get rid of all the draw cards.
    int i = 0;
    while (i < 103) {
      model.discardDraw(i);
      i++;
    }
    // Still not over -- can remove King
    assertFalse(model.isGameOver());

    model.remove(0, 0);
    assertTrue(model.isGameOver()); // Now it is over
    assertEquals(0, model.getScore());
  }

  // make sure the strict rules apply -- no relaxed-removals here!
  @Test(expected = IllegalArgumentException.class)
  public void strictRulesApply() {
    List<Card> deckQueen = new ArrayList<>(fullDeck);
    deckQueen.set(4, new Card(Suit.CLUBS, Value.QUEEN));
    deckQueen.set(11, new Card(Suit.CLUBS, Value.TWO)); // Swap queen and 4
    deckQueen.set(5, new Card(Suit.CLUBS, Value.KING));
    deckQueen.set(12, new Card(Suit.CLUBS, Value.THREE)); // Swap king and 5
    model.startGame(deckQueen, false, 2, 3);
    assertFalse(model.isGameOver()); // can remove a K and 6-7 pair

    model.remove(1, 1); // get rid of king
    model.remove(1, 2, 1, 3); // get rid of 6-7
    assertTrue(model.isGameOver()); // now the game is over
    model.remove(0, 0, 1, 0); // can't remove the relaxed-removable pair
  }
}