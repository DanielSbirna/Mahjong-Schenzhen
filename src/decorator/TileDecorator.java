package decorator;

import model.Tile;

public class TileDecorator {
    protected Tile tile;

    public TileDecorator(Tile tile) {
        this.tile = tile;
    }
}
