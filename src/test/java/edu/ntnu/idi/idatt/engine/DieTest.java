package edu.ntnu.idi.idatt.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Die}.
 */
public class DieTest {

  @Test
  void testRollReturnsValidValue() {
    Die die = new Die();
    int value = die.roll();
    assertTrue(value > 0 && value < 7);
  }

  @Test
  void testGetValueMatchesLastRoll() {
    Die die = new Die();
    int rollValue = die.roll();
    int getValue = die.getValue();
    assertEquals(rollValue, getValue);
  }

  @Test
  void testMultipleRollsProduceDifferentValues() {
    Die die = new Die();
    int firstRoll = die.roll();
    int secondRoll = die.roll();
    assertNotEquals(firstRoll, secondRoll);
  }
}

