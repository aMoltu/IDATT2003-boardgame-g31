package edu.ntnu.idi.idatt.viewmodel;

import java.util.HashMap;

public record BoardViewModel(int width, int height, int tileAmount,
                             HashMap<Integer, TileViewModel> tiles) {

}
