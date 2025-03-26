package edu.ntnu.idi.idatt.fileio;

import edu.ntnu.idi.idatt.model.Board;
import java.nio.file.Path;

public interface BoardFileReader {

  public Board readBoard(Path path);
}
