package edu.ntnu.idi.idatt.ui;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

import edu.ntnu.idi.idatt.engine.BoardGame;
import edu.ntnu.idi.idatt.viewmodel.BoardViewModel;
import edu.ntnu.idi.idatt.viewmodel.PlayerViewModel;
import edu.ntnu.idi.idatt.viewmodel.TileViewModel;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
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
  private final BoardGame game;
  private final BoardController controller;
  private BoardViewModel board;
  private GridPane mainContainer;
  private int tileWidth = 30;
  private int tileHeight = 30;
  private Pane topSection;
  private Pane playerSection;
  private Pane gameSection;
  private Pane infoSection;
  private Pane bottomSection;

  public BoardView(BoardGame game, BoardController controller) {
    root = new StackPane();
    this.game = game;
    this.controller = controller;
    game.addObserver(this);
  }

  private GridPane setupLayout() {
    GridPane container = new GridPane();

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
    VBox infoSection = new VBox(5);
    infoSection.setPadding(new Insets(10));
    infoSection.setStyle("""
            -fx-background-color: #eee;
            -fx-background-radius: 10;
            -fx-border-radius: 10;
            -fx-border-width: 1;
        """);
    infoSection.setEffect(new DropShadow(3, Color.gray(0.2)));

    HBox infoBox1 = createInfoBox(Color.RED, "Go down ladder");
    HBox infoBox2 = createInfoBox(Color.INDIANRED, "Bottom of bad ladder");
    HBox infoBox3 = createInfoBox(Color.GREEN, "Go up ladder");
    HBox infoBox4 = createInfoBox(Color.LIME, "Top of good ladder");
    infoSection.getChildren().addAll(infoBox1, infoBox2, infoBox3, infoBox4);

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

    Color[] color = new Color[board.tileAmount() + 1];
    ArrayList<Pair<Integer, Integer>> ladderStartEnd = new ArrayList<>();

    setupTileColors(color, ladderStartEnd);

    drawGameBoard(boardCanvas, color, ladderStartEnd);

    // Make sure canvas size corresponds to window size
    double relativeCanvasSize = 0.9;
    boardCanvas.widthProperty().bind(gameSection.widthProperty().multiply(relativeCanvasSize));
    boardCanvas.heightProperty().bind(gameSection.heightProperty().multiply(relativeCanvasSize));
    playerCanvas.widthProperty().bind(gameSection.widthProperty().multiply(relativeCanvasSize));
    playerCanvas.heightProperty().bind(gameSection.heightProperty().multiply(relativeCanvasSize));

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
    double tileSize =
        Math.min(gameSection.getWidth() / board.width(), gameSection.getHeight() / board.height())
            * sizeMultiplier;
    tileWidth = (int) tileSize;
    tileHeight = (int) tileSize;
    this.drawGameBoard(boardCanvas, color, ladderStartEnd);
    this.drawPlayerPieces(playerCanvas);
  }

  private void setupTileColors(Color[] color, ArrayList<Pair<Integer, Integer>> ladderStartEnd) {
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

  private void drawGameBoard(Canvas canvas, Color[] color,
      ArrayList<Pair<Integer, Integer>> ladderStartEnd) {
    GraphicsContext gc = canvas.getGraphicsContext2D();

    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

    // Draw tiles
    for (int i = 1; i <= board.tileAmount(); i++) {
      int x = calculateCornerX(i);
      int y = calculateCornerY(i);

      gc.setFill(Color.BLACK);
      gc.fillRect(x, y, tileWidth, tileHeight); // Draw tile border
      gc.setFill(color[i]);
      gc.fillRect(x + 0.5, y + 0.5, tileWidth - 1, tileHeight - 1);
      gc.setFill(Color.BLACK);
      gc.fillText(String.valueOf(i), x + 1, y + tileHeight - 1);
    }

    // Draw ladders
    for (Pair<Integer, Integer> startEnd : ladderStartEnd) {
      drawLadder(gc, startEnd.getKey(), startEnd.getValue());
    }
  }

  private Pane setupPlayerSection() {
    VBox playerSection = new VBox(5);

    playerSection.setPadding(new Insets(10));
    playerSection.setStyle("""
            -fx-background-color: #eee;
            -fx-background-radius: 10;
            -fx-border-radius: 10;
            -fx-border-width: 1;
        """);
    playerSection.setEffect(new DropShadow(3, Color.gray(0.2)));

    playerSection.getChildren().clear();

    List<PlayerViewModel> playerInformation = game.getPlayerViewModels();

    playerInformation.forEach(player -> playerSection.getChildren().add(createPlayerBox(player)));
    return playerSection;
  }

  private void drawPlayerPieces(Canvas canvas) {
    //clear the player canvas
    canvas.getGraphicsContext2D()
        .clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

    GraphicsContext gc = canvas.getGraphicsContext2D();

    game.getPlayerViewModels().forEach(player -> {
      int position = player.positionProperty().get();
      int x = calculateCenterX(position);
      int y = calculateCenterY(position);

      gc.setFill(player.color());
      drawPlayerShape(gc, player.shape(), x, y);
    });
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
    VBox bottomCenter = new VBox(10);
    bottomCenter.setAlignment(Pos.CENTER);

    // Add dice throw button with active player indicator
    Button throwDiceButton = new Button();
    throwDiceButton.setOnAction(e -> controller.throwDice());
    throwDiceButton.setPrefSize(200, 40);
    throwDiceButton.textProperty().bind(Bindings.createStringBinding(
        () -> "Throw dice for " + game.getActivePlayerProperty().get(),
        game.getActivePlayerProperty()));

    Label statusLabel = new Label("Game in progress");

    bottomCenter.getChildren().addAll(throwDiceButton, statusLabel);

    return bottomCenter;
  }


  private GridPane initScene() {
    GridPane container = setupLayout();

    board = game.getBoardViewModel();

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
    return board.tiles().get(i).x() * tileWidth;
  }

  private int calculateCenterX(int i) {
    return board.tiles().get(i).x() * tileWidth + tileWidth / 2;
  }

  private int calculateCornerY(int i) {
    return board.tiles().get(i).y() * tileHeight;
  }

  private int calculateCenterY(int i) {
    return board.tiles().get(i).y() * tileHeight + tileHeight / 2;
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
  private VBox createPlayerBox(PlayerViewModel player) {
    // Player color and name
    Label nameLabel = new Label(player.name());
    nameLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
    nameLabel.setTextFill(player.color());

    Rectangle colorRect = new Rectangle(20, 20, player.color());

    HBox infoBox = new HBox(5, colorRect, nameLabel);
    infoBox.setAlignment(Pos.CENTER_LEFT);

    // Player shape
    Label shapeLabel = new Label(player.shape());
    shapeLabel.setFont(Font.font("System", 12));
    shapeLabel.setTextFill(Color.DARKGRAY);

    // Player position
    Label positionLabel = new Label();
    positionLabel.setFont(Font.font("System", 12));
    // Using a binding so position gets updated automatically
    positionLabel.textProperty().bind(Bindings.createStringBinding(
        () -> "Position: " + player.positionProperty().get(), player.positionProperty()));

    VBox box = new VBox(2);
    box.getChildren().addAll(infoBox, shapeLabel, positionLabel);
    box.setStyle("-fx-background-color: #eee; -fx-padding: 5px;");
    box.setMinWidth(150);

    return box;
  }

  private HBox createInfoBox(Color color, String infoText) {
    Rectangle rect = new Rectangle(50, 50, color);
    Text text = new Text(infoText);
    text.setFont(Font.font("System", 12));

    HBox box = new HBox(5);
    box.setAlignment(Pos.CENTER_LEFT);
    box.getChildren().addAll(rect, text);
    box.setStyle("-fx-background-color: #eee;");
    box.setMinWidth(150);

    return box;
  }

  public StackPane getRoot() {
    return root;
  }

  @Override
  public void update() {
    if (root.getChildren().isEmpty()) {
      mainContainer = initScene();
      root.getChildren().add(mainContainer);
    }

    Canvas playerCanvas = (Canvas) gameSection.getChildren().get(1);

    drawPlayerPieces(playerCanvas);

    if (game.getWinner() != null) {
      disableGameControls();
      changeStateLabel();
    }
  }

  private void disableGameControls() {
    Button btn = (Button) bottomSection.getChildren().getFirst();
    btn.setDisable(true);
  }

  private void changeStateLabel() {
    Label lbl = (Label) bottomSection.getChildren().getLast();
    lbl.setText("Winner: " + game.getActivePlayerProperty().get() + "!");
    lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
  }
}
