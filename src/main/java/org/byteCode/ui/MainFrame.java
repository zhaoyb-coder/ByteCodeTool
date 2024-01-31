package org.byteCode.ui;

import javax.swing.*;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

/**
 * @author zhaoyubo
 * @title MainFrame
 * @description <TODO description class purpose>
 * @create 2024/1/16 16:03
 **/
public class MainFrame {

    public static void out() {
        try {
            BeautyEyeLNFHelper.launchBeautyEyeLNF();
            UIManager.put("RootPane.setupButtonVisible", false);
            // 实例化一个JFrame对象
            JFrame frame = new JFrame("ByteCodeTool");
            frame.setContentPane(new TabPane());
            frame.setVisible(true);
            frame.pack();// 使窗体可视
            frame.setSize(1000, 850); // 设置窗体显示位置和大小
        } catch (Exception e) {
        }
    }

}
