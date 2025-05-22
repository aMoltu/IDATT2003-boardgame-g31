package edu.ntnu.idi.idatt.ui.controller;

import edu.ntnu.idi.idatt.engine.BoardGame;
import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.model.QuestionTileAction;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

/**
 * Controller for managing game board interactions and state.
 */
public class BoardController {

  private BoardGame game;
  private SceneController sceneController;

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
   * Triggers a dice-roll if no winner has been determined.
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

  /**
   * Processes the question tile action and handles player response.
   *
   * @param action The question tile action
   * @param player The player who landed on the tile
   */
  public void handleQuestionTileAction(QuestionTileAction action, Player player) {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Question Tile");
    dialog.setHeaderText(action.getQuestion());
    dialog.setContentText("Please enter your answer:");

    dialog.showAndWait().ifPresent(answer -> {
      if (action.isCorrectAnswer(answer)) {
        showAlert("Correct!", "You answered correctly! Moving forward...");
        action.movePlayer(player);
        game.notifyObservers();
      } else {
        showAlert("Incorrect", "Sorry, that's not the correct answer.");
      }
    });
  }

  /**
   * Displays an alert dialog with the specified title and content.
   *
   * @param title   Alert title
   * @param content Alert message
   */
  public void showAlert(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
  }
}
