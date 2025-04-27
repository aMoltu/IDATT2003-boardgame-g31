package edu.ntnu.idi.idatt.ui;

import edu.ntnu.idi.idatt.engine.BoardGame;
import edu.ntnu.idi.idatt.model.Board;
import edu.ntnu.idi.idatt.model.Player;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class GameMenuController {
  private final List<String> playerNames = new ArrayList<>();
  private String selectedBoard = "Default";
  private static BoardGame game;
  private BoardGameObserver observer;

  public GameMenuController() {}

  public void setSelectedBoard(String selectedBoard) {
    this.selectedBoard = selectedBoard;
  }

  public void setObserver(BoardGameObserver observer) {
    this.observer = observer;
  }

  public void setGame(BoardGame game) {
    this.game = game;
  }

  public static BoardGame getGame() {
    return game;
  }

  public void addPlayer(TextField playerNameField, ObservableList<String> observablePlayerList) {
    String playerName = playerNameField.getText().trim();
    if (!playerName.isEmpty() && !playerNames.contains(playerName) && playerNames.size() < 4) {
      playerNames.add(playerName);
      observablePlayerList.add(playerName);
      playerNameField.clear();
    } else if (playerNames.contains(playerName)) {
      showAlert("Duplicate name", "A player with this name is already added");
    } else if (playerNames.size() >= 4) {
      showAlert("Max players", "The player count can not exceed 4");
    }
  }

  public void removePlayer(ListView<String> playerListView, ObservableList<String> observablePlayerList) {
    String selectedPlayer = playerListView.getSelectionModel().getSelectedItem();
    if (selectedPlayer != null) {
      playerNames.remove(selectedPlayer);
      observablePlayerList.remove(selectedPlayer);
    }
  }

  public void startGame() {
    if (playerNames.isEmpty()) {
      showAlert("No players selected", "Add at least one player to start game");
      return;
    }
    initializeGame();
  }

  private void initializeGame() {
    if (selectedBoard.equals("Default")) {
      this.game = new BoardGame();
    } else {
      this.game = new BoardGame(selectedBoard);
    }

    //Add players to the game
    for (String playerName : playerNames) {
      game.addPlayer(new Player(playerName, game));
    }

    BoardView boardView = new BoardView(new BoardController(game));
    if (observer != null) {
      observer.update();
    }
  }

  private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
