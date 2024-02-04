package org.byteCode.config;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.swing.*;

import org.byteCode.ClassObj;
import org.byteCode.message.WatchMethod;
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

    public static volatile List<WatchMsg> watchRes = new ArrayList<>();

    public static void setWatchMsg(String method, String className, String execTime, String request, String response) {
        WatchMsg watch = new WatchMsg();
        watch.setMethodName(method);
        watch.setClassName(className);
        watch.setRequest(request);
        watch.setResponse(response);
        watch.setExecTime(execTime);
        watchRes.add(watch);
    }

    public static volatile WatchMethod watchMethod = new WatchMethod();

    public static volatile CountDownLatch cd = new CountDownLatch(1);

}
