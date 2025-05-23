package edu.ntnu.idi.idatt.engine;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Dice}.
 */
public class DiceTest {

  @Test
  void testSingleDiceValues() {
    Dice dice = new Dice(1);

    for (int i = 0; i < 25; i++) {
      int value = dice.roll();
      assert (0 < value && value < 7);
    }
  }

  @Test
  void testMultipleDiceValues() {
    Dice dice = new Dice(10);

    for (int i = 0; i < 25; i++) {
      int value = dice.roll();
      assert (9 < value && value < 61);
    }
  }
}

