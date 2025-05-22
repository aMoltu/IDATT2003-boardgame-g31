package edu.ntnu.idi.idatt.viewmodel;

/**
 * View model for a game tile containing display information.
 *
 * @param id             Unique identifier of the tile
 * @param x              X-coordinate on the board
 * @param y              Y-coordinate on the board
 * @param landActionType Type of action when landing on this tile (e.g., "LadderAction",
 *                       "QuestionTileAction")
 * @param nextId         ID of the next tile in sequence, or null if this is the last tile
 */
public record TileViewModel(int id, int x, int y, String landActionType, Integer nextId) {

}