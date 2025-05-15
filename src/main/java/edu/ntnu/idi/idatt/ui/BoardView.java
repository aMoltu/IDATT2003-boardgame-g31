package edu.ntnu.idi.idatt.ui;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

import edu.ntnu.idi.idatt.engine.BoardGame;
import edu.ntnu.idi.idatt.model.Board;
import edu.ntnu.idi.idatt.model.LadderAction;
import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.model.RollAgain;
import edu.ntnu.idi.idatt.model.Tile;
import java.util.ArrayList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
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
  private final ArrayList<VBox> playerBoxes = new ArrayList<>();
  private GridPane mainContainer;
  private int tileWidth = 30;
  private int tileHeight = 30;
  private Pane topSection;
  private Pane playerSection;
  private Pane gameSection;
  private Pane infoSection;
  private Pane bottomSection;

  public BoardView(BoardController controller) {
    root = new StackPane();
    this.controller = controller;
    controller.getGame().addObserver(this);
  }

  private GridPane setupLayout() {
    GridPane container = new GridPane();
    container.setGridLinesVisible(true); //temporary
    container.setHgap(10);
    container.setVgap(10);

    ColumnConstraints outsideColumn = new ColumnConstraints();
    ColumnConstraints sideColumn = new ColumnConstraints();
    ColumnConstraints middleColumn = new ColumnConstraints();
    outsideColumn.setPercentWidth(5);
    sideColumn.setPercentWidth(25);
    middleColumn.setPercentWidth(40);
    container.getColumnConstraints()
        .addAll(outsideColumn, sideColumn, middleColumn, sideColumn, outsideColumn);

    RowConstraints outsideRow = new RowConstraints();
    RowConstraints middleRow = new RowConstraints();
    outsideRow.setPercentHeight(15);
    middleRow.setPercentHeight(70);
    container.getRowConstraints().addAll(outsideRow, middleRow, outsideRow);

    return container;
  }

  private Pane setupInfoSection() {
    VBox infoSection = new VBox();
    infoSection.setPadding(new Insets(10));
    infoSection.setStyle("""
            -fx-background-color: #eee;
            -fx-background-radius: 10;
            -fx-border-radius: 10;
            -fx-border-color: #ccc;
            -fx-border-width: 1;
        """);

    HBox infoBox1 = createInfoBox(Color.RED, "go down ladder");
    HBox infoBox2 = createInfoBox(Color.INDIANRED, "bottom of bad ladder");
    HBox infoBox3 = createInfoBox(Color.GREEN, "go up ladder");
    HBox infoBox4 = createInfoBox(Color.LIME, "top of good ladder");
    infoSection.getChildren().addAll(infoBox1, infoBox2, infoBox3, infoBox4);

    infoSection.setEffect(new DropShadow(5, Color.gray(0.3)));
    return infoSection;
  }

  private class ResizableCanvas extends Canvas {

    @Override
    public double prefWidth(double height) {
      return 0;
    }

    @Override
    public double prefHeight(double width) {
      return 0;
    }
  }

  private Pane setupGameBoard() {
    StackPane gameSection = new StackPane();

    Canvas boardCanvas = new ResizableCanvas();
    Canvas playerCanvas = new ResizableCanvas();
    gameSection.getChildren().addAll(boardCanvas, playerCanvas);

    double relativeCanvasSize = 0.9;
    boardCanvas.widthProperty().bind(gameSection.widthProperty().multiply(relativeCanvasSize));
    boardCanvas.heightProperty().bind(gameSection.heightProperty().multiply(relativeCanvasSize));
    playerCanvas.widthProperty().bind(gameSection.widthProperty().multiply(relativeCanvasSize));
    playerCanvas.heightProperty().bind(gameSection.heightProperty().multiply(relativeCanvasSize));

    Color[] color = new Color[91];
    ArrayList<Pair<Integer, Integer>> ladderStartEnd = new ArrayList<>();

    setupTileColors(color, ladderStartEnd);

    drawGameBoard(boardCanvas, color, ladderStartEnd);

    // Make sure canvas size corresponds to window size
    gameSection.widthProperty().addListener(
        (obs, oldVal, newVal) -> redrawCanvasAfterResize(boardCanvas, color, ladderStartEnd,
            playerCanvas, relativeCanvasSize));
    gameSection.heightProperty().addListener(
        (obs, oldVal, newVal) -> redrawCanvasAfterResize(boardCanvas, color, ladderStartEnd,
            playerCanvas, relativeCanvasSize));
    return gameSection;
  }

  private void redrawCanvasAfterResize(Canvas boardCanvas, Color[] color,
      ArrayList<Pair<Integer, Integer>> ladderStartEnd, Canvas playerCanvas,
      double sizeMultiplier) {
    // TODO replace magic numbers with horizontal and vertical tile amount
    double tileSize =
        Math.min(gameSection.getHeight() / 9, gameSection.getWidth() / 10) * sizeMultiplier;
    tileWidth = (int) tileSize;
    tileHeight = (int) tileSize;
    this.drawGameBoard(boardCanvas, color, ladderStartEnd);
    this.drawPlayerPieces(playerCanvas);
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
      ArrayList<Pair<Integer, Integer>> ladderStartEnd) {
    GraphicsContext gc = canvas.getGraphicsContext2D();

    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

    // Draw tiles
    for (int i = 1; i <= 90; i++) {
      int x = calculateCornerX(i);
      int y = calculateCornerY(i);

      gc.setFill(color[i]);
      gc.fillRect(x, y, tileWidth, tileHeight);
      gc.setFill(Color.BLACK);
      gc.fillText(String.valueOf(i), x, y + tileHeight);
    }

    // Draw ladders
    for (Pair<Integer, Integer> startEnd : ladderStartEnd) {
      drawLadder(gc, startEnd.getKey(), startEnd.getValue());
    }
  }

  private Pane setupPlayerSection() {
    BoardGame game = controller.getGame();
    VBox playerSection = new VBox();

    playerSection.setPadding(new Insets(10));
    playerSection.setStyle("""
            -fx-background-color: #eee;
            -fx-background-radius: 10;
            -fx-border-radius: 10;
            -fx-border-color: #ccc;
            -fx-border-width: 1;
        """);
    playerSection.setEffect(new DropShadow(5, Color.gray(0.3)));

    playerSection.getChildren().clear();
    playerBoxes.clear(); // Clear the existing player boxes

    for (int i = 0; i < game.getPlayers().size(); i++) {
      Player player = game.getPlayers().get(i);

      VBox playerBox = createPlayerBox(player.getName(), player.getShape(), player.getColor(),
          player.getCurrentTile().getTileId());
      playerSection.getChildren().add(playerBox);
      playerBoxes.add(playerBox); // Store reference to player box
    }
    return playerSection;
  }

  private void drawPlayerPieces(Canvas canvas) {
    //clear the player canvas
    canvas.getGraphicsContext2D()
        .clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

    BoardGame game = controller.getGame();
    GraphicsContext gc = canvas.getGraphicsContext2D();
    for (int i = 0; i < game.getPlayers().size(); i++) {
      Player player = game.getPlayers().get(i);
      int position = player.getCurrentTile().getTileId();

      gc.setFill(player.getColor());
      int x = calculateCenterX(position);
      int y = calculateCenterY(position);

      drawPlayerShape(gc, player.getShape(), x, y);
    }
  }

  private void drawPlayerShape(GraphicsContext gc, String shape, int x, int y) {
    switch (shape) {
      case "Circle" -> {
        gc.fillOval(x - 12, y - 12, 25, 25);
      }
      case "Square" -> {
        gc.fillRect(x - 10, y - 10, 20, 20);
      }
      case "Triangle" -> {
        double[] xTriPoints = {x - 10, x, x + 10};
        double[] yTriPoints = {y - 10, y + 10, y - 10};
        gc.fillPolygon(xTriPoints, yTriPoints, 3);
      }
      case "Diamond" -> {
        double[] xDiaPoints = {x, x - 10, x, x + 10};
        double[] yDiaPoints = {y - 10, y, y + 10, y};
        gc.fillPolygon(xDiaPoints, yDiaPoints, 4);
      }
      default -> {
        System.err.println("Unknown shape: " + shape);
        gc.fillOval(x - 12, y - 12, 25, 25);
      }
    }
  }

  private Pane setupTopSection() {
    HBox topCenter = new HBox();
    topCenter.setAlignment(Pos.CENTER);

    Text title = new Text("The ladder game");
    title.setFont(Font.font("System", FontWeight.BOLD, 24));

    topCenter.getChildren().add(title);
    return topCenter;
  }

  private Pane setupBottomSection() {
    BoardGame game = controller.getGame();
    VBox bottomCenter = new VBox();
    bottomCenter.setAlignment(Pos.CENTER);

    // Add dice throw button with active player indicator
    Button btn = new Button("Throw dice for "
        + game.getPlayers().get(game.getActivePlayer()).getName());
    btn.setOnAction(e -> controller.throwDice());
    bottomCenter.getChildren().add(btn);
    btn.setPrefSize(200, 40);

    // Add game status label
    Label statusLabel = new Label("Game in progress");
    if (game.getWinner() != null) {
      statusLabel.setText("Winner: " + game.getWinner().getName() + "!");
      statusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
      btn.setDisable(true);
    }
    bottomCenter.getChildren().add(statusLabel);

    return bottomCenter;
  }


  private GridPane initScene() {
    GridPane container = setupLayout();

    topSection = setupTopSection();
    container.add(topSection, 2, 0);
    playerSection = setupPlayerSection();
    container.add(playerSection, 1, 1);
    gameSection = setupGameBoard();
    container.add(gameSection, 2, 1);
    infoSection = setupInfoSection();
    container.add(infoSection, 3, 1);
    bottomSection = setupBottomSection();
    container.add(bottomSection, 2, 2);

    return container;
  }

  private int calculateCornerX(int i) {
    Board board = controller.getGame().getBoard();
    return board.getTile(i).getX() * tileWidth;
  }

  private int calculateCenterX(int i) {
    Board board = controller.getGame().getBoard();
    return board.getTile(i).getX() * tileWidth + tileWidth / 2;
  }

  private int calculateCornerY(int i) {
    Board board = controller.getGame().getBoard();
    return board.getTile(i).getY() * tileHeight;
  }

  private int calculateCenterY(int i) {
    Board board = controller.getGame().getBoard();
    return board.getTile(i).getY() * tileHeight + tileHeight / 2;
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
    box.setMinWidth(100);

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
    box.setMinWidth(100);

    return box;
  }

  public StackPane getRoot() {
    return root;
  }

  @Override
  public void update() {
    BoardGame game = controller.getGame();

    if (root.getChildren().isEmpty()) {
      mainContainer = initScene();
      root.getChildren().add(mainContainer);
    }

    Canvas playerCanvas = (Canvas) gameSection.getChildren().get(1);

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
    // Update button text
    for (javafx.scene.Node node : bottomSection.getChildren()) {
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
