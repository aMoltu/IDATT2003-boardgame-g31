package edu.ntnu.idi.idatt.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.ntnu.idi.idatt.board.Board;
import edu.ntnu.idi.idatt.board.Tile;
import edu.ntnu.idi.idatt.game.Player;
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
