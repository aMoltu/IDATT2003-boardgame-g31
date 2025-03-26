package edu.ntnu.idi.idatt.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import edu.ntnu.idi.idatt.model.Player;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Player}.
 */
public class BoardGameTest {

  private BoardGame game;

  @BeforeEach
  void init() {
    game = new BoardGame();
  }

  @Test
  void testConstructor() {
    assertNotNull(game.getBoard().getTile(5));
  }

  @Test
  void testAddingPlayers() {
    Player p1 = new Player("P1");
    Player p2 = new Player("P2");
    game.addPlayer(p1);
    game.addPlayer(p2);

    ArrayList<Player> players = game.getPlayers();

    assertNotNull(players);
    assertEquals(2, players.size());
    assertEquals(p1, players.get(0));
    assertEquals(p2, players.get(1));
  }
}