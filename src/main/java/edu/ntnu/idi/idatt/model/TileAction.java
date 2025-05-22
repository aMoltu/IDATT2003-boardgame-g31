package edu.ntnu.idi.idatt.model;

/**
 * Interface for defining player actions triggered by landing on a game tile.
 */
public interface TileAction {

  /**
   * Executes the tile's action on the specified player.
   *
   * @param player The player to perform the action on
   */
  void perform(Player player);
}
