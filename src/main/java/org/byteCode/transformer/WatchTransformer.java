package org.byteCode.transformer;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.byteCode.config.MainConfig;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

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
            String outputStr =
                "\nSystem.out.println(\"this method " + methodName + " cost:\" +(endTime - startTime) +\"ms.\");";
            System.out.println("outputStr -----------------------------");
            CtMethod ctmethod = ctClass.getDeclaredMethod(methodName);// 得到这方法实例
            String newMethodName = methodName + "$old";// 新定义一个方法叫做比如sayHello$old
            ctmethod.setName(newMethodName);// 将原来的方法名字修改

            // 创建新的方法，复制原来的方法，名字为原来的名字
            CtMethod newMethod = CtNewMethod.copy(ctmethod, methodName, ctClass, null);

            // 构建新的方法体
            StringBuilder bodyStr = new StringBuilder();
            bodyStr.append("{");
            bodyStr.append(prefix);
            bodyStr.append(newMethodName + "($$);\n");// 调用原有代码，类似于method();($$)表示所有的参数
            bodyStr.append(postfix);
            bodyStr.append(outputStr);
            bodyStr.append("}");

            newMethod.setBody(bodyStr.toString());// 替换新方法
            ctClass.addMethod(newMethod);// 增加新方法
            return ctClass.toBytecode();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
