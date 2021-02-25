package cs3500.pyramidsolitaire;

import cs3500.pyramidsolitaire.controller.PyramidSolitaireTextualController;
import cs3500.pyramidsolitaire.model.hw02.Card;
import cs3500.pyramidsolitaire.model.hw02.PyramidSolitaireModel;
import cs3500.pyramidsolitaire.model.hw04.PyramidSolitaireCreator;
import cs3500.pyramidsolitaire.model.hw04.PyramidSolitaireCreator.GameType;
import java.io.InputStreamReader;

/**
 * The main point of entry for a game of pyramid solitaire; initializes the controller and reads in
 * input as command-line arguments. All model and controller errors result in the main method
 * terminating without throwing exceptions.
 */
public final class PyramidSolitaire {

    /**
     * A main method through which to play a game of pyramid solitaire.
     *
     * @param args the input arguments to read and parse
     */
  public static void main(String[] args) {
    PyramidSolitaireCreator creator = new PyramidSolitaireCreator();

    try {
      // If the command length isn't 1 or 3, the command can't be valid.
      if (args.length == 1) {
        // Has to be of type card based on signature of create
        PyramidSolitaireModel<Card> model = creator.create(GameType.valueOf(args[0].toUpperCase()));
        launchGame(model, 7, 3);
      } else if (args.length == 3) {
        PyramidSolitaireModel<Card> model = creator.create(GameType.valueOf(args[0].toUpperCase()));
        int numRows = Integer.parseInt(args[1]);
        int numDraw = Integer.parseInt(args[2]);
        launchGame(model, numRows, numDraw);
      }
    } catch (Exception e) {
      // Illegal state or args encountered; do nothing.
    }
  }

  /**
   * Launches the textual controller with the given parameters to pass to playGame.
   *
   * @param model   the model to play with
   * @param numRows the number of rows in the pyramid
   * @param numDraw the number of visible draw cards
   * @param <K>     the type of cards this model uses
   */
  private static <K> void launchGame(PyramidSolitaireModel<K> model, int numRows, int numDraw) {
    PyramidSolitaireTextualController controller = new PyramidSolitaireTextualController(
        new InputStreamReader(System.in), System.out);
    controller.playGame(model, model.getDeck(), true, numRows, numDraw);
  }
}