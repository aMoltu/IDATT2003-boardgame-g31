package edu.ntnu.idi.idatt.viewmodel;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.scene.paint.Color;

public record PlayerViewModel(String name, String shape, Color color,
                              ReadOnlyIntegerProperty positionProperty) {

}
