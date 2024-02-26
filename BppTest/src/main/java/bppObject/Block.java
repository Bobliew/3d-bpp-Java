package bppObject;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Block {
    private int lx;
    private int ly;
    private int lz;
    private List<Integer> requireList;

    private int volume;
    private List<Block> children;
    private String direction;
    private int ax;
    private int ay;
    private int times;
    private int fitness;

    public Block(int lx, int ly, int lz, List<Integer> requireList, List<Block> children, String direction) {
        this.lx = lx;
        this.ly = ly;
        this.lz = lz;
        this.requireList = requireList;
        this.volume = 0;
        this.children = children;
        this.direction = direction;
        this.ax = 0;
        this.ay = 0;
        this.times = 0;
        this.fitness = 0;
    }
    public Block(int lx, int ly, int lz, List<Integer> requireList) {
        this(lx, ly, lz, requireList, new ArrayList<>(), "");
    }
    public Block(int lx, int ly, int lz) {
        this(lx, ly, lz, new ArrayList<>(), new ArrayList<>(), "");
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

    public void setLx(int lx) {
        this.lx = lx;
    }

    public void setLy(int ly) {
        this.ly = ly;
    }

    public void setLz(int lz) {
        this.lz = lz;
    }

    public void setRequireList(List<Integer> requireList) {
        this.requireList = requireList;
    }

    public List<Integer> getRequireList() {
        return requireList;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public List<Block> getChildren() {
        return children;
    }

    public void setChildren(List<Block> children) {
        this.children = children;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getAx() {
        return ax;
    }

    public void setAx(int ax) {
        this.ax = ax;
    }

    public int getAy() {
        return ay;
    }

    public void setAy(int ay) {
        this.ay = ay;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }


    @Override
    public String toString() {
        return String.format("lx: %s, ly: %s, lz: %s, volume: %s, ax: %s, ay: %s, times:%s, fitness: %s, require: %s, children: %s, direction: %s",
                lx, ly, lz, volume, ax, ay, times, fitness, requireList, children, direction);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Block)) return false;
        Block block = (Block) o;
        return lx == block.lx && ly == block.ly && lz == block.lz && ax == block.ax && ay == block.ay && Objects.equals(requireList, block.requireList);
    }

    @Override
    public int hashCode() {
        Object[] args = {lx, ly, lz, ax, ay, requireList};
        return Arrays.hashCode(args);
    }
}
