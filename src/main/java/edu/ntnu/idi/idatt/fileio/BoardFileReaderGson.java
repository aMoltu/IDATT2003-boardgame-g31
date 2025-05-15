package edu.ntnu.idi.idatt.fileio;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.ntnu.idi.idatt.model.TileAction;
import edu.ntnu.idi.idatt.model.Board;
import edu.ntnu.idi.idatt.model.Tile;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

/**
 * File reader that uses Gson to create a board from a json file.
 */
public class BoardFileReaderGson implements BoardFileReader {

  /**
   * Constructor for the Gson board file reader.
   */
  public BoardFileReaderGson() {
  }

  /**
   * Create a Board instance using a JSON file and Gson.
   *
   * @param path the path pointing to the JSON file
   * @return a Board instance corresponding to the JSON file
   */
  public Board readBoard(Path path) {

    // read json string
    StringBuilder jsonString = new StringBuilder();
    File file = new File(path.toString());
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        jsonString.append(line);
      }
    } catch (IOException e) {
      System.err.println("Error reading file: " + e.getMessage());
      return null;
    }

    JsonParser parser = new JsonParser();
    JsonElement rootNode = parser.parse(jsonString.toString());
    if (rootNode.isJsonObject()) {
      JsonObject details = rootNode.getAsJsonObject();

      Board board = new Board();
      // add tiles to board
      JsonArray tiles = details.getAsJsonArray("tiles");
      for (JsonElement tileElement : tiles) {
        JsonObject tileObject = tileElement.getAsJsonObject();
        int id = tileObject.get("id").getAsInt();
        int x = tileObject.get("x").getAsInt();
        int y = tileObject.get("y").getAsInt();
        Tile tile = new Tile(id, x, y);
        board.addTile(tile);
      }
      // add more information to tiles
      for (JsonElement tileElement : tiles) {
        JsonObject tileObject = tileElement.getAsJsonObject();
        int id = tileObject.get("id").getAsInt();
        if (tileObject.has("nextTile")) {
          // set nextTile
          int nextTile = tileObject.get("nextTile").getAsInt();
          Tile tile = board.getTile(id);
          tile.setNextTile(board.getTile(nextTile));
        }
        if (tileObject.has("action")) {
          // set action
          JsonObject actionObject = tileObject.get("action").getAsJsonObject();
          try {
            TileAction action = TileActionFactory.get(actionObject, board);
            board.getTile(id).setLandAction(action);
          } catch (UnknownTileActionException e) {
            System.err.println(e.getMessage());
          }
        }
      }
      return board;
    }
    return null;
  }
}
