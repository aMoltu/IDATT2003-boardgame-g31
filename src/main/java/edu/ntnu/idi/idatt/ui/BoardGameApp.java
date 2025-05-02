package edu.ntnu.idi.idatt.ui;

import edu.ntnu.idi.idatt.engine.BoardGame;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class BoardGameApp extends Application {

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Board Game");

    // used for preventing resizing when launching application
    Scene emptyScene = new Scene(new VBox(), 640, 480);

    SceneController sceneController = new SceneController(primaryStage, emptyScene);
    BoardGame game = new BoardGame(); // consider making a game factory

    GameMenuController gameMenuController = new GameMenuController(game, sceneController);
    GameMenuView gameMenuView = new GameMenuView(gameMenuController);

    BoardController boardController = new BoardController(game, sceneController);
    BoardView boardView = new BoardView(boardController);

    sceneController.addScene("main", new Scene(gameMenuView.getRoot(), 640, 480));
    sceneController.addScene("game1", new Scene(boardView.getRoot(), 640, 480));
    sceneController.setScene("main");
  }
}
