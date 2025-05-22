package edu.ntnu.idi.idatt.observer;

/**
 * Interface for objects that need to be notified of game state changes.
 */
public interface BoardGameObserver {

  /**
   * Called when the game state changes. Observers should update their state based on the new game
   * state.
   */
  void update();
}
