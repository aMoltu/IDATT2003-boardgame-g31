package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.ui.BoardGameApp;
import javafx.application.Application;


/**
 * Application entry point that launches the board game.
 */
public class Main {

  /**
   * Launches the JavaFX application.
   *
   * @param args Command line arguments (unused)
   */
  public static void main(String[] args) {
    Application.launch(BoardGameApp.class, args);
  }
}