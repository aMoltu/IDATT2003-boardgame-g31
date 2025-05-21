package edu.ntnu.idi.idatt.fileio;

import edu.ntnu.idi.idatt.model.Player;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Class for writing player information to CSV files.
 */
public class PlayerCsvWriter {

  /**
   * Write player names on separate lines.
   *
   * @param path    path to the output file
   * @param players list of players with a name, shape and color
   */
  public static void write(Path path, List<Player> players) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()))) {
      for (Player player : players) {
        writer.write(player.getName() + ",");
        writer.write(player.getShape() + ",");
        writer.write(player.getColor().toString());
        writer.newLine();
      }
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
}
