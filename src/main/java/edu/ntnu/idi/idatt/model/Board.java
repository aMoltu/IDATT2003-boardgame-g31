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

    if (gameType.equals("Ladder Game")) {
      if (boardName.equals("Default")) {
        createLadderBoard();
      } else if (boardName.equals("Tornado")) {
        createLadderTornadoBoard();
      } else {
        System.err.println("Invalid board name: " + boardName);
      }
    } else if (gameType.equals("Trivia Game")) {
      if (boardName.equals("Default")) {
        createTriviaBoard();
      } else if (boardName.equals("Tornado")) {
        createTriviaTornadoBoard();
      } else {
        System.err.println("Invalid board name: " + boardName);
      }
    } else {
      System.err.println("Invalid game type: " + gameType);
    }
  }

  /**
   * Creates a standard ladder game board with predefined ladders and special tiles.
   */
  private void createLadderBoard() {
    width = 10;
    height = 9;
    Tile startTile = new Tile(1, 0, 8);
    this.addTile(startTile);

    // Create the basic board structure
    int tileAmount = 90;
    for (int i = 2; i <= tileAmount; i++) {
      Tile tile = new Tile(i, (i - 1) % 10, (tileAmount - i) / 10);
      Tile previousTile = this.getTile(i - 1);
      previousTile.setNextTile(tile);
      this.addTile(tile);
    }

    // Add ladders
    int[] ladderStart = new int[]{5, 9, 39, 60, 29, 80};
    int[] ladderEnd = new int[]{19, 30, 20, 34, 48, 69};
    for (int i = 0; i < ladderStart.length; i++) {
      LadderAction ladderAction = new LadderAction(this.getTile(ladderEnd[i]));
      this.getTile(ladderStart[i]).setLandAction(ladderAction);
    }
  }

  /**
   * Creates a board with a tornado pattern for the ladder game. Features a spiral layout with gaps
   * and predefined ladders.
   */
  private void createLadderTornadoBoard() {
    width = 10;
    height = 10;

    int[][] boardLayout = {
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

    // Add ladders
    int[] ladderStart = new int[]{5, 9, 39, 50, 29};
    int[] ladderEnd = new int[]{19, 30, 20, 34, 48};
    for (int i = 0; i < ladderStart.length; i++) {
      LadderAction ladderAction = new LadderAction(this.getTile(ladderEnd[i]));
      this.getTile(ladderStart[i]).setLandAction(ladderAction);
    }
  }

  /**
   * Creates a standard trivia game board with questions from various categories. Questions are
   * placed at specific tiles with corresponding destination tiles.
   */
  public void createTriviaBoard() {
    width = 10;
    height = 9;
    Tile startTile = new Tile(1, 0, 8);
    this.addTile(startTile);

    int tileAmount = 90;
    // Create the basic board structure
    for (int i = 2; i <= tileAmount; i++) {
      Tile tile = new Tile(i, (i - 1) % 10, (tileAmount - i) / 10);
      Tile previousTile = this.getTile(i - 1);
      previousTile.setNextTile(tile);
      this.addTile(tile);
    }

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
   * Creates a trivia board with a tornado pattern. Uses the same spiral layout as the ladder
   * tornado board but with trivia questions.
   */
  private void createTriviaTornadoBoard() {
    width = 10;
    height = 10;

    int[][] boardLayout = {
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

    // Create tiles based on the layout
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (boardLayout[y][x] != 0) {
          Tile tile = new Tile(boardLayout[y][x], x, y);
          addTile(tile);
        }
      }
    }

    // connect tiles together
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (boardLayout[y][x] > 0 && boardLayout[y][x] != getTileAmount()) {
          getTile(boardLayout[y][x]).setNextTile(getTile(boardLayout[y][x] + 1));
        }
      }
    }

    // add questions
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
