package edu.ntnu.idi.idatt.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class BoardGameApp extends Application implements BoardGameObserver {

  private Stage primaryStage;

  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;
    primaryStage.setTitle("Board Game");

    GameMenuController controller = new GameMenuController();
    controller.setObserver(this);

    GameMenuView view = new GameMenuView(controller);
    Scene scene = new Scene(view.getRoot(), 640, 480);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  @Override
  public void update() {
    BoardView boardView = new BoardView(new BoardController(GameMenuController.getGame()));
    Scene gameScene = new Scene(boardView.getRoot(), 640, 480);
    primaryStage.setScene(gameScene);
  }
}
