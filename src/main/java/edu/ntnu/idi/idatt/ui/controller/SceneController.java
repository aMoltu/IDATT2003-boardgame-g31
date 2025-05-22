package edu.ntnu.idi.idatt.ui.controller;

import java.util.HashMap;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneController {

  Stage stage;
  HashMap<String, Scene> scenes;
  int defaultWidth = 1000;
  int defaultHeight = 600;

  public SceneController(Stage stage, Scene initialScene) {
    scenes = new HashMap<>();
    this.stage = stage;
    this.stage.setScene(initialScene);
    stage.setWidth(defaultWidth);
    stage.setHeight(defaultHeight);
    stage.show();
  }

  public void addScene(String name, Scene scene) {
    scenes.put(name, scene);
  }

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
