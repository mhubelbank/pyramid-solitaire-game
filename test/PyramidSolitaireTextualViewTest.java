import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import cs3500.pyramidsolitaire.model.hw02.BasicPyramidSolitaire;
import cs3500.pyramidsolitaire.model.hw02.Card;
import cs3500.pyramidsolitaire.model.hw02.PyramidSolitaireModel;
import cs3500.pyramidsolitaire.model.hw02.Suit;
import cs3500.pyramidsolitaire.model.hw02.Value;
import cs3500.pyramidsolitaire.model.hw04.MultiPyramidSolitaire;
import cs3500.pyramidsolitaire.model.hw04.RelaxedPyramidSolitaire;
import cs3500.pyramidsolitaire.view.PyramidSolitaireTextualView;
import cs3500.pyramidsolitaire.view.PyramidSolitaireView;
import java.io.IOException;
import java.io.PipedWriter;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * Tests for the {@code PyramidSolitaireTextualView} class.
 */
public class PyramidSolitaireTextualViewTest {

  // test that a game in pregame is rendered properly
  @Test
  public void pregame() {
    BasicPyramidSolitaire bpsPre = new BasicPyramidSolitaire();
    PyramidSolitaireTextualView viewSmall = new PyramidSolitaireTextualView(bpsPre);

    assertEquals("", bpsPre.toString());
  }

  // test that a small game is rendered properly
  @Test
  public void smallGame() {
    /*
            A♣
          2♣  3♣
     */
    PyramidSolitaireModel small = new BasicPyramidSolitaire();
    small.startGame(small.getDeck(), false, 2, 1);
    PyramidSolitaireTextualView viewSmall = new PyramidSolitaireTextualView(small);

    assertEquals("  A♣\n2♣  3♣\nDraw: 4♣", viewSmall.toString());
  }

  // test that blank pyramid cards are rendered properly
  @Test
  public void smallMoveGame() {
    /*
            A♣     ->     A♣
          2♣  J♣
     */
    PyramidSolitaireModel smallMove = new BasicPyramidSolitaire();
    List<Card> oneTwoEleven = new ArrayList<>();
    oneTwoEleven.addAll(smallMove.getDeck());
    // Create the above pyramid.
    oneTwoEleven.set(2, new Card(Suit.CLUBS, Value.JACK));
    oneTwoEleven.set(10, new Card(Suit.CLUBS, Value.THREE));
    smallMove.startGame(oneTwoEleven, false, 2, 1);
    // Remove the 2 and Jack.
    smallMove.remove(1, 0, 1, 1);
    PyramidSolitaireTextualView viewMove = new PyramidSolitaireTextualView(smallMove);

    assertEquals("  A♣\n\nDraw: 4♣", viewMove.toString());
  }

  // test a pyramid with an incomplete draw pile
  @Test
  public void incDraw() {
    PyramidSolitaireModel incDraw = new BasicPyramidSolitaire();
    incDraw.startGame(incDraw.getDeck(), false, 9, 7); // 45 + 7 = 52
    PyramidSolitaireTextualView viewIncDraw = new PyramidSolitaireTextualView(incDraw);

    incDraw.discardDraw(0);
    incDraw.discardDraw(2);
    incDraw.discardDraw(6);
    assertTrue(viewIncDraw.toString().endsWith("Draw: .  , 8♠, .  , 10♠, J♠, Q♠, ."));
  }

  // test a pyramid with an empty draw pile
  @Test
  public void mtDraw() {
    PyramidSolitaireModel mtDraw = new BasicPyramidSolitaire();
    mtDraw.startGame(mtDraw.getDeck(), false, 9, 7); // 45 + 7 = 52
    PyramidSolitaireTextualView viewMtDraw = new PyramidSolitaireTextualView(mtDraw);

    // Discard everything in the draw pile.
    for (int i = 0; i < 7; i++) {
      mtDraw.discardDraw(i);
    }

    assertTrue(viewMtDraw.toString().endsWith("Draw:"));
  }

  // test a pyramid that's empty (win)
  @Test
  public void win() {
    /*
          K♣
        2♣  J♣
    */
    PyramidSolitaireModel win = new BasicPyramidSolitaire();
    List<Card> winDeck = new ArrayList<>();
    winDeck.addAll(win.getDeck());
    // Create the above pyramid.
    winDeck.set(0, new Card(Suit.CLUBS, Value.KING));
    winDeck.set(12, new Card(Suit.CLUBS, Value.ACE));
    winDeck.set(2, new Card(Suit.CLUBS, Value.JACK));
    winDeck.set(10, new Card(Suit.CLUBS, Value.THREE));
    win.startGame(winDeck, false, 2, 1);
    // Remove the three cards.
    win.remove(1, 0, 1, 1);
    win.remove(0, 0);
    PyramidSolitaireTextualView viewWin = new PyramidSolitaireTextualView(win);

    assertEquals("You win!", viewWin.toString());
  }

  // test a game-over state
  @Test
  public void over() {
    PyramidSolitaireModel over = new BasicPyramidSolitaire();
    over.startGame(over.getDeck(), false, 2, 0);
    PyramidSolitaireTextualView viewOver = new PyramidSolitaireTextualView(over);

    assertEquals("Game over. Score: 6", viewOver.toString());
  }

  // test the render method without exception
  @Test
  public void render() {
    PyramidSolitaireModel m = new BasicPyramidSolitaire();
    Appendable out = new StringBuffer();
    PyramidSolitaireView view = new PyramidSolitaireTextualView(m, out);

    assertEquals(m.toString(), out.toString()); // view should be appended to out
  }

  // test that the render method can throw an IO exception
  @Test(expected = IOException.class)
  public void renderIO() throws IOException {
    PyramidSolitaireModel m = new BasicPyramidSolitaire();
    PipedWriter out = new PipedWriter();
    PyramidSolitaireView view = new PyramidSolitaireTextualView(m, out);
    out.close();
    view.render();
  }

  // test that a relaxed game is rendered properly
  @Test
  public void relaxed() {
    /*
            A♣
          2♣  3♣
     */
    PyramidSolitaireModel relaxed = new RelaxedPyramidSolitaire();
    relaxed.startGame(relaxed.getDeck(), false, 2, 1);
    PyramidSolitaireTextualView viewRel = new PyramidSolitaireTextualView(relaxed);

    assertEquals("  A♣\n2♣  3♣\nDraw: 4♣", viewRel.toString());
  }

  // test that a multi game is rendered properly (no spaces)
  @Test
  public void multiNoSpace() {
    PyramidSolitaireModel multi = new MultiPyramidSolitaire();
    multi.startGame(multi.getDeck(), false, 1, 1);
    PyramidSolitaireTextualView viewMul = new PyramidSolitaireTextualView(multi);

    assertEquals("A♣\nDraw: 2♣", viewMul.toString());
  }

  // test that a multi game is rendered properly (spaces)
  @Test
  public void multiSpace() {
    PyramidSolitaireModel multi = new MultiPyramidSolitaire();
    multi.startGame(multi.getDeck(), false, 4, 3);
    PyramidSolitaireTextualView viewMul = new PyramidSolitaireTextualView(multi);

    assertTrue(viewMul.toString().stripLeading().startsWith("A♣  .   2♣  .   3♣\n"));
    assertTrue(viewMul.toString().endsWith("Draw: Q♦, K♦, A♥"));
  }
}