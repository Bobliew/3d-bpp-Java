package Algorithms;
import java.util.List;
import java.util.ArrayList;
import bppObject.*;
import static Utils.Global.*;
import java.util.Collections;
import java.util.Comparator;
import java.math.*;
import java.util.stream.Collectors;
import java.util.stream.*;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class Algorithms {

    public static boolean combineCommonCheck(Block combine, Space container, List<Integer> numList) {
        // 合共块尺寸不得大于容器尺寸
        if (combine.getLx() > container.getLx()) {
            return false;
        }
        if (combine.getLy() > container.getLy()) {
            return false;
        }
        if (combine.getLz() > container.getLz()) {
            return false;
        }
        // 合共块需要的箱子数量不得大于箱子总的数量
        for (int i = 0; i < combine.getRequireList().size(); i++) {
            if (combine.getRequireList().get(i) > numList.get(i)) {
                return false;
            }
        }
        // 合并块的填充体积不得小于最小填充率
        if (combine.getVolume() / (combine.getLx() * combine.getLy() * combine.getLz()) < MIN_FILL_RATE) {
            return false;
        }
        // 合并块的顶部可放置矩形必须足够大
        if ((combine.getAx() * combine.getAy()) / (combine.getLx() * combine.getLy()) < MIN_AREA_RATE) {
            return false;
        }
        // 合并块的复杂度不得超过最大复杂度
        if (combine.getTimes() > MAX_TIMES) {
            return false;
        }
        return true;
    }

    public static void combineCommon(Block a, Block b, Block combine) {
        // 合并块的需求箱子数量
        List<Integer> requireList = new ArrayList<>();
        for (int i = 0; i < a.getRequireList().size(); i++) {
            requireList.add(a.getRequireList().get(i) + b.getRequireList().get(i));
        }
        combine.setRequireList(requireList);

        // 合并填充体积
        combine.setVolume(a.getVolume() + b.getVolume());

        // 构建父子关系
        List<Block> children = new ArrayList<>();
        children.add(a);
        children.add(b);
        combine.setChildren(children);

        // 合并后的复杂度
        combine.setTimes(Math.max(a.getTimes(), b.getTimes()) + 1);
    }
    public static List<Block> gen_simple_block(Space container, List<Box> boxList, List<Integer> numList) {
        List<Block> blockTable = new ArrayList<>();

        for (Box box : boxList) {
            int iOne = numList.get(box.getType()); //recipe 1:避免每次查询数组
            for (int nx = 1; nx <= iOne; nx++) {
                int iTwo = iOne / nx; // same as recipe 1
                //iTwo = Math.min(iTwo, numList.size());
                for (int ny = 1; ny <= iTwo; ny++) {
                    int iThree = iTwo / ny;
                    for (int nz = 1; nz <= iThree; nz++) {
                        if (box.getLx() * nx <= container.getLx() && box.getLy() * ny <= container.getLy() &&
                                box.getLz() * nz <= container.getLz()) {
                            // 该简单块需要的立体箱子数量
                            List<Integer> requires = new ArrayList<>(Collections.nCopies(numList.size(), 0));
                            requires.set(box.getType(), nx * ny * nz);
                            // 简单块
                            List<Block> children;
                            Block block = new Block(box.getLx() * nx, box.getLy() * ny, box.getLz() * nz, requires );
                            // 顶部可放置矩形
                            block.setAx(box.getLx() * nx);
                            block.setAy(box.getLy() * ny);
                            // 简单块填充体积
                            block.setVolume(box.getLx() * nx * box.getLy() * ny * box.getLz() * nz);
                            // 简单块复杂度
                            block.setTimes(0);
                            blockTable.add(block);
                        }
                    }
                }
            }
        }
        Collections.sort(blockTable, (x, y) -> Double.compare(y.getVolume(), x.getVolume()));
        return blockTable;
    }

    public static List<Block> gen_complex_block(Space container, List<Box> boxList, List<Integer> numList) {
        // 先生成简单块
        List<Block> blockTable = gen_simple_block(container, boxList, numList);
        for (int times = 0; times < MAX_TIMES; times++) {
            List<Block> newBlockTable = new ArrayList<>();
            // 循环所有简单块，两两配对
            for (int i = 0; i < blockTable.size(); i++) {
                // 第一个简单块
                Block a = blockTable.get(i);
                for (int j = 0; j < blockTable.size(); j++) {
                    // 简单块不跟自己复合
                    if (j == i) {
                        continue;
                    }
                    // 第二个简单块
                    Block b = blockTable.get(j);
                    // 复杂度满足当前复杂度
                    if (a.getTimes() == times || b.getTimes() == times) {
                        Block c = new Block(0, 0, 0);
                        // 按x轴方向复合
                        if (a.getAx() == a.getLx() && b.getAx() == b.getLx() && a.getLz() == b.getLz()) {
                            c.setDirection("x");
                            c.setAx(a.getAx() + b.getAx());
                            c.setAy(Math.min(a.getAy(), b.getAy()));
                            c.setLx(a.getLx() + b.getLx());
                            c.setLy(Math.max(a.getLy(), b.getLy()));
                            c.setLz(a.getLz());
                            combineCommon(a, b, c);
                            if (combineCommonCheck(c, container, numList)) {
                                newBlockTable.add(c);
                                continue;
                            }
                        }
                        // 按y轴方向复合
                        if (a.getAy() == a.getLy() && b.getAy() == b.getLy() && a.getLz() == b.getLz()) {
                            c.setDirection("y");
                            c.setAx(Math.min(a.getAx(), b.getAx()));
                            c.setAy(a.getAy() + b.getAy());
                            c.setLx(Math.max(a.getLx(), b.getLx()));
                            c.setLy(a.getLy() + b.getLy());
                            c.setLz(a.getLz());
                            combineCommon(a, b, c);
                            if (combineCommonCheck(c, container, numList)) {
                                newBlockTable.add(c);
                                continue;
                            }
                        }
                        // 按z轴方向复合
                        if (a.getAx() >= b.getLx() && a.getAy() >= b.getLy()) {
                            c.setDirection("z");
                            c.setAx(b.getAx());
                            c.setAy(b.getAy());
                            c.setLx(a.getLx());
                            c.setLy(a.getLy());
                            c.setLz(a.getLz() + b.getLz());
                            combineCommon(a, b, c);
                            if (combineCommonCheck(c, container, numList)) {
                                newBlockTable.add(c);
                                continue;
                            }
                        }
                    }
                }
            }
            // 加入新生成的复合块
            blockTable.addAll(newBlockTable);
            // 去重，拥有相同三边长度、物品需求和顶部可放置矩形的复合块被视为等价块，重复生成的等价块将被忽略
            blockTable = blockTable.stream().distinct().collect(Collectors.toList());
        }
        // 按填充体积对复合块进行排序
        Collections.sort(blockTable, new Comparator<Block>() {
            @Override
            public int compare(Block a, Block b) {
                return Integer.compare(b.getVolume(), a.getVolume());
            }
        });
        return blockTable;
    }
    public static List<Block> gen_block_list(Space space, List<Integer> avail, List<Block> blockTable) {
        List<Block> blockList = new ArrayList<>();
        for (Block block : blockTable) {
            boolean allAvailable = true;
            for (int i = 0; i < block.getRequireList().size(); i++) {
                if (avail.isEmpty()) { break;}
                if (block.getRequireList().get(i) > avail.get(i)) {
                    allAvailable = false;
                    break;
                }
            }
            if (allAvailable && block.getLx() <= space.getLx() && block.getLy() <= space.getLy() && block.getLz() <= space.getLz()) {
                blockList.add(block);
            }
        }
        return blockList;
    }
    public static List<Space> gen_residual_space(Space space, Block block) {
        List<Space> residualSpaces = new ArrayList<>();
        // 三个维度的剩余尺寸
        int rmx = space.getLx() - block.getLx();
        int rmy = space.getLy() - block.getLy();
        int rmz = space.getLz() - block.getLz();
        // 三个新裁切出的剩余空间（按入栈顺序依次返回）
        if (rmx >= rmy) {
            // 可转移空间归属于 x 轴切割空间
            Space drs_x = new Space(space.getX() + block.getLx(), space.getY(), space.getZ(), rmx, space.getLy(), space.getLz(), space);
            Space drs_y = new Space( space.getX(), space.getY() + block.getLy(), space.getZ(), block.getLx(), rmy, space.getLz(), space);
            Space drs_z = new Space( space.getX(), space.getY(), space.getZ() + block.getLz(), block.getAx(), block.getAy(), rmz, null);
            residualSpaces.add(drs_z);
            residualSpaces.add(drs_y);
            residualSpaces.add(drs_x);
        } else {
            // 可转移空间归属于 y 轴切割空间
            Space drs_x = new Space(space.getX() + block.getLx(), space.getY(), space.getZ(), rmx, block.getLy(), space.getLz(), space);
            Space drs_y = new Space(space.getX(), space.getY() + block.getLy(), space.getZ(), space.getLx(), rmy, space.getLz(), space);
            Space drs_z = new Space(space.getX(), space.getY(), space.getZ() + block.getLz(), block.getAx(), block.getAy(), rmz, null);
            residualSpaces.add(drs_z);
            residualSpaces.add(drs_x);
            residualSpaces.add(drs_y);
        }
        return residualSpaces;
    }

    public static Space transfer_space(Space space, Stack<Space> spaceStack) {
        // 仅剩一个空间的话，直接弹出
        if (spaceStack.size() <= 1) {
            spaceStack.pop();
            return null;
        }
        // 待转移空间的原始空间
        Space discard = space;
        // 目标空间
        spaceStack.pop();
        Space target = spaceStack.top();
        // 将可转移的空间转移给目标空间
        if (discard.getOrigin() != null && target.getOrigin() != null && discard.getOrigin() == target.getOrigin()) {
            Space newTarget = new Space(target);
            // 可转移空间原先归属于 y 轴切割空间的情况
            if (discard.getLx() == discard.getOrigin().getLx()) {
                newTarget.setLy(discard.getOrigin().getLy());
            }
            // 可转移空间原来归属于 x 轴切割空间的情况
            else if (discard.getLy() == discard.getOrigin().getLy()) {
                newTarget.setLx(discard.getOrigin().getLx());
            } else {
                return null;
            }
            spaceStack.pop();
            spaceStack.push(newTarget);
            // 返回未发生转移之前的目标空间
            return target;
        }
        return null;
    }
    public static void transfer_space_back(Space space, Stack<Space> spaceStack, Space revertSpace) {
        spaceStack.pop();
        spaceStack.push(revertSpace);
        spaceStack.push(space);
    }
    public static Place place_block(PackingState ps, Block block) {
        // 栈顶剩余空间
        // System.out.println("1");
        Space space = ps.getSpaceStack().pop();
        // 更新可用箱体数目
        List<Integer> newAvailList = new ArrayList<>(Collections.nCopies(ps.getAvailList().size(), 0));
        //System.out.println(newAvailList.size());
        for (int i = 0; i < newAvailList.size(); i++) {
            newAvailList.set(i, ps.getAvailList().get(i) - block.getRequireList().get(i));
        }
        //System.out.println("3.5" + newAvailList);
        ps.setAvailList(newAvailList);
        // 更新放置计划
        Place place = new Place(space, block);
        //ps.getPlanList().add(place);
        // 更新体积利用率
        ps.setVolume(ps.getVolume() + block.getVolume());
        // 压入新的剩余空间
        List<Space> residualSpaces = gen_residual_space(space, block);
        for (Space residualSpace : residualSpaces) {
            ps.getSpaceStack().push(residualSpace);
        }
        // 返回临时生成的放置
        return place;
    }
    public static void remove_block(PackingState ps, Block block, Place place, Space space) {
        // 还原可用箱体数目
        //System.out.println("3.1 " + ps.getAvailList());
        //List<Place> newPlaceList = new ArrayList<Place>();
        //newPlaceList = ps.getPlanList();
        //System.out.println("2");
        List<Integer> newAvailList = new ArrayList<>(Collections.nCopies(ps.getAvailList().size(), 0));
        for (int i = 0; i < newAvailList.size(); i++) {
            newAvailList.set(i, ps.getAvailList().get(i) + block.getRequireList().get(i));
        }
        ps.setAvailList(newAvailList);
        // 还原排样计划

        List<Place> newPlanList = ps.getPlanList();
        for (int i = 0; i < newPlanList.size(); ++i) {
            Place p1 = newPlanList.get(i);
            if (p1.getSpace() == place.getSpace() && p1.getBlock() == place.getBlock()) {
                // 找到一个重复的元素，从 newPlanListatomicPackingState 中删除它
                newPlanList.remove(i);
                // 注意这里需要将 i 减一，因为元素已经被删除了
                i--;
            }
        }
        // 还原体积利用率
        ps.setVolume(ps.getVolume() - block.getVolume());
        // 移除在此之前裁切出的新空间
        for (int i = 0; i < 3; i++) {
            if (ps.getSpaceStack().empty()) {break;}
            ps.getSpaceStack().pop();
        }
        // 还原之前的空间
        ps.getSpaceStack().push(space);
    }

    public static void complete(PackingState ps, List<Block> blockTable) {
        // 不对当前的放置状态进行修改
        PackingState tmp = new PackingState(ps);
        while (!tmp.getSpaceStack().empty()) {
            // 栈顶空间
            Space space = tmp.getSpaceStack().top();
            // 可用块列表
            List<Block> blockList = gen_block_list(space, ps.getAvailList(), blockTable);
            if (blockList.size() > 0) {
                // 放置块
                //Random random = new Random();
                //System.out.println("222");
                place_block(tmp, blockList.get(0));
            } else {
                // 空间转移
                transfer_space(space, tmp.getSpaceStack());
            }
        }
        // 补全后的使用体积
        ps.setVolumeComplete(tmp.getVolume());
        //ps.setPlanList(tmp.getPlanList());
    }

    public static void depth_first_search(PackingState ps, int depth, int branch, List<Block> blockTable) {
        // 全局变量 tmp_best_ps
        //System.out.println("3.3" + ps.getAvailList());
        if (depth != 0) {
            //System.out.println("111");
            Space space = ps.getSpaceStack().top();
            List<Block> blockList = gen_block_list(space, ps.getAvailList(), blockTable);
            if (blockList.size() > 0) {
                // 遍历所有分支
                // 这里是可以优化的
                for (int i = 0; i < Math.min(branch, blockList.size()); i++) {
                    // 放置块
                    Place place = place_block(ps, blockList.get(i));
                    // dfs
                    //System.out.println("3");
                    depth_first_search(ps, depth - 1, branch, blockTable);
                    //parallel_dfs(ps, depth - 1, branch, blockTable);
                    // 移除刚才添加的块
                    remove_block(ps, blockList.get(i), place, space);
                }
            } else {
                // 转移空间
                Space oldTarget = transfer_space(space, ps.getSpaceStack());

                // 如果转移空间存在（非空）
                if (oldTarget != null) {
                    // 放置下一个块
                    depth_first_search(ps, depth, branch, blockTable);
                    //parallel_dfs(ps, depth, branch, blockTable);
                    // 还原转移空间
                    transfer_space_back(space, ps.getSpaceStack(), oldTarget);
                }
            }
        } else {
            // 补全该方案
            //System.out.println("4");
            complete(ps, blockTable);
            // 更新最优解
            if (ps.getVolumeComplete() > tmp_best_ps.getVolumeComplete()) {
                //System.out.println(tmp_best_ps.getVolumeComplete());

                tmp_best_ps = new PackingState(ps);
                //System.out.println(ps.getPlanList().size());
                //ps.setPlanList(new ArrayList<>());
            }

        }
    }



    public static int estimate(PackingState ps, List<Block> blockTable) {
        // 全局变量 tmp_best_ps
        // 空的放置方案
        tmp_best_ps = new PackingState(new ArrayList<Place>(), new Stack<Space>(), new ArrayList<Integer>());
        // 开始深度优先搜索
        //System.out.println("3.4 "+ ps.getAvailList());
        depth_first_search(ps, MAX_DEPTH, MAX_BRANCH, blockTable);
        //parallel_dfs(ps, MAX_DEPTH, MAX_BRANCH, blockTable);
        return tmp_best_ps.getVolumeComplete();
    }

    public static Block find_next_block(PackingState ps, List<Block> blockList, List<Block> blockTable) {
        // 最优适应度
        int bestFitness = 0;
        // 初始化最优块为第一个块（填充体积最大的块）
        Block bestBlock = blockList.get(0);
        // 遍历所有可行块
        for (Block block : blockList) {
            // 栈顶空间
            Space space = ps.getSpaceStack().top();
            // 放置块
            Place place = place_block(ps, block);
            // 评价值
            int fitness = estimate(ps, blockTable);
            // 移除刚才添加的块
            //System.out.println("5");
            remove_block(ps, block, place, space);
            // 更新最优解
            if (fitness > bestFitness) {
                bestFitness = fitness;
                bestBlock = block;
            }
        }
        return bestBlock;
    }


    public static List<double[]> build_box_position(Block block, double[] initPos, List<Box> boxList) {
        List<double[]> pos = new ArrayList<>();
        // 遇到简单块时进行坐标计算,如果没有孩子说明是简单块；
        if (block.getChildren().size() <= 0 && block.getTimes() == 0) {
            // 箱体类型索引
            int boxIdx = IntStream.range(0, block.getRequireList().size()).filter(i -> block.getRequireList().get(i) > 0).findFirst().orElse(-1);
            if (boxIdx > -1) {
                // 所需箱体
                Box box = boxList.get(boxIdx);
                // 箱体的相对坐标
                double nx = block.getLx() / box.getLx();
                double ny = block.getLy() / box.getLy();
                double nz = block.getLz() / box.getLz();
                List<Double> xList = DoubleStream.iterate(0, i -> i + box.getLx()).limit((long) nx).boxed().collect(Collectors.toList());
                List<Double> yList = DoubleStream.iterate(0, i -> i + box.getLy()).limit((long) ny).boxed().collect(Collectors.toList());
                List<Double> zList = DoubleStream.iterate(0, i -> i + box.getLz()).limit((long) nz).boxed().collect(Collectors.toList());
                // 箱体的绝对坐标
                List<double[]> dimensions = cartesianProduct(xList, yList, zList).stream().map(d -> new double[] {d[0] + initPos[0], d[1] + initPos[1], d[2] + initPos[2], box.getLx(), box.getLy(), box.getLz()}).collect(Collectors.toList());
                Collections.sort(dimensions, (x1, x2) -> {
                    if (x1[0] != x2[0]) {
                        return Double.compare(x1[0], x2[0]);
                    } else if (x1[1] != x2[1]) {
                        return Double.compare(x1[1], x2[1]);
                    } else {
                        return Double.compare(x1[2], x2[2]);
                    }
                });
                pos.addAll(dimensions);
            }
            return pos;
        }

        for (Block child : block.getChildren()) {
            pos.addAll(build_box_position(child, new double[] {initPos[0], initPos[1], initPos[2]}, boxList));
            // 根据子块的复合方向，确定下一个子块的左后下角坐标
            if (block.getDirection().equals("x")) {
                initPos[0] += child.getLx();
            } else if (block.getDirection().equals("y")) {
                initPos[1] += child.getLy();
            } else if (block.getDirection().equals("z")) {
                initPos[2] += child.getLz();
            }
        }
        return pos;
    }


    // 计算笛卡尔积
    public static List<double[]> cartesianProduct(List<Double>... lists) {
        List<double[]> result = new ArrayList<>();
        if (lists == null || lists.length == 0) {
            result.add(new double[0]);
            return result;
        }
        int[] pointers = new int[lists.length];
        while (true) {
            double[] current = new double[lists.length];
            boolean end = true;
            for (int i = 0; i < lists.length; i++) {
                List<Double> list = lists[i];
                int pointer = pointers[i];
                if (pointer >= list.size()) {
                    pointers[i] = 0;
                    pointer = 0;
                }
                double value = list.get(pointer);
                current[i] = value;
                if (i < lists.length - 1 && pointers[i + 1] != 0) {
                    end = false;
                }
            }
            result.add(current);
            if (end) {
                break;
            }
            pointers[lists.length - 1]++;
        }
        return result;
    }

    public static PackingState basicHeuristic(boolean isComplex, Problem problem) {
        List<Box> boxList = problem.getBoxList();
        List<Integer> numList = problem.getNumList();
        Space container = problem.getContainer();
        List<Block> blockTable;
        if (isComplex) {
            // 生成复合块
            blockTable = gen_complex_block(container, boxList, numList);
            //System.out.println("2 "+blockTable);
        } else {
            // 生成简单块
            blockTable = gen_simple_block(container, boxList, numList);
        }
        // 初始化排样状态
        PackingState ps = new PackingState(numList);
        // 开始时，剩余空间堆栈中只有容器本身
        ps.getSpaceStack().push(container);
        // 所有剩余空间均转满，则停止
        while (ps.getSpaceStack().size() > 0) {
            Space space = ps.getSpaceStack().top();

            List<Block> blockList = gen_block_list(space, ps.getAvailList(), blockTable);
            if (!blockList.isEmpty()) {
                // 查找下一个近似最优块

                Block block = find_next_block(ps, blockList, blockTable);
                //System.out.println(ps.getPlanList().size());
                // 弹出顶部剩余空间
                //System.out.println("3.0" + ps.getAvailList());
                ps.getSpaceStack().pop();
                //System.out.println("3 "+ ps.getAvailList());
                // 更新可用物品数量
                List<Integer> requireList = block.getRequireList();
                System.out.println("requireList"+requireList);
                // 从可用列表中减去加入到ps的块所需的sku数量；
                for (int i = 0; i < requireList.size(); i++) {
                    //if (availList.isEmpty()) { break;}
                    ps.getAvailList().set(i, ps.getAvailList().get(i) - requireList.get(i));
                }
                // 更新排样计划
                ps.getPlanList().add(new Place(space, block));
                // 更新已利用体积
                ps.setVolume(ps.getVolume() + block.getVolume());
                // 压入新裁切的剩余空间
                List<Space> residualSpaceList = gen_residual_space(space, block);
                for (Space residualSpace : residualSpaceList) {
                    ps.getSpaceStack().push(residualSpace);
                }
            } else {
                // 转移剩余空间
                transfer_space(space, ps.getSpaceStack());
            }
        }
        // 打印剩余箱体和已使用容器的体积
        System.out.println("ps.avail_list");
        System.out.println(ps.getAvailList());
        System.out.println(ps.getVolume());
        return ps;
    }
}
