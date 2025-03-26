package edu.ntnu.idi.idatt.fileio;

import edu.ntnu.idi.idatt.board.Board;
import java.nio.file.Path;

public interface BoardFileReader {

  public Board readBoard(Path path);
}
