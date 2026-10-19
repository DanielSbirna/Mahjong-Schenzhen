package model;

import javax.swing.ImageIcon;
import java.net.URL;

public class ItemTile extends Tile {
    private ItemType itemType;
    private ImageIcon icon;

    public ItemTile(ItemType itemType) {
        this.itemType = itemType;
        loadIcon();
    }

    private void loadIcon() {
        // Căutăm imaginea în folderul resources/images/
        String path = "/images/" + itemType.name().toLowerCase() + ".png";
        URL imgURL = getClass().getResource(path);

        if (imgURL != null) {
            this.icon = new ImageIcon(imgURL);
        } else {
            System.err.println("Image not found in path " + path);
        }
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public ItemType getItemType() {
        return itemType;
    }

    @Override
    public boolean matches(Tile other) {
        if (!(other instanceof ItemTile)) {
            return false;
        }

        ItemTile otherTile = (ItemTile) other;
        return this.itemType == otherTile.itemType;
    }

    @Override
    public ItemTile clone() {
        return new ItemTile(this.itemType);
    }
}
