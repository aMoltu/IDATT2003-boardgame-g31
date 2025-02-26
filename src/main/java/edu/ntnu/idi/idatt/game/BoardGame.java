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
  private final ArrayList<Player> players;
  private Dice dice;
  private final Tile startTile;
  private Player winner;
  private final int tileAmount = 90;

  public BoardGame() {
    startTile = new Tile(0);
    players = new ArrayList<>();
    winner = null;
    createBoard();
    createDice();
  }

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
      //TODO add actions to some tiles
      board.addTile(tile);
    }
  }

  private void createDice() {
    dice = new Dice(2);
  }

  public void play() {
    for (Player player : players) {
      int steps = dice.roll();
      player.move(steps);
      System.out.println(
          "player " + player.getName() + " moves to " + player.getCurrentTile().getTileId());

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
