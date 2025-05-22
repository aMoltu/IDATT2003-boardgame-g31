package edu.ntnu.idi.idatt.ui.controller;

import edu.ntnu.idi.idatt.engine.BoardGame;

/**
 * Controller for managing game board interactions and state.
 */
public class BoardController {

  private BoardGame game;
  private SceneController sceneController;

  /**
   * Creates an empty board controller.
   */
  public BoardController() {
  }

  /**
   * Creates a board controller with game and scene management.
   *
   * @param game            The game instance to control
   * @param sceneController Controller for managing scene transitions
   */
  public BoardController(BoardGame game, SceneController sceneController) {
    this.game = game;
    this.sceneController = sceneController;
  }

  /**
   * Initializes a new game instance.
   */
  public void startGame() {
    game = new BoardGame();
  }

  /**
   * Triggers a dice roll if no winner has been determined.
   */
  public void throwDice() {
    if (game.getWinner() == null) {
      game.throwDice();
    }
  }

  /**
   * Gets the current game instance.
   *
   * @return The active game
   */
  public BoardGame getGame() {
    return game;
  }
}
