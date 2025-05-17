package edu.ntnu.idi.idatt.model;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.scene.paint.Color;

public record PlayerViewModel(String name, String shape, Color color,
                              ReadOnlyIntegerProperty positionProperty) {

}
