package edu.ntnu.idi.idatt.fileio;

/**
 * Exception thrown when encountering an unknown tile action.
 */
public class UnknownTileActionException extends Exception {

  /**
   * Constructs an UnknownTileActionException.
   *
   * @param msg the message describing the exception
   */
  public UnknownTileActionException(String msg) {
    super(msg);
  }
}
