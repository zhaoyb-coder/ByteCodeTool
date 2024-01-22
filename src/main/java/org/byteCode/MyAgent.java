package org.byteCode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import javassist.*;
import jdk.nashorn.internal.parser.JSONParser;
import org.smartboot.http.server.HttpBootstrap;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;
import org.smartboot.http.server.HttpServerHandler;
import org.smartboot.http.server.handler.HttpRouteHandler;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaoyubo
 * @title MyAgent
 * @description <TODO description class purpose>
 * @create 2024/1/17 10:19
 **/
public class MyAgent {
    public static void agentmain(String agentArgs, Instrumentation inst) {
        try {
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
                        String pfeName = "com.doe.afs";
                        if(name.startsWith(pfeName) && !name.contains("$")) {
                            nameList.add(name);
                            String var1 = "file:/";
                            String jarPath1 = allLoadedClass.getProtectionDomain().getCodeSource().getLocation().getFile();
                            System.out.println(jarPath1);
                            jarPath =  jarPath1.replace(var1,"");
                            jarPath = jarPath.substring(0,jarPath.lastIndexOf("jar"));
                            System.out.println(jarPath);
                        }
                    }
                    ClassObj classObj = new ClassObj();
                    classObj.setJarPath(jarPath);
                    classObj.setClassName(nameList);
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

    public static void main(String[] args) {
        String jarPath = "a....jar";
        jarPath = jarPath.substring(0,jarPath.lastIndexOf("jar"));
        System.out.println(jarPath);
    }
}


