package org.byteCode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import javassist.*;
import jdk.nashorn.internal.parser.JSONParser;
import org.byteCode.config.MainConfig;
import org.smartboot.http.server.HttpBootstrap;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;
import org.smartboot.http.server.HttpServerHandler;
import org.smartboot.http.server.handler.HttpRouteHandler;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zhaoyubo
 * @title MyAgent
 * @description <TODO description class purpose>
 * @create 2024/1/17 10:19
 **/
public class MyAgent {
    public static void agentmain(String agentArgs, Instrumentation inst) {
        try {
            MainConfig.inst = inst;
            MainConfig.mainPkg = agentArgs;
            Class[] allLoadedClasses = inst.getAllLoadedClasses();
            // 启动一个服务接口，供外部进行服务调用，进行动态字节码操作
            // 创建HttpServer服务器
            HttpBootstrap bootstrap = new HttpBootstrap();
            bootstrap.configuration().debug(true);
            ObjectMapper mapper = new ObjectMapper();
            //1. 实例化路由Handle
            HttpRouteHandler routeHandle = new HttpRouteHandler();
            routeHandle.route("/all", new HttpServerHandler() {
                @Override
                public void handle(HttpRequest request, HttpResponse response) throws IOException {
                    List<String> nameList = new ArrayList<>();
                    String jarPath = "";
                    for (Class allLoadedClass : allLoadedClasses) {
                        String name = allLoadedClass.getName();
                        if(name.startsWith(agentArgs) && !name.contains("$")) {
                            nameList.add(name);
                            String var1 = "file:/";
                            String jarPath1 = allLoadedClass.getProtectionDomain().getCodeSource().getLocation().getFile();
                            jarPath =  jarPath1.replace(var1,"");
                            jarPath = jarPath.substring(0,jarPath.lastIndexOf("jar")+3);
                        }
                    }
                    ClassObj classObj = new ClassObj();
                    classObj.setJarPath(jarPath);
                    classObj.setClassName(nameList);
                    MainConfig.classObj = classObj;
                    response.write(mapper.writeValueAsBytes(classObj));
                }
            });
            // 3. 启动服务
            bootstrap.httpHandler(routeHandle);
            bootstrap.configuration().bannerEnabled(false).debug(false);
            bootstrap.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


