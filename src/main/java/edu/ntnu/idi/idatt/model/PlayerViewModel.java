package edu.ntnu.idi.idatt.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.scene.paint.Color;

public class PlayerViewModel {

  private final String name;
  private final String shape;
  private final Color color;
  private final ReadOnlyIntegerProperty positionProperty;

  public PlayerViewModel(String name, String shape, Color color, IntegerProperty positionProperty) {
    this.name = name;
    this.shape = shape;
    this.color = color;
    this.positionProperty = positionProperty;
  }

  public String getName() {
    return name;
  }

  public String getShape() {
    return shape;
  }

  public Color getColor() {
    return color;
  }

  public ReadOnlyIntegerProperty getPositionProperty() {
    return positionProperty;
  }
}
