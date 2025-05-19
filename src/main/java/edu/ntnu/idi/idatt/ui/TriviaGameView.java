package edu.ntnu.idi.idatt.ui;

import edu.ntnu.idi.idatt.engine.BoardGame;
import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.model.QuestionTileAction;
import edu.ntnu.idi.idatt.model.Tile;
import edu.ntnu.idi.idatt.viewmodel.TileViewModel;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * A view for the trivia game that extends BoardView.
 */
public class TriviaGameView extends BoardView implements QuestionTileObserver {

  public TriviaGameView(BoardGame game, BoardController controller) {
    super(game, controller);
  }

  @Override
  protected void setupTileColors(Color[] colors) {
    for (int i = 1; i <= game.getBoard().getTileAmount(); i++) {
      Tile tile = game.getBoard().getTile(i);
      if (tile != null && tile.getLandAction() instanceof QuestionTileAction) {
        QuestionTileAction action = (QuestionTileAction) tile.getLandAction();
        switch (action.getType()) {
          case "math":
            colors[i] = Color.BLUE;
            break;
          case "science":
            colors[i] = Color.GREEN;
            break;
          case "geography":
            colors[i] = Color.ORANGE;
            break;
          case "history":
            colors[i] = Color.PURPLE;
            break;
          default:
            colors[i] = Color.GRAY;
        }
        if (action != null) {
          action.addObserver(this);
        }
      } else if (colors[i] == null) {
        colors[i] = Color.WHITE;
      }
    }
  }

  @Override
  protected String getGameTitle() {
    return "The Trivia Game";
  }

  @Override
  protected GridPane initScene() {
    GridPane scene = super.initScene();

    Pane mathBox = createInfoBox(Color.BLUE, "Math Questions");
    Pane scienceBox = createInfoBox(Color.GREEN, "Science Questions");
    Pane geographyBox = createInfoBox(Color.ORANGE, "Geography Questions");
    Pane historyBox = createInfoBox(Color.PURPLE, "History Questions");

    infoSection.getChildren().addAll(mathBox, scienceBox, geographyBox, historyBox);

    return scene;
  }

  @Override
  public void onQuestionTile(QuestionTileAction action, Player player) {
    super.update();
    handleQuestionTileAction(action, player);
  }

  private void handleQuestionTileAction(QuestionTileAction action, Player player) {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Question Tile");
    dialog.setHeaderText(action.getQuestion());
    dialog.setContentText("Please enter your answer:");

    dialog.showAndWait().ifPresent(answer -> {
      if (action.isCorrectAnswer(answer)) {
        showAlert("Correct!", "You answered correctly! Moving forward...",
            Alert.AlertType.INFORMATION);
        action.movePlayer(player);
        update();
      } else {
        showAlert("Incorrect", "Sorry, that's not the correct answer.",
            Alert.AlertType.INFORMATION);
      }
    });
  }

  private void showAlert(String title, String content, Alert.AlertType type) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
  }
}