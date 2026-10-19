package factory;

import model.ItemTile;
import model.ItemType;
import model.Tile;

public class ItemTileFactory extends TileFactory {
    @Override
    public ItemTile createTile(ItemType type) {
        return new ItemTile(type);
    }
}
