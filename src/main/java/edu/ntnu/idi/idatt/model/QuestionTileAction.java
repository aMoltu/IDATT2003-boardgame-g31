package edu.ntnu.idi.idatt.model;

import edu.ntnu.idi.idatt.observer.QuestionTileObserver;
import java.util.ArrayList;
import java.util.List;

/**
 * A tile action that presents a question to the player and moves them forward if answered
 * correctly. Implements the observer pattern to notify observers when a player lands on this tile.
 */
public class QuestionTileAction implements TileAction {

  private final String question;
  private final String answer;
  private final String type;
  private final Tile destinationTile;
  private final List<QuestionTileObserver> observers;

  /**
   * Creates a new question tile action with specified question, answer, type and destination.
   *
   * @param question        The question to present to the player
   * @param answer          The correct answer to the question
   * @param type            The category of the question (e.g., "math", "science")
   * @param destinationTile The tile to move the player to if they answer correctly
   */
  public QuestionTileAction(String question, String answer, String type, Tile destinationTile) {
    this.question = question;
    this.answer = answer;
    this.type = type;
    this.destinationTile = destinationTile;
    this.observers = new ArrayList<>();
  }

  /**
   * Registers a new observer to be notified when a player lands on this tile.
   *
   * @param observer The observer to register
   */
  public void addObserver(QuestionTileObserver observer) {
    observers.add(observer);
  }

  /**
   * Notifies all registered observers that a player has landed on this tile.
   *
   * @param player The player who landed on the tile
   */
  @Override
  public void perform(Player player) {
    // Create a copy of the observers list to avoid ConcurrentModificationException
    List<QuestionTileObserver> observersCopy = new ArrayList<>(observers);
    // Clear the original observers list
    observers.clear();
    // Notify all observers that a player has landed on this tile
    for (QuestionTileObserver observer : observersCopy) {
      observer.onQuestionTile(this, player);
    }
  }

  /**
   * Moves the player to the destination tile.
   *
   * @param player The player to move
   */
  public void movePlayer(Player player) {
    player.placeOnTile(destinationTile);
  }

  /**
   * Checks if the player's answer matches the correct answer. Comparison is case-insensitive and
   * ignores leading/trailing whitespace.
   *
   * @param playerAnswer The answer provided by the player
   * @return true if the answer is correct, false otherwise
   */
  public boolean isCorrectAnswer(String playerAnswer) {
    return playerAnswer.trim().equalsIgnoreCase(answer.trim());
  }

  /**
   * Gets the question text.
   *
   * @return The question to be presented
   */
  public String getQuestion() {
    return question;
  }

  /**
   * Gets the question category.
   *
   * @return The type/category of the question
   */
  public String getType() {
    return type;
  }

  /**
   * Gets the correct answer.
   *
   * @return The answer to the question
   */
  public String getAnswer() {
    return answer;
  }

  /**
   * Gets the destination tile for correct answers.
   *
   * @return The tile to move the player to
   */
  public Tile getDestinationTile() {
    return destinationTile;
  }
}