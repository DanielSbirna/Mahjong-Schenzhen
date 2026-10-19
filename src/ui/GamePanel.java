package ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import model.ItemTile;

public class GamePanel extends JPanel {

    // Board Size
    private int rows = 5;
    private int cols = 8;

    // Flat list of tiles
    private List<ItemTile> tiles;

    private ItemTile firstSelected = null;
    private ItemTile secondSelected = null;

    private int score = 0;

    private static final int TILE_SIZE = 55;
    private static final int GAP = 2;

    private static final int PADDING_TOP = 20;
    private static final int PADDING_LEFT = 20;
    private static final int PADDING_RIGHT = 20;
    private static final int PADDING_BOTTOM = 60;

    private ItemTile draggedTile = null;
    private int draggedIndex = -1;

    public GamePanel(int rows, int cols, List<ItemTile> tiles) {
        this.rows = rows;
        this.cols = cols;
        this.tiles = tiles;
        setPreferredSize(new Dimension(cols * (TILE_SIZE + GAP) + PADDING_LEFT + PADDING_RIGHT, rows * (TILE_SIZE + GAP) + PADDING_TOP + PADDING_BOTTOM));

        initMouse();
    }

    private void initMouse() {

        MouseAdapter mouse = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                int index = getTileIndexAt(e.getX(), e.getY());
                if (index == -1) return;

                ItemTile tile = tiles.get(index);
                if (tile == null) return;

                // If right-click / shift-click, we could add drag, but simplest:
                // For drag:
                draggedTile = tile;
                draggedIndex = index;

                // For click selection:
                if (firstSelected == null) {
                    firstSelected = tile;
                } else if (tile != firstSelected) {
                    secondSelected = tile;
                    checkMatch();
                }

                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (draggedTile == null) return;

                int targetIndex = getTileIndexAt(e.getX(), e.getY());
                if (targetIndex == -1) {
                    resetDrag();
                    return;
                }

                if (isValidDragMove(draggedIndex, targetIndex)) {
                    tiles.set(targetIndex, draggedTile);
                    tiles.set(draggedIndex, null);
                }

                resetDrag();
                repaint();
            }

        };

        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    // Game Logic
    private void checkMatch() {
        if(firstSelected == null || secondSelected == null) return;

        int i1 = tiles.indexOf(firstSelected);
        int i2 = tiles.indexOf(secondSelected);

        if(i1 == -1 || i2 == -1) return;

        // with canMatch
        if (canMatch(i1, i2)){
            removeTile(i1);
            removeTile(i2);
            score += 10;
        }

        firstSelected = null;
        secondSelected = null;
        // call repaint to fix noMoreMoves
        repaint();
    }

    private boolean areAdjacent(int i1, int i2) {
        int r1 = i1 / cols;
        int c1 = i1 % cols;

        int r2 = i2 / cols;
        int c2 = i2 % cols;

        int dr = Math.abs(r1 - r2);
        int dc = Math.abs(c1 - c2);

        return (dr + dc) == 1;
    }

    private boolean canSlideStraight(int fromIndex, int toIndex) {
        int fromRow = fromIndex / cols;
        int fromCol = fromIndex % cols;
        int toRow = toIndex / cols;
        int toCol = toIndex % cols;

        // Only horizontal or vertical movement
        if (fromRow != toRow && fromCol != toCol) return false;

        // Horizontal move
        if (fromRow == toRow) {
            int start = Math.min(fromCol, toCol) + 1;
            int end = Math.max(fromCol, toCol);
            for (int c = start; c < end; c++) {
                if (tiles.get(fromRow * cols + c) != null) return false; // blocked
            }
            return true;
        }

        // Vertical move
        if (fromCol == toCol) {
            int start = Math.min(fromRow, toRow) + 1;
            int end = Math.max(fromRow, toRow);
            for (int r = start; r < end; r++) {
                if (tiles.get(r * cols + fromCol) != null) return false; // blocked
            }
            return true;
        }

        return false;
    }

    private boolean canMatch(int i1, int i2) {
        ItemTile t1 = tiles.get(i1);
        ItemTile t2 = tiles.get(i2);

        if (t1 == null || t2 == null) return false;
        if (t1.getItemType() != t2.getItemType()) return false;

        int r1 = i1 / cols;
        int c1 = i1 % cols;
        int r2 = i2 / cols;
        int c2 = i2 % cols;

        // Adjacent
        if (areAdjacent(i1, i2)) return true;

        // Same row, empty in between
        if (r1 == r2) {
            int start = Math.min(c1, c2) + 1;
            int end = Math.max(c1, c2);
            for (int c = start; c < end; c++) {
                if (tiles.get(r1 * cols + c) != null) return false;
            }
            return true;
        }

        // Same column, empty in between
        if (c1 == c2) {
            int start = Math.min(r1, r2) + 1;
            int end = Math.max(r1, r2);
            for (int r = start; r < end; r++) {
                if (tiles.get(r * cols + c1) != null) return false;
            }
            return true;
        }

        return false;
    }

    private boolean isValidDragMove(int from, int to) {
        // Must move into empty space
        if (tiles.get(to) != null) return false;

        // Must be in a straight line over empty spaces
        if (!canSlideStraight (from, to)) return false;

        // Must be the same type
        return hasMatchingNeighbor (to, draggedTile);
    }

    private void removeTile(int index) {
        tiles.set(index, null);
    }

    // Helpers
    private int getTileIndexAt(int x, int y) {
        // Calculating the startX used in paintComponent
        int boardWidth = cols * (TILE_SIZE + GAP);
        int startX = PADDING_LEFT + (getWidth() - PADDING_LEFT - PADDING_RIGHT - boardWidth) / 2;
        int startY = PADDING_TOP;

        // Adjusting click coords by the offsets
        int adjustedX = x - startX;
        int adjustedY = y - startY;

        // Performing the division
        int col = adjustedX / (TILE_SIZE + GAP);
        int row = adjustedY / (TILE_SIZE + GAP);

        // Bounds checking
        if (adjustedX < 0 || adjustedY < 0 || row < 0 || row >= rows || col < 0 || col >= cols ) {
            return -1;
        }

        int index = row * cols + col;
        return (index >= 0 && index < tiles.size()) ? index : -1;
    }

    private boolean hasMatchingNeighbor(int index, ItemTile tile) {
        int row = index / cols;
        int col = index % cols;

        int[][] dirs = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}
        };

        for (int[] d : dirs) {
            int r = row + d[0];
            int c = col + d[1];

            if (r < 0 || r >= rows || c < 0 || c >= cols)
                continue;

            int neighborIndex = r * cols + c;
            ItemTile neighbor = tiles.get(neighborIndex);

            // Ignore the tile we are dragging from only during a drag
            if (draggedTile != null && neighborIndex == draggedIndex)
                continue;

            if (neighbor != null && neighbor.getItemType() == tile.getItemType()) {
                return true;
            }
        }

        return false;
    }

    private void resetDrag() {
        draggedTile = null;
        draggedIndex = -1;
    }

    private boolean noMoreMoves() {
        for (int i = 0; i< tiles.size(); i++) {
            ItemTile tile = tiles.get(i);
            if (tile == null) continue;

            int row = i / cols;
            int col = i % cols;

            int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

            for (int[] d : dirs) {
                int r = row + d[0];
                int c = col + d[1];

                if (r < 0 || r >= rows || c < 0 || c >= cols) continue;

                int neighborIndex = r * cols + c;
                ItemTile neighbor = tiles.get(neighborIndex);

                if (neighbor != null && neighbor.getItemType() == tile.getItemType()) {
                    return false; // Can remove
                }

                if (neighbor == null && hasMatchingNeighbor(neighborIndex, tile)) {
                    return false; // Can move here
                }
            }
        }
        return true;
    }

    // Render
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Calculate board size
        int boardWidth = cols * (TILE_SIZE + GAP);
        int boardHeight = rows * (TILE_SIZE + GAP);

        // Calculate top-left corner to center the board horizontally
        int startX = PADDING_LEFT + (getWidth() - PADDING_LEFT - PADDING_RIGHT - boardWidth) /2;
        int startY = PADDING_TOP; // alligned with top padding

        int index = 0;

        // Draw tiles
        for(int r = 0; r < rows; r++) {
            for(int c = 0; c < cols; c++) {
                int x = startX + c * (TILE_SIZE + GAP);
                int y = startY + r * (TILE_SIZE + GAP);

                // Draw tile background
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, TILE_SIZE, TILE_SIZE);

                // Draw tile image
                if (index < tiles.size()) {
                    ItemTile tile = tiles.get(index);
                    if (tile != null) {
                        Image img = TileImageLoader.getImage(tile.getItemType());
                        if (img != null) {
                            g.drawImage(img, x, y, TILE_SIZE, TILE_SIZE, null);
                        }

                        // Highlite selected title
                        if (tile == firstSelected) {
                            g.setColor(Color.YELLOW);
                            g.drawRect(x + 2, y + 2, TILE_SIZE - 4, TILE_SIZE - 4);
                        }
                    }
                }
                index++;
            }
        }

        // Draw score at bottom
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Score: " + score, PADDING_LEFT, getHeight() - PADDING_BOTTOM / 2);

        // Optional: No more moves message
        if (noMoreMoves()) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.drawString("No more moves!", PADDING_LEFT + 20, getHeight() - 20);
        }
    }

}
