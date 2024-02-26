package bppObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class PackingState {
    // 已生成的装箱方案列表
    private List<Place> planList = new ArrayList<Place>();
    // 剩余空间堆栈
    private Stack<Space> spaceStack = new Stack<>();
    // 剩余可用箱体数量
    private List<Integer> availList;
    // 已装载物品总体积
    private int volume = 0;
    // 最终装载物品的总体积的评估值
    private int volumeComplete = 0;
    private static AtomicReference<PackingState> atomicPackingState = new AtomicReference<>(new PackingState());

    // test for 原子变量

    public PackingState(List<Place> planList, Stack<Space> spaceStack, List<Integer> availList) {
        this.planList = planList;
        this.spaceStack = spaceStack;
        this.availList = availList;
        this.volume = 0;
        this.volumeComplete = 0;
    }

    public PackingState(List<Integer> availList){
        this.availList = availList;
    }

    public PackingState(PackingState other) {
        this.planList = other.planList;
        this.spaceStack = other.spaceStack;
        this.availList = other.availList;
        this.volume = other.volume;
        this.volumeComplete = other.volumeComplete;
    }

    public PackingState() {
        this.planList = new ArrayList<>();
        this.spaceStack = new Stack<>();
        this.availList = new ArrayList<>();
        this.volume = 0;
        this.volumeComplete = 0;
    }
    // getter 和 setter 方法
    public List<Place> getPlanList() {
        return this.planList;
    }
    public void setPlanList(List<Place> value) {
        this.planList = value;
    }

    public Stack<Space> getSpaceStack() {
        return spaceStack;
    }

    public void setSpaceStack(Stack<Space> spaceStack) {
        this.spaceStack = spaceStack;
    }

    public List<Integer> getAvailList() {
        return availList;
    }

    public void setAvailList(List<Integer> availList) {
        this.availList = availList;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getVolumeComplete() {
        return volumeComplete;
    }

    public void setVolumeComplete(int volumeComplete) {
        this.volumeComplete = volumeComplete;
    }
    public static PackingState getAtomicPackingState() {
        return atomicPackingState.get();
    }

    // 设置原子变量的值
    public static void setAtomicPackingState(PackingState newValue) {
        atomicPackingState.set(newValue);
    }


}