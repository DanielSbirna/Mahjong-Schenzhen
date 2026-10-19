package ui;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import model.ItemType;

public class TileImageLoader {
    private static final Map<ItemType, Image> images = new HashMap<>();

    public static void loadImages() {
        for(ItemType type: ItemType.values()) {
            String path = "/images/" + type.name().toLowerCase()+".png";
            java.net.URL imgURL = TileImageLoader.class.getResource(path);

            if(imgURL == null) {
                System.out.println("Could not find image: " + path);
                continue;
            }

            ImageIcon icon = new ImageIcon(imgURL);
            images.put(type, icon.getImage());
        }
    }

    public static Image getImage(ItemType type) {
        return images.get(type);
    }
}
