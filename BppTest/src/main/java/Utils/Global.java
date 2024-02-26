package Utils;
import bppObject.PackingState;

public class Global {
    // 复合块的最小填充率
    public static final double MIN_FILL_RATE = 0.9;
    // 可行放置矩形与相应复合块顶部面积比的最小值
    public static final double MIN_AREA_RATE = 0.9;
    // 复合块最大复杂度
    public static final int MAX_TIMES = 3;
    // 搜索树深度
    public static final int MAX_DEPTH = 3;
    // 搜索树节点分支数
    public static final int MAX_BRANCH = 2;
    // 临时的最优放置方案
    public static PackingState tmp_best_ps = null;

}