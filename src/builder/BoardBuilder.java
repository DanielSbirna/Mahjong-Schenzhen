package builder;

import model.Board;

public interface BoardBuilder {
    void buildTiles();
    Board getBoard();
}
