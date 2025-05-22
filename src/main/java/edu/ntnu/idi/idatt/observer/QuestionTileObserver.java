package edu.ntnu.idi.idatt.observer;

import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.model.QuestionTileAction;

/**
 * Interface for observers that want to be notified when a player lands on a question tile.
 */
public interface QuestionTileObserver {

  /**
   * Called when a player lands on a question tile.
   *
   * @param action the question tile action
   * @param player the player that landed on the tile
   */
  void onQuestionTile(QuestionTileAction action, Player player);
} 