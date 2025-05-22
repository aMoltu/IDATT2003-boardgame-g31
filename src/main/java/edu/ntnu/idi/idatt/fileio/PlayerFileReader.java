package edu.ntnu.idi.idatt.fileio;

import edu.ntnu.idi.idatt.model.Player;
import java.nio.file.Path;
import java.util.List;

/**
 * Interface for reading player information from CSV files.
 */
public interface PlayerFileReader {

  /**
   * Read player information from a file.
   *
   * @param path path to the file
   * @return list of players created from content of CSV file
   */
  List<Player> read(Path path);
}
