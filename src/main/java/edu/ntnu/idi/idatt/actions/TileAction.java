package edu.ntnu.idi.idatt.actions;

import edu.ntnu.idi.idatt.game.Player;

/**
 * Defines some action that should be done to a player when they land on a tile
 */
public interface TileAction {

  /**
   * Performs an action on the given player.
   *
   * @param player the player which the action will be performed on
   */
  void perform(Player player);
}
