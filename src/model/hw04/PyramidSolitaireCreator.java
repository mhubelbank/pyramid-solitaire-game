package cs3500.pyramidsolitaire.model.hw04;

import cs3500.pyramidsolitaire.model.hw02.BasicPyramidSolitaire;
import cs3500.pyramidsolitaire.model.hw02.Card;
import cs3500.pyramidsolitaire.model.hw02.PyramidSolitaireModel;

/**
 * Represent different game types of Pyramid Solitaire, and allow clients to create model instances
 * using a factory method.
 */
public class PyramidSolitaireCreator {

  /**
   * Represent different game types of Pyramid Solitaire, and produces game model instances.
   */
  public enum GameType {
    BASIC, RELAXED, MULTIPYRAMID;
  }

  /**
   * Depending on the value of the parameter, returns an instance of (an appropriate subclass of)
   * PyramidSolitaireModel.
   *
   * @param type the type of {@link MultiPyramidSolitaire} to create
   * @return the instance of the proper {@link MultiPyramidSolitaire} class
   */
  public static PyramidSolitaireModel<Card> create(GameType type) {
    /* I am not sure if there's a way to utilize a factory method here.
       Please let me know if there's a better way to implement this! */
    switch (type) {
      case BASIC:
        return new BasicPyramidSolitaire();
      case RELAXED:
        return new RelaxedPyramidSolitaire();
      case MULTIPYRAMID:
        return new MultiPyramidSolitaire();
      default:
        return null;
    }
  }
}