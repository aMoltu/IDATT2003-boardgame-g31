package edu.ntnu.idi.idatt.actions;

import edu.ntnu.idi.idatt.dice.Dice;
import edu.ntnu.idi.idatt.game.Player;

public class RollAgain implements TileAction {


  @Override
  public void perform(Player player) {
    player.move(new Dice(2).roll());
  }
}
