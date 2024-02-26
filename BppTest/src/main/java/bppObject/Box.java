package bppObject;

public class Box {
    private int lx;
    private int ly;
    private int lz;
    private int type;

    public Box(int lx, int ly, int lz, int type) {
        this.lx = lx;
        this.ly = ly;
        this.lz = lz;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public int getLx() {
        return lx;
    }

    public int getLy() {
        return ly;
    }

    public int getLz() {
        return lz;
    }

    @Override
    public String toString() {
        return String.format("lx: %d, ly: %d, lz: %d, type: %d", lx, ly, lz, type);
    }
}



