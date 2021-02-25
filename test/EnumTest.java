import static org.junit.Assert.assertEquals;

import cs3500.pyramidsolitaire.model.hw02.Suit;
import cs3500.pyramidsolitaire.model.hw02.Value;
import org.junit.Test;

/**
 * Tests for the {@code Suit} and {@code Value} enums.
 */
public class EnumTest {

  // test symbol getter
  @Test
  public void symbol() {
    Suit clubs = Suit.CLUBS;
    Suit diamonds = Suit.DIAMONDS;
    Suit hearts = Suit.HEARTS;
    Suit spades = Suit.SPADES;
    assertEquals('♣', clubs.getSymbol());
    assertEquals('♦', diamonds.getSymbol());
    assertEquals('♥', hearts.getSymbol());
    assertEquals('♠', spades.getSymbol());
  }

  // test value getter
  @Test
  public void val() {
    Value two = Value.TWO;
    Value ten = Value.TEN;
    Value ace = Value.ACE;
    Value king = Value.KING;
    assertEquals(2, two.getValue());
    assertEquals(10, ten.getValue());
    assertEquals(1, ace.getValue());
    assertEquals(13, king.getValue());
  }
}