package edu.ntnu.idi.idatt.ui;

import edu.ntnu.idi.idatt.engine.BoardGame;
import edu.ntnu.idi.idatt.ui.controller.BoardController;
import edu.ntnu.idi.idatt.ui.controller.GameMenuController;
import edu.ntnu.idi.idatt.ui.controller.SceneController;
import edu.ntnu.idi.idatt.ui.view.BoardView;
import edu.ntnu.idi.idatt.ui.view.GameMenuView;
import edu.ntnu.idi.idatt.ui.view.LadderGameView;
import edu.ntnu.idi.idatt.ui.view.TriviaGameView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Main application class for the board game that initializes the JavaFX UI and game components.
 */
public class BoardGameApp extends Application {

  /**
   * Initializes and starts the application, setting up the game window and scenes.
   *
   * @param primaryStage The primary stage for the application
   */
  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Board Game");

    // used for preventing resizing when launching application
    Scene emptyScene = new Scene(new VBox());

    SceneController sceneController = new SceneController(primaryStage, emptyScene);
    BoardGame game = new BoardGame(); // consider making a game factory

    GameMenuController gameMenuController = new GameMenuController(game, sceneController);
    GameMenuView gameMenuView = new GameMenuView(gameMenuController);

    BoardController boardController = new BoardController(game, sceneController);
    BoardView ladderGameView = new LadderGameView(game, boardController);
    BoardView triviaGameView = new TriviaGameView(game, boardController);

    sceneController.addScene("main", new Scene(gameMenuView.getRoot()));
    sceneController.addScene("game1", new Scene(ladderGameView.getRoot()));
    sceneController.addScene("game2", new Scene(triviaGameView.getRoot()));

    sceneController.setScene("main");
  }
}
