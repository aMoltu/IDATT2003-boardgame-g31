package edu.ntnu.idi.idatt.model;

import edu.ntnu.idi.idatt.engine.BoardGame;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.Color;

/**
 * Game player with position tracking and movement capabilities.
 */
public class Player {

  private final String name;
  private String shape;
  private Color color;
  private Tile currentTile;
  private BoardGame game;
  private final IntegerProperty currentTileId = new SimpleIntegerProperty();

  /**
   * Creates a new player with a name and game reference.
   *
   * @param name Player's name
   * @param game Reference to the game instance
   */
  public Player(String name, BoardGame game) {
    this.name = name;
    this.game = game;
  }

  /**
   * Creates a new player with name, shape, color and game reference.
   *
   * @param name  Player's name
   * @param shape Player's shape identifier
   * @param color Player's color
   * @param game  Reference to the game instance
   */
  public Player(String name, String shape, Color color, BoardGame game) {
    this.name = name;
    this.shape = shape;
    this.color = color;
    this.game = game;
  }

  /**
   * Creates a new player with name, shape and color.
   *
   * @param name  Player's name
   * @param shape Player's shape identifier
   * @param color Player's color
   */
  public Player(String name, String shape, Color color) {
    this.name = name;
    this.shape = shape;
    this.color = color;
  }

  /**
   * Creates a test player with only a name.
   *
   * @param name Player's name
   */
  public Player(String name) {
    this.name = name;
  }

  /**
   * Places the player on a specific tile and updates their position.
   *
   * @param tile The tile to place the player on
   */
  public void placeOnTile(Tile tile) {
    currentTile = tile;
    currentTileId.set(tile.getTileId());
  }

  /**
   * Moves the player forward by the specified number of tiles.
   *
   * @param steps Number of tiles to move forward
   */
  public void move(int steps) {
    for (int i = 0; i < steps; i++) {
      currentTile.leavePlayer(this);
    }
    currentTile.landPlayer(this);

    currentTileId.set(currentTile.getTileId());
  }

  /**
   * Gets the player's current tile.
   *
   * @return The tile the player is currently on
   */
  public Tile getCurrentTile() {
    return currentTile;
  }

  /**
   * Gets the player's current position as a JavaFX property.
   *
   * @return Integer property containing current tile ID
   */
  public IntegerProperty getPosition() {
    return currentTileId;
  }

  /**
   * Gets the player's name.
   *
   * @return Player's name
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the player's shape identifier.
   *
   * @return Player's shape
   */
  public String getShape() {
    return shape;
  }

  /**
   * Gets the player's color.
   *
   * @return Player's color
   */
  public Color getColor() {
    return color;
  }

  /**
   * Gets the game instance this player belongs to.
   *
   * @return Reference to the game
   */
  public BoardGame getGame() {
    return game;
  }
}
