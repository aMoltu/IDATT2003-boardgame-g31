package edu.ntnu.idi.idatt.ui.view;

import edu.ntnu.idi.idatt.engine.BoardGame;
import edu.ntnu.idi.idatt.observer.BoardGameObserver;
import edu.ntnu.idi.idatt.ui.controller.BoardController;
import edu.ntnu.idi.idatt.viewmodel.BoardViewModel;
import edu.ntnu.idi.idatt.viewmodel.PlayerViewModel;
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

/**
 * Abstract base class for game board views implementing common board display functionality.
 */
public abstract class BoardView implements BoardGameObserver {

  protected final StackPane root;
  protected final BoardGame game;
  protected final BoardController controller;
  protected BoardViewModel board;
  protected GridPane mainContainer;
  protected int tileWidth = 30;
  protected int tileHeight = 30;
  protected Pane topSection;
  protected Pane playerSection;
  protected Pane gameSection;
  protected Pane infoSection;
  protected Pane bottomSection;

  /**
   * Creates a new board view with game and controller.
   *
   * @param game       The game instance to display
   * @param controller Controller for board interactions
   */
  public BoardView(BoardGame game, BoardController controller) {
    root = new StackPane();
    this.game = game;
    this.controller = controller;
    game.addObserver(this);
  }

  /**
   * Sets up the main layout grid with column and row constraints.
   *
   * @return Configured grid pane for the board layout
   */
  protected GridPane setupLayout() {
    GridPane container = new GridPane();

    container.setHgap(10);
    container.setVgap(10);
    container.setPadding(new Insets(10));

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

    // Back button
    Button backButton = new Button("←");
    backButton.setStyle(
        "-fx-font-size: 32px; -fx-font-weight: bold; -fx-background-color: transparent;"
            + " -fx-border-color: transparent; -fx-cursor: hand;");
    backButton.setOnAction(event -> controller.goBack());
    backButton.setAlignment(Pos.TOP_LEFT);
    backButton.setPadding(new Insets(0, 0, 0, 10));

    container.add(backButton, 1, 0);

    return container;
  }

  /**
   * Sets up the information section with styling.
   *
   * @return Styled pane for displaying game information
   */
  protected Pane setupInfoSection() {
    VBox infoSection = new VBox(5);
    infoSection.setPadding(new Insets(10));
    infoSection.setStyle("""
            -fx-background-color: #eee;
            -fx-background-radius: 10;
            -fx-border-radius: 10;
            -fx-border-width: 1;
        """);
    infoSection.setEffect(new DropShadow(3, Color.gray(0.2)));

    return infoSection;
  }

  /**
   * Resizable canvas that adapts to container dimensions.
   */
  protected static class ResizableCanvas extends Canvas {

    @Override
    public double prefWidth(double height) {
      return 0;
    }

    @Override
    public double prefHeight(double width) {
      return 0;
    }
  }

  /**
   * Sets up the game board with tiles and player pieces.
   *
   * @return Pane containing the game board
   */
  protected Pane setupGameBoard() {
    StackPane gameSection = new StackPane();

    Canvas boardCanvas = new ResizableCanvas();
    Canvas playerCanvas = new ResizableCanvas();
    gameSection.getChildren().addAll(boardCanvas, playerCanvas);

    Color[] color = new Color[board.tileAmount() + 1];
    setupTileColors(color);

    drawGameBoard(boardCanvas, color);

    // Make sure canvas size corresponds to window size
    double relativeCanvasSize = 0.9;
    boardCanvas.widthProperty().bind(gameSection.widthProperty().multiply(relativeCanvasSize));
    boardCanvas.heightProperty().bind(gameSection.heightProperty().multiply(relativeCanvasSize));
    playerCanvas.widthProperty().bind(gameSection.widthProperty().multiply(relativeCanvasSize));
    playerCanvas.heightProperty().bind(gameSection.heightProperty().multiply(relativeCanvasSize));

    gameSection.widthProperty().addListener(
        (obs, oldVal, newVal) -> redrawCanvasAfterResize(boardCanvas, color,
            playerCanvas, relativeCanvasSize));
    gameSection.heightProperty().addListener(
        (obs, oldVal, newVal) -> redrawCanvasAfterResize(boardCanvas, color,
            playerCanvas, relativeCanvasSize));

    return gameSection;
  }

  /**
   * Redraws the canvas after window resize.
   *
   * @param boardCanvas    Canvas for drawing the board
   * @param color          Array of colors for each tile
   * @param playerCanvas   Canvas for drawing player pieces
   * @param sizeMultiplier Size multiplier for the canvas
   */
  protected void redrawCanvasAfterResize(Canvas boardCanvas, Color[] color,
      Canvas playerCanvas, double sizeMultiplier) {
    double tileSize =
        Math.min(gameSection.getWidth() / board.width(), gameSection.getHeight() / board.height())
            * sizeMultiplier;
    tileWidth = (int) tileSize;
    tileHeight = (int) tileSize;
    this.drawGameBoard(boardCanvas, color);
    this.drawPlayerPieces(playerCanvas);
  }

  /**
   * Sets up tile colors based on their types.
   *
   * @param color Array to store tile colors
   */
  protected abstract void setupTileColors(Color[] color);

  /**
   * Returns the title of the game to be displayed in the top section.
   *
   * @return The game title
   */
  protected abstract String getGameTitle();

  /**
   * Sets up the top section with the game title.
   *
   * @return Pane containing the game title
   */
  protected Pane setupTopSection() {
    HBox topCenter = new HBox();
    topCenter.setAlignment(Pos.CENTER);

    Text title = new Text(getGameTitle());
    title.setFont(Font.font("System", FontWeight.BOLD, 24));

    topCenter.getChildren().add(title);
    return topCenter;
  }

  /**
   * Draws the game board with tiles and their colors.
   *
   * @param canvas Canvas to draw on
   * @param color  Array of colors for each tile
   */
  protected void drawGameBoard(Canvas canvas, Color[] color) {
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
  }

  /**
   * Sets up the player section with player information.
   *
   * @return Pane containing player information
   */
  protected Pane setupPlayerSection() {
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

  /**
   * Draws player pieces on the board.
   *
   * @param canvas Canvas to draw on
   */
  protected void drawPlayerPieces(Canvas canvas) {
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

  /**
   * Draws a player shape at the specified coordinates.
   *
   * @param gc    Graphics context for drawing
   * @param shape Shape identifier to draw
   * @param x     X-coordinate
   * @param y     Y-coordinate
   */
  protected void drawPlayerShape(GraphicsContext gc, String shape, int x, int y) {
    switch (shape) {
      case "Circle" -> gc.fillOval(x - 12, y - 12, 25, 25);
      case "Square" -> gc.fillRect(x - 10, y - 10, 20, 20);
      case "Triangle" -> {
        double[] triPointsX = {x - 10, x, x + 10};
        double[] triPointsY = {y - 10, y + 10, y - 10};
        gc.fillPolygon(triPointsX, triPointsY, 3);
      }
      case "Diamond" -> {
        double[] diaPointsX = {x, x - 10, x, x + 10};
        double[] diaPointsY = {y - 10, y, y + 10, y};
        gc.fillPolygon(diaPointsX, diaPointsY, 4);
      }
      default -> {
        System.err.println("Unknown shape: " + shape);
        gc.fillOval(x - 12, y - 12, 25, 25);
      }
    }
  }

  /**
   * Sets up the bottom section with dice and controls.
   *
   * @return Pane containing game controls
   */
  protected Pane setupBottomSection() {
    VBox bottomCenter = new VBox(10);
    bottomCenter.setAlignment(Pos.CENTER);

    HBox diceAndButton = new HBox(20);
    diceAndButton.setAlignment(Pos.CENTER);

    // Add dice throw button with active player indicator
    Button throwDiceButton = new Button();
    throwDiceButton.setOnAction(e -> controller.throwDice());
    throwDiceButton.setPrefSize(200, 40);
    throwDiceButton.textProperty().bind(Bindings.createStringBinding(
        () -> "Throw dice for " + game.getActivePlayerProperty().get(),
        game.getActivePlayerProperty()));

    Canvas leftDie = new Canvas(40, 40);
    Canvas rightDie = new Canvas(40, 40);

    diceAndButton.getChildren().addAll(leftDie, throwDiceButton, rightDie);

    Label statusLabel = new Label("Game in progress");

    bottomCenter.getChildren().addAll(diceAndButton, statusLabel);

    return bottomCenter;
  }

  /**
   * Initializes the game scene with all components.
   *
   * @return Grid pane containing the complete game layout
   */
  protected GridPane initScene() {
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

  /**
   * Calculates the X-coordinate for a tile's corner.
   *
   * @param i Tile number
   * @return X-coordinate
   */
  protected int calculateCornerX(int i) {
    return board.tiles().get(i).x() * tileWidth;
  }

  /**
   * Calculates the X-coordinate for a tile's center.
   *
   * @param i Tile number
   * @return X-coordinate
   */
  protected int calculateCenterX(int i) {
    return board.tiles().get(i).x() * tileWidth + tileWidth / 2;
  }

  /**
   * Calculates the Y-coordinate for a tile's corner.
   *
   * @param i Tile number
   * @return Y-coordinate
   */
  protected int calculateCornerY(int i) {
    return board.tiles().get(i).y() * tileHeight;
  }

  /**
   * Calculates the Y-coordinate for a tile's center.
   *
   * @param i Tile number
   * @return Y-coordinate
   */
  protected int calculateCenterY(int i) {
    return board.tiles().get(i).y() * tileHeight + tileHeight / 2;
  }

  /**
   * Creates a box displaying player information.
   *
   * @param player Player view model
   * @return VBox containing player information
   */
  protected VBox createPlayerBox(PlayerViewModel player) {
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

  /**
   * Creates a box displaying information with color.
   *
   * @param color    Color for the box
   * @param infoText Text to display
   * @return HBox containing the information
   */
  protected HBox createInfoBox(Color color, String infoText) {
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

  /**
   * Gets the root pane of the view.
   *
   * @return Root stack pane
   */
  public StackPane getRoot() {
    return root;
  }

  /**
   * Updates the view when game state changes.
   */
  @Override
  public void update() {
    // Always reinitialize the scene when the game state changes
    mainContainer = initScene();
    root.getChildren().clear();
    root.getChildren().add(mainContainer);

    // Update dice display
    HBox diceAndButton = (HBox) bottomSection.getChildren().getFirst();
    Canvas leftDie = (Canvas) diceAndButton.getChildren().get(0);
    Canvas rightDie = (Canvas) diceAndButton.getChildren().get(2);
    drawDie(leftDie.getGraphicsContext2D(), game.getDie(0));
    drawDie(rightDie.getGraphicsContext2D(), game.getDie(1));

    if (game.getWinner() != null) {
      disableGameControls();
      changeStateLabel();
    }
  }

  /**
   * Disables game controls when game ends.
   */
  protected void disableGameControls() {
    HBox diceAndButton = (HBox) bottomSection.getChildren().getFirst();
    Button btn = (Button) diceAndButton.getChildren().get(1);
    btn.setDisable(true);
  }

  /**
   * Updates the state label when game ends.
   */
  protected void changeStateLabel() {
    Label lbl = (Label) bottomSection.getChildren().getLast();
    lbl.setText("Winner: " + game.getActivePlayerProperty().get() + "!");
    lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
  }

  /**
   * Draws a die with the specified value.
   *
   * @param gc    Graphics context for drawing
   * @param value Value to display
   */
  private void drawDie(GraphicsContext gc, int value) {
    // Draw die background
    gc.setFill(Color.WHITE);
    gc.setStroke(Color.BLACK);
    gc.fillRect(5, 5, 30, 30);
    gc.strokeRect(5, 5, 30, 30);

    // Draw dots based on value
    gc.setFill(Color.BLACK);
    int dotSize = 30 / 6;
    int padding = 30 / 4;

    switch (value) {
      case 1 -> drawDot(gc, 5 + 30 / 2, 5 + 30 / 2, dotSize);
      case 2 -> {
        drawDot(gc, 5 + padding, 5 + padding, dotSize);
        drawDot(gc, 5 + 30 - padding, 5 + 30 - padding, dotSize);
      }
      case 3 -> {
        drawDot(gc, 5 + padding, 5 + padding, dotSize);
        drawDot(gc, 5 + 30 / 2, 5 + 30 / 2, dotSize);
        drawDot(gc, 5 + 30 - padding, 5 + 30 - padding, dotSize);
      }
      case 4 -> {
        drawDot(gc, 5 + padding, 5 + padding, dotSize);
        drawDot(gc, 5 + 30 - padding, 5 + padding, dotSize);
        drawDot(gc, 5 + padding, 5 + 30 - padding, dotSize);
        drawDot(gc, 5 + 30 - padding, 5 + 30 - padding, dotSize);
      }
      case 5 -> {
        drawDot(gc, 5 + padding, 5 + padding, dotSize);
        drawDot(gc, 5 + 30 - padding, 5 + padding, dotSize);
        drawDot(gc, 5 + 30 / 2, 5 + 30 / 2, dotSize);
        drawDot(gc, 5 + padding, 5 + 30 - padding, dotSize);
        drawDot(gc, 5 + 30 - padding, 5 + 30 - padding, dotSize);
      }
      case 6 -> {
        drawDot(gc, 5 + padding, 5 + padding, dotSize);
        drawDot(gc, 5 + padding, 5 + 30 / 2, dotSize);
        drawDot(gc, 5 + padding, 5 + 30 - padding, dotSize);
        drawDot(gc, 5 + 30 - padding, 5 + padding, dotSize);
        drawDot(gc, 5 + 30 - padding, 5 + 30 / 2, dotSize);
        drawDot(gc, 5 + 30 - padding, 5 + 30 - padding, dotSize);
      }
      default -> {
      }
    }
  }

  /**
   * Draws a dot on the die.
   *
   * @param gc   Graphics context for drawing
   * @param x    X-coordinate
   * @param y    Y-coordinate
   * @param size Size of the dot
   */
  private void drawDot(GraphicsContext gc, int x, int y, int size) {
    gc.fillOval(x - (double) size / 2, y - (double) size / 2, size, size);
  }
}
