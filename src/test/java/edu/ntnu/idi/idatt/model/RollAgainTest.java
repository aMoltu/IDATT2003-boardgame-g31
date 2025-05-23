package edu.ntnu.idi.idatt.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link RollAgain}.
 */
public class RollAgainTest {

  @Test
  void testPerformAction() {
    Player player = new Player("name");
    Tile startTile = new Tile(0);
    Tile endTile = new Tile(1);
    startTile.setNextTile(endTile);
    player.placeOnTile(startTile);

    RollAgain action = new RollAgain();
    action.perform(player);

    assertEquals(endTile, player.getCurrentTile());
  }

  @Test
  void testPerformActionWithNoNextTile() {
    Player player = new Player("name");
    Tile startTile = new Tile(0);
    player.placeOnTile(startTile);

    RollAgain action = new RollAgain();
    action.perform(player);

    assertEquals(startTile, player.getCurrentTile());
  }
}
