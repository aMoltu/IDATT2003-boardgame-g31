package edu.ntnu.idi.idatt.actions;

import edu.ntnu.idi.idatt.board.Tile;
import edu.ntnu.idi.idatt.game.Player;

public class LadderAction implements TileAction {

  public Tile destinationTile;

  public LadderAction(Tile destinationTile) {
    this.destinationTile = destinationTile;
  }

  @Override
  public void perform(Player player) {
    System.out.println(
        player.getName() + " takes ladder from " + player.getCurrentTile().getTileId() + " to "
            + destinationTile.getTileId());
    player.placeOnTile(destinationTile);
  }
}
