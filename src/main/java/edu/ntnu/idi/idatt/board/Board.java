package edu.ntnu.idi.idatt.board;

import java.util.HashMap;
import java.util.Map;

public class Board {

  private final Map<Integer, Tile> tiles;

  /**
   * Creates a new Board that will store some tiles.
   */
  public Board() {
    tiles = new HashMap<>();
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
}
