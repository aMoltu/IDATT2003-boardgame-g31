package edu.ntnu.idi.idatt.fileio;

import edu.ntnu.idi.idatt.model.Player;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;

/**
 * Class for gathering player information from CSV files.
 */
public class PlayerCsvReader {

  /**
   * Read player information from a file.
   *
   * @param path path to the file
   * @return list of players created from content of CSV file
   */
  public static List<Player> read(Path path) {
    List<Player> players = new ArrayList<Player>();
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path.toFile()))) {
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        String[] parts = line.split(",");
        players.add(new Player(parts[0], parts[1], Color.web(parts[2])));
      }
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
    return players;
  }
}
