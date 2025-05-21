package edu.ntnu.idi.idatt.fileio;

import edu.ntnu.idi.idatt.model.Player;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Implementation of PlayerFileWriter that writes player information to CSV files.
 */
public class PlayerCsvWriter implements PlayerFileWriter {

  public PlayerCsvWriter() {
  }

  @Override
  public void write(Path path, List<Player> players) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()))) {
      for (Player player : players) {
        writer.write(player.getName() + ",");
        writer.write(player.getShape() + ",");
        writer.write(player.getColor().toString());
        writer.newLine();
      }
    }
  }
} 