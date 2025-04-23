package edu.ntnu.idi.idatt.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class BoardGameApp extends Application {

  @Override
  public void start(Stage primaryStage) {
    BoardController controller = new BoardController();
    controller.startGame();
    BoardView view = new BoardView(controller);

    Scene scene = new Scene(view.getRoot(), 640, 480);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
