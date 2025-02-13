package edu.ntnu.idi.idatt.dice;

import java.util.Random;

public class Die {
  private int lastRolledValue;
  private final Random rand = new Random();


  public int roll() {
    lastRolledValue = rand.nextInt(1, 7);
    return lastRolledValue;
  }

  public int getValue() {
    return lastRolledValue;
  }

}
