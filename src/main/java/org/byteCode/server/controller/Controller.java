package org.byteCode.server.controller;

import java.io.IOException;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.byteCode.ClassObj;
import org.byteCode.config.MainConfig;
import org.byteCode.transformer.WatchTransformer;
import org.byteCode.util.Json;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;
import org.smartboot.http.server.HttpServerHandler;
import org.smartboot.http.server.handler.HttpRouteHandler;

/**
 * @author zhaoyubo
 * @title JadController
 * @description 反编译功能-服务端入口
 * @create 2024/1/24 12:43
 **/
public class Controller {

    public static HttpRouteHandler getRoute(Class[] allLoadedClasses, String pfeName) {
        HttpRouteHandler routeHandle = new HttpRouteHandler();
        routeHandle.route("/all", new HttpServerHandler() {
            @Override
            public void handle(HttpRequest request, HttpResponse response) throws IOException {
                List<String> nameList = new ArrayList<>();
                String jarPath = "";
                for (Class allLoadedClass : allLoadedClasses) {
                    String name = allLoadedClass.getName();
                    if (name.startsWith(pfeName) && !name.contains("$")) {
                        nameList.add(name);
                        String var1 = "file:/";
                        String jarPath1 = allLoadedClass.getProtectionDomain().getCodeSource().getLocation().getFile();
                        jarPath = jarPath1.replace(var1, "");
                        jarPath = jarPath.substring(0, jarPath.lastIndexOf("jar") + 3);
                    }
                }
                ClassObj classObj = new ClassObj();
                classObj.setJarPath(jarPath);
                classObj.setClassName(nameList);
                response.write(Json.toBytes(classObj));
            }
        }).route("/allMethod", new HttpServerHandler() {
            @Override
            public void handle(HttpRequest request, HttpResponse response) throws IOException {
                List<String> nameList = new ArrayList<>();
                String jarPath = "";
                Map<String, List<String>> methodMap = new HashMap<>();
                for (Class allLoadedClass : allLoadedClasses) {
                    String name = allLoadedClass.getName();
                    if (name.startsWith(pfeName) && !name.contains("$")) {
                        nameList.add(name);
                        String var1 = "file:/";
                        String jarPath1 = allLoadedClass.getProtectionDomain().getCodeSource().getLocation().getFile();
                        jarPath = jarPath1.replace(var1, "");
                        jarPath = jarPath.substring(0, jarPath.lastIndexOf("jar") + 3);

                        // 增加方法结果
                        String fileName = name.substring(name.lastIndexOf(".") + 1, name.length());
                        Method[] declaredMethods = allLoadedClass.getDeclaredMethods();
                        List<String> methodList = new ArrayList<>();
                        for (Method declaredMethod : declaredMethods) {
                            String methodName = declaredMethod.getName();
                            methodList.add(methodName);
                        }
                        methodMap.put(fileName, methodList);
                    }
                }
                ClassObj classObj = new ClassObj();
                classObj.setJarPath(jarPath);
                classObj.setClassName(nameList);
                classObj.setMethodList(methodMap);
                response.write(Json.toBytes(classObj));
            }
        }).route("/watch", new HttpServerHandler() {
            @Override
            public void handle(HttpRequest request, HttpResponse response)
                throws IOException, UnmodifiableClassException {
                WatchTransformer watchTransformer = new WatchTransformer();
                System.out.println("-------start watch-------------" + MainConfig.mainPkg);
                MainConfig.inst.addTransformer(watchTransformer, true);
                for (Class<?> c : allLoadedClasses) {
                    if (c.getName().startsWith("com.doe.afs") && !c.getName().contains("$")) {
                        MainConfig.inst.retransformClasses(c);
                    }
                }
                // 进入等待
                MainConfig.watchRes.setWait(true);
                while (MainConfig.watchRes.isWait()) {
                    // 等待watch的目标方法执行
                }
                response.write(Json.toBytes(MainConfig.watchRes));
            }
        });
        return routeHandle;
    }
}
