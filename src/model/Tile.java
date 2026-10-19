package model;

public abstract class Tile {
    private boolean removed;

    // get
    public boolean isRemoved() {
        return removed;
    }

    public void remove(){
        this.removed = true;
    }

    // matching rule
    public abstract boolean matches(Tile other);

    @Override
    public Tile clone() {
        try {
            return (Tile) super.clone();
        } catch (CloneNotSupportedException e){
            throw new AssertionError(e);
        }
    }
}
