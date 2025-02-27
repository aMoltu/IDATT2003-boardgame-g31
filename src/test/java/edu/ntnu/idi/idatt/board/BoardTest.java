package edu.ntnu.idi.idatt.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Board}.
 */
public class BoardTest {

  @Test
  void testAddingTile() {
    Board board = new Board();
    Tile tile = new Tile(0);

    board.addTile(tile);

    assertEquals(tile, board.getTile(0));
  }

  @Test
  void testGetFromEmptyBoard() {
    Board board = new Board();

    assertNull(board.getTile(12345));
  }
}
