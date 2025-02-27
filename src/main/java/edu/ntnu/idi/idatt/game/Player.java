package edu.ntnu.idi.idatt.game;

import edu.ntnu.idi.idatt.board.Board;
import edu.ntnu.idi.idatt.board.Tile;

/**
 * Class representing a player of the game.
 */
public class Player {

  private final String name;
  private Tile currentTile;
  private BoardGame game;

  /**
   * Constructor for the Player class. Assigns name and game.
   *
   * @param name String representing the player's name.
   * @param game BoardGame
   */
  public Player(String name, BoardGame game) {
    this.name = name;
    this.game = game;
  }

  /**
   * Simpler constructor for the Player class used for testing. Only assigns name.
   *
   * @param name String representing the player's name.
   */
  public Player(String name) {
    this.name = name;
  }

  /**
   * Places player on a specific tile.
   *
   * @param tile that player gets placed on.
   */
  public void placeOnTile(Tile tile) {
    currentTile = tile;
  }

  /**
   * moves the player a given amount of tiles.
   *
   * @param steps int number of tiles the player moves.
   */
  public void move(int steps) {
    for (int i = 0; i < steps; i++) {
      currentTile.leavePlayer(this);
    }
    currentTile.landPlayer(this);
  }

  public Tile getCurrentTile() {
    return currentTile;
  }

  public String getName() {
    return name;
  }

  public BoardGame getGame() {
    return game;
  }
}
