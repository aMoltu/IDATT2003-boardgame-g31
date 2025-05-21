package edu.ntnu.idi.idatt.fileio;

import edu.ntnu.idi.idatt.board.Board;
import java.io.IOException;
import java.nio.file.Path;

public interface BoardFileWriter {

  public void writeBoard(Board board, Path path) throws IOException;

}
