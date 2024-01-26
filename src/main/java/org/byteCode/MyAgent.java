package org.byteCode;

import java.lang.instrument.Instrumentation;

import org.byteCode.config.MainConfig;
import org.byteCode.server.controller.JadController;
import org.smartboot.http.server.HttpBootstrap;

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
            // 开启http服务
            startHttp(agentArgs, inst.getAllLoadedClasses());
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
        bootstrap.httpHandler(JadController.getAllPackage(allLoadedClasses, agentArgs));
        // 2. 启动服务
        bootstrap.configuration().bannerEnabled(false).debug(false);
        bootstrap.start();
    }
}
