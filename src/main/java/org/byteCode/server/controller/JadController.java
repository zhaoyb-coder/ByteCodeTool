package org.byteCode.server.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.byteCode.ClassObj;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;
import org.smartboot.http.server.HttpServerHandler;
import org.smartboot.http.server.handler.HttpRouteHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author zhaoyubo
 * @title JadController
 * @description 反编译功能-服务端入口
 * @create 2024/1/24 12:43
 **/
public class JadController {

    public static ObjectMapper mapper = new ObjectMapper();

    public static HttpRouteHandler getAllPackage(Class[] allLoadedClasses, String pfeName) {
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
                response.write(mapper.writeValueAsBytes(classObj));
            }
        });
        return routeHandle;
    }

}
