package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.engine.BoardGame;
import edu.ntnu.idi.idatt.ui.BoardGameApp;
import javafx.application.Application;


/**
 * The Main class of the game, here the game gets initiated.
 */
public class Main {

  /**
   * initiates the game.
   *
   * @param args defaults Java for main method.
   */
  public static void main(String[] args) {
//    BoardGame game = new BoardGame("firstBoard.json");
//    game.init();
//    BoardGameApp app = new BoardGameApp(args);
    Application.launch(BoardGameApp.class, args);
  }
}