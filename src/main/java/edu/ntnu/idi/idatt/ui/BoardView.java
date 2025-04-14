package edu.ntnu.idi.idatt.ui;

import edu.ntnu.idi.idatt.engine.BoardGame;
import edu.ntnu.idi.idatt.model.Tile;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class BoardView {

  private final StackPane root;
  private final BoardController controller;

  public BoardView(BoardController controller) {
    root = new StackPane();
    this.controller = controller;
    root.getChildren().add(startMenu());
  }

  private BorderPane startMenu() {
    BorderPane pane = new BorderPane();
    pane.setStyle("-fx-background-color: #eee");
    Button btn = new Button("Start game");
    btn.setOnAction(event -> {
      root.getChildren().clear();
      root.getChildren().add(theGame());
    });
    pane.setCenter(btn);
    return pane;
  }

  private Canvas theGame() {
    Canvas canvas = new Canvas(300, 300);

    BoardGame game = controller.getGame();
    int tileWidth = 30;
    int tileHeight = 30;
    for (int i = 1; i <= 90; i++) {
      Tile tile = game.getBoard().getTile(i);
      int x = ((i - 1) % 10) * tileWidth;
      int y = tileHeight * 9 - (((i - 1) / 10) * tileHeight);

      GraphicsContext gc = canvas.getGraphicsContext2D();
      gc.setFill(Color.rgb(i * 2, i * 2, i * 2));
      gc.fillRect(x, y, tileWidth, tileHeight);
      gc.setFill(Color.RED);
      gc.fillText(String.valueOf(i), x, y + tileHeight);
    }
    return canvas;
  }

  public StackPane getRoot() {
    return root;
  }
}
