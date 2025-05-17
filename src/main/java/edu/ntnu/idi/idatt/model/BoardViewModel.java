package edu.ntnu.idi.idatt.model;

import java.util.HashMap;

public record BoardViewModel(int width, int height, int tileAmount,
                             HashMap<Integer, TileViewModel> tiles) {

}
