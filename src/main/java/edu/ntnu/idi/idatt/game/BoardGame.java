package edu.ntnu.idi.idatt.game;

import edu.ntnu.idi.idatt.board.Board;
import edu.ntnu.idi.idatt.dice.Dice;
import java.util.ArrayList;

/** This class is responsible for initiating the game and performing the methods required for the
 * main game loop.
 *
 */
public class BoardGame {
  private Board board;
  private Player currentPlayer;
  private ArrayList<Player> players;
  private Dice dice;

  public void addPlayer(Player player) {
    players.add(player);
  }

  public void createBoard() {
    board = new Board();
  }

  public void createDice() {
    dice = new Dice(2);
  }

  public void play() {
    for (Player player : players) {
      currentPlayer = player;
      int steps = dice.roll();
      currentPlayer.move(steps);
    }
  }

 public void getWinner() {}

}
