import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import cs3500.pyramidsolitaire.controller.PyramidSolitaireController;
import cs3500.pyramidsolitaire.controller.PyramidSolitaireTextualController;
import cs3500.pyramidsolitaire.model.hw02.BasicPyramidSolitaire;
import cs3500.pyramidsolitaire.model.hw02.Card;
import cs3500.pyramidsolitaire.model.hw02.PyramidSolitaireModel;
import cs3500.pyramidsolitaire.model.hw02.Suit;
import cs3500.pyramidsolitaire.model.hw02.Value;
import java.io.IOException;
import java.io.PipedWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the {@code PyramidSolitaireTextualController} class, to be used on a basic model.
 */
public class BasicPyramidSolitaireTextualControllerTest {

  /* Note: I used inheritance for these test methods, just as I did for the model tests.
     Please let me know if there's a better way to accomplish this! */

  protected static PyramidSolitaireModel m;
  protected static List<Card> deck;
  protected static final String INV_MOVE_MSG = "Invalid move. Play again. ";

  @Before
  public void initialize() {
    this.m = new BasicPyramidSolitaire();
  }

  /**
   * Helper method to initialize the perfect, un-shuffled deck of cards.
   */
  @Before
  public void initDeck() {
    this.deck = new ArrayList<Card>();
    // Iterate over the 52 combinations of 4 Suits x 13 Values
    for (Suit suit : Suit.values()) {
      for (Value value : Value.values()) {
        this.deck.add(new Card(suit, value));
      }
    }
  }

  // ILLEGAL ARGUMENT TESTS --------------------------------------------------------------

  // Test null Readable.
  @Test(expected = IllegalArgumentException.class)
  public void nullReadable() {
    Appendable out = new StringBuffer();
    PyramidSolitaireController c = new PyramidSolitaireTextualController(null, out);
  }

  // Test null Appendable.
  @Test(expected = IllegalArgumentException.class)
  public void nullAppendable() {
    Readable in = new StringReader("rm1 7 5");
    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, null);
  }

  // Test that a game can't be started if the model is null.
  @Test(expected = IllegalArgumentException.class)
  public void nullModel() {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm1 7 5");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(null, deck, false, 7, 3);
  }

  // Test that a game can't be started if the deck is null.
  @Test(expected = IllegalArgumentException.class)
  public void nullDeck() {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm1 7 5");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, null, false, 7, 3);
  }

  // Test that a game can't be started with an invalid deck.
  @Test(expected = IllegalStateException.class)
  public void invDeck() {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm1 7 5");

    List<Card> badDeck = Arrays.asList(new Card(Suit.SPADES, Value.KING));

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, badDeck, false, 0, 3);
  }

  // Test that a game can't be started with invalid rows.
  @Test(expected = IllegalStateException.class)
  public void invRows() {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm1 7 5");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 0, 3);
  }

  // Test that a game can't be started with invalid draw.
  @Test(expected = IllegalStateException.class)
  public void invDraw() {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm1 7 5");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, -1);
  }

  // Test that a game can't be started when a full pyramid/draw pile can't be dealt.
  @Test(expected = IllegalStateException.class)
  public void pyrTooBig() {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm1 7 5");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 10, 3);
  }

  // Test that a game can't be started when a full pyramid/draw pile can't be dealt.
  @Test(expected = IllegalStateException.class)
  public void drawTooBig() {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm1 7 5");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 5, 100);
  }

  // ILLEGAL STATE TESTS --------------------------------------------------------------

  // Test appendable-model communication error-throwing (IO -> State)
  @Test(expected = IllegalStateException.class)
  public void ioState() throws IOException {
    PipedWriter out = new PipedWriter();
    Readable in = new StringReader("doesn't matter");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    out.close(); // An example of an IO exception to catch
    c.playGame(m, deck, false, 7, 3);
  }

  // Test readable-model communication error-throwing (NoSuchElem -> State)
  @Test(expected = IllegalStateException.class)
  public void elementState() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm1 4"); // Needs a column, too

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
  }

  // MISC. STATE TESTS --------------------------------------------------------------

  // Test that the game can be rendered as over before a player has input anything.
  @Test
  public void over() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm1 7 5");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 2, 0); // 1 + 2 + 3
    assertTrue(out.toString().contains("Game over."));
  }

  // Test that the game can be won
  @Test
  public void win() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm1 1 1");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    List<Card> winDeck = new ArrayList<Card>(deck);
    winDeck.set(0, new Card(Suit.CLUBS, Value.KING));
    winDeck.set(12, new Card(Suit.CLUBS, Value.ACE));
    c.playGame(m, winDeck, false, 1, 0);
    assertTrue(out.toString().contains("You win!"));
  }

  // QUITTING TESTS --------------------------------------------------------------

  // Test the output right after the game is started and the user immediately quits ('q').
  @Test
  public void initialOutputAndLowercaseQuit() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertTrue(out.toString().contains("Game quit!"));
  }

  // Test the output for quitting with 'Q'.
  @Test
  public void uppercaseQuit() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("Q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertTrue(out.toString().contains("Game quit!"));
  }

  // test mid-remove1 quit
  @Test
  public void midRemove1Quit() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm1 4 q 4");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertTrue(out.toString().contains("Game quit!"));
  }

  // test mid-remove2 quit
  @Test
  public void midRemove2Quit() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm2 4 Q 4 3 3");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertTrue(out.toString().contains("Game quit!"));
  }

  // test mid-remove-with-draw quit
  @Test
  public void midRemoveDrawQuit() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rmwd 0 Q 4 3");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertTrue(out.toString().contains("Game quit!"));
  }

  // test mid-discard-draw quit
  @Test
  public void midDiscardDrawQuit() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("dd Q 1");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertTrue(out.toString().contains("Game quit!"));
  }

  // REMOVE ONE TESTS --------------------------------------------------------------

  // Test valid remove-one input.
  @Test
  public void removeOne() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm1 7 5 q"); // King

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertFalse(out.toString().contains(INV_MOVE_MSG));
  }

  // Test no impact on pyramid of invalid move command (good command = no problems).
  @Test
  public void invMoveNoEffect() {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("amit rm1 7 5 q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertFalse(out.toString().contains(INV_MOVE_MSG));
  }

  // After valid remove-one move, check for error with re-request (null card)
  @Test
  public void removeOneNull() {
    Appendable out = new StringBuffer();
    // Remove 7-5 and then try to do it again.
    Readable in = new StringReader("rm1 7 5 rm1 7 5 q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertTrue(out.toString().contains(INV_MOVE_MSG));
  }

  // Check for error if not king.
  @Test
  public void removeOneNonKing() {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm1 7 4 q"); // Remove 7-5 and then try to do it again.

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertTrue(out.toString().contains(INV_MOVE_MSG));
  }

  // Check for error if card not exposed.
  @Test
  public void removeOneCovered() {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm1 1 1 q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertTrue(out.toString().contains(INV_MOVE_MSG));
  }

  // Test invalid remove-one row input
  @Test
  public void removeOneBadRow() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm1 8 1 q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertTrue(out.toString().contains(INV_MOVE_MSG));
  }

  // Test invalid remove-one card column input
  @Test
  public void removeOneBadCol() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm1 7 8 q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertTrue(out.toString().contains(INV_MOVE_MSG));
  }

  // Test invalid remove-one card column input and re-entry (accept first and third tokens)
  @Test
  public void removeOneBadColRE() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm1 7 egg 5 q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertFalse(out.toString().contains(INV_MOVE_MSG));
  }

  // REMOVE TWO TESTS --------------------------------------------------------------

  // Test valid remove-two input.
  @Test
  public void removeTwo() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm2 7 4 7 6 q"); // Queen and Ace

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertFalse(out.toString().contains(INV_MOVE_MSG));
  }

  // After valid remove-two move, check for error with re-request (null cards)
  @Test
  public void removeTwoNull() {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm2 7 4 7 6 rm2 7 4 7 6 q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertTrue(out.toString().contains(INV_MOVE_MSG));
  }

  // Check for error if remove-two cards don't sum to 13.
  @Test
  public void removeTwoInvSum() {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm2 7 4 7 5 q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertTrue(out.toString().contains(INV_MOVE_MSG));
  }

  // Check for error if card 1 is not exposed.
  @Test
  public void removeTwoOneCovered() {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm2 1 1 7 4 q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertTrue(out.toString().contains(INV_MOVE_MSG));
  }

  // Check for error if card 2 is not exposed.
  @Test
  public void removeTwoTwoCovered() {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm2 7 4 1 1 q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertTrue(out.toString().contains(INV_MOVE_MSG));
  }

  // Test invalid remove-two row 1 input
  @Test
  public void removeTwoBadRow1() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm2 8 2 7 2 q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertTrue(out.toString().contains(INV_MOVE_MSG));
  }

  // Test invalid remove-two card column 1 input
  @Test
  public void removeOneBadCol1() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm2 4 5 7 1 q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertTrue(out.toString().contains(INV_MOVE_MSG));
  }

  // Test invalid remove-two row 2 input
  @Test
  public void removeTwoBadRow2() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm2 7 2 8 1 q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertTrue(out.toString().contains(INV_MOVE_MSG));
  }

  // Test invalid remove-two card column 2 input
  @Test
  public void removeTwoBadCol2() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm2 7 2 7 8 q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertTrue(out.toString().contains(INV_MOVE_MSG));
  }

  // Test invalid remove-two row 2 input with re-entry (accept tokens 1,2,4,5)
  @Test
  public void removeTwoBadRow2RE() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm2 7 4 egg 7 6 q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertFalse(out.toString().contains(INV_MOVE_MSG));
  }

  // REMOVE WITH DRAW TESTS --------------------------------------------------------------

  // Test valid remove-with-draw input.
  @Test
  public void removeWithDraw() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rmwd 1 7 2 q"); // 3 (draw) and 10 (pyr)

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertFalse(out.toString().contains(INV_MOVE_MSG));
  }

  // Check for error with remove-draw with no draw pile.
  @Test
  public void removeDrawNullDrawPile() {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rmwd 1 7 2 q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 0);
    assertTrue(out.toString().contains(INV_MOVE_MSG));
  }

  // Check for error with remove-draw with null draw card.
  @Test
  public void removeDrawNullDrawCard() {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rmwd 1 9 9 rmwd 1 1 1 q"); // Remove draw1 (none left in stock).

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 9, 7);
    assertTrue(out.toString().contains(INV_MOVE_MSG));
  }

  // After valid remove-with-draw move, check for error with re-request (null pyramid card)
  @Test
  public void removeDrawNullCard() {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rmwd 1 7 2 rmwd 1 7 2 q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertTrue(out.toString().contains(INV_MOVE_MSG));
  }

  // Check for error if remove-draw cards don't sum to 13.
  @Test
  public void removeDrawInvSum() {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rmwd 1 7 3 q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertTrue(out.toString().contains(INV_MOVE_MSG));
  }

  // Check for remove-draw error if pyramid card is not exposed.
  @Test
  public void removeDrawCovered() {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rmwd 1 4 4 q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertTrue(out.toString().contains(INV_MOVE_MSG));
  }

  // Test invalid remove-draw draw index input
  @Test
  public void removeDrawBadDraw() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rmwd 4 7 4 q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertTrue(out.toString().contains(INV_MOVE_MSG));
  }

  // Test invalid remove-draw row input
  @Test
  public void removeDrawBadRow() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rmwd 1 8 5 q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertTrue(out.toString().contains(INV_MOVE_MSG));
  }

  // Test invalid remove-draw card column input
  @Test
  public void removeDrawBadCol() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rmwd 1 7 8 q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertTrue(out.toString().contains(INV_MOVE_MSG));
  }

  // Test invalid remove-draw card column input and re-entry (accept tokens 1,2,4)
  @Test
  public void removeDrawBadColRE() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rmwd 1 7 egg 2 q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertFalse(out.toString().contains(INV_MOVE_MSG));
  }

  // DISCARD DRAW TESTS --------------------------------------------------------------

  // Test valid discard-draw input.
  @Test
  public void discardDraw() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("dd 3 q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertFalse(out.toString().contains(INV_MOVE_MSG));
  }

  // Test invalid discard-draw input.
  @Test
  public void discardDrawInv() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("dd 4 q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertTrue(out.toString().contains(INV_MOVE_MSG));
  }

  // Check for error with discard-draw with no draw pile.
  @Test
  public void discardDrawNullDrawPile() {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("dd 1 q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 0);
    assertTrue(out.toString().contains(INV_MOVE_MSG));
  }
}