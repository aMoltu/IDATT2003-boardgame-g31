package edu.ntnu.idi.idatt.model;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is a set of Tiles making up a gameboard.
 */
public class Board {

  private final Map<Integer, Tile> tiles;
  private final int width;
  private final int height;

  /**
   * Creates a new Board that will store some tiles.
   *
   * @param width  the width of the board, in tiles.
   * @param height the height of the board, in tiles.
   */
  public Board(int width, int height) {
    tiles = new HashMap<>();
    this.width = width;
    this.height = height;
  }

  /**
   * Adds a Tile to the Board.
   *
   * @param tile the tile to be added
   */
  public void addTile(Tile tile) {
    tiles.put(tile.getTileId(), tile);
  }

  /**
   * Returns a Tile with the corresponding id.
   *
   * @param tileId the id of the desired Tile
   * @return A Tile with corresponding id
   */
  public Tile getTile(int tileId) {
    return tiles.get(tileId);
  }

  /**
   * Returns the width of the board.
   *
   * @return width of the board. the unit is tiles
   */
  public int getWidth() {
    return width;
  }

  /**
   * Returns the height of the board.
   *
   * @return height of the board. the unit is tiles
   */
  public int getHeight() {
    return height;
  }
}
