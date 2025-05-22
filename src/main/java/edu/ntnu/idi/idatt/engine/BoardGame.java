package edu.ntnu.idi.idatt.engine;

import edu.ntnu.idi.idatt.viewmodel.BoardViewModel;
import edu.ntnu.idi.idatt.model.LadderAction;
import edu.ntnu.idi.idatt.model.Board;
import edu.ntnu.idi.idatt.viewmodel.PlayerViewModel;
import edu.ntnu.idi.idatt.model.Tile;
import edu.ntnu.idi.idatt.fileio.BoardFileReaderGson;
import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.viewmodel.TileViewModel;
import edu.ntnu.idi.idatt.observer.BoardGameObserver;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Core game controller managing player turns, dice rolls, and game state.
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
   * Initializes a new game with empty player list and observer list.
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
   * Adds a player to the game and places them on the start tile. The first player added becomes the
   * active player.
   *
   * @param player The player to add to the game
   */
  public void addPlayer(Player player) {
    players.add(player);
    player.placeOnTile(startTile);
    if (players.size() == 1) {
      activePlayerProperty.set(player.getName());
    }
  }

  /**
   * Creates a set of two dice for the game.
   */
  private void createDice() {
    dice = new Dice(2);
  }

  /**
   * Processes a dice roll for the active player. Moves the player and checks for win condition or
   * switches to next player.
   */
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

  /**
   * Registers a new observer to receive game state updates.
   *
   * @param observer The observer to register
   */
  public void addObserver(BoardGameObserver observer) {
    observers.add(observer);
  }

  /**
   * Removes an observer from receiving game state updates.
   *
   * @param observer The observer to remove
   */
  public void removeObserver(BoardGameObserver observer) {
    observers.remove(observer);
  }

  /**
   * Notifies all registered observers of game state changes.
   */
  public void notifyObservers() {
    for (BoardGameObserver observer : observers) {
      observer.update();
    }
  }

  /**
   * Gets the current winner of the game.
   *
   * @return The winning player, or null if no winner yet
   */
  public Player getWinner() {
    return winner;
  }

  /**
   * Gets the current game board.
   *
   * @return The active game board
   */
  public Board getBoard() {
    return board;
  }

  /**
   * Gets the list of players in the game.
   *
   * @return List of all players
   */
  public ArrayList<Player> getPlayers() {
    return players;
  }

  /**
   * Gets the read-only property containing the active player's name.
   *
   * @return String property with active player name
   */
  public ReadOnlyStringProperty getActivePlayerProperty() {
    return activePlayerProperty;
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

  /**
   * Converts all players to view models for UI display.
   *
   * @return List of player view models
   */
  public List<PlayerViewModel> getPlayerViewModels() {
    return players.stream()
        .map(p -> new PlayerViewModel(p.getName(), p.getShape(), p.getColor(), p.getPosition()))
        .collect(Collectors.toList());
  }

  /**
   * Creates a view model for a specific tile.
   *
   * @param id Tile ID to create view model for
   * @return View model containing tile display information
   */
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

  /**
   * Creates a view model for the entire board.
   *
   * @return View model containing board layout and tile information
   */
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

  /**
   * Gets the current value of a specific die.
   *
   * @param i Index of the die (0 or 1)
   * @return Current value of the specified die
   */
  public int getDie(int i) {
    return dice.getDie(i);
  }
}
