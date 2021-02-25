import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import cs3500.pyramidsolitaire.model.hw02.Card;
import cs3500.pyramidsolitaire.model.hw02.Suit;
import cs3500.pyramidsolitaire.model.hw02.Value;
import cs3500.pyramidsolitaire.model.hw04.RelaxedPyramidSolitaire;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * Tests for the {@code RelaxedPyramidSolitaire} class; adds onto the Basic tests.
 */
public class RelaxedPyramidSolitaireTest extends BasicPyramidSolitaireTest {

  // Override the initialization method that is called before each test method
  @Override
  public void initialize() {
    this.model = new RelaxedPyramidSolitaire();
  }

  // test the remove 2-param and 1-param methods, and a right mini pyramid pair
  @Override
  public void remove() {
    model.startGame(fullDeck, false, 7, 3);

    model.remove(6, 4); // normal remove 1
    assertEquals(null, model.getCardAt(6, 4));

    model.remove(6, 3, 6, 5); // 12 + 1 = 13 // normal remove (bottom row)
    assertEquals(null, model.getCardAt(6, 3));
    assertEquals(null, model.getCardAt(6, 5));

    model.removeUsingDraw(1, 6, 0); // use RWD 9 and 4 to reveal this:
    /*    3♦      (bottom left of RP7R3D)       3♦
        9♦  10♦       ------------>               10♦
     */
    // Now the 3 and 10 can be removed together :)
    model.remove(5, 0, 6, 1);
    assertEquals(null, model.getCardAt(5, 0));
    assertEquals(null, model.getCardAt(6, 1));
  }

  // test the remove 2-param and 1-param methods, and a left mini-pyramid pair
  // also check that the game over state is determined properly (in addition to superclass tests)
  @Test
  public void removeRelaxedLeft() {
    /* pyramid (we want to remove the king and then the ace-queen pair)
         A♣                       A♣
       Q♣  K♣       --->        Q♣
   */
    List<Card> deckQueen = new ArrayList<>(fullDeck);
    deckQueen.set(1, new Card(Suit.CLUBS, Value.QUEEN));
    deckQueen.set(11, new Card(Suit.CLUBS, Value.TWO)); // Swap queen and 2
    deckQueen.set(2, new Card(Suit.CLUBS, Value.KING));
    deckQueen.set(12, new Card(Suit.CLUBS, Value.THREE)); // Swap king and 3
    model.startGame(deckQueen, false, 2, 3);
    assertFalse(model.isGameOver());

    model.remove(1, 1); // bye bye king
    assertEquals(null, model.getCardAt(1, 1));
    assertFalse(model.isGameOver()); // MOST IMPORTANT: this would be true in Basic

    // Remove the relaxed-removable pair
    model.remove(0, 0, 1, 0);
    assertEquals(null, model.getCardAt(0, 0));
    assertEquals(null, model.getCardAt(1, 0));
    assertTrue(model.isGameOver());
  }

  // the above test checks that a game isn't over with a left relaxed-removable pair
  // this test checks a right relaxed-removable pair
  // also switch the top-bottom order to make sure order doesn't matter
  @Test
  public void gameNotOverRightPairLeft() {
    /* pyramid (once again we want to remove the king and then the ace-queen pair)
         A♣                       A♣
       K♣  Q♣         --->          Q♣
   */
    List<Card> deckQueen = new ArrayList<>(fullDeck);
    deckQueen.set(2, new Card(Suit.CLUBS, Value.QUEEN));
    deckQueen.set(11, new Card(Suit.CLUBS, Value.THREE)); // Swap queen and 3
    deckQueen.set(1, new Card(Suit.CLUBS, Value.KING));
    deckQueen.set(12, new Card(Suit.CLUBS, Value.TWO)); // Swap king and 2
    model.startGame(deckQueen, false, 2, 3);
    assertFalse(model.isGameOver());

    model.remove(1, 0); // bye bye king 2: electric boogaloo
    assertEquals(null, model.getCardAt(1, 0));
    assertFalse(model.isGameOver()); // this would be true in Basic

    // Remove the relaxed-removable pair
    model.remove(1, 1, 0, 0);
    assertEquals(null, model.getCardAt(0, 0));
    assertEquals(null, model.getCardAt(1, 1));
    assertTrue(model.isGameOver());
  }

  // test that the third mini-pyramid card needs to be null to remove
  @Test(expected = IllegalArgumentException.class)
  public void removeThirdNonNullLeft() {
    /*   A♣
       Q♣  K♣
   */
    List<Card> deckQueen = new ArrayList<>(fullDeck);
    deckQueen.set(1, new Card(Suit.CLUBS, Value.QUEEN));
    deckQueen.set(11, new Card(Suit.CLUBS, Value.TWO)); // Swap queen and 2
    deckQueen.set(2, new Card(Suit.CLUBS, Value.KING));
    deckQueen.set(12, new Card(Suit.CLUBS, Value.THREE)); // Swap king and 3
    model.startGame(deckQueen, false, 2, 3);
    assertFalse(model.isGameOver());

    // try (and fail, hopefully) to remove a pair that's otherwise relaxed-removable
    model.remove(0, 0, 1, 0);
  }

  // test that the third mini-pyramid card needs to be null to remove
  // test for right pair side and with order of pair reversed
  @Test(expected = IllegalArgumentException.class)
  public void removeThirdNonNullRight() {
    /*
         A♣
       K♣  Q♣
   */
    List<Card> deckQueen = new ArrayList<>(fullDeck);
    deckQueen.set(2, new Card(Suit.CLUBS, Value.QUEEN));
    deckQueen.set(11, new Card(Suit.CLUBS, Value.THREE)); // Swap queen and 3
    deckQueen.set(1, new Card(Suit.CLUBS, Value.KING));
    deckQueen.set(12, new Card(Suit.CLUBS, Value.TWO)); // Swap king and 2
    model.startGame(deckQueen, false, 2, 3);
    assertFalse(model.isGameOver());

    // try (and fail, hopefully) to remove a pair that's otherwise relaxed-removable
    model.remove(1, 1, 0, 0);
  }

  // strict rules don't apply; just throw an exception to appease super
  @Override
  public void strictRulesApply() {
    throw new IllegalArgumentException();
  }
}