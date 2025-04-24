package edu.ntnu.idi.idatt.ui;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class GameMenuView {
  private final BorderPane root;
  private final GameMenuController controller;
  private final ListView<String> playerListView = new ListView<>();
  private final ObservableList<String> observablePlayerList = FXCollections.observableArrayList();
  private ComboBox<String> boardSelector;

  public GameMenuView(GameMenuController controller) {
    this.controller = controller;
    root = new BorderPane();
    setUpMenuScreen();
  }

  private void setUpMenuScreen() {
    root.setStyle("-fx-background-color: #eee;");

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

    //Player section
    Text playerSectionTitle = new Text("Add players");
    playerSectionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));

    HBox playerInputBox = new HBox(10);
    TextField playerNameField = new TextField();
    playerNameField.setPromptText("Enter player Name");
    playerNameField.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        controller.addPlayer(playerNameField, observablePlayerList);
      }
    });
    Button addPlayerButton = new Button("Add Player");
    playerInputBox.getChildren().addAll(playerNameField, addPlayerButton);
    playerInputBox.setAlignment(Pos.CENTER);

    playerListView.setItems(observablePlayerList);
    playerListView.setPrefHeight(60);
    playerListView.setPrefWidth(300);
    playerListView.setOrientation(javafx.geometry.Orientation.HORIZONTAL);

    Button removePlayerButton = new Button("Remove Player");
    removePlayerButton.setDisable(true);

    playerListView.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> removePlayerButton.setDisable(newValue == null));

    addPlayerButton.setOnAction(event -> controller.addPlayer(playerNameField, observablePlayerList));

    removePlayerButton.setOnAction(event -> controller.removePlayer(playerListView, observablePlayerList));

    //Start game Button
    Button startGameButton = new Button("Start Game");
    startGameButton.setStyle("-fx-background-color: #4VAF50;");
    startGameButton.setPrefWidth(200);
    startGameButton.setPrefHeight(40);
    startGameButton.setFont(Font.font("System", FontWeight.BOLD, 14));

    startGameButton.setOnAction(event -> controller.startGame());

    //Add all components to the content box
    contentBox.getChildren().addAll(gameSelectionTitle, gameSelector,
        boardSelectionTitle, boardSelector,
        playerSectionTitle, playerInputBox, playerListView,
        removePlayerButton, new Separator(), startGameButton);

    root.setTop(titleBox);
    root.setCenter(contentBox);
  }

  public BorderPane getRoot() {
    return root;
  }
}
