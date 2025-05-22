package edu.ntnu.idi.idatt.viewmodel;

import java.util.HashMap;

/**
 * View model for the game board containing layout and tile information.
 *
 * @param width      Board width in tiles
 * @param height     Board height in tiles
 * @param tileAmount Total number of tiles on the board
 * @param tiles      Map of tile IDs to their view models
 */
public record BoardViewModel(int width, int height, int tileAmount,
                             HashMap<Integer, TileViewModel> tiles) {

}
