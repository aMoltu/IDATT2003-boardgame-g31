package edu.ntnu.idi.idatt.game;

import edu.ntnu.idi.idatt.actions.LadderAction;
import edu.ntnu.idi.idatt.board.Board;
import edu.ntnu.idi.idatt.board.Tile;
import edu.ntnu.idi.idatt.dice.Dice;
import edu.ntnu.idi.idatt.fileio.BoardFileReaderGson;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is responsible for initiating the game and running through the game logics.
 */
public class BoardGame {

  private final Scanner scanner = new Scanner(System.in);
  private Board board;
  private ArrayList<Player> players;
  private Dice dice;
  private final Tile startTile;
  private Player winner;
  private final int tileAmount;

  /**
   * Constructs the game. Creates a Board, and a set of dice Declares the start tile
   */
  public BoardGame() {
    startTile = new Tile(0);
    tileAmount = 90;
    players = new ArrayList<>();
    winner = null;
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
    board = new Board();
    board.addTile(startTile);

    for (int i = 1; i <= tileAmount; i++) {
      Tile tile = new Tile(i);
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

  public Player getWinner() {
    return winner;
  }

  public Board getBoard() {
    return board;
  }

  public ArrayList<Player> getPlayers() {
    return players;
  }
}
