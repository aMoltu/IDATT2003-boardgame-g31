package edu.ntnu.idi.idatt.engine;

import edu.ntnu.idi.idatt.model.BoardViewModel;
import edu.ntnu.idi.idatt.model.LadderAction;
import edu.ntnu.idi.idatt.model.Board;
import edu.ntnu.idi.idatt.model.PlayerViewModel;
import edu.ntnu.idi.idatt.model.Tile;
import edu.ntnu.idi.idatt.fileio.BoardFileReaderGson;
import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.model.TileViewModel;
import edu.ntnu.idi.idatt.ui.BoardGameObserver;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * This class is responsible for initiating the game and running through the game logics.
 */
public class BoardGame {

  private final Scanner scanner = new Scanner(System.in);
  private Board board;
  private ArrayList<Player> players;
  private Dice dice;
  private Tile startTile;
  private Player winner;
  private final int tileAmount;
  private ArrayList<BoardGameObserver> observers;
  private int activePlayer;

  /**
   * Constructs the game. Creates a Board, and a set of dice Declares the start tile
   */
  public BoardGame() {
    tileAmount = 90;
    startTile = new Tile(1, 0, 8);
    players = new ArrayList<>();
    winner = null;
    observers = new ArrayList<>();
    activePlayer = 0;
    createBoard();
    createDice();
  }

  /**
   * Initialize board game with a custom board imported from json file.
   */
  public BoardGame(String boardName) {
    tileAmount = 90;
    players = new ArrayList<>();
    winner = null;
    BoardFileReaderGson reader = new BoardFileReaderGson();
    Path path = FileSystems.getDefault().getPath("src", "main", "resources", "boards", boardName);
    board = reader.readBoard(path);
    observers = new ArrayList<>();
    startTile = board.getTile(1);
    createDice();
  }

  /**
   * Here the game gets initiated and by temporary text based ui the user adds the players.
   */
  public void init() {
    int playerCount = 1;
    boolean addMorePlayers = true;
    while (playerCount < 5 && addMorePlayers) {
      System.out.print("Type the name of player " + playerCount + ": ");
      String name = scanner.nextLine();
      this.addPlayer(new Player(name, this));
      playerCount++;

      if (playerCount < 5) {
        String command = "";
        while (!command.equals("y") && !command.equals("n")) {
          System.out.print("Do you want to add more players? y/n: ");
          command = scanner.nextLine();
        }
        if (command.equals("n")) {
          addMorePlayers = false;
        }
      }
    }

    System.out.println("\nAnd the game begins!\n");
    this.loop();
  }

  /**
   * This method runs a new round until a winner is decided and the game is concluded.
   */
  public void loop() {
    while (this.getWinner() == null) {
      this.play();
      scanner.nextLine();
    }

    System.out.println("And the winner is " + this.getWinner().getName());
  }

  /**
   * method to add a new player to the game and places them on the start.
   *
   * @param player the player to be added.
   */
  public void addPlayer(Player player) {
    players.add(player);
    player.placeOnTile(startTile);
  }

  private void createBoard() {
    board = new Board(10, 9);
    board.addTile(startTile);

    for (int i = 2; i <= tileAmount; i++) {
      Tile tile = new Tile(i, (i - 1) % 10, (tileAmount - i) / 10);
      Tile previousTile = board.getTile(i - 1);
      previousTile.setNextTile(tile);
      board.addTile(tile);
    }

    int[] ladderStart = new int[]{5, 9, 39, 60, 29, 80};
    int[] ladderEnd = new int[]{19, 30, 20, 34, 48, 69};
    for (int i = 0; i < ladderStart.length; i++) {
      LadderAction ladderAction = new LadderAction(board.getTile(ladderEnd[i]));
      board.getTile(ladderStart[i]).setLandAction(ladderAction);
    }
  }

  private void createDice() {
    dice = new Dice(2);
  }

  /**
   * This is the game cycle of the game. Here every player in the game does their turn in a round in
   * this case a turn consists of a player rolling dice and moving that amount of steps
   */
  public void play() {
    for (Player player : players) {
      int steps = dice.roll();
      player.move(steps);

      // check if player has won the game
      if (winner == null && player.getCurrentTile().getTileId() == tileAmount) {
        winner = player;
      }
    }
  }

  public void throwDice() {
    Player player = players.get(activePlayer);
    int steps = dice.roll();
    player.move(steps);

    if (player.getCurrentTile().getTileId() == tileAmount) {
      winner = player;
    }
    activePlayer++;
    activePlayer %= players.size();
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

  public int getActivePlayer() {
    return activePlayer;
  }

  public void setBoard(String boardName) {
    BoardFileReaderGson reader = new BoardFileReaderGson();
    Path path = FileSystems.getDefault().getPath("src", "main", "resources", "boards", boardName);
    board = reader.readBoard(path);
    startTile = board.getTile(1);
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

    for (int id = startTile.getTileId(); board.getTile(id) != null; id++) {
      tiles.put(id, getTileViewModel(id));
    }

    return new BoardViewModel(board.getWidth(), board.getHeight(), tileAmount, tiles);
  }
}
