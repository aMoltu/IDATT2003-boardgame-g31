package edu.ntnu.idi.idatt.dice;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.ntnu.idi.idatt.game.Player;
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

