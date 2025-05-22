package edu.ntnu.idi.idatt.engine;

import java.util.Random;

/**
 * A six-sided die with roll and value retrieval functionality.
 */
public class Die {

  private int lastRolledValue;
  private final Random rand = new Random();

  /**
   * Rolls the die and returns a random value between 1 and 6.
   *
   * @return The new value of the die
   */
  public int roll() {
    lastRolledValue = rand.nextInt(1, 7);
    return lastRolledValue;
  }

  public int getValue() {
    return lastRolledValue;
  }

}
