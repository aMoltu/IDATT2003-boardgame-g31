package edu.ntnu.idi.idatt.ui;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneController {

  Stage stage;
  HashMap<String, Scene> scenes;

  public SceneController(Stage stage, Scene initialScene) {
    scenes = new HashMap<>();
    this.stage = stage;
    this.stage.setScene(initialScene);
    stage.show();
  }

  public void addScene(String name, Scene scene) {
    scenes.put(name, scene);
  }

  public void setScene(String name) {
    Scene scene = scenes.get(name);
    if (scene != null) {
      stage.setScene(scene);
    } else {
      System.err.println("No scene with name " + name + " found");
    }
  }
}
