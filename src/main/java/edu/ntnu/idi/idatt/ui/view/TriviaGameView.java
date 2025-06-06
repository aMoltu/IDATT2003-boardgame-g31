package edu.ntnu.idi.idatt.ui.view;

import edu.ntnu.idi.idatt.engine.BoardGame;
import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.model.QuestionTileAction;
import edu.ntnu.idi.idatt.model.Tile;
import edu.ntnu.idi.idatt.observer.QuestionTileObserver;
import edu.ntnu.idi.idatt.ui.controller.BoardController;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * View for the trivia game that extends BoardView and implements question tile observation.
 */
public class TriviaGameView extends BoardView implements QuestionTileObserver {

  /**
   * Creates a new trivia game view.
   *
   * @param game       The game instance to display
   * @param controller Controller for board interactions
   */
  public TriviaGameView(BoardGame game, BoardController controller) {
    super(game, controller);
    // Register as observer for all question tiles
    for (int i = 1; i <= game.getBoard().getTileAmount(); i++) {
      Tile tile = game.getBoard().getTile(i);
      if (tile != null && tile.getLandAction() instanceof QuestionTileAction action) {
        action.addObserver(this);
      }
    }
  }

  /**
   * Sets up tile colors based on question categories.
   *
   * @param colors Array to store tile colors
   */
  @Override
  protected void setupTileColors(Color[] colors) {
    for (int i = 1; i <= game.getBoard().getTileAmount(); i++) {
      Tile tile = game.getBoard().getTile(i);
      if (tile != null && tile.getLandAction() instanceof QuestionTileAction action) {
        switch (action.getType()) {
          case "math":
            colors[i] = Color.BLUE;
            break;
          case "science":
            colors[i] = Color.GREEN;
            break;
          case "geography":
            colors[i] = Color.ORANGE;
            break;
          case "history":
            colors[i] = Color.PURPLE;
            break;
          default:
            colors[i] = Color.GRAY;
        }
      } else if (colors[i] == null) {
        colors[i] = Color.WHITE;
      }
    }
  }

  /**
   * Returns the title of the trivia game.
   *
   * @return The game title
   */
  @Override
  protected String getGameTitle() {
    return "The Trivia Game";
  }

  /**
   * Initializes the game scene with all components and question category legend.
   *
   * @return Grid pane containing the complete game layout
   */
  @Override
  protected GridPane initScene() {
    GridPane scene = super.initScene();

    Pane mathBox = createInfoBox(Color.BLUE, "Math Questions");
    Pane scienceBox = createInfoBox(Color.GREEN, "Science Questions");
    Pane geographyBox = createInfoBox(Color.ORANGE, "Geography Questions");
    Pane historyBox = createInfoBox(Color.PURPLE, "History Questions");

    infoSection.getChildren().addAll(mathBox, scienceBox, geographyBox, historyBox);

    return scene;
  }

  /**
   * Handles player landing on a question tile.
   *
   * @param action The question tile action
   * @param player The player who landed on the tile
   */
  @Override
  public void onQuestionTile(QuestionTileAction action, Player player) {
    super.update();
    controller.handleQuestionTileAction(action, player);
  }
}