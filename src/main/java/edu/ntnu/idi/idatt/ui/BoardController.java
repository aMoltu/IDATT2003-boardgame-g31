package edu.ntnu.idi.idatt.ui;

import edu.ntnu.idi.idatt.engine.BoardGame;
import java.util.List;
import javafx.util.Pair;

public class BoardController {

  private BoardGame game;
  private SceneController sceneController;

  public BoardController() {
  }

  public BoardController(BoardGame game, SceneController sceneController) {
    this.game = game;
    this.sceneController = sceneController;
  }

  public void startGame() {
    game = new BoardGame();
  }

  public void throwDice() {
    if (game.getWinner() == null) {
      game.throwDice();
    }
  }

  public BoardGame getGame() {
    return game;
  }
}
