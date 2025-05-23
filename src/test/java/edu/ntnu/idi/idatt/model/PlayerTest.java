package edu.ntnu.idi.idatt.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Player}.
 */
public class PlayerTest {

  @Test
  void testPlaceOnTile() {
    Tile tile = new Tile(0);
    Player player = new Player("name");
    player.placeOnTile(tile);
    assertEquals(tile, player.getCurrentTile());
  }

  @Test
  void testMoveForward() {
    Tile tile1 = new Tile(0);
    Tile tile2 = new Tile(1);
    Tile tile3 = new Tile(2);
    tile1.setNextTile(tile2);
    tile2.setNextTile(tile3);

    Player player = new Player("name");
    player.placeOnTile(tile1);
    player.move(2);

    assertEquals(tile3, player.getCurrentTile());
  }

  @Test
  void testMoveBeyondBoard() {
    Tile tile = new Tile(0);
    Player player = new Player("name");
    player.placeOnTile(tile);

    player.move(1);
    assertEquals(tile, player.getCurrentTile());
  }
}

