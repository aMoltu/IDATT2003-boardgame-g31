package edu.ntnu.idi.idatt.model;

/**
 * This class is used to represent a single tile on a game board, and contains methods for when a
 * player lands on the space, and when they leave. As well as having a pointer to the next tile.
 */
public class Tile {

  /**
   * Attributes that the Tile uses: nextTile - that points to the upcoming tile on a board tileId -
   * unique integer, used to see where the tile is landAction - an optional tile action that does
   * something when a player lands on the tile.
   */
  private Tile nextTile;
  private final int tileId;
  private TileAction landAction;
  private int tileX;
  private int tileY;

  /**
   * Constructor for Tile. Used for testing where x and y position does not matter.
   *
   * @param tileId is a unique integer used to reference to the tile.
   */
  public Tile(int tileId) {
    this.tileId = tileId;
  }

  /**
   * Primary constructor for Tile.
   *
   * @param tileId is a unique integer used to reference to the tile.
   * @param x      the x position of a tile relative to other tiles. As x increases, tiles move
   *               rightwards.
   * @param y      the y position of a tile relative to other tiles. As y increases, tiles move
   *               downwards.
   */
  public Tile(int tileId, int x, int y) {
    this.tileId = tileId;
    this.tileX = x;
    this.tileY = y;
  }

  public void setLandAction(TileAction landAction) {
    this.landAction = landAction;
  }

  /**
   * method for when a player is done moving and lands on the tile. if the tile has a land action we
   * perform said action.
   *
   * @param player the player that has landed on this tile.
   */
  public void landPlayer(Player player) {
    System.out.println(
        "player " + player.getName() + " moves to " + player.getCurrentTile().getTileId());
    if (landAction != null) {
      landAction.perform(player);
    }
  }

  /**
   * method that moves the player to the next tile.
   *
   * @param player the player that stood on the space before.
   */
  public void leavePlayer(Player player) {
    if (nextTile != null) {
      player.placeOnTile(nextTile);
    }
  }

  public void setNextTile(Tile nextTile) {
    this.nextTile = nextTile;
  }

  public int getTileId() {
    return tileId;
  }

  public TileAction getLandAction() {
    return landAction;
  }

  public Tile getNextTile() {
    return nextTile;
  }

  public int getTileX() {
    return tileX;
  }

  public int getTileY() {
    return tileY;
  }
}
