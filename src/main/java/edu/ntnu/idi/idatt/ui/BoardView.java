package edu.ntnu.idi.idatt.ui;

import edu.ntnu.idi.idatt.engine.BoardGame;
import edu.ntnu.idi.idatt.model.LadderAction;
import edu.ntnu.idi.idatt.model.Tile;
import edu.ntnu.idi.idatt.model.TileAction;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class BoardView {

  private final StackPane root;
  private final BoardController controller;

  public BoardView(BoardController controller) {
    root = new StackPane();
    this.controller = controller;
    root.getChildren().add(snakesAndLadders());
  }

  private BorderPane snakesAndLadders() {
    // set up layout
    BorderPane container = new BorderPane();
    VBox playerSection = new VBox();
    VBox gameSection = new VBox();
    VBox infoSection = new VBox();

    container.setLeft(playerSection);
    container.setCenter(gameSection);
    container.setRight(infoSection);

    // ensure that left and right sections have the same size
    playerSection.minWidthProperty().bind(infoSection.widthProperty());
    infoSection.minWidthProperty().bind(playerSection.widthProperty());

    HBox topCenter = new HBox();
    Canvas canvas = new Canvas(300, 300);
    HBox bottomCenter = new HBox();
    gameSection.getChildren().addAll(topCenter, canvas, bottomCenter);

    gameSection.alignmentProperty().set(Pos.CENTER);
    topCenter.alignmentProperty().set(Pos.CENTER);
    bottomCenter.alignmentProperty().set(Pos.CENTER);

    // ensure that top and bottom sections have the same size
    topCenter.minHeightProperty().bind(bottomCenter.heightProperty());
    bottomCenter.minHeightProperty().bind(topCenter.heightProperty());

    // add content
    VBox playerBox1 = createProfileBox("Player1", "cube");
    VBox playerBox2 = createProfileBox("Player2", "triangle");
    playerSection.getChildren().addAll(playerBox1, playerBox2);

    HBox infoBox1 = createInfoBox(Color.RED, "go down ladder");
    infoSection.getChildren().add(infoBox1);

    BoardGame game = controller.getGame();
    int tileWidth = 30;
    int tileHeight = 30;
    for (int i = 1; i <= 90; i++) {
      Tile tile = game.getBoard().getTile(i);
      int x = ((i - 1) % 10) * tileWidth;
      int y = tileHeight * 9 - (((i - 1) / 10) * tileHeight);

      GraphicsContext gc = canvas.getGraphicsContext2D();

      if (tile.getLandAction() != null) {
        TileAction action = tile.getLandAction();
        if (action instanceof LadderAction) {
          int nextId = tile.getNextTile().getTileId();
          if (nextId > tile.getTileId()) {
            gc.setFill(Color.GREEN);
          } else {
            gc.setFill(Color.RED);
          }
        } else {
          gc.setFill(Color.BLUE);
        }
      } else {
        gc.setFill(Color.WHITE);
      }

      gc.fillRect(x, y, tileWidth, tileHeight);
      gc.setFill(Color.BLACK);
      gc.fillText(String.valueOf(i), x, y + tileHeight);
    }

    topCenter.getChildren().add(new Text("The ladder game"));

    bottomCenter.getChildren().add(new Button("throw dice"));

    return container;
  }

  private VBox createProfileBox(String name, String shape) {
    Label nameLabel = new Label(name);
    nameLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
    nameLabel.setTextFill(Color.BLACK);

    Label shapeLabel = new Label(shape);
    shapeLabel.setFont(Font.font("System", 12));
    shapeLabel.setTextFill(Color.DARKGRAY);

    VBox box = new VBox(2);
    box.getChildren().addAll(nameLabel, shapeLabel);
    box.setStyle(
        "-fx-background-color: #eee; -fx-border-color: #ccc;");
    box.setMinWidth(150);

    return box;
  }

  private HBox createInfoBox(Color color, String infoText) {
    Rectangle rect = new Rectangle(50, 50, color);
    Text text = new Text(infoText);
    text.setFont(Font.font("System", 12));

    HBox box = new HBox(2);
    box.getChildren().addAll(rect, text);
    box.setStyle(
        "-fx-background-color: #eee; -fx-border-color: #ccc;");
    box.setMinWidth(150);

    return box;
  }

  public StackPane getRoot() {
    return root;
  }
}
