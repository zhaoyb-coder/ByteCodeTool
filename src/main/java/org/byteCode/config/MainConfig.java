package org.byteCode.config;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import org.byteCode.ClassObj;
import org.byteCode.message.WatchMsg;

import javassist.ClassPool;

/**
 * @author zhaoyubo
 * @title MainConfig
 * @description 全局配置类，记录运行时信息
 * @create 2024/1/24 14:19
 **/
public class MainConfig {

    /**
     * 全局Instrumentation
     */
    public volatile static Instrumentation inst = null;

    /**
     * Jad 传递的class数据
     */
    public volatile static ClassObj classObj = null;

    /**
     * 全局 ClassPool
     */
    public static ClassPool classPool = ClassPool.getDefault();

    /**
     * Jad 需要编译的包前缀（业务包）
     */
    public volatile static String mainPkg = "";

    /**
     * Jad 反编译展示UI组件
     */
    public volatile static JTextArea jadText = null;

    /**
     * Watch 展示UI组件
     */
    public volatile static JTextArea watchText = null;

    /**
     * Http端口
     */
    public static final int HTTP_PORT = 10086;

    public static volatile WatchMsg watchRes = new WatchMsg();

    public static volatile Map<String, List<String>> currentWatchMap = new HashMap<>();

    public static void addWatch(String className, String method) {
        if (currentWatchMap.containsKey(className)) {
            List<String> methodList = currentWatchMap.get(className);
            methodList.add(method);
        } else {
            List<String> methodList = new ArrayList<>();
            methodList.add(method);
            currentWatchMap.put(className, methodList);
        }
    }
}
