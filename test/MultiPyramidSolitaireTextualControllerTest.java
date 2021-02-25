import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import cs3500.pyramidsolitaire.controller.PyramidSolitaireController;
import cs3500.pyramidsolitaire.controller.PyramidSolitaireTextualController;
import cs3500.pyramidsolitaire.model.hw02.Card;
import cs3500.pyramidsolitaire.model.hw02.Suit;
import cs3500.pyramidsolitaire.model.hw02.Value;
import cs3500.pyramidsolitaire.model.hw04.MultiPyramidSolitaire;
import java.io.StringReader;
import java.util.ArrayList;
import org.junit.Test;

/**
 * Tests for the {@code PyramidSolitaireTextualController} class, to be used on a multi model.
 */
public class MultiPyramidSolitaireTextualControllerTest extends
    BasicPyramidSolitaireTextualControllerTest {

  // model is a multi-pyramid
  @Override
  public void initialize() {
    this.m = new MultiPyramidSolitaire();
  }

  /**
   * Helper method to initialize the perfect, un-shuffled deck of cards.
   */
  @Override
  public void initDeck() {
    this.deck = new ArrayList<Card>();
    // add both halves
    this.deckHelp();
    this.deckHelp();
  }

  // helper to iterate over the 52 combinations of 4 Suits x 13 Values and add each card
  private void deckHelp() {
    for (Suit suit : Suit.values()) {
      for (Value value : Value.values()) {
        this.deck.add(new Card(suit, value));
      }
    }
  }

  // Test no impact on pyramid of invalid move command (good command = no problems).
  @Override
  public void invMoveNoEffect() {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("amit rm1 7 1 q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertFalse(out.toString().contains(INV_MOVE_MSG));
  }

  // Test that the game can be rendered as over before a player has input anything.
  @Override
  public void over() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm1 7 5");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 1, 0);
    assertTrue(out.toString().contains("Game over."));
  }

  // Test valid remove-one input.
  @Override
  public void removeOne() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm1 7 1 q"); // King

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertFalse(out.toString().contains(INV_MOVE_MSG));
  }

  // Test invalid remove-one card column input and re-entry (accept first and third tokens)
  @Override
  public void removeOneBadColRE() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm1 7 egg 1 q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertFalse(out.toString().contains(INV_MOVE_MSG));
  }

  // Test valid remove-two input.
  @Override
  public void removeTwo() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm2 7 2 7 13 q"); // Queen and Ace

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertFalse(out.toString().contains(INV_MOVE_MSG));
  }

  // Test invalid remove-two row 2 input with re-entry (accept tokens 1,2,4,5)
  @Override
  public void removeTwoBadRow2RE() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rm2 7 2 egg 7 13 q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertFalse(out.toString().contains(INV_MOVE_MSG));
  }

  // Test valid remove-with-draw input.
  @Override
  public void removeWithDraw() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rmwd 2 7 13 q"); // 3 (draw) and 10 (pyr)

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertFalse(out.toString().contains(INV_MOVE_MSG));
  }

  // Test invalid remove-draw card column input and re-entry (accept tokens 1,2,4)
  @Override
  public void removeDrawBadColRE() throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rmwd 2 7 egg 13 q");

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 7, 3);
    assertFalse(out.toString().contains(INV_MOVE_MSG));
  }

  // Check for error with remove-draw with null draw card.
  @Test
  public void removeDrawNullDrawCard() {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("rmwd 8 8 8 rmwd 8 8 8 q"); // None left in stock

    PyramidSolitaireController c = new PyramidSolitaireTextualController(in, out);
    c.playGame(m, deck, false, 8, 16);
    assertTrue(out.toString().contains(INV_MOVE_MSG));
  }
}