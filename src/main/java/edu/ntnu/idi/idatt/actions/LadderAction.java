package edu.ntnu.idi.idatt.actions;

import edu.ntnu.idi.idatt.board.Tile;
import edu.ntnu.idi.idatt.game.Player;

/**
 * performs an action on a player by moving them to some predefined destination tile.
 */
public class LadderAction implements TileAction {

  public Tile destinationTile;

  /**
   * Creates a new LadderAction that moves a player to the specified destination tile.
   *
   * @param destinationTile the tile to which the player will be moved
   */
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
