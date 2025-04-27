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

  private void setupLayout(BorderPane container) {
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
    StackPane canvasContainer = new StackPane();
    Canvas boardCanvas = new Canvas(300, 300);
    Canvas playerCanvas = new Canvas(300, 300);
    canvasContainer.getChildren().addAll(boardCanvas, playerCanvas);
    HBox bottomCenter = new HBox();
    gameSection.getChildren().addAll(topCenter, canvasContainer, bottomCenter);

    gameSection.alignmentProperty().set(Pos.CENTER);
    topCenter.alignmentProperty().set(Pos.CENTER);
    bottomCenter.alignmentProperty().set(Pos.CENTER);

    // ensure that top and bottom sections have the same size
    topCenter.minHeightProperty().bind(bottomCenter.heightProperty());
    bottomCenter.minHeightProperty().bind(topCenter.heightProperty());
  }

  private void setupInfoSection(BorderPane container) {
    VBox infoSection = (VBox) container.getRight();

    HBox infoBox1 = createInfoBox(Color.RED, "go down ladder");
    HBox infoBox2 = createInfoBox(Color.INDIANRED, "bottom of bad ladder");
    HBox infoBox3 = createInfoBox(Color.GREEN, "go up ladder");
    HBox infoBox4 = createInfoBox(Color.LIME, "top of good ladder");
    infoSection.getChildren().addAll(infoBox1, infoBox2, infoBox3, infoBox4);
  }

  private void setupGameBoard(BorderPane container) {
    VBox gameSection = (VBox) container.getCenter();
    StackPane canvasContainer = (StackPane) gameSection.getChildren().get(1);
    Canvas boardCanvas = (Canvas) canvasContainer.getChildren().getFirst();

    int tileWidth = 30;
    int tileHeight = 30;
    Color[] color = new Color[91];
    ArrayList<Pair<Integer, Integer>> ladderStartEnd = new ArrayList<>();

    // Setup tile colors and ladder connections
    setupTileColors(color, ladderStartEnd);

    // Draw the board
    drawGameBoard(boardCanvas, color, ladderStartEnd, tileWidth, tileHeight);
  }

  private void setupTileColors(Color[] color, ArrayList<Pair<Integer, Integer>> ladderStartEnd) {
    BoardGame game = controller.getGame();

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
  }

  private void drawGameBoard(Canvas canvas, Color[] color,
                             ArrayList<Pair<Integer, Integer>> ladderStartEnd,
                             int tileWidth, int tileHeight) {
    GraphicsContext gc = canvas.getGraphicsContext2D();

    // Draw tiles
    for (int i = 1; i <= 90; i++) {
      int x = calculateX(i, tileWidth);
      int y = calculateY(i, tileHeight);

      gc.setFill(color[i]);
      gc.fillRect(x, y, tileWidth, tileHeight);
      gc.setFill(Color.BLACK);
      gc.fillText(String.valueOf(i), x, y + tileHeight);
    }

    // Draw ladders
    for (Pair<Integer, Integer> startEnd : ladderStartEnd) {
      drawLadder(gc, startEnd.getKey(), startEnd.getValue(), tileWidth, tileHeight);
    }
  }

  private void setupPlayerSection(BorderPane container) {
    BoardGame game = controller.getGame();
    VBox playerSection = (VBox) container.getLeft();

    playerSection.getChildren().clear();
    playerBoxes.clear(); // Clear the existing player boxes

    for (int i = 0; i < game.getPlayers().size(); i++) {
      Player player = game.getPlayers().get(i);
      Color playerColor = PLAYER_COLORS[i % PLAYER_COLORS.length];
      String playerShape = PLAYER_SHAPES[i % PLAYER_SHAPES.length];

      VBox playerBox = createPlayerBox(player.getName(), playerShape, playerColor,
          player.getCurrentTile().getTileId());
      playerSection.getChildren().add(playerBox);
      playerBoxes.add(playerBox); // Store reference to player box
    }
  }

  private void drawPlayerPieces(Canvas canvas) {
    BoardGame game = controller.getGame();
    GraphicsContext gc = canvas.getGraphicsContext2D();
    int tileWidth = 30;
    int tileHeight = 30;
    for (int i = 0; i < game.getPlayers().size(); i++) {
      Player player = game.getPlayers().get(i);
      int position = player.getCurrentTile().getTileId();
      Color playerColor = PLAYER_COLORS[i % PLAYER_COLORS.length];

      gc.setFill(playerColor);
      int x = (calculateX(position, tileWidth) + tileWidth / 2);
      int y = (calculateY(position, tileHeight) + tileHeight / 2);

      drawPlayerShape(gc, i, x, y);
    }
  }

  private void drawPlayerShape(GraphicsContext gc, int playerIndex, int x, int y) {
    switch (playerIndex % PLAYER_SHAPES.length) {
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

  private void setupGameControls(BorderPane container) {
    BoardGame game = controller.getGame();
    VBox gameSection = (VBox) container.getCenter();
    HBox topCenter = (HBox) gameSection.getChildren().get(0);
    HBox bottomCenter = (HBox) gameSection.getChildren().get(2);

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
  }


  private BorderPane snakesAndLadders() {
    // set up layout
    BorderPane container = new BorderPane();

    setupLayout(container);
    setupInfoSection(container);
    setupGameBoard(container);
    setupPlayerSection(container);
    setupGameControls(container);

    //initial drawing of player pieces (just in case)
    VBox gameSection = (VBox) container.getCenter();
    StackPane canvasContainer = (StackPane) gameSection.getChildren().get(1);
    Canvas playerCanvas = (Canvas) canvasContainer.getChildren().get(1);
    drawPlayerPieces(playerCanvas);

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
    BoardGame game = controller.getGame();

    VBox gameSection = (VBox) ((BorderPane) root.getChildren().getFirst()).getCenter();
    StackPane canvasContainer = (StackPane) gameSection.getChildren().get(1);
    Canvas playerCanvas = (Canvas) canvasContainer.getChildren().get(1);

    //clear the player canvas
    playerCanvas.getGraphicsContext2D().clearRect(0, 0, playerCanvas.getWidth(), playerCanvas.getHeight());

    drawPlayerPieces(playerCanvas);

    updatePlayerBoxes(game);

    updateGameControls(game);
  }


  private void updatePlayerBoxes(BoardGame game) {
    // Update player position information in the player boxes
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
  }

  private void updateGameControls(BoardGame game) {
    // Update game controls (button text and status)
    VBox gameSection = (VBox) ((BorderPane) root.getChildren().getFirst()).getCenter();
    HBox bottomCenter = (HBox) gameSection.getChildren().get(2);

    // Update button text
    for (javafx.scene.Node node : bottomCenter.getChildren()) {
      if (node instanceof Button) {
        Button btn = (Button) node;
        btn.setText("Throw dice for " + game.getPlayers().get(game.getActivePlayer()).getName());

        // Disable button if game is over
        if (game.getWinner() != null) {
          btn.setDisable(true);
        }
      } else if (node instanceof Label) {
        Label statusLabel = (Label) node;
        if (game.getWinner() != null) {
          statusLabel.setText("Winner: " + game.getWinner().getName() + "!");
          statusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
        } else {
          statusLabel.setText("Game in progress");
        }
      }
    }
  }
}
