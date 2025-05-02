package edu.ntnu.idi.idatt.ui;

import edu.ntnu.idi.idatt.engine.BoardGame;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class BoardGameApp extends Application implements BoardGameObserver {

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Board Game");

    SceneController sceneController = new SceneController(primaryStage);
    BoardGame game = new BoardGame(); // consider making a game factory

    GameMenuController gameMenuController = new GameMenuController(game, sceneController);
    GameMenuView gameMenuView = new GameMenuView(gameMenuController);

    BoardController boardController = new BoardController(game, sceneController);
    BoardView boardView = new BoardView(boardController);

    sceneController.addScene("main", new Scene(gameMenuView.getRoot(), 640, 480));
    sceneController.addScene("game1", new Scene(boardView.getRoot(), 640, 480));

//    GameMenuController controller = new GameMenuController();
//    controller.setObserver(this);
//
//    GameMenuView view = new GameMenuView(controller);
//    Scene scene = new Scene(view.getRoot(), 640, 480);
//    primaryStage.setScene(scene);
//    primaryStage.show();
    sceneController.setScene("main");
  }

  @Override
  public void update() {
//    BoardView boardView = new BoardView(new BoardController(GameMenuController.getGame()));
//    Scene gameScene = new Scene(boardView.getRoot(), 640, 480);
//    primaryStage.setScene(gameScene);
  }
}
