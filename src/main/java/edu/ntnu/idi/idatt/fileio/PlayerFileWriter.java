package edu.ntnu.idi.idatt.fileio;

import edu.ntnu.idi.idatt.model.Player;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Interface for writing player information to CSV files.
 */
public interface PlayerFileWriter {

  /**
   * Write player information to a file.
   *
   * @param path    path to the output file
   * @param players list of players with a name, shape and color
   * @throws IOException if an I/O error occurs
   */
  void write(Path path, List<Player> players) throws IOException;
}
