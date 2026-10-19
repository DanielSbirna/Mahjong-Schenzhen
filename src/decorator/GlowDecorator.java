package decorator;

import model.Tile;

public class GlowDecorator extends TileDecorator {

    public GlowDecorator(Tile tile) {
        super(tile);
    }

    public boolean matches(Tile other) {
        return tile.matches(other);
    }
}
