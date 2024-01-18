package org.byteCode;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * @author zhaoyubo
 * @title AttachAgent
 * @description <TODO description class purpose>
 * @create 2024/1/17 10:20
 **/
public class AttachAgent {
    public static void main(String[] args) {
        // 获取当前运行的 Java 进程的 PID
        String pid = "";
        Scanner scanner = new Scanner(System.in);
        List<VirtualMachineDescriptor> jps = VirtualMachine.list();
        jps.sort(Comparator.comparing(VirtualMachineDescriptor::displayName));
        int i = 0;
        for (; i < jps.size(); i++) {
            System.out.printf("[%s] %s %s%n", i, jps.get(i).id(), jps.get(i).displayName());
        }
        System.out.printf("[%s] %s%n", i, "Custom PID");
        System.out.println(">>>>>>>>>>>>Please enter the serial number");
        while (true) {
            int index = scanner.nextInt();
            if (index < 0 || index > i) continue;
            if (index == i) {
                System.out.println(">>>>>>>>>>>>Please enter the PID");
                pid = String.valueOf(scanner.nextInt());
                break;
            }
            pid = jps.get(index).id();
            break;
        }
        try {
            // 附加代理到 Java 进程
            VirtualMachine vm = VirtualMachine.attach(pid);
            URL jarUrl = MyAgent.class.getProtectionDomain().getCodeSource().getLocation();
            String curJarPath = Paths.get(jarUrl.toURI()).toString();
            vm.loadAgent(curJarPath);
            System.out.println("*** Attach finish ***");
            MainFrame.out();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

