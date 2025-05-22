package edu.ntnu.idi.idatt.ui.controller;

import edu.ntnu.idi.idatt.engine.BoardGame;
import edu.ntnu.idi.idatt.fileio.BoardFileReaderGson;
import edu.ntnu.idi.idatt.fileio.BoardFileWriterGson;
import edu.ntnu.idi.idatt.fileio.PlayerCsvReader;
import edu.ntnu.idi.idatt.fileio.PlayerCsvWriter;
import edu.ntnu.idi.idatt.model.Board;
import edu.ntnu.idi.idatt.model.Player;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

/**
 * Controller for managing the game menu, including player setup and game configuration.
 */
public class GameMenuController {

  private final List<String> playerNames;
  private final List<String> playerShapes;
  private final List<Color> playerColors;
  private String selectedBoard;
  private String selectedGame;
  private BoardGame game;
  private final SceneController sceneController;

  /**
   * Creates a game menu controller with game and scene management.
   *
   * @param game            The game instance to control
   * @param sceneController Controller for managing scene transitions
   */
  public GameMenuController(BoardGame game, SceneController sceneController) {
    this.game = game;
    this.sceneController = sceneController;
    playerNames = new ArrayList<>();
    playerShapes = new ArrayList<>();
    playerColors = new ArrayList<>();
    selectedBoard = "Default";
    selectedGame = "Ladder Game";
  }

  /**
   * Sets the selected game type.
   *
   * @param selectedGame The game type to select
   */
  public void setSelectedGame(String selectedGame) {
    this.selectedGame = selectedGame;
  }

  /**
   * Sets the selected board configuration.
   *
   * @param selectedBoard The board configuration to select
   */
  public void setSelectedBoard(String selectedBoard) {
    this.selectedBoard = selectedBoard;
  }

  /**
   * Updates the game instance.
   *
   * @param game The new game instance
   */
  public void setGame(BoardGame game) {
    this.game = game;
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
   * Adds a new player to the game if valid.
   *
   * @param playerNameField      Text field containing player name
   * @param playerShape          Selected shape for the player
   * @param playerColor          Selected color for the player
   * @param observablePlayerList List to update with new player
   */
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

  /**
   * Removes a selected player from the game.
   *
   * @param playerListView       List view containing player selection
   * @param observablePlayerList List to update after removal
   */
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

  /**
   * Exports player configurations to a CSV file.
   *
   * @param path File path for export
   */
  public void exportPlayers(Path path) {
    try {
      List<Player> players = new ArrayList<>();
      for (int i = 0; i < playerNames.size(); i++) {
        players.add(new Player(playerNames.get(i), playerShapes.get(i), playerColors.get(i)));
      }
      new PlayerCsvWriter().write(path, players);
      showAlert("Success", "Players exported successfully!");
    } catch (IOException e) {
      showAlert("Error", "Failed to export players: " + e.getMessage());
    }
  }

  /**
   * Exports board configuration to a JSON file.
   *
   * @param path File path for export
   */
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

  /**
   * Displays an information alert dialog.
   *
   * @param title   Alert title
   * @param message Alert message
   */
  private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  /**
   * Handles board file import and updates UI accordingly.
   *
   * @param boardSelector ComboBox for board selection
   */
  public void handleBoardImport(ComboBox<String> boardSelector) {
    File selectedFile = showFileChooser(
        "Select Board File",
        "JSON Files",
        "*.json",
        false
    );

    if (selectedFile != null) {
      importBoard(selectedFile.toPath());
      // Update the board selector
      String boardName = selectedFile.getName();
      if (!boardSelector.getItems().contains(boardName)) {
        boardSelector.getItems().add(boardName);
      }
      boardSelector.setValue(boardName);
      // Store the full path for the selected board
      setSelectedBoard(selectedFile.getAbsolutePath());
    }
  }

  /**
   * Handles board file export.
   */
  public void handleBoardExport() {
    File selectedFile = showFileChooser(
        "Save Board File",
        "JSON Files",
        "*.json",
        true
    );

    if (selectedFile != null) {
      exportBoard(selectedFile.toPath());
    }
  }

  /**
   * Handles player file import and updates UI accordingly.
   *
   * @param observablePlayerList List to update with imported players
   */
  public void handlePlayerImport(ObservableList<String> observablePlayerList) {
    File selectedFile = showFileChooser(
        "Select Player File",
        "CSV Files",
        "*.csv",
        false
    );

    if (selectedFile != null) {
      importPlayers(selectedFile.toPath(), observablePlayerList);
    }
  }

  /**
   * Handles player file export.
   */
  public void handlePlayerExport() {
    File selectedFile = showFileChooser(
        "Save Player File",
        "CSV Files",
        "*.csv",
        true
    );

    if (selectedFile != null) {
      exportPlayers(selectedFile.toPath());
    }
  }

  /**
   * Shows a file chooser dialog for import/export operations.
   *
   * @param title             Dialog title
   * @param filterDescription Description of file filter
   * @param filterExtension   File extension filter
   * @param isSaveDialog      Whether this is a save dialog
   * @return Selected file or null if cancelled
   */
  private File showFileChooser(String title, String filterDescription, String filterExtension,
      boolean isSaveDialog) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(title);
    fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter(filterDescription, filterExtension)
    );

    if (isSaveDialog) {
      // Set appropriate default filename based on the file type
      String defaultName = filterExtension.equals("*.json") ? "board.json" : "players.csv";
      fileChooser.setInitialFileName(defaultName);
      return fileChooser.showSaveDialog(null);
    } else {
      return fileChooser.showOpenDialog(null);
    }
  }

  /**
   * Imports a board configuration from a JSON file.
   *
   * @param path File path for import
   */
  public void importBoard(Path path) {
    try {
      // Read the board file
      BoardFileReaderGson reader = new BoardFileReaderGson();
      Board board = reader.readBoard(path);

      if (board != null) {
        String gameType = "Ladder Game";
        setSelectedGame(gameType);
        setSelectedBoard(path.toString());
        showAlert("Success", "Board imported successfully!");
      } else {
        throw new Exception("Failed to read board file");
      }
    } catch (Exception e) {
      showAlert("Error", "Failed to import board: " + e.getMessage());
    }
  }

  /**
   * Imports player configurations from a CSV file.
   *
   * @param path                 File path for import
   * @param observablePlayerList List to update with imported players
   */
  public void importPlayers(Path path, ObservableList<String> observablePlayerList) {
    try {
      List<Player> players = new PlayerCsvReader().read(path);
      for (Player player : players) {
        playerNames.add(player.getName());
        playerShapes.add(player.getShape());
        playerColors.add(player.getColor());
        observablePlayerList.add(player.getName());
      }
      showAlert("Success", "Players imported successfully!");
    } catch (Exception e) {
      showAlert("Error", "Failed to import players: " + e.getMessage());
    }
  }

  /**
   * Starts the game if at least one player is added.
   */
  public boolean startGame() {
    if (playerNames.isEmpty()) {
      showAlert("No players selected", "Add at least one player to start game");
      return false;
    }
    initializeGame();
    return true;
  }

  /**
   * Initializes the game with selected board and players.
   */
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
}
