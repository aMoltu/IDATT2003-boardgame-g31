package edu.ntnu.idi.idatt.board;

import edu.ntnu.idi.idatt.actions.LadderAction;
import edu.ntnu.idi.idatt.actions.TileAction;
import edu.ntnu.idi.idatt.game.Player;

public class Tile {

  private Tile nextTile;
  private int tileId;
  private TileAction landAction;

  public Tile(int tileId) {
    this.tileId = tileId;
  }

  public void landPlayer(Player player) {
    landAction = new LadderAction(this);
    landAction.perform(player);
  }

  public void leavePlayer(Player player) {
    player.placeOnTile(nextTile);
  }

  public void setNextTile(Tile nextTile) {
    this.nextTile = nextTile;
  }

  public int getTileId() {
    return tileId;
  }
}
