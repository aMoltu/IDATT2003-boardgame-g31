package edu.ntnu.idi.idatt.board;

import edu.ntnu.idi.idatt.actions.TileAction;
import edu.ntnu.idi.idatt.game.Player;

public class Tile {

  private Tile nextTile;
  private final int tileId;
  private TileAction landAction;

  public Tile(int tileId) {
    this.tileId = tileId;
  }

  public void setLandAction(TileAction landAction) {
    this.landAction = landAction;
  }

  public void landPlayer(Player player) {
    System.out.println(
        "player " + player.getName() + " moves to " + player.getCurrentTile().getTileId());
    if (landAction != null) {
      landAction.perform(player);
    }
  }

  public void leavePlayer(Player player) {
    if (nextTile != null) {
      player.placeOnTile(nextTile);
    }
  }

  public void setNextTile(Tile nextTile) {
    this.nextTile = nextTile;
  }

  public int getTileId() {
    return tileId;
  }
}
