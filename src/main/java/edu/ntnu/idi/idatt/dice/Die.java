package edu.ntnu.idi.idatt.dice;

import java.util.Random;

/**
 * This class represents a single six sided die, and contains method to roll and read the value.
 */
public class Die {
  private int lastRolledValue;
  private final Random rand = new Random();

  /**
   * Rolling the die will change the shown value to a new random one between 1 and 6.
   *
   * @return the new value of the die.
   */
  public int roll() {
    lastRolledValue = rand.nextInt(1, 7);
    return lastRolledValue;
  }

  public int getValue() {
    return lastRolledValue;
  }

}
