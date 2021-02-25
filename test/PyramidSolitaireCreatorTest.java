import static org.junit.Assert.assertTrue;

import cs3500.pyramidsolitaire.model.hw02.BasicPyramidSolitaire;
import cs3500.pyramidsolitaire.model.hw02.Card;
import cs3500.pyramidsolitaire.model.hw02.PyramidSolitaireModel;
import cs3500.pyramidsolitaire.model.hw04.MultiPyramidSolitaire;
import cs3500.pyramidsolitaire.model.hw04.PyramidSolitaireCreator;
import cs3500.pyramidsolitaire.model.hw04.PyramidSolitaireCreator.GameType;
import cs3500.pyramidsolitaire.model.hw04.RelaxedPyramidSolitaire;
import org.junit.Test;

/**
 * Tests for the {@code PyramidSolitaireCreator} class.
 */
public class PyramidSolitaireCreatorTest {

  PyramidSolitaireCreator creator = new PyramidSolitaireCreator();

  // test the create factory method for a basic model
  @Test
  public void createBasic() {
    PyramidSolitaireModel<Card> basic = creator.create(GameType.BASIC);
    assertTrue(basic instanceof BasicPyramidSolitaire);
  }

  // test the create factory method for a multi model
  @Test
  public void createMulti() {
    PyramidSolitaireModel<Card> multi = creator.create(GameType.MULTIPYRAMID);
    assertTrue(multi instanceof MultiPyramidSolitaire);
  }

  // test the create factory method for a relaxed model
  @Test
  public void createRelaxed() {
    PyramidSolitaireModel<Card> relaxed = creator.create(GameType.RELAXED);
    assertTrue(relaxed instanceof RelaxedPyramidSolitaire);
  }
}