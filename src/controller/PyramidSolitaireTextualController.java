package cs3500.pyramidsolitaire.controller;

import cs3500.pyramidsolitaire.model.hw02.PyramidSolitaireModel;
import cs3500.pyramidsolitaire.view.PyramidSolitaireTextualView;
import cs3500.pyramidsolitaire.view.PyramidSolitaireView;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

/**
 * Provides a textual controller for interacting with a {@link PyramidSolitaireModel}. Allows a
 * player to play a pyramid solitaire game using valid commands, and quit the game upon request.
 */
public class PyramidSolitaireTextualController implements PyramidSolitaireController {

  private final Readable rd;
  private final Appendable ap;
  private Scanner scan;
  private boolean quit;
  private static final Set<String> MOVES = new HashSet<>(Arrays.asList("rm1", "rm2", "rmwd", "dd"));
  private static final String INV_MOVE_MSG = "Invalid move. Play again. ";

  /**
   * Constructs a textual controller for Pyramid Solitaire using the given IO objects.
   *
   * @param rd the Readable instance to read
   * @param ap the Appendanble instance to append onto
   * @throws IllegalArgumentException if either parameter is null
   */
  public PyramidSolitaireTextualController(Readable rd, Appendable ap)
      throws IllegalArgumentException {
    checkNonNull(rd, "Readable");
    checkNonNull(ap, "Appendable");
    this.rd = rd;
    this.ap = ap;
    quit = false;
  }

  @Override
  public <K> void playGame(PyramidSolitaireModel<K> model, List<K> deck, boolean shuffle,
      int numRows, int numDraw) throws IllegalArgumentException, IllegalStateException {
    checkNonNull(model, "Model");
    checkNonNull(deck, "Deck");

    // Initiate Scanner instance.
    this.scan = new Scanner(this.rd);

    // Try to start the game, or throw an exception if illegal args entered.
    try {
      model.startGame(deck, shuffle, numRows, numDraw);
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("The game cannot be started with these parameters.");
    }

    // Pass the model and Appendable into the view
    PyramidSolitaireView view = new PyramidSolitaireTextualView(model, this.ap);

    try {
      // Render the initial pyramid.
      view.render();
      this.append("\nScore: " + model.getScore() + "\n");
      // Keep playing the game until the player quits or the game is over.
      while (scan.hasNext() && !this.quit && !model.isGameOver()) {
        if (scan.hasNext()) { // Get next token if present.
          String in = scan.next();
          switch (in) {
            case "q":
            case "Q":
              this.quit();
              break;
            case "rm1":
              this.removeOne(model);
              break;
            case "rm2":
              this.removeTwo(model);
              break;
            case "rmwd":
              this.removeWithDraw(model);
              break;
            case "dd":
              this.discardDraw(model);
              break;
            default:
              // Invalid token; keep waiting for valid input.
              break;
          }
          // Break out if quit or game over.
          if (this.quit || model.isGameOver()) {
            break;
          }
          // Render the updated model.
          view.render();
          this.append("\nScore: " + model.getScore() + "\n");
        }
      }

      // If we reach here, either the game is over or they quit.
      // If quit, add the quit messages; render draws pyramid.
      if (this.quit) {
        this.append("Game quit!\nState of game when quit:\n");
        view.render(); // Render the pyramid
        this.append("\nScore: " + model.getScore());
      }
      if (model.isGameOver()) {
        view.render(); // Render either the win or game-over message.
      }

      // If view.render() throws an IO exception, throw state exception.
    } catch (IOException e) {
      throw new IllegalStateException("Appendable-Model communication error.");
      // If Scanner does not have next, there are not enough args => throw state exception.
    } catch (NoSuchElementException e) {
      throw new IllegalStateException("Readable-Model communication error.");
    }

    // Error if the scanner runs out of input and the user didn't quit or finish the game.
    if (!(this.quit || model.isGameOver())) {
      throw new IllegalStateException("Readable-Model communication error.");
    }
  }

  /**
   * Checks if the given object is null.
   *
   * @param o    the object to check
   * @param name the name of the object, to use in the exception message
   * @throws IllegalArgumentException if the object is null
   */
  private static void checkNonNull(Object o, String name) throws IllegalArgumentException {
    if (o == null) {
      throw new IllegalArgumentException(name + " cannot be null.");
    }
  }

  /**
   * Quits the game by setting the quit field's value to true.
   */
  private void quit() {
    this.quit = true;
  }

  /**
   * Appends the given string to this instance's Appendable object, handling IO exceptions as
   * needed.
   *
   * @param s the String to append to the Appendable.
   * @throws IllegalStateException if an IO exception is thrown by the Appendable
   */
  private void append(String s) throws IllegalStateException {
    try {
      this.ap.append(s);
    } catch (IOException e) {
      throw new IllegalStateException("Appendable-Model communication error.");
    }
  }

  /**
   * Performs the remove-one move requested by the user, unless the user requests to quit or the
   * move is invalid.
   *
   * @param model the Pyramid Solitaire Model to update if the move is valid
   * @param <K>   the type of Card used by the model
   */
  private <K> void removeOne(PyramidSolitaireModel<K> model) {
    int row = this.readInt();
    if (this.quit) {
      return;
    }
    int col = this.readInt();
    if (this.quit) {
      return;
    }
    try {
      model.remove(row - 1, col - 1);
    } catch (IllegalArgumentException e) {
      this.append(INV_MOVE_MSG + e.getMessage() + "\n");
    }
  }

  /**
   * Performs the remove-two move requested by the user, unless the user requests to quit or the
   * move is invalid.
   *
   * @param model the Pyramid Solitaire Model to update if the move is valid
   * @param <K>   the type of Card used by the model
   */
  private <K> void removeTwo(PyramidSolitaireModel<K> model) {
    int row1 = this.readInt();
    if (this.quit) {
      return;
    }
    int col1 = this.readInt();
    if (this.quit) {
      return;
    }
    int row2 = this.readInt();
    if (this.quit) {
      return;
    }
    int col2 = this.readInt();
    if (this.quit) {
      return;
    }
    try {
      model.remove(row1 - 1, col1 - 1, row2 - 1, col2 - 1); // One-indexed inputs
    } catch (IllegalArgumentException e) {
      this.append(INV_MOVE_MSG + e.getMessage() + "\n");
    }
  }

  /**
   * Performs the remove-with-draw move requested by the user, unless the user requests to quit or
   * the move is invalid.
   *
   * @param model the Pyramid Solitaire Model to update if the move is valid
   * @param <K>   the type of Card used by the model
   */
  private <K> void removeWithDraw(PyramidSolitaireModel<K> model) {
    int draw = this.readInt();
    if (this.quit) {
      return;
    }
    int row = this.readInt();
    if (this.quit) {
      return;
    }
    int col = this.readInt();
    if (this.quit) {
      return;
    }
    try {
      model.removeUsingDraw(draw - 1, row - 1, col - 1);
    } catch (IllegalArgumentException e) {
      this.append(INV_MOVE_MSG + e.getMessage() + "\n");
    }
  }

  /**
   * Performs the discard-draw move requested by the user, unless the user requests to quit or the
   * move is invalid.
   *
   * @param model the Pyramid Solitaire Model to update if the move is valid
   * @param <K>   the type of Card used by the model
   */
  private <K> void discardDraw(PyramidSolitaireModel<K> model) {
    int draw = this.readInt();
    if (this.quit) {
      return;
    }
    try {
      model.discardDraw(draw - 1);
    } catch (IllegalArgumentException e) {
      this.append(INV_MOVE_MSG + e.getMessage() + "\n");
    }
  }

  /**
   * Read input until the user enters a valid number or wants to quit.
   *
   * @return the user-entered number
   */
  private int readInt() {
    String first = this.scan.next();
    // Can only return once a valid number is entered or they quit.
    if (first.equalsIgnoreCase("q")) {
      this.quit();
      return -1; // Doesn't matter what this value is -- we're going to quit anyway.
    } else {
      try {
        return Integer.parseInt(first);
        // Keep trying! There's got to be a number here somewhere!
      } catch (NumberFormatException e) {
        return this.readInt();
      }
    }
  }

}