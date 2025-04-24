package edu.ntnu.idi.idatt.ui;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

import edu.ntnu.idi.idatt.engine.BoardGame;
import edu.ntnu.idi.idatt.model.LadderAction;
import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.model.RollAgain;
import edu.ntnu.idi.idatt.model.Tile;
import java.util.ArrayList;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Pair;

public class BoardView implements BoardGameObserver {

  private final StackPane root;
  private final BoardController controller;
  private final Color[] PLAYER_COLORS = {Color.BLUE, Color.PURPLE, Color.BROWN, Color.ORANGE};
  private final String[] PLAYER_SHAPES = {"Circle", "Square", "Triangle", "Diamond"};
  private final ArrayList<VBox> playerBoxes = new ArrayList<>();

  public BoardView(BoardController controller) {
    root = new StackPane();
    this.controller = controller;
    controller.getGame().addObserver(this);
    root.getChildren().add(snakesAndLadders());
  }

  private BorderPane startMenu() {
    BorderPane pane = new BorderPane();
    pane.setStyle("-fx-background-color: #eee");
    Button btn = new Button("Start game");
    btn.setOnAction(event -> {
      root.getChildren().clear();
      root.getChildren().add(snakesAndLadders());
    });
    pane.setCenter(btn);
    return pane;
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
    //VBox playerBox1 = createProfileBox("Player1", "cube");
    //VBox playerBox2 = createProfileBox("Player2", "triangle");
    //playerSection.getChildren().addAll(playerBox1, playerBox2);

    HBox infoBox1 = createInfoBox(Color.RED, "go down ladder");
    HBox infoBox2 = createInfoBox(Color.INDIANRED, "bottom of bad ladder");
    HBox infoBox3 = createInfoBox(Color.GREEN, "go up ladder");
    HBox infoBox4 = createInfoBox(Color.LIME, "top of good ladder");
    infoSection.getChildren().addAll(infoBox1, infoBox2, infoBox3, infoBox4);

    BoardGame game = controller.getGame();

    int tileWidth = 30;
    int tileHeight = 30;
    Color[] color = new Color[91];
    ArrayList<Pair<Integer, Integer>> ladderStartEnd = new ArrayList<>();
    for (int i = 1; i <= 90; i++) {
      Tile tile = game.getBoard().getTile(i);
      switch (tile.getLandAction()) {
        case LadderAction a -> {
          int destinationId = a.destinationTile.getTileId();
          Color otherColor = color[destinationId];
          boolean shouldChangeOppositeLadderColor =
              otherColor == null || otherColor.equals(Color.WHITE);
          ladderStartEnd.add(new Pair<>(i, destinationId));

          if (destinationId > i) {
            color[i] = Color.GREEN;
            if (shouldChangeOppositeLadderColor) {
              color[destinationId] = Color.LIME;
            }
          } else {
            color[i] = Color.RED;
            if (shouldChangeOppositeLadderColor) {
              color[destinationId] = Color.INDIANRED;
            }
          }
        }
        case RollAgain a -> color[i] = Color.BLUE;
        case null, default -> {
          if (color[i] == null) {
            color[i] = Color.WHITE;
          }
        }
      }
    }


    //Create player boxes based on actual players
    playerSection.getChildren().clear();
    for (int i = 0; i < game.getPlayers().size(); i++) {
      Player player = game.getPlayers().get(i);
      Color playerColor = PLAYER_COLORS[i % PLAYER_COLORS.length];
      String playerShape = PLAYER_SHAPES[i % PLAYER_SHAPES.length];

      VBox playerBox = createPlayerBox(player.getName(), playerShape, playerColor,
          player.getCurrentTile().getTileId());
      playerSection.getChildren().add(playerBox);
    }

    // draw the game
    GraphicsContext gc = canvas.getGraphicsContext2D();
    for (int i = 1; i <= 90; i++) {
      int x = calculateX(i, tileWidth);
      int y = calculateY(i, tileHeight);

      gc.setFill(color[i]);
      gc.fillRect(x, y, tileWidth, tileHeight);
      gc.setFill(Color.BLACK);
      gc.fillText(String.valueOf(i), x, y + tileHeight);
    }
    for (Pair<Integer, Integer> startEnd : ladderStartEnd) {
      drawLadder(gc, startEnd.getKey(), startEnd.getValue(), tileWidth, tileHeight);
    }


    //draw player pieces
    for (int i = 0; i < game.getPlayers().size(); i++) {
      Player player = game.getPlayers().get(i);
      int position = player.getCurrentTile().getTileId();
      Color playerColor = PLAYER_COLORS[i % PLAYER_COLORS.length];

      gc.setFill(playerColor);

      // Drawing the different shapes
      int x = (calculateX(position, tileWidth) + tileWidth / 2);
      int y = (calculateY(position, tileHeight) + tileHeight / 2);

      switch (i % PLAYER_SHAPES.length) {
        case 0 -> { // Circle
          gc.fillOval(x - 12, y - 12, 25, 25);
        }
        case 1 -> { //Square
          gc.fillRect(x - 10, y - 10, 20, 20);
        }
        case 2 -> { // Triangle
          double[] xTriPoints = {x - 10, x, x + 10};
          double[] yTriPoints = {y - 10, y + 10, y - 10};
          gc.fillPolygon(xTriPoints, yTriPoints, 3);
        }
        case 3 -> { //Diamond
          double[] xDiaPoints = {x, x - 10, x, x + 10};
          double[] yDiaPoints = {y - 10, y, y + 10, y};
          gc.fillPolygon(xDiaPoints, yDiaPoints, 4);
        }
      }
    }

    topCenter.getChildren().add(new Text("The ladder game"));

    // Add dice throw button with active player indicator
    Button btn = new Button("Throw dice for "
        + game.getPlayers().get(game.getActivePlayer()).getName());
    btn.setOnAction(e -> controller.throwDice());
    bottomCenter.getChildren().add(btn);

    // Add game status label
    Label statusLabel = new Label("Game in progress");
    if (game.getWinner() != null) {
      statusLabel.setText("Winner: " + game.getWinner().getName() + "!");
      statusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
      btn.setDisable(true);
    }
    bottomCenter.getChildren().add(statusLabel);

    return container;
  }

  private int calculateX(int i, int tileWidth) {
    return ((i - 1) % 10) * tileWidth;
  }

  private int calculateY(int i, int tileHeight) {
    return tileHeight * 9 - (((i - 1) / 10) * tileHeight);
  }

  private void drawLadder(GraphicsContext gc, int start, int end, int tileWidth, int tileHeight) {
    // find center of startTile (x1, y1) and center of end tile (x2, y2)
    double x1 = calculateX(start, tileWidth) + (double) tileWidth / 2;
    double y1 = calculateY(start, tileHeight) + (double) tileWidth / 2;
    double x2 = calculateX(end, tileWidth) + (double) tileWidth / 2;
    double y2 = calculateY(end, tileHeight) + (double) tileWidth / 2;
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

  // New method to create player boxes with position information
  private VBox createPlayerBox(String name, String shape, Color color, int position) {
    Label nameLabel = new Label(name);
    nameLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
    nameLabel.setTextFill(color);

    Label shapeLabel = new Label(shape);
    shapeLabel.setFont(Font.font("System", 12));
    shapeLabel.setTextFill(Color.DARKGRAY);

    Label positionLabel = new Label("Position: " + position);
    positionLabel.setFont(Font.font("System", 12));

    Rectangle colorRect = new Rectangle(20, 20, color);

    HBox infoBox = new HBox(5, colorRect, nameLabel);
    infoBox.setAlignment(Pos.CENTER_LEFT);

    VBox box = new VBox(5);
    box.getChildren().addAll(infoBox, shapeLabel, positionLabel);
    box.setStyle("-fx-background-color: #eee; -fx-border-color: #ccc; -fx-padding: 5px;");
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

  @Override
  public void update() {
    // Update player position information
    BoardGame game = controller.getGame();
    for (int i = 0; i < game.getPlayers().size() && i < playerBoxes.size(); i++) {
      Player player = game.getPlayers().get(i);
      VBox playerBox = playerBoxes.get(i);

      // Find and update the position label
      for (javafx.scene.Node node : playerBox.getChildren()) {
        if (node instanceof Label && ((Label) node).getText().startsWith("Position:")) {
          ((Label) node).setText("Position: " + player.getCurrentTile().getTileId());
          break;
        }
      }
    }

    // Redraw the entire board
    root.getChildren().clear();
    root.getChildren().add(snakesAndLadders());

  }
}
