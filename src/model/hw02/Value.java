package cs3500.pyramidsolitaire.model.hw02;

/**
 * Represent a card value in a standard playing card deck.
 */
public enum Value {
  ACE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(
      11), QUEEN(12), KING(13);

  private final int value;

  /**
   * Constructs a playing card value.
   *
   * @param value the value to initialize
   */
  Value(int value) {
    this.value = value;
  }

  /**
   * Gets the corresponding int for this playing card value.
   *
   * @return this value as an int
   */
  public int getValue() {
    return this.value;
  }
}
