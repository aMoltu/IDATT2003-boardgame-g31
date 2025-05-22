package edu.ntnu.idi.idatt.ui.controller;

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

  /**
   * Creates a new scene controller with initial scene and window settings.
   *
   * @param stage        The primary stage for the application
   * @param initialScene The first scene to display
   */
  public SceneController(Stage stage, Scene initialScene) {
    scenes = new HashMap<>();
    this.stage = stage;
    this.stage.setScene(initialScene);
    stage.setWidth(defaultWidth);
    stage.setHeight(defaultHeight);
    stage.show();
  }

  /**
   * Adds a new scene to the controller's scene collection.
   *
   * @param name  Identifier for the scene
   * @param scene The scene to add
   */
  public void addScene(String name, Scene scene) {
    scenes.put(name, scene);
  }

  /**
   * Switches to a different scene while maintaining window dimensions.
   *
   * @param name Identifier of the scene to switch to
   */
  public void setScene(String name) {
    Scene scene = scenes.get(name);
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
