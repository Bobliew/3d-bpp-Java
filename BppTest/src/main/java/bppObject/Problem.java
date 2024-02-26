package bppObject;
import java.util.List;

public class Problem {

    private Space container;
    private List<Box> box_list;
    private List<Integer> num_list;


    public Problem(Space container, List<Box> boxList, List<Integer> numList) {
        this.container = container;
        this.box_list = boxList;
        this.num_list = numList;
    }

    public Space getContainer() {
        return container;
    }

    public void setContainer(Space container) {
        this.container = container;
    }

    public List<Box> getBoxList() {
        return box_list;
    }

    public void setBoxList(List<Box> boxList) {
        this.box_list = boxList;
    }

    public List<Integer> getNumList() {
        return num_list;
    }

    public void setNumList(List<Integer> numList) {
        this.num_list = numList;
    }
}

