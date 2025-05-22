package edu.ntnu.idi.idatt.fileio;

import edu.ntnu.idi.idatt.model.Board;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Interface for writing game board configurations to files.
 */
public interface BoardFileWriter {

  /**
   * Writes a game board configuration to a file.
   *
   * @param board The game board to write
   * @param path  Path to the output file
   * @throws IOException if file operations fail
   */
  void writeBoard(Board board, Path path) throws IOException;

}
