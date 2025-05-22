package edu.ntnu.idi.idatt.ui.controller;

import edu.ntnu.idi.idatt.engine.BoardGame;
import edu.ntnu.idi.idatt.ui.view.GameMenuView;
import edu.ntnu.idi.idatt.ui.view.LadderGameView;
import edu.ntnu.idi.idatt.ui.view.TriviaGameView;
import java.util.HashMap;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controller for managing scene transitions and window properties.
 */
public class SceneController {

  Stage stage;
  HashMap<String, Scene> scenes;
  int defaultWidth = 1000;
  int defaultHeight = 600;
  private BoardGame game;

  /**
   * Creates a new scene controller with initial scene and window settings.
   *
   * @param stage The primary stage for the application
   */
  public SceneController(Stage stage) {
    scenes = new HashMap<>();
    this.stage = stage;
    stage.setWidth(defaultWidth);
    stage.setHeight(defaultHeight);
    stage.show();
  }

  /**
   * Switches to a different scene while maintaining window dimensions. Recreates the entire scene
   * to make sure old data doesn't carry over.
   *
   * @param name Identifier of the scene to switch to
   */
  public void setScene(String name) {
    Scene scene;
    BoardController boardController;
    switch (name) {
      case "main" -> {
        // Recreate the main menu scene
        game = new BoardGame();
        GameMenuController gameMenuController = new GameMenuController(game, this);
        GameMenuView gameMenuView = new GameMenuView(gameMenuController);
        scene = new Scene(gameMenuView.getRoot());
      }
      case "game1" -> {
        // Recreate the ladder game scene
        boardController = new BoardController(game, this);
        LadderGameView ladderGameView = new LadderGameView(game, boardController);
        game.notifyObservers();
        scene = new Scene(ladderGameView.getRoot());
      }
      case "game2" -> {
        // Recreate the trivia game scene
        boardController = new BoardController(game, this);
        TriviaGameView triviaGameView = new TriviaGameView(game, boardController);
        game.notifyObservers();
        scene = new Scene(triviaGameView.getRoot());
      }
      default -> scene = scenes.get(name);
    }

    if (scene != null) {
      double prevWidth = stage.getWidth();
      double prevHeight = stage.getHeight();

      stage.setScene(scene);
      stage.setWidth(prevWidth);
      stage.setHeight(prevHeight);
    } else {
      System.err.println("No scene with name " + name + " found");
    }
  }
}
