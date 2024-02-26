// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import  bppObject.*;
import java.util.List;
import java.util.ArrayList;
import static Algorithms.Algorithms.basicHeuristic;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static String readFile(String filePath) {
        try {
            // 创建 File 对象
            File file = new File(filePath);
            // 创建 Scanner 对象
            Scanner scanner = new Scanner(file);
            // 读取文件内容并返回
            StringBuilder sb = new StringBuilder();
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine());
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        /*
        Space container = new Space(0, 0, 0, 10, 10, 10);
        List<Box> boxList = new ArrayList<>();
        boxList.add(new Box(1, 2, 3, 0));
        boxList.add(new Box(4, 5, 5, 1));
        boxList.add(new Box(1, 1, 1, 2));
        boxList.add(new Box(2, 2, 2, 3));
        boxList.add(new Box(4, 5, 2, 4));
        List<Integer> numList = new ArrayList<>();
        numList.add(11);
        numList.add(4);
        numList.add(5);
        numList.add(8);
        numList.add(6);

        // 问题
        Problem problem = new Problem(container, boxList, numList);
        // 具体计算
        // public static void basicHeuristic(boolean isComplex, Problem problem)
        basicHeuristic(true, problem);
        */
        // 读取本地的 test.json 文件
        String filePath = "D:\\bppJa\\BppTest\\quickStart\\test.json";
        String jsonStr = readFile(filePath);

        // 将读取到的 JSON 字符串传递给 runMain 函数，并打印返回结果
        PackingState result = runMain(jsonStr);


        long endTime = System.currentTimeMillis();
        double runTime = (endTime - startTime) / 1000.0;

        System.out.println("程序运行时间为：" + runTime + "秒");
    }

    // RunMain只是单个任务
    public static PackingState runMain(String request) {
        JSONObject jsonRequest = new JSONObject(request);

        JSONArray orderInfoArray = jsonRequest.getJSONArray("skuInfo");
        JSONObject containerInfo = jsonRequest.getJSONObject("containerInfo");
        int length = containerInfo.getInt("Length");
        int width = containerInfo.getInt("Width");
        int height = containerInfo.getInt("Height");
        Space container = new Space(0, 0, 0, length, width, height);
        // 创建商品列表和数量列表
        List<Box> boxList = new ArrayList<>();
        List<Integer> numList = new ArrayList<>();
        int type = 0;
        //System.out.println(orderInfoArray);
        for (int i = 0; i < orderInfoArray.length(); i++) {
            JSONObject skuInfo = orderInfoArray.getJSONObject(i);
            //JSONArray skuInfoArray = orderInfo.getJSONArray("skuInfo");
            int skuLength = skuInfo.getInt("Length");
            int skuWidth = skuInfo.getInt("Width");
            int skuHeight = skuInfo.getInt("Height");
            boxList.add(new Box(skuLength, skuWidth, skuHeight, type));
            numList.add(skuInfo.getInt("Amount"));
            type++;
        }

        Problem problem = new Problem(container, boxList, numList);

        PackingState ps = basicHeuristic(true, problem);
        return ps;
    }
}
