package edu.ntnu.idi.idatt.game;

import edu.ntnu.idi.idatt.board.Tile;

public class Player {
  private String name;
  private Tile currentTile;

  public Player(String name, BoardGame game) {
    this.name = name;
  }

  public void placeOnTile(Tile tile) {
    this.currentTile = tile;
  }

  public void move(int steps) {
    for (int i = 0; i < steps; i++) {
      currentTile = currentTile.getNextTile();
    }
  }
}
