package org.byteCode.message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author zhaoyubo
 * @title WatchMethod
 * @description 监控的方法列表
 * @create 2024/2/1 10:10
 **/
public class WatchMethod implements Serializable {

    private static final long serialVersionUID = -5540205557206208721L;

    public Map<String, Set<String>> currentWatchMap = new HashMap<>();

    public void addWatch(String className, String method) {
        if (currentWatchMap.containsKey(className)) {
            Set<String> methodList = currentWatchMap.get(className);
            methodList.add(method);
        } else {
            Set<String> methodList = new HashSet<>();
            methodList.add(method);
            currentWatchMap.put(className, methodList);
        }
    }

    public void remove() {
        currentWatchMap = new HashMap<>();
    }
}
