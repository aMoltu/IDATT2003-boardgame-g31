package edu.ntnu.idi.idatt.game;

import edu.ntnu.idi.idatt.actions.LadderAction;
import edu.ntnu.idi.idatt.board.Board;
import edu.ntnu.idi.idatt.board.Tile;
import edu.ntnu.idi.idatt.dice.Dice;
import java.util.ArrayList;

/**
 * This class is responsible for initiating the game and performing the methods required for the
 * main game loop.
 */
public class BoardGame {

  private Board board;
  private ArrayList<Player> players;
  private Dice dice;
  private final Tile startTile;
  private Player winner;
  private final int tileAmount;

  /**
   * Initiates the game.
   *  Creates a Board, and a set of dice
   *  Declares the start tile
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
   * this is the game cycle of the game. Here every player in the game does their turn in a round
   * in this case a turn consists of a player rolling dice and moving that amount of steps
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
}
