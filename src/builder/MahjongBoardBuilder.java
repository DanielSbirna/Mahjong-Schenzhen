package builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import model.Board;
import model.Tile;
import factory.ItemTileFactory;
import model.ItemType;

public class MahjongBoardBuilder implements BoardBuilder {
    private List<Tile> tiles = new ArrayList<>();

    @Override
    public void buildTiles() {
        ItemTileFactory factory = new ItemTileFactory();

        for (ItemType type : ItemType.values()) {
            Tile tile = factory.createTile(type);
            for (int i = 0; i < 4; i++) { // 4 copies of each
                tiles.add(tile.clone());
            }
        }

        Collections.shuffle(tiles);
    }

    @Override
    public Board getBoard() {
        return new Board(tiles);
    }
}
