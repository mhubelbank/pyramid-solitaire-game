import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import cs3500.pyramidsolitaire.model.hw02.Card;
import cs3500.pyramidsolitaire.model.hw02.Suit;
import cs3500.pyramidsolitaire.model.hw02.Value;
import org.junit.Test;

/**
 * Tests for the {@code Card} class.
 */
public class CardTest {

  private Card aceSpades = new Card(Suit.SPADES, Value.ACE);
  private Card otherAceSpades = new Card(Suit.SPADES, Value.ACE);
  private Card aceHearts = new Card(Suit.HEARTS, Value.ACE);
  private Card twoSpades = new Card(Suit.SPADES, Value.TWO);

  // test equality of cards
  @Test
  public void equal() {
    assertEquals(aceSpades, aceSpades);
    assertEquals(aceSpades, otherAceSpades);
    assertFalse(aceSpades == null);
    assertFalse(aceSpades.equals(aceHearts));
    assertFalse(aceHearts.equals(aceSpades));
    assertFalse(aceSpades.equals(twoSpades));
    assertFalse(twoSpades.equals(aceSpades));
  }

  // test hash code of cards to ensure equality reflection
  @Test
  public void hashCodes() {
    assertEquals(aceSpades.hashCode(), aceSpades.hashCode());
    assertEquals(aceSpades.hashCode(), otherAceSpades.hashCode());
    assertNotEquals(aceSpades.hashCode(), aceHearts.hashCode());
    assertNotEquals(aceHearts.hashCode(), aceSpades.hashCode());
    assertNotEquals(aceSpades.hashCode(), twoSpades.hashCode());
    assertNotEquals(twoSpades.hashCode(), aceSpades.hashCode());
  }

  // test that cards are rendered correctly as strings
  @Test
  public void strings() {
    assertEquals("2♣", new Card(Suit.CLUBS, Value.TWO).toString());
    assertEquals("J♦", new Card(Suit.DIAMONDS, Value.JACK).toString());
    assertEquals("Q♥", new Card(Suit.HEARTS, Value.QUEEN).toString());
    assertEquals("K♠", new Card(Suit.SPADES, Value.KING).toString());
    assertEquals("A♣", new Card(Suit.CLUBS, Value.ACE).toString());
    assertEquals("10♦", new Card(Suit.DIAMONDS, Value.TEN).toString());
  }
}