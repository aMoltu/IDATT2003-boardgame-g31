package edu.ntnu.idi.idatt.engine;

import edu.ntnu.idi.idatt.viewmodel.BoardViewModel;
import edu.ntnu.idi.idatt.model.LadderAction;
import edu.ntnu.idi.idatt.model.Board;
import edu.ntnu.idi.idatt.viewmodel.PlayerViewModel;
import edu.ntnu.idi.idatt.model.Tile;
import edu.ntnu.idi.idatt.fileio.BoardFileReaderGson;
import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.viewmodel.TileViewModel;
import edu.ntnu.idi.idatt.ui.BoardGameObserver;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class is responsible for initiating the game and running through the game logics.
 */
public class BoardGame {

  private Board board;
  private ArrayList<Player> players;
  private Dice dice;
  private Tile startTile;
  private Player winner;
  private ArrayList<BoardGameObserver> observers;
  private int activePlayer;
  private StringProperty activePlayerProperty;

  /**
   * Constructs the game. Creates a Board, and a set of dice Declares the start tile
   */
  public BoardGame() {
    players = new ArrayList<>();
    winner = null;
    observers = new ArrayList<>();
    activePlayer = 0;
    activePlayerProperty = new SimpleStringProperty();
    createDice();
  }

  /**
   * Method to add a new player to the game. Players get added to the start tile, and the first
   * player is considered the active player (first to move).
   *
   * @param player the player to be added.
   */
  public void addPlayer(Player player) {
    players.add(player);
    player.placeOnTile(startTile);
    if (players.size() == 1) {
      activePlayerProperty.set(player.getName());
    }
  }

  private void createDice() {
    dice = new Dice(2);
  }

  public void throwDice() {
    Player player = players.get(activePlayer);
    int steps = dice.roll();
    player.move(steps);

    if (player.getCurrentTile().getTileId() == board.getTileAmount()) {
      winner = player;
    } else {
      activePlayer = (activePlayer + 1) % players.size();
      activePlayerProperty.set(players.get(activePlayer).getName());
    }

    notifyObservers();
  }

  public void addObserver(BoardGameObserver observer) {
    observers.add(observer);
  }

  public void removeObserver(BoardGameObserver observer) {
    observers.remove(observer);
  }

  public void notifyObservers() {
    for (BoardGameObserver observer : observers) {
      observer.update();
    }
  }

  public Player getWinner() {
    return winner;
  }

  public Board getBoard() {
    return board;
  }

  public ArrayList<Player> getPlayers() {
    return players;
  }

  public ReadOnlyStringProperty getActivePlayerProperty() {
    return activePlayerProperty;
  }

  /**
   * Loads a board from a file.
   *
   * @param boardName the name of the board file
   * @return the loaded board
   */
  public Board loadBoardFromFile(String boardName) {
    BoardFileReaderGson reader = new BoardFileReaderGson();
    Path path = FileSystems.getDefault().getPath("src", "main", "resources", "boards", boardName);
    return reader.readBoard(path);
  }

  /**
   * Sets the board based on the game type and board name.
   *
   * @param gameType  the type of game ("ladder" or "trivia")
   * @param boardName the name of the board
   */
  public void setBoard(String gameType, String boardName) {
    board = new Board(gameType, boardName);
    startTile = board.getTile(1);
  }

  /**
   * Sets a custom board from a file.
   *
   * @param filePath the path to the board file
   */
  public void setCustomBoard(String filePath) {
    BoardFileReaderGson reader = new BoardFileReaderGson();
    board = reader.readBoard(Path.of(filePath));
    if (board != null) {
      startTile = board.getTile(1);
    }
  }

  public List<PlayerViewModel> getPlayerViewModels() {
    return players.stream()
        .map(p -> new PlayerViewModel(p.getName(), p.getShape(), p.getColor(), p.getPosition()))
        .collect(Collectors.toList());
  }

  public TileViewModel getTileViewModel(int id) {
    Tile tile = board.getTile(id);

    String action = "";
    Integer nextId = null;

    if (tile.getLandAction() != null) {
      action = tile.getLandAction().getClass().getSimpleName();
      if (tile.getLandAction() instanceof LadderAction) {
        nextId = ((LadderAction) tile.getLandAction()).destinationTile.getTileId();
      }
    }
    return new TileViewModel(id, tile.getX(), tile.getY(), action, nextId);
  }

  public BoardViewModel getBoardViewModel() {
    HashMap<Integer, TileViewModel> tiles = new HashMap<>();

    // Get all tiles from the board and create view models for them
    for (int id = 1; id <= board.getTileAmount(); id++) {
      Tile tile = board.getTile(id);
      if (tile != null) {
        tiles.put(id, getTileViewModel(id));
      }
    }

    return new BoardViewModel(board.getWidth(), board.getHeight(), board.getTileAmount(), tiles);
  }
}
