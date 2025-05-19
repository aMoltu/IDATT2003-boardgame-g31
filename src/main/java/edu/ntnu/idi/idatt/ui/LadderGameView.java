package edu.ntnu.idi.idatt.ui;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

import edu.ntnu.idi.idatt.engine.BoardGame;
import edu.ntnu.idi.idatt.viewmodel.TileViewModel;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Pair;
import java.util.ArrayList;

/**
 * A view for the ladder game that extends BoardView.
 */
public class LadderGameView extends BoardView {

  private final ArrayList<Pair<Integer, Integer>> ladderStartEnd;

  public LadderGameView(BoardGame game, BoardController controller) {
    super(game, controller);
    this.ladderStartEnd = new ArrayList<>();
  }

  @Override
  protected void setupTileColors(Color[] color) {
    for (int i = 1; i <= board.tileAmount(); i++) {
      TileViewModel tile = board.tiles().get(i);
      switch (tile.landActionType()) {
        case "LadderAction" -> {
          Color otherColor = color[tile.nextId()];
          boolean shouldChangeOppositeLadderColor =
              otherColor == null || otherColor.equals(Color.WHITE);
          ladderStartEnd.add(new Pair<>(i, tile.nextId()));

          if (tile.nextId() > i) {
            color[i] = Color.GREEN;
            if (shouldChangeOppositeLadderColor) {
              color[tile.nextId()] = Color.LIME;
            }
          } else {
            color[i] = Color.RED;
            if (shouldChangeOppositeLadderColor) {
              color[tile.nextId()] = Color.INDIANRED;
            }
          }
        }
        case "RollAgain" -> color[i] = Color.BLUE;
        case null, default -> {
          if (color[i] == null) {
            color[i] = Color.WHITE;
          }
        }
      }
    }
  }

  @Override
  protected void drawGameBoard(Canvas canvas, Color[] color) {
    super.drawGameBoard(canvas, color);

    // Draw ladders on the board canvas
    GraphicsContext gc = canvas.getGraphicsContext2D();
    for (Pair<Integer, Integer> startEnd : ladderStartEnd) {
      drawLadder(gc, startEnd.getKey(), startEnd.getValue());
    }
  }

  @Override
  protected Pane setupTopSection() {
    HBox topCenter = new HBox();
    topCenter.setAlignment(Pos.CENTER);

    Text title = new Text("The Ladder Game");
    title.setFont(Font.font("System", FontWeight.BOLD, 24));

    topCenter.getChildren().add(title);
    return topCenter;
  }

  @Override
  protected GridPane initScene() {
    GridPane scene = super.initScene();

    HBox infoBox1 = createInfoBox(Color.RED, "Go down ladder");
    HBox infoBox2 = createInfoBox(Color.INDIANRED, "Bottom of bad ladder");
    HBox infoBox3 = createInfoBox(Color.GREEN, "Go up ladder");
    HBox infoBox4 = createInfoBox(Color.LIME, "Top of good ladder");
    infoSection.getChildren().addAll(infoBox1, infoBox2, infoBox3, infoBox4);

    return scene;
  }

  private void drawLadder(GraphicsContext gc, int start, int end) {
    // find center of startTile (x1, y1) and center of end tile (x2, y2)
    double x1 = calculateCenterX(start);
    double y1 = calculateCenterY(start);
    double x2 = calculateCenterX(end);
    double y2 = calculateCenterY(end);
    // calculate the ladder vector (dx, dy)
    double dx = (x2 - x1);
    double dy = (y2 - y1);
    double length = sqrt(dx * dx + dy * dy);
    // find vector orthogonal to ladder vector and scaled
    double scaledOrthogonalX = dy / length * tileWidth / 3;
    double scaledOrthogonalY = (-dx) / length * tileHeight / 3;

    // draw main ladder lines
    gc.strokeLine(x1 + scaledOrthogonalX, y1 + scaledOrthogonalY, x2 + scaledOrthogonalX,
        y2 + scaledOrthogonalY);
    gc.strokeLine(x1 - scaledOrthogonalX, y1 - scaledOrthogonalY, x2 - scaledOrthogonalX,
        y2 - scaledOrthogonalY);

    // normalize ladder vector using manhattan distance so it becomes symmetrical
    double manhattanDist = abs(y2 - y1) + abs(x2 - x1);
    dy = (((y2 - y1) / manhattanDist) * tileWidth) / 2;
    dx = (((x2 - x1) / manhattanDist) * tileHeight) / 2;

    // draw ladder steps
    while (abs(y2 - y1 - dy) + abs(x2 - x1 - dx) > 0.1) {
      x1 += dx;
      y1 += dy;
      gc.strokeLine(x1 + scaledOrthogonalX, y1 + scaledOrthogonalY, x1 - scaledOrthogonalX,
          y1 - scaledOrthogonalY);
    }
  }
}
