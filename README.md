# Portfolio project IDATT2003 group 31

## Project description

This project is the final assignment in the course IDATT2003 at NTNU.The project revolves around
creating a javafx application where a user can play board games. The project includes a graphical
user
interface, two different games, and two different layouts. It is also possible to import files for a
more nuanced gameplay experience. The game allows up to 5 players to play at once.

## Project structure

The source files are stored in src/main/java/edu/ntnu/idi/idatt. The source files are divided into
several packages. One for models, one for file input and output (fileio), one for the engine, one
for observers, one for view models and lastly one for user interface (ui) which is subdivided into a
view package and a controller package.

The test classes are stored in test/main/java/edu/ntnu/idi/idatt. Here you will find test
classes for some of the main classes.

Additionally, there is an example folder containing two example files that can be used to test the
file system of the application.

## How to run the project

To run the project, clone this repository and use the command `mvn javafx:run` or `mvn clean javafx:
run`. This requires maven and java version 21 to be installed.

## How to run the tests

To run tests, use the command `mvn test`.

## Limitations

- The application does not feature online multiplayer. In order for multiple people to play
  together,
  a single computer should be used.
- The application does not use FXML.

## Link to repository

https://github.com/aMoltu/IDATT2003-boardgame-g31

## Contributors

- Aleksander Emil Moltu (aMoltu)
- Alexander Oddsen Arne (AlexanderArne)
