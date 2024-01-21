package org.byteCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class main {

    public static void main(String[] args) {
        List<String> classNames = new ArrayList<>();
        classNames.add("com.exp.controller.bill.a.class");
        classNames.add("com.exp.controller.bill.b.class");
        classNames.add("com.exp.controller.expense.c.class");
        classNames.add("com.exp.service.bill.as.class");

        Map<String, Object> packageTree = buildPackageTree(classNames);

        // 打印目录树
        System.out.println(packageTree);
    }

    private static Map<String, Object> buildPackageTree(List<String> classNames) {
        Map<String, Object> packageTree = new HashMap<>();

        for (String className : classNames) {
            addClassToTree(packageTree, className);
        }

        return packageTree;
    }

    private static void addClassToTree(Map<String, Object> packageTree, String className) {
        String[] packages = className.split("\\.");
        Map<String, Object> currentLevel = packageTree;

        for (String packageName : packages) {
            currentLevel = (Map<String, Object>) currentLevel.computeIfAbsent(packageName, k -> new HashMap<>());
        }

        // 获取最后一个包名，作为文件名
        String fileName = packages[packages.length - 1];
        // 移除 ".class" 后缀
        //fileName = fileName.substring(0, fileName.lastIndexOf(".class"));

        // 在叶子节点添加文件名
        currentLevel.computeIfAbsent(fileName, k -> new ArrayList<>());

        // 如果是列表，添加文件名
        if (currentLevel.get(fileName) instanceof List) {
            ((List<String>) currentLevel.get(fileName)).add(fileName + ".class");
        }
    }
}
