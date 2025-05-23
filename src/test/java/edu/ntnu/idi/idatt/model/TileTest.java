package edu.ntnu.idi.idatt.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Tile}.
 */
public class TileTest {

  @Test
  void testLeavePlayer() {
    Tile tile = new Tile(23);
    Tile nextTile = new Tile(68);
    tile.setNextTile(nextTile);
    Player player = new Player("name");
    player.placeOnTile(tile);

    tile.leavePlayer(player);
    assertEquals(nextTile, player.getCurrentTile());
  }

  @Test
  void testLeavePlayerWithNoNextTile() {
    Tile tile = new Tile(23);
    Player player = new Player("name");
    player.placeOnTile(tile);

    tile.leavePlayer(player);
    assertEquals(tile, player.getCurrentTile());
  }

  @Test
  void testSetNextTile() {
    Tile tile = new Tile(23);
    Tile nextTile = new Tile(68);
    tile.setNextTile(nextTile);
    assertEquals(nextTile, tile.getNextTile());
  }
}
