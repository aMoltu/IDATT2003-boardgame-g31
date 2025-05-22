package edu.ntnu.idi.idatt.ui;

import edu.ntnu.idi.idatt.ui.controller.SceneController;
import javafx.application.Application;
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

    SceneController sceneController = new SceneController(primaryStage);

    sceneController.setScene("main");
  }
}
