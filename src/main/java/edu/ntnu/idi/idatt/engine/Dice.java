package edu.ntnu.idi.idatt.engine;

import java.util.ArrayList;

/**
 * This is a class representing a set of dice, has methods for rolling them, and counting the number
 * on any die.
 */
public class Dice {

  private final ArrayList<Die> diceSet;

  /**
   * Constructor of the dice class, adds given amount of die to its set.
   *
   * @param numberOfDice an integer given to specify how many dice to have in the set
   */
  public Dice(int numberOfDice) {
    diceSet = new ArrayList<>();
    for (int i = 0; i < numberOfDice; i++) {
      diceSet.add(new Die());
    }
  }

  /**
   * Method to roll the dice.
   *
   * @return the total number of eyes on all the dice rolled
   */
  public int roll() {
    int result = 0;
    for (Die die : diceSet) {
      result += die.roll();
    }
    return result;
  }

  /**
   * Looks at a single die in the set and returns it value.
   *
   * @param dieNumber the die you want to look at.
   * @return the value on the die currently.
   */
  public int getDie(int dieNumber) {
    return diceSet.get(dieNumber).getValue();
  }

}
