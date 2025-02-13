package edu.ntnu.idi.idatt.dice;

import java.util.ArrayList;

public class Dice {
  private final ArrayList<Die> dice;

  public Dice (int numberOfDice) {
    dice = new ArrayList<>();
    for (int i = 0; i < numberOfDice; i++) {
      dice.add(new Die());
    }
  }

}
