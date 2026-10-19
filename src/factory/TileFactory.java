package factory;

import model.ItemTile;
import model.ItemType;

public abstract class TileFactory {
    public abstract ItemTile createTile(ItemType type);
}
