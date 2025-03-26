package edu.ntnu.idi.idatt.fileio;

import com.google.gson.Gson;
import edu.ntnu.idi.idatt.board.Board;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;

/**
 * File Writer that uses Gson to create a json file from a board.
 */
public class BoardFileWriterGson implements BoardFileWriter{

  /**
   * Constructor for the Gson writer.
   */
  public BoardFileWriterGson() {}

  /**
   *Create a Json file using Gson from a Board
   *
   * @param board A board that's to be converted to json file
   * @param path desired path + name for the json file
   * @throws IOException
   */
  public void writeBoard(Board board, Path path) throws IOException {
    Gson gson = new Gson();
    try (Writer writer = new FileWriter(path.toFile())) {
      gson.toJson(board, writer);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
