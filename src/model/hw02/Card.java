package cs3500.pyramidsolitaire.model.hw02;

/**
 * Represent a standard playing card.
 */
public final class Card {

  private final Suit suit;
  private final Value value;

  /**
   * Construct a standard playing card.
   *
   * @param suit  the suit of the card
   * @param value the value of the card
   */
  public Card(Suit suit, Value value) {
    this.suit = suit;
    this.value = value;
  }

  /**
   * Gets the corresponding numerical value for this card's value enum.
   *
   * @return the integer value of this card
   */
  public int getVal() {
    return this.value.getValue();
  }

  /**
   * Gets the corresponding suit symbol for this card's suit enum.
   *
   * @return the suit symbol as a String
   */
  public char getSuit() {
    return this.suit.getSymbol();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Card)) {
      return false;
    }
    Card card = (Card) o;
    return this.getSuit() == card.getSuit() && this.getVal() == card.getVal();
  }

  @Override
  public int hashCode() {
    int suitFirstLetter = (int) this.suit.name().charAt(0);
    return (suitFirstLetter * 1000) + this.getVal();
  }

  @Override
  public String toString() {
    int val = this.getVal();
    String suit = Character.toString(this.getSuit());
    String cardString = suit;

    // If J/Q/K/A, add letter; else add number value.
    if (val == 1 || val > 10) {
      char firstChar = this.value.name().charAt(0);
      cardString = firstChar + cardString;
    } else {
      cardString = val + cardString;
    }

    return cardString;
  }
}
