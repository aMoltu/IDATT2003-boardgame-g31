package edu.ntnu.idi.idatt.ui;

import edu.ntnu.idi.idatt.engine.BoardGame;

public class BoardController {

  BoardGame game;

  public BoardController() {
  }

  public void startGame() {
    game = new BoardGame();
  }

  public BoardGame getGame() {
    return game;
  }
}
