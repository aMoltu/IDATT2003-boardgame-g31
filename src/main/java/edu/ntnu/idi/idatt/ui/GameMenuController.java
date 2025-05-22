package edu.ntnu.idi.idatt.ui;

import edu.ntnu.idi.idatt.engine.BoardGame;
import edu.ntnu.idi.idatt.fileio.BoardFileWriterGson;
import edu.ntnu.idi.idatt.model.Player;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import edu.ntnu.idi.idatt.fileio.PlayerCsvReader;
import edu.ntnu.idi.idatt.fileio.PlayerCsvWriter;

public class GameMenuController {

  private final List<String> playerNames = new ArrayList<>();
  private final List<String> playerShapes = new ArrayList<>();
  private final List<Color> playerColors = new ArrayList<>();
  private String selectedBoard = "Default";
  private String selectedGame = "Ladder Game";
  private BoardGameObserver observer;
  private BoardGame game;
  private SceneController sceneController;

  public GameMenuController(BoardGame game, SceneController sceneController) {
    this.game = game;
    this.sceneController = sceneController;
  }

  public void setSelectedGame(String selectedGame) {
    this.selectedGame = selectedGame;
  }

  public void setSelectedBoard(String selectedBoard) {
    this.selectedBoard = selectedBoard;
  }

  public void setObserver(BoardGameObserver observer) {
    this.observer = observer;
  }

  public void setGame(BoardGame game) {
    this.game = game;
  }

  public BoardGame getGame() {
    return game;
  }

  public void addPlayer(TextField playerNameField, String playerShape, Color playerColor,
      ObservableList<String> observablePlayerList) {
    String playerName = playerNameField.getText().trim();
    if (!playerName.isEmpty() && !playerNames.contains(playerName) && playerNames.size() < 5) {
      playerNames.add(playerName);
      playerShapes.add(playerShape);
      playerColors.add(playerColor);
      observablePlayerList.add(playerName);
      playerNameField.clear();
    } else if (playerNames.contains(playerName)) {
      showAlert("Duplicate name", "A player with this name is already added");
    } else if (playerNames.size() >= 5) {
      showAlert("Max players", "The player count can not exceed 5");
    } else if (playerName.isEmpty()) {
      showAlert("Empty name", "PLayer name cannot be empty");
    } else {
      showAlert("Unknown error", "Could not add player");
    }
  }

  public void removePlayer(ListView<String> playerListView,
      ObservableList<String> observablePlayerList) {
    String selectedPlayer = playerListView.getSelectionModel().getSelectedItem();
    if (selectedPlayer != null) {
      int i = playerNames.indexOf(selectedPlayer);
      playerNames.remove(i);
      playerShapes.remove(i);
      playerColors.remove(i);
      observablePlayerList.remove(i);
    }
  }

  public void addPlayers(Path path, ObservableList<String> observablePlayerList) {
    List<Player> players = new PlayerCsvReader().read(path);
    for (Player player : players) {
      playerNames.add(player.getName());
      playerShapes.add(player.getShape());
      playerColors.add(player.getColor());
      observablePlayerList.add(player.getName());
    }
  }

  public void exportPlayers(Path path) {
    List<Player> players = new ArrayList<>();
    for (int i = 0; i < playerNames.size(); i++) {
      players.add(new Player(playerNames.get(i), playerShapes.get(i), playerColors.get(i)));
    }
    try {
      new PlayerCsvWriter().write(path, players);
      showAlert("Success", "Players exported successfully!");
    } catch (IOException e) {
      showAlert("Error", "Failed to export players: " + e.getMessage());
    }
  }

  public void exportBoard(Path path) {
    try {
      // Initialize the board first
      if (selectedBoard.endsWith(".json")) {
        game.setCustomBoard(selectedBoard);
      } else {
        game.setBoard(selectedGame, selectedBoard);
      }

      BoardFileWriterGson writer = new BoardFileWriterGson();
      writer.writeBoard(game.getBoard(), path);

      // Show success message
      showAlert("Success", "Board exported successfully!");
    } catch (IOException e) {
      showAlert("Error", "Failed to export board: " + e.getMessage());
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
    if (selectedBoard.endsWith(".json")) {
      game.setCustomBoard(selectedBoard);
    } else {
      game.setBoard(selectedGame, selectedBoard);
    }

    //Add players to the game
    for (int i = 0; i < playerNames.size(); i++) {
      game.addPlayer(
          new Player(playerNames.get(i), playerShapes.get(i), playerColors.get(i), game));
    }

    game.notifyObservers();

    if (selectedGame.equals("Ladder Game")) {
      sceneController.setScene("game1");
    } else {
      sceneController.setScene("game2");
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
