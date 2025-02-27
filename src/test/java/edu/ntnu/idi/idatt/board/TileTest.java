package edu.ntnu.idi.idatt.board;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.ntnu.idi.idatt.game.Player;
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
}
