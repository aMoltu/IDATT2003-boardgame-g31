package edu.ntnu.idi.idatt.model;

import edu.ntnu.idi.idatt.engine.BoardGame;
import javafx.scene.paint.Color;

/**
 * Class representing a player of the game.
 */
public class Player {

  private final String name;
  private String shape;
  private Color color;
  private Tile currentTile;
  private BoardGame game;

  /**
   * Constructor for the Player class. Assigns name and game.
   *
   * @param name String representing the player's name.
   * @param game BoardGame
   */
  public Player(String name, BoardGame game) {
    this.name = name;
    this.game = game;
  }

  public Player(String name, String shape, Color color, BoardGame game) {
    this.name = name;
    this.shape = shape;
    this.color = color;
    this.game = game;
  }

  public Player(String name, String shape, Color color) {
    this.name = name;
    this.shape = shape;
    this.color = color;
  }

  /**
   * Simpler constructor for the Player class used for testing. Only assigns name.
   *
   * @param name String representing the player's name.
   */
  public Player(String name) {
    this.name = name;
  }

  /**
   * Places player on a specific tile.
   *
   * @param tile that player gets placed on.
   */
  public void placeOnTile(Tile tile) {
    currentTile = tile;
  }

  /**
   * Moves the player a given amount of tiles.
   *
   * @param steps int number of tiles the player moves.
   */
  public void move(int steps) {
    for (int i = 0; i < steps; i++) {
      currentTile.leavePlayer(this);
    }
    currentTile.landPlayer(this);
  }

  public Tile getCurrentTile() {
    return currentTile;
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

  public BoardGame getGame() {
    return game;
  }
}
