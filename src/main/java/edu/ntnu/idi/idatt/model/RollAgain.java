package edu.ntnu.idi.idatt.model;

import edu.ntnu.idi.idatt.engine.Dice;

/**
 * A tile action that allows the player to roll the dice again and continue moving. When a player
 * lands on this tile, they get an additional turn to move.
 */
public class RollAgain implements TileAction {

  /**
   * Creates a new roll again action.
   */
  public RollAgain() {
  }

  /**
   * Performs the roll again action by rolling two dice and moving the player.
   *
   * @param player The player to perform the action on
   */
  @Override
  public void perform(Player player) {
    player.move(new Dice(2).roll());
  }
}
