package edu.ntnu.idi.idatt.fileio;

import edu.ntnu.idi.idatt.model.Board;
import java.nio.file.Path;

/**
 * Interface for reading game board configurations from files.
 */
public interface BoardFileReader {

  /**
   * Reads a game board configuration from a file.
   *
   * @param path Path to the board configuration file
   * @return The loaded game board, or null if loading fails
   */
  Board readBoard(Path path);
}
