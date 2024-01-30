package org.byteCode.transformer;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.byteCode.config.MainConfig;

import javassist.CtClass;
import javassist.CtMethod;

/**
 * @author zhaoyubo
 * @title WatchTransformer
 * @description 监听实现
 * @create 2024/1/30 15:45
 **/
public class WatchTransformer implements ClassFileTransformer {
    final static String prefix = "\nlong startTime = System.currentTimeMillis();\n";
    final static String postfix = "\nlong endTime = System.currentTimeMillis();\n";

    public WatchTransformer() {}

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
        ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        System.out.println("start transform -----------------------------");
        try {
            CtClass ctClass = MainConfig.classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
            System.out.println("ctclass -----------------------------" + ctClass.getName());
            if (!ctClass.getName().equals("com.doe.afs.controller.house.HouseController")) {
                return ctClass.toBytecode();
            }
            String methodName = "test";
            CtMethod ctMethod = ctClass.getDeclaredMethod(methodName);// 得到这方法实例

            ctMethod.addLocalVariable("startTime", CtClass.longType);
            ctMethod.addLocalVariable("endTime", CtClass.longType);
            ctMethod.addLocalVariable("duration", CtClass.longType);
            ctMethod.insertBefore("startTime = System.currentTimeMillis();");
            ctMethod.insertAfter("endTime = System.currentTimeMillis();");
            ctMethod.insertAfter("duration = endTime - startTime;");
            ctMethod.insertAfter("System.out.println(\"执行时间\" + duration);");

            return ctClass.toBytecode();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
