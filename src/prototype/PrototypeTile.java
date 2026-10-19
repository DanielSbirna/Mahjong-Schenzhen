package prototype;

import model.Tile;

public class PrototypeTile extends Tile implements Prototype<PrototypeTile> {
    @Override
    public PrototypeTile clone(){
        return new PrototypeTile();
    }

    @Override
    public boolean matches(Tile other) {
        return other.getClass() == this.getClass();
    }
}