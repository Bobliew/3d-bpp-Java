package main.runMain;

import bppObject.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static Algorithms.Algorithms.basicHeuristic;

public class runMain {
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
        String filePath = "D:\\bppJa\\BppTest\\quickStart\\test.json";
        String jsonStr = readFile(filePath);
        PackingState result = runMain(jsonStr);
        long endTime = System.currentTimeMillis();
        double runTime = (endTime - startTime) / 1000.0;

        System.out.println("程序运行时间为：" + runTime + "秒");
    }
    public static PackingState runMain(String request) {
        JSONObject jsonRequest = new JSONObject(request);

        JSONArray skuInfoArray = jsonRequest.getJSONArray("skuInfo");
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
        for (int i = 0; i < skuInfoArray.length(); i++) {
            JSONObject skuInfo = skuInfoArray.getJSONObject(i);
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
        List<Place> newPlanList = ps.getPlanList();

        return ps;
    }
}
