import cs3500.pyramidsolitaire.model.hw04.RelaxedPyramidSolitaire;

/**
 * Tests for the {@code PyramidSolitaireTextualController} class, to be used on a relaxed model.
 */
public class RelaxedPyramidSolitaireTextualControllerTest extends
    BasicPyramidSolitaireTextualControllerTest {

  // model is a relaxed-pyramid
  @Override
  public void initialize() {
    this.m = new RelaxedPyramidSolitaire();
  }
}