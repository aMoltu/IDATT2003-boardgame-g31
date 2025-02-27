package edu.ntnu.idi.idatt.actions;

import edu.ntnu.idi.idatt.dice.Dice;
import edu.ntnu.idi.idatt.game.Player;

/**
 * This class is a tile action that will make the player roll 2 dice and move that amount of spaces.
 */
public class RollAgain implements TileAction {


  @Override
  public void perform(Player player) {
    player.move(new Dice(2).roll());
  }
}
