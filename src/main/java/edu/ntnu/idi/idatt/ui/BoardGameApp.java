package edu.ntnu.idi.idatt.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class BoardGameApp extends Application {

  @Override
  public void start(Stage primaryStage) {
    BoardController controller = new BoardController();
    controller.startGame();
    BoardView view = new BoardView(controller);

    Scene scene = new Scene(view.getRoot(), 400, 300);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  //  public BoardGameApp(String[] args) {
//    launch(args);
//  }
//  public static void main(String[] args) {
//    launch(args);
//  }

}
