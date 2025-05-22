package edu.ntnu.idi.idatt.engine;

import java.util.ArrayList;

/**
 * A collection of dice with roll and value tracking functionality.
 */
public class Dice {

  private final ArrayList<Die> diceSet;

  /**
   * Creates a new set of dice with the specified number of dice.
   *
   * @param numberOfDice Number of dice in the set
   */
  public Dice(int numberOfDice) {
    diceSet = new ArrayList<>();
    for (int i = 0; i < numberOfDice; i++) {
      diceSet.add(new Die());
    }
  }

  /**
   * Rolls all dice in the set and returns their total value.
   *
   * @return Sum of all dice values
   */
  public int roll() {
    int result = 0;
    for (Die die : diceSet) {
      result += die.roll();
    }
    return result;
  }

  /**
   * Gets the current value of a specific die in the set.
   *
   * @param dieNumber Index of the die to check (0-based)
   * @return Current value of the specified die
   */
  public int getDie(int dieNumber) {
    return diceSet.get(dieNumber).getValue();
  }

}
