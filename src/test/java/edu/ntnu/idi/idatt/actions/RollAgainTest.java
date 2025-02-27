package edu.ntnu.idi.idatt.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.ntnu.idi.idatt.board.Tile;
import edu.ntnu.idi.idatt.game.Player;
import org.junit.jupiter.api.Test;

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
