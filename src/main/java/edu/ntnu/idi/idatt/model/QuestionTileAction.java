package edu.ntnu.idi.idatt.model;

import edu.ntnu.idi.idatt.ui.QuestionTileObserver;
import java.util.ArrayList;
import java.util.List;

/**
 * A tile action that presents a question to the player and moves them forward if answered
 * correctly.
 */
public class QuestionTileAction implements TileAction {

  private final String question;
  private final String answer;
  private final String type;
  private final Tile destinationTile;
  private final List<QuestionTileObserver> observers;

  public QuestionTileAction(String question, String answer, String type, Tile destinationTile) {
    this.question = question;
    this.answer = answer;
    this.type = type;
    this.destinationTile = destinationTile;
    this.observers = new ArrayList<>();
  }

  public void addObserver(QuestionTileObserver observer) {
    observers.add(observer);
  }

  public void removeObserver(QuestionTileObserver observer) {
    observers.remove(observer);
  }

  @Override
  public void perform(Player player) {
    // Notify all observers that a player has landed on this tile
    for (QuestionTileObserver observer : observers) {
      observer.onQuestionTile(this, player);
    }
  }

  public void movePlayer(Player player) {
    player.placeOnTile(destinationTile);
  }

  public boolean isCorrectAnswer(String playerAnswer) {
    return playerAnswer.trim().equalsIgnoreCase(answer.trim());
  }

  public String getQuestion() {
    return question;
  }

  public String getType() {
    return type;
  }

  public String getAnswer() {
    return answer;
  }

  public Tile getDestinationTile() {
    return destinationTile;
  }
}