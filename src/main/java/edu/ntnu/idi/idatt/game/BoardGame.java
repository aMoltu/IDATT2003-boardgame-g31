package edu.ntnu.idi.idatt.game;

import edu.ntnu.idi.idatt.board.Board;
import edu.ntnu.idi.idatt.dice.Dice;
import java.util.ArrayList;

/** This class is responsible for initiating the game and performing the methods required for the
 * main game loop.
 *
 */
public class BoardGame {
  Board board;
  Player currentPlayer;
  ArrayList<Player> players;
  Dice dice;

  void addPlayer(Player player) {
    players.add(player);
  }

  void createBoard() {
    board = new Board();
  }

  void createDice() {
    dice = new Dice(2);
  }

  void play() {}

  void getWinner() {}

}
