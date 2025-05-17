package edu.ntnu.idi.idatt.ui;

import edu.ntnu.idi.idatt.engine.BoardGame;
import edu.ntnu.idi.idatt.model.Player;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class BoardGameApp extends Application {

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
    BoardView boardView = new BoardView(game, boardController);

    sceneController.addScene("main", new Scene(gameMenuView.getRoot()));
    sceneController.addScene("game1", new Scene(boardView.getRoot()));
    
    sceneController.setScene("main");
  }
}
