package edu.ntnu.idi.idatt.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Game board composed of connected tiles with width and height dimensions.
 */
public class Board {

  private final Map<Integer, Tile> tiles;
  private int width;
  private int height;

  private static final int[][] DEFAULT_LAYOUT = {
      {81, 82, 83, 84, 85, 86, 87, 88, 89, 90},
      {80, 79, 78, 77, 76, 75, 74, 73, 72, 71},
      {61, 62, 63, 64, 65, 66, 67, 68, 69, 70},
      {60, 59, 58, 57, 56, 55, 54, 53, 52, 51},
      {41, 42, 43, 44, 45, 46, 47, 48, 49, 50},
      {40, 39, 38, 37, 36, 35, 34, 33, 32, 31},
      {21, 22, 23, 24, 25, 26, 27, 28, 29, 30},
      {20, 19, 18, 17, 16, 15, 14, 13, 12, 11},
      {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}
  };

  private static final int[][] TORNADO_LAYOUT = {
      {1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
      {0, 0, 0, 0, 0, 0, 0, 0, 0, 11},
      {35, 36, 37, 38, 39, 40, 41, 42, 0, 12},
      {34, 0, 0, 0, 0, 0, 0, 43, 0, 13},
      {33, 0, 55, 56, 57, 58, 0, 44, 0, 14},
      {32, 0, 54, 0, 0, 59, 0, 45, 0, 15},
      {31, 0, 53, 0, 0, 0, 0, 46, 0, 16},
      {30, 0, 52, 51, 50, 49, 48, 47, 0, 17},
      {29, 0, 0, 0, 0, 0, 0, 0, 0, 18},
      {28, 27, 26, 25, 24, 23, 22, 21, 20, 19}
  };

  /**
   * Creates an empty game board ready for tile configuration.
   */
  public Board() {
    tiles = new HashMap<>();
  }

  /**
   * Creates a new game board with specified dimensions.
   *
   * @param width  Board width in tiles
   * @param height Board height in tiles
   */
  public Board(int width, int height) {
    tiles = new HashMap<>();
    this.width = width;
    this.height = height;
  }

  /**
   * Creates a new board based on game type and board name. Supports "Ladder Game" and "Trivia Game"
   * with "Default" and "Tornado" board layouts.
   *
   * @param gameType  Type of game to create board for
   * @param boardName Name of the board layout to use
   */
  public Board(String gameType, String boardName) {
    tiles = new HashMap<>();

    int[][] layout = boardName.equals("Default") ? DEFAULT_LAYOUT : TORNADO_LAYOUT;
    createBoardFromLayout(layout);

    if (gameType.equals("Ladder Game")) {
      addLadderActions();
      addRollAgainTile();
    } else if (gameType.equals("Trivia Game")) {
      addTriviaActions();
    } else {
      System.err.println("Invalid game type: " + gameType);
    }
  }

  /**
   * Creates the board structure from a 2D layout array.
   *
   * @param boardLayout 2D array representing the board layout
   */
  private void createBoardFromLayout(int[][] boardLayout) {
    height = boardLayout.length;
    width = boardLayout[0].length;

    // Create tiles based on the layout
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (boardLayout[y][x] != 0) {
          Tile tile = new Tile(boardLayout[y][x], x, y);
          addTile(tile);
        }
      }
    }

    // Connect tiles together
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (boardLayout[y][x] > 0 && boardLayout[y][x] != getTileAmount()) {
          getTile(boardLayout[y][x]).setNextTile(getTile(boardLayout[y][x] + 1));
        }
      }
    }
  }

  /**
   * Adds ladder actions to the board.
   */
  private void addLadderActions() {
    int[] ladderStart = new int[]{5, 9, 39, 60, 29, 80};
    int[] ladderEnd = new int[]{19, 30, 20, 34, 48, 69};
    for (int i = 0; i < ladderStart.length; i++) {
      if (ladderStart[i] < tiles.size() && ladderEnd[i] < tiles.size()) {
        LadderAction ladderAction = new LadderAction(this.getTile(ladderEnd[i]));
        this.getTile(ladderStart[i]).setLandAction(ladderAction);
      }
    }
  }

  /**
   * Adds a RollAgain land action to the board.
   */
  private void addRollAgainTile() {
    tiles.get(32).setLandAction(new RollAgain());
  }

  /**
   * Adds trivia questions to the board.
   */
  private void addTriviaActions() {
    getTile(5).setLandAction(new QuestionTileAction(
        "What is the capital of Norway?",
        "Oslo",
        "geography",
        getTile(10)
    ));
    getTile(9).setLandAction(new QuestionTileAction(
        "What is 15 × 7?",
        "105",
        "math",
        getTile(14)
    ));
    getTile(15).setLandAction(new QuestionTileAction(
        "What is the largest planet in our solar system?",
        "Jupiter",
        "science",
        getTile(20)
    ));
    getTile(25).setLandAction(new QuestionTileAction(
        "In which year did World War II end?",
        "1945",
        "history",
        getTile(30)
    ));
    getTile(35).setLandAction(new QuestionTileAction(
        "What is the chemical symbol for gold?",
        "Au",
        "science",
        getTile(40)
    ));
    getTile(45).setLandAction(new QuestionTileAction(
        "What is the square root of 144?",
        "12",
        "math",
        getTile(50)
    ));
    getTile(52).setLandAction(new QuestionTileAction(
        "Which country has the most natural lakes?",
        "Canada",
        "geography",
        getTile(57)
    ));
  }

  /**
   * Adds a Tile to the Board.
   *
   * @param tile the tile to be added
   */
  public void addTile(Tile tile) {
    tiles.put(tile.getTileId(), tile);
  }

  /**
   * Returns a Tile with the corresponding id.
   *
   * @param tileId the id of the desired Tile
   * @return A Tile with corresponding id
   */
  public Tile getTile(int tileId) {
    return tiles.get(tileId);
  }

  /**
   * Returns the width of the board.
   *
   * @return width of the board. the unit is tiles
   */
  public int getWidth() {
    return width;
  }

  /**
   * Returns the height of the board.
   *
   * @return height of the board. the unit is tiles
   */
  public int getHeight() {
    return height;
  }

  /**
   * Returns the total number of tiles on the board.
   *
   * @return the total number of tiles
   */
  public int getTileAmount() {
    return tiles.size();
  }
}
