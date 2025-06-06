package edu.ntnu.idi.idatt.model;

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
    player.placeOnTile(destinationTile);
  }
}
