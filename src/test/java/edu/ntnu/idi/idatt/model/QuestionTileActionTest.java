package edu.ntnu.idi.idatt.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link QuestionTileAction}.
 */
public class QuestionTileActionTest {

  @Test
  void testPerformActionWithIncorrectAnswer() {
    Player player = new Player("name");
    Tile startTile = new Tile(0);
    player.placeOnTile(startTile);

    QuestionTileAction action = new QuestionTileAction("What is 2+2?", "4", "math", null);
    action.perform(player);

    assertEquals(startTile, player.getCurrentTile());
  }

  @Test
  void testGetQuestionAndAnswer() {
    String question = "What is 2+2?";
    String answer = "4";
    String type = "math";
    Tile destinationTile = new Tile(5);

    QuestionTileAction action = new QuestionTileAction(question, answer, type, destinationTile);

    assertEquals(question, action.getQuestion());
    assertEquals(answer, action.getAnswer());
    assertEquals(type, action.getType());
  }
} 