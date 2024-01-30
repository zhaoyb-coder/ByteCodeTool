package org.byteCode.server.controller;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

import org.byteCode.config.MainConfig;
import org.byteCode.transformer.WatchTransformer;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;
import org.smartboot.http.server.HttpServerHandler;
import org.smartboot.http.server.handler.HttpRouteHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author zhaoyubo
 * @title WatchController
 * @description <TODO description class purpose>
 * @create 2024/1/30 18:50
 **/
public class WatchController {

    public static ObjectMapper mapper = new ObjectMapper();

    public static HttpRouteHandler setWatchMethod(Class[] allLoadedClasses, Instrumentation inst) {
        System.out.println("inst1===" + inst);
        HttpRouteHandler routeHandle = new HttpRouteHandler();
        routeHandle.route("/watch", new HttpServerHandler() {
            @Override
            public void handle(HttpRequest request, HttpResponse response)
                throws IOException, UnmodifiableClassException {
                WatchTransformer watchTransformer = new WatchTransformer();
                MainConfig.inst.addTransformer(watchTransformer, true);
                System.out.println("inst2===" + MainConfig.inst);
                for (Class<?> c : allLoadedClasses) {
                    if (c.getName().startsWith("com.doe.afs") && !c.getName().contains("$")) {
                        System.out.println(c.getName() + "-------------");
                        MainConfig.inst.retransformClasses(c);
                    }
                }
            }
        });
        return routeHandle;
    }
}
