package edu.ntnu.idi.idatt.viewmodel;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.scene.paint.Color;

/**
 * View model for a player containing display information.
 *
 * @param name             Player's name
 * @param shape            Player's shape identifier
 * @param color            Player's color
 * @param positionProperty JavaFX property containing the player's current tile ID
 */
public record PlayerViewModel(String name, String shape, Color color,
                              ReadOnlyIntegerProperty positionProperty) {

}
