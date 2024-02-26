package bppObject;

public class Space {
    // 坐标
    private int x;
    private int y;
    private int z;
    // 长
    private int lx;
    // 宽
    private int ly;
    // 高
    private int lz;
    // 表示从哪个剩余空间切割而来
    private Space origin;

    public Space(int x, int y, int z, int lx, int ly, int lz, Space origin) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.lx = lx;
        this.ly = ly;
        this.lz = lz;
        this.origin = origin;
    }
    public Space(int x, int y, int z, int lx, int ly, int lz) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.lx = lx;
        this.ly = ly;
        this.lz = lz;

    }
    public Space(Space other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
        this.lx = other.lx;
        this.ly = other.ly;
        this.lz = other.lz;
        this.origin = other.origin != null ? new Space(other.origin) : null;
    }

    @Override
    public String toString() {
        return "x:" + x + ",y:" + y + ",z:" + z + ",lx:" + lx + ",ly:" + ly + ",lz:" + lz;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Space)) {
            return false;
        }
        Space other = (Space) obj;
        return this.x == other.x && this.y == other.y && this.z == other.z && this.lx == other.lx && this.ly == other.ly
                && this.lz == other.lz;
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Space getOrigin() {
        return origin;
    }

    public void setLx(int lx) {
        this.lx = lx;
    }

    public void setLy(int ly) {
        this.ly = ly;
    }

    public void setLz(int lz) {
        this.lz = lz;
    }
}
