import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import model.*;
import ui.*;

public class Main {
    public static void main(String[] args) {

        // upload Images
        TileImageLoader.loadImages();

        // create tile list
        List<ItemTile> initialTiles = Arrays.asList(
                new ItemTile(ItemType.arrow),
                new ItemTile(ItemType.axe),
                new ItemTile(ItemType.axolotl),
                new ItemTile(ItemType.bell),
                new ItemTile(ItemType.berries),
                new ItemTile(ItemType.book),
                new ItemTile(ItemType.brush),
                new ItemTile(ItemType.cake),
                new ItemTile(ItemType.carrot),
                new ItemTile(ItemType.cod),
                new ItemTile(ItemType.diamond),
                new ItemTile(ItemType.egg),
                new ItemTile(ItemType.firework),
                new ItemTile(ItemType.potato),
                new ItemTile(ItemType.seeds),
                new ItemTile(ItemType.steak),
                new ItemTile(ItemType.sugar_cane),
                new ItemTile(ItemType.sword),
                new ItemTile(ItemType.trident),
                new ItemTile(ItemType.water_bottle)
        );

        // clone tiles
        List<ItemTile> tilesForBoard = new ArrayList<>();
        for (ItemTile tile : initialTiles) {
            for (int i = 0; i < 4; i++) {
                tilesForBoard.add(tile.clone());
            }
        }

        // shuffle
        Collections.shuffle(tilesForBoard);

        // create panel + frame
        GamePanel panel = new GamePanel(8, 10, tilesForBoard); // 4 rânduri x 6 coloane

        JFrame frame = new JFrame("Mahjong Shenzhen - Minecraft Edition");
        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}