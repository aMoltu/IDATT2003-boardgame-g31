package edu.ntnu.idi.idatt.dice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Die}.
 */
public class DieTest {

  @Test
  void testDieValues() {
    Die die = new Die();

    for (int i = 0; i < 25; i++) {
      int value1 = die.roll();
      int value2 = die.getValue();

      assertEquals(value1, value2);
      assert (0 < value1 && value1 < 7);
    }
  }
}

