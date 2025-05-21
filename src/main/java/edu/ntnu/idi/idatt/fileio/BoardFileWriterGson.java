package edu.ntnu.idi.idatt.fileio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.ntnu.idi.idatt.model.Board;
import edu.ntnu.idi.idatt.model.Tile;
import edu.ntnu.idi.idatt.model.TileAction;
import edu.ntnu.idi.idatt.model.LadderAction;
import edu.ntnu.idi.idatt.model.QuestionTileAction;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;

/**
 * File Writer that uses Gson to create a json file from a board.
 */
public class BoardFileWriterGson implements BoardFileWriter {

  /**
   * Constructor for the Gson writer.
   */
  public BoardFileWriterGson() {
  }

  /**
   * Create a Json file using Gson from a Board
   *
   * @param board A board that's to be converted to json file
   * @param path  desired path + name for the json file
   * @throws IOException
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
      tileObj.addProperty("x", tile.getX());
      tileObj.addProperty("y", tile.getY());
      
      if (tile.getNextTile() != null) {
        tileObj.addProperty("nextTile", tile.getNextTile().getTileId());
      }

      TileAction action = tile.getLandAction();
      if (action != null) {
        JsonObject actionObj = new JsonObject();
        if (action instanceof LadderAction) {
          actionObj.addProperty("type", "LadderAction");
          actionObj.addProperty("destinationTile", ((LadderAction) action).destinationTile.getTileId());
          actionObj.addProperty("description", "Ladder from " + tile.getTileId() + " to " + ((LadderAction) action).destinationTile.getTileId());
        } else if (action instanceof QuestionTileAction) {
          QuestionTileAction qAction = (QuestionTileAction) action;
          actionObj.addProperty("type", "QuestionTileAction");
          actionObj.addProperty("question", qAction.getQuestion());
          actionObj.addProperty("answer", qAction.getAnswer());
          actionObj.addProperty("questionType", qAction.getType());
          actionObj.addProperty("destinationTile", qAction.getDestinationTile().getTileId());
        }
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

}
