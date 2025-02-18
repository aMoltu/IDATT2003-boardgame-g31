package edu.ntnu.idi.idatt.game;

import edu.ntnu.idi.idatt.board.Tile;

/** Class representing a player of the game.
 *
 */
public class Player {
  private final String name;
  private Tile currentTile;

  /**
   * Constructor for the Player class.
   * Assigns name and game.
   *
   * @param name String representing the player's name.
   * @param game BoardGame
   */
  public Player(String name, BoardGame game) {
    this.name = name;
  }

  /** Places player on a specific tile.
   *
   * @param tile that player gets placed on.
   */
  public void placeOnTile(Tile tile) {
    currentTile = tile;
  }

  /** moves the player a given amount of tiles.
   *
   * @param steps int number of tiles the player moves.
   */
  public void move(int steps) {
    for (int i = 0; i < steps; i++) {
      currentTile.leavePlayer(this);
    }
    currentTile.landPlayer(this);
  }

  public String getName() {
    return name;
  }
}
