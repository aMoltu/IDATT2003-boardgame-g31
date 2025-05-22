package edu.ntnu.idi.idatt.fileio;

import com.google.gson.JsonObject;
import edu.ntnu.idi.idatt.model.Board;
import edu.ntnu.idi.idatt.model.LadderAction;
import edu.ntnu.idi.idatt.model.QuestionTileAction;
import edu.ntnu.idi.idatt.model.RollAgain;
import edu.ntnu.idi.idatt.model.TileAction;

/**
 * Factory class for creating tile actions.
 */
public class TileActionFactory {

  /**
   * Creates a tile action instance based on a JSON object.
   *
   * @param actionObject the JSON object containing the necessary information
   * @param board        the board that the game is played on
   * @return a corresponding tile action instance
   * @throws UnknownTileActionException if the tile action is not recognized
   */
  public static TileAction get(JsonObject actionObject, Board board)
      throws UnknownTileActionException {
    String type = actionObject.get("type").getAsString();
    return switch (type) {
      case "LadderAction" ->
          new LadderAction(board.getTile(actionObject.get("destinationTile").getAsInt()));
      case "RollAgain" -> new RollAgain();
      case "QuestionTileAction" -> new QuestionTileAction(
          actionObject.get("question").getAsString(),
          actionObject.get("answer").getAsString(),
          actionObject.get("questionType").getAsString(),
          board.getTile(actionObject.get("destinationTile").getAsInt())
      );
      default -> throw new UnknownTileActionException("Unknown tile action with name " + type);
    };
  }
}
