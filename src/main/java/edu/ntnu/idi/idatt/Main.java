package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.game.BoardGame;
import edu.ntnu.idi.idatt.game.Player;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    BoardGame game = new BoardGame();
    Scanner scanner = new Scanner(System.in);

    //add between 1 and 4 players
    int playerCount = 1;
    boolean addMorePlayers = true;
    while (playerCount < 5 && addMorePlayers) {
      System.out.print("Type the name of player " + playerCount + ": ");
      String name = scanner.nextLine();
      game.addPlayer(new Player(name, game));
      playerCount++;

      if (playerCount < 5) {
        String command = "";
        while (!command.equals("y") && !command.equals("n")) {
          System.out.print("Do you want to add more players? y/n: ");
          command = scanner.nextLine();
        }
        if (command.equals("n")) {
          addMorePlayers = false;
        }
      }
    }

    System.out.println("\nAnd the game begins!\n");

    while (game.getWinner() == null) {
      game.play();
      scanner.nextLine();
    }

    System.out.println("And the winner is " + game.getWinner().getName());
  }
}