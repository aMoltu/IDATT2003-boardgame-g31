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
    player.placeOnTile(startTile);

    Tile destinationTile = new Tile(5);
    LadderAction action = new LadderAction(destinationTile);

    action.perform(player);

    assertEquals(player.getCurrentTile(), destinationTile);
  }
}
