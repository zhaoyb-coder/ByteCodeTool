package org.byteCode;

import java.lang.instrument.Instrumentation;

import org.byteCode.config.MainConfig;
import org.byteCode.server.controller.Controller;
import org.smartboot.http.server.HttpBootstrap;

import javassist.LoaderClassPath;

/**
 * @author zhaoyubo
 * @title MyAgent
 * @description <TODO description class purpose>
 * @create 2024/1/17 10:19
 **/
public class MyAgent {
    public static void agentmain(String agentArgs, Instrumentation inst) {
        try {
            // 保存全局配置
            MainConfig.inst = inst;
            MainConfig.mainPkg = agentArgs;
            // 初始化ClassPool#ClassesLoaded
            // initClassesLoaded(inst.getAllLoadedClasses());
            // 开启http服务
            startHttp(agentArgs, inst.getAllLoadedClasses());
            // 增加Watch
            // inst.addTransformer(new WatchTransformer());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @description 开启Http服务
     * @param agentArgs
     * @param allLoadedClasses
     * @return void
     * @author zhaoyubo
     * @time 2024/1/26 10:01
     **/
    public static void startHttp(String agentArgs, Class[] allLoadedClasses) {
        // 启动一个服务接口，供外部进行服务调用，进行动态字节码操作
        // 创建HttpServer服务器
        HttpBootstrap bootstrap = new HttpBootstrap();
        bootstrap.configuration().debug(true);
        // 1. 实例化路由Handle
        bootstrap.httpHandler(Controller.getRoute(allLoadedClasses, agentArgs));
        // 2. 启动服务
        bootstrap.configuration().bannerEnabled(false).debug(false);
        bootstrap.setPort(MainConfig.HTTP_PORT);
        bootstrap.start();
    }

    public static void initClassesLoaded(Class[] allLoadedClasses) {
        for (Class cla : allLoadedClasses) {
            ClassLoader classLoader = cla.getClassLoader();
            if (null != classLoader) {
                MainConfig.classPool.appendClassPath(new LoaderClassPath(classLoader));
            }
        }
    }
}
