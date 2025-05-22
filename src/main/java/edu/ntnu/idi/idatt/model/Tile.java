package edu.ntnu.idi.idatt.model;

/**
 * Represents a single tile on a game board. Contains methods for handling player movement and tile
 * actions. Each tile has a unique ID and position on the board.
 */
public class Tile {

  /**
   * The next tile in sequence on the board.
   */
  private Tile nextTile;

  /**
   * Unique identifier for this tile.
   */
  private final int tileId;

  /**
   * Action to perform when a player lands on this tile.
   */
  private TileAction landAction;

  /**
   * X-coordinate of the tile on the board.
   */
  private int tileX;

  /**
   * Y-coordinate of the tile on the board.
   */
  private int tileY;

  /**
   * Constructor for Tile. Used for testing where x and y position does not matter.
   *
   * @param tileId Unique identifier for the tile
   */
  public Tile(int tileId) {
    this.tileId = tileId;
  }

  /**
   * Primary constructor for Tile.
   *
   * @param tileId Unique identifier for the tile
   * @param x      X-coordinate on the board (increases rightwards)
   * @param y      Y-coordinate on the board (increases downwards)
   */
  public Tile(int tileId, int x, int y) {
    this.tileId = tileId;
    this.tileX = x;
    this.tileY = y;
  }

  /**
   * Sets the action to perform when a player lands on this tile.
   *
   * @param landAction The action to perform
   */
  public void setLandAction(TileAction landAction) {
    this.landAction = landAction;
  }

  /**
   * Handles a player landing on this tile. Performs the tile's action if one is set.
   *
   * @param player The player who landed on the tile
   */
  public void landPlayer(Player player) {
    System.out.println(
        "player " + player.getName() + " moves to " + player.getCurrentTile().getTileId());
    if (landAction != null) {
      landAction.perform(player);
    }
  }

  /**
   * Handles a player leaving this tile. Moves the player to the next tile in sequence.
   *
   * @param player The player who is leaving the tile
   */
  public void leavePlayer(Player player) {
    if (nextTile != null) {
      player.placeOnTile(nextTile);
    }
  }

  /**
   * Sets the next tile in sequence.
   *
   * @param nextTile The next tile on the board
   */
  public void setNextTile(Tile nextTile) {
    this.nextTile = nextTile;
  }

  /**
   * Gets the unique identifier of this tile.
   *
   * @return The tile's ID
   */
  public int getTileId() {
    return tileId;
  }

  /**
   * Gets the action performed when landing on this tile.
   *
   * @return The tile's landing action
   */
  public TileAction getLandAction() {
    return landAction;
  }

  /**
   * Gets the next tile in sequence.
   *
   * @return The next tile on the board
   */
  public Tile getNextTile() {
    return nextTile;
  }

  /**
   * Gets the X-coordinate of this tile.
   *
   * @return The tile's X position
   */
  public int getTileX() {
    return tileX;
  }

  /**
   * Gets the Y-coordinate of this tile.
   *
   * @return The tile's Y position
   */
  public int getTileY() {
    return tileY;
  }
}
