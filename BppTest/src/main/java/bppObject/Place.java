package bppObject;

import java.util.Objects;

public class Place {
    private Space space;
    private Block block;

    public Place(Space space, Block block) {
        this.space = space;
        this.block = block;
    }
    public Place(Place other){
        this.space = other.space;
        this.block = other.block;
    }

    public Space getSpace() {
        return space;
    }

    public Block getBlock() {
        return block;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Place)) {
            return false;
        }
        Place other = (Place) obj;
        return this.space == other.space && this.block == other.block;
    }

    @Override
    public int hashCode() {
        return Objects.hash(space, block);
    }
}
