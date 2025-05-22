package edu.ntnu.idi.idatt.fileio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.ntnu.idi.idatt.model.Board;
import edu.ntnu.idi.idatt.model.LadderAction;
import edu.ntnu.idi.idatt.model.QuestionTileAction;
import edu.ntnu.idi.idatt.model.Tile;
import edu.ntnu.idi.idatt.model.TileAction;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;

/**
 * JSON-based board file writer using Gson for serializing game board configurations.
 */
public class BoardFileWriterGson implements BoardFileWriter {

  /**
   * Creates a new Gson-based board file writer.
   */
  public BoardFileWriterGson() {
  }

  /**
   * Serializes a game board to a JSON file.
   *
   * @param board The game board to serialize
   * @param path  Output file path for the JSON
   * @throws IOException if file operations fail
   */
  public void writeBoard(Board board, Path path) throws IOException {
    JsonObject root = new JsonObject();
    root.addProperty("name", "Custom Board");
    root.addProperty("description", "A custom board created by the user");
    root.addProperty("width", board.getWidth());
    root.addProperty("height", board.getHeight());

    JsonArray tiles = new JsonArray();
    for (int i = 1; i <= board.getTileAmount(); i++) {
      Tile tile = board.getTile(i);
      JsonObject tileObj = new JsonObject();
      tileObj.addProperty("id", tile.getTileId());
      tileObj.addProperty("x", tile.getTileX());
      tileObj.addProperty("y", tile.getTileY());

      if (tile.getNextTile() != null) {
        tileObj.addProperty("nextTile", tile.getNextTile().getTileId());
      }

      TileAction action = tile.getLandAction();
      if (action != null) {
        JsonObject actionObj = getJsonObject(action, tile);
        tileObj.add("action", actionObj);
      }

      tiles.add(tileObj);
    }
    root.add("tiles", tiles);

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    try (Writer writer = new FileWriter(path.toFile())) {
      gson.toJson(root, writer);
    }
  }

  private static JsonObject getJsonObject(TileAction action, Tile tile) {
    JsonObject actionObj = new JsonObject();
    if (action instanceof LadderAction) {
      actionObj.addProperty("type", "LadderAction");
      actionObj.addProperty("destinationTile",
          ((LadderAction) action).destinationTile.getTileId());
      actionObj.addProperty("description", "Ladder from " + tile.getTileId() + " to "
          + ((LadderAction) action).destinationTile.getTileId());
    } else if (action instanceof QuestionTileAction questionAction) {
      actionObj.addProperty("type", "QuestionTileAction");
      actionObj.addProperty("question", questionAction.getQuestion());
      actionObj.addProperty("answer", questionAction.getAnswer());
      actionObj.addProperty("questionType", questionAction.getType());
      actionObj.addProperty("destinationTile", questionAction.getDestinationTile().getTileId());
    }
    return actionObj;
  }

}
