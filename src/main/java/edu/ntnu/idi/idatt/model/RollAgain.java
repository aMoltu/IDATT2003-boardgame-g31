package edu.ntnu.idi.idatt.model;

import edu.ntnu.idi.idatt.engine.Dice;

/**
 * This class is a tile action that will let the player roll again and keep moving.
 */
public class RollAgain implements TileAction {


  @Override
  public void perform(Player player) {
    player.move(new Dice(2).roll());
  }
}
