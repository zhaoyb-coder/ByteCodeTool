package org.byteCode.transformer;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Map;
import java.util.Set;

import org.byteCode.config.MainConfig;
import org.byteCode.message.WatchMethod;

import javassist.CtClass;
import javassist.CtMethod;

/**
 * @author zhaoyubo
 * @title WatchTransformer
 * @description 监听实现
 * @create 2024/1/30 15:45
 **/
public class WatchTransformer implements ClassFileTransformer {

    public static volatile WatchMethod watchMethod = new WatchMethod();

    public WatchTransformer() {}

    public WatchTransformer(WatchMethod watchMethod) {
        this.watchMethod = watchMethod;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
        ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        try {
            CtClass ctClass = MainConfig.classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
            Map<String, Set<String>> currentWatchMap = watchMethod.currentWatchMap;
            if (!currentWatchMap.containsKey(ctClass.getName())) {
                ctClass.detach();
                ctClass.defrost();
                return ctClass.toBytecode();
            }
            Set<String> methodList = currentWatchMap.get(ctClass.getName());
            for (String method : methodList) {
                CtMethod ctMethod = ctClass.getDeclaredMethod(method);// 得到这方法实例
                ctMethod.addLocalVariable("startTime", CtClass.longType);
                ctMethod.addLocalVariable("endTime", CtClass.longType);
                ctMethod.addLocalVariable("execTime", CtClass.longType);
                ctMethod.addLocalVariable("execTime", CtClass.longType);
                ctMethod.addLocalVariable("request", MainConfig.classPool.get(String.class.getName()));
                ctMethod.addLocalVariable("response", MainConfig.classPool.get(String.class.getName()));
                ctMethod.insertBefore("startTime = System.currentTimeMillis();");
                ctMethod.insertAfter("endTime = System.currentTimeMillis();");
                ctMethod.insertAfter("execTime = endTime - startTime;");
                ctMethod.insertAfter("org.byteCode.config.MainConfig.watchRes.setMethodName(\"" + method + "\");");
                ctMethod.insertAfter("org.byteCode.config.MainConfig.watchRes.setClassName(\"" + className + "\");");
                ctMethod.insertAfter("org.byteCode.config.MainConfig.watchRes.setExecTime(execTime+\"ms\");");
                ctMethod.insertAfter("request = org.byteCode.util.Json.toJson($args);");
                ctMethod.insertAfter("response = org.byteCode.util.Json.toJson($_);");
                ctMethod.insertAfter("org.byteCode.config.MainConfig.watchRes.setRequest(request);");
                ctMethod.insertAfter("org.byteCode.config.MainConfig.watchRes.setResponse(response);");
                ctMethod.insertAfter("org.byteCode.config.MainConfig.watchRes.setWait(false);");
            }
            ctClass.detach();
            ctClass.defrost();
            return ctClass.toBytecode();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
