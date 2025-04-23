package edu.ntnu.idi.idatt.ui;

import edu.ntnu.idi.idatt.engine.BoardGame;

public class BoardController {

  BoardGame game;

  public BoardController() {
  }

  public BoardController(BoardGame game) {
    this.game = game;
  }

  public void startGame() {
    game = new BoardGame();
  }

  public void throwDice() {
    game.throwDice();
  }

  public BoardGame getGame() {
    return game;
  }
}
