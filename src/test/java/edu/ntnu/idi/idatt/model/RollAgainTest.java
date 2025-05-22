package edu.ntnu.idi.idatt.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Tile}.
 */
public class RollAgainTest {

  @Test
  void testPerformAction() {
    Player player = new Player("name");
    Tile startTile = new Tile(0);
    player.placeOnTile(startTile);
    Tile endTile = new Tile(1);
    startTile.setNextTile(endTile);
    RollAgain action = new RollAgain();
    action.perform(player);

    assertEquals(player.getCurrentTile(), endTile);
  }
}
