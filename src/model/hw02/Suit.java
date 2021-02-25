package cs3500.pyramidsolitaire.model.hw02;

/**
 * Represent a suit in a standard playing card deck.
 */
public enum Suit {
  CLUBS('♣'), DIAMONDS('♦'), HEARTS('♥'), SPADES('♠');

  private final char symbol;

  /**
   * Constructs a playing card suit with corresponding symbol.
   *
   * @param symbol the symbol to initialize
   */
  Suit(char symbol) {
    this.symbol = symbol;
  }

  /**
   * Gets the corresponding symbol for this suit.
   *
   * @return the suit as a String representation
   */
  public char getSymbol() {
    return this.symbol;
  }
}
