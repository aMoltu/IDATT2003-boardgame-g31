package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.game.BoardGame;


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
    BoardGame game = new BoardGame();
    game.init();
  }
}