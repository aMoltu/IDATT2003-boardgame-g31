package edu.ntnu.idi.idatt.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link LadderAction}.
 */
public class LadderActionTest {

  @Test
  void testPerformAction() {
    Player player = new Player("name");
    Tile startTile = new Tile(0);
    Tile destinationTile = new Tile(5);
    player.placeOnTile(startTile);

    LadderAction action = new LadderAction(destinationTile);
    action.perform(player);

    assertEquals(destinationTile, player.getCurrentTile());
  }
}
