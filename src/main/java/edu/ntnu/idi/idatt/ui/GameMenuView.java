package edu.ntnu.idi.idatt.ui;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class GameMenuView {

  private final StackPane root;
  private final GameMenuController controller;
  private final ListView<String> playerListView = new ListView<>();
  private final BorderPane gameSelectScreen;
  private final BorderPane playerSelectScreen;
  private final ObservableList<String> observablePlayerList = FXCollections.observableArrayList();
  private ComboBox<String> boardSelector;

  public GameMenuView(GameMenuController controller) {
    this.controller = controller;
    gameSelectScreen = generateGameSelectScreen();
    playerSelectScreen = generatePlayerSelectScreen();

    root = new StackPane();
    root.getChildren().setAll(gameSelectScreen);
  }

  private BorderPane generateGameSelectScreen() {
    BorderPane ret = new BorderPane();
    ret.setStyle("-fx-background-color: #eee;");

    //Title section
    Text title = new Text("Game Menu");
    title.setFont(Font.font("System", FontWeight.BOLD, 24));
    HBox titleBox = new HBox(title);
    titleBox.setAlignment(Pos.CENTER);
    titleBox.setPadding(new Insets(20, 0, 30, 0));

    //Main content section
    VBox contentBox = new VBox(15);
    contentBox.setAlignment(Pos.TOP_CENTER);
    contentBox.setPadding(new Insets(10, 50, 10, 50));

    //Game selection section
    Text gameSelectionTitle = new Text("Select a game");
    gameSelectionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
    ComboBox<String> gameSelector = new ComboBox<>();
    gameSelector.getItems().add("Ladder Game");
    gameSelector.setValue("Ladder Game");
    gameSelector.setMinWidth(200);

    //Board selection section
    Text boardSelectionTitle = new Text("Select a board");
    boardSelectionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
    boardSelector = new ComboBox<>();
    boardSelector.getItems().add("Default");

    //Add available board files from resources
    Path boardsPath = FileSystems.getDefault().getPath("src", "main", "resources", "boards");
    File boardsDir = new File(boardsPath.toString());
    if (boardsDir.isDirectory() && boardsDir.exists()) {
      File[] boardFiles = boardsDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));
      if (boardFiles != null) {
        for (File boardFile : boardFiles) {
          boardSelector.getItems().add(boardFile.getName());
        }
      }
    }
    boardSelector.setValue("Default");
    boardSelector.setMinWidth(200);
    boardSelector.setOnAction(event -> controller.setSelectedBoard(boardSelector.getValue()));

    // Button that goes to player select screen
    Button startGameButton = new Button("Continue");
    startGameButton.setStyle("-fx-background-color: #4caf50;");
    startGameButton.setPrefWidth(200);
    startGameButton.setPrefHeight(40);
    startGameButton.setFont(Font.font("System", FontWeight.BOLD, 14));

    startGameButton.setOnAction(event -> root.getChildren().setAll(playerSelectScreen));

    //Add all components to the content box
    contentBox.getChildren().addAll(gameSelectionTitle, gameSelector,
        boardSelectionTitle, boardSelector, new Separator(), startGameButton);

    ret.setTop(titleBox);
    ret.setCenter(contentBox);
    return ret;
  }

  private BorderPane generatePlayerSelectScreen() {
    BorderPane ret = new BorderPane();
    ret.setStyle("-fx-background-color: #eee;");

    //Go back button
    Button goBackBtn = new Button("go back");
    goBackBtn.setOnAction(event -> root.getChildren().setAll(gameSelectScreen));
    goBackBtn.setAlignment(Pos.TOP_LEFT);

    //Title section
    Text title = new Text("Game Menu");
    title.setFont(Font.font("System", FontWeight.BOLD, 24));
    HBox titleBox = new HBox(title);
    titleBox.setAlignment(Pos.CENTER);
    titleBox.setPadding(new Insets(20, 0, 30, 0));

    VBox topBox = new VBox();
    topBox.getChildren().addAll(goBackBtn, titleBox);

    //Main content section
    VBox contentBox = new VBox(15);
    contentBox.setAlignment(Pos.TOP_CENTER);
    contentBox.setPadding(new Insets(10, 50, 10, 50));

    //Player section
    Text playerSectionTitle = new Text("Add players");
    playerSectionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));

    TextField playerNameField = new TextField();
    playerNameField.setPromptText("Enter player Name");
    playerNameField.setMaxWidth(200);

    ComboBox<String> playerShapeSelector = new ComboBox<>();
    playerShapeSelector.getItems().addAll("Circle", "Triangle", "Square", "Diamond");
    playerShapeSelector.setValue("Circle");
    playerShapeSelector.setPrefWidth(200);

    ColorPicker playerColorPicker = new ColorPicker();
    playerColorPicker.setPrefWidth(200);

    Button addPlayerButton = new Button("Add Player");

    //Import player from file
    Text separator = new Text("Or");
    separator.setFont(Font.font("System", FontWeight.BOLD, 16));

    ComboBox<String> fileSelector = new ComboBox<>();
    Path playerPath = FileSystems.getDefault().getPath("src", "main", "resources", "players");
    File playerDir = new File(playerPath.toString());
    if (playerDir.isDirectory() && playerDir.exists()) {
      File[] playerFiles = playerDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
      if (playerFiles != null) {
        for (File file : playerFiles) {
          fileSelector.getItems().add(file.getName());
          fileSelector.setValue(file.getName());
        }
      }
    }

    Button importButton = new Button("Import players from file");
    importButton.setOnAction(
        event -> controller.addPlayers(
            playerPath.resolve(fileSelector.getValue()), observablePlayerList));

    VBox playerInputBox = new VBox(10);
    playerInputBox.getChildren()
        .addAll(playerNameField, playerShapeSelector, playerColorPicker, addPlayerButton,
            separator, fileSelector, importButton);
    playerInputBox.setAlignment(Pos.CENTER);

    playerListView.setItems(observablePlayerList);
    playerListView.setPrefHeight(60);
    playerListView.setPrefWidth(300);
    playerListView.setOrientation(javafx.geometry.Orientation.HORIZONTAL);

    Button removePlayerButton = new Button("Remove Player");
    removePlayerButton.setDisable(true);

    playerListView.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> removePlayerButton.setDisable(newValue == null));

    addPlayerButton.setOnAction(
        event -> controller.addPlayer(playerNameField, playerShapeSelector.getValue(),
            playerColorPicker.getValue(), observablePlayerList));

    removePlayerButton.setOnAction(
        event -> controller.removePlayer(playerListView, observablePlayerList));

    //Start game Button
    Button startGameButton = new Button("Start Game");
    startGameButton.setStyle("-fx-background-color: #4caf50;");
    startGameButton.setPrefWidth(200);
    startGameButton.setPrefHeight(40);
    startGameButton.setFont(Font.font("System", FontWeight.BOLD, 14));

    startGameButton.setOnAction(event -> controller.startGame());

    //Add all components to the content box
    contentBox.getChildren().addAll(playerSectionTitle, playerInputBox, playerListView,
        removePlayerButton, new Separator(), startGameButton);

    ret.setTop(topBox);
    ret.setCenter(contentBox);

    return ret;
  }

  public StackPane getRoot() {
    return root;
  }
}
