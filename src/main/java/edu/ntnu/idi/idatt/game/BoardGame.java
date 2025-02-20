package edu.ntnu.idi.idatt.game;

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
  private Player currentPlayer;
  private ArrayList<Player> players;
  private Dice dice;
  private Tile startTile;
  private Player winner;
  private static int tileAmount = 90;

  public BoardGame() {
    startTile = new Tile(0);
    winner = null;
  }

  public void addPlayer(Player player) {
    players.add(player);
    player.placeOnTile(startTile);
  }

  public void createBoard() {
    board = new Board();
    board.addTile(startTile);

    for (int i = 1; i <= tileAmount; i++) {
      Tile tile = new Tile(i);
      Tile previousTile = board.getTile(i - 1);
      tile.setNextTile(previousTile);
      //TODO add actions to some tiles
      board.addTile(tile);
    }
  }

  public void createDice() {
    dice = new Dice(2);
  }

  public void play() {
    for (Player player : players) {
      currentPlayer = player;
      int steps = dice.roll();
      currentPlayer.move(steps);

      // check if player has won the game
      if (winner == null && player.getCurrentTile().getTileId() == tileAmount) {
        winner = player;
      }
    }
  }

  public Player getWinner() {
    return winner;
  }

}
