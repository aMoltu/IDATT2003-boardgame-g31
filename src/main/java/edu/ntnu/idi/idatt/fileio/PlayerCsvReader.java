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
 * Implementation of PlayerFileReader that reads player information from CSV files. Each line in the
 * CSV file should contain: name,shape,color
 */
public class PlayerCsvReader implements PlayerFileReader {

  /**
   * Creates a new CSV player file reader.
   */
  public PlayerCsvReader() {
  }

  /**
   * Reads player information from a CSV file. Each line should contain: name,shape,color
   *
   * @param path Path to the CSV file containing player information
   * @return List of players created from the CSV file content
   */
  @Override
  public List<Player> read(Path path) {
    List<Player> players = new ArrayList<>();
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