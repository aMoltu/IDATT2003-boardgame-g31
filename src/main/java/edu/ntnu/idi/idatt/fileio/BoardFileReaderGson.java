package edu.ntnu.idi.idatt.fileio;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.ntnu.idi.idatt.actions.LadderAction;
import edu.ntnu.idi.idatt.actions.TileAction;
import edu.ntnu.idi.idatt.board.Board;
import edu.ntnu.idi.idatt.board.Tile;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.HashMap;

public class BoardFileReaderGson implements BoardFileReader {

  public BoardFileReaderGson() {

  }

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
      System.out.println("Error reading file: " + e.getMessage());
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
        Tile tile = new Tile(id);
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
          if (actionObject.get("type").getAsString().equals("LadderAction")) {
            Tile destinationTile = board.getTile(actionObject.get("destinationTile").getAsInt());
            LadderAction action = new LadderAction(destinationTile);
            board.getTile(id).setLandAction(action);
          }
          //TODO add support for more actions
        }
      }
      return board;
    }
    return null;
  }
}
