package org.byteCode.ui;

import java.awt.*;
import java.awt.event.KeyEvent;

import javax.swing.*;

import org.byteCode.config.MainConfig;

/**
 * @author zhaoyubo
 * @title TabPane
 * @description 功能页签组件
 * @create 2024/1/24 12:54
 **/
public class TabPane extends JPanel {

    // 存放选项卡的组件
    private JTabbedPane jTabbedpane = new JTabbedPane(SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

    public TabPane() throws InterruptedException {
        layoutComponents();
    }

    private void layoutComponents() throws InterruptedException {
        // 增加反编译页签
        JPanel jadJPanel = new JPanel();
        // jadJPanel划分为两个大的区域
        JScrollPane jadPkgPanel = new JScrollPane(JadPkgTree.of());
        jadPkgPanel.setBackground(Color.WHITE);
        jadPkgPanel.setPreferredSize(new Dimension(200, 700));

        // 反编译展示组件
        JTextArea jadText = new JTextArea();
        MainConfig.jadText = jadText;
        JScrollPane jadCodePanel = new JScrollPane();
        jadCodePanel.setViewportView(MainConfig.jadText);
        jadCodePanel.setBackground(Color.lightGray);
        jadCodePanel.setPreferredSize(new Dimension(700, 700));

        jadJPanel.add(jadPkgPanel, BorderLayout.WEST);
        jadJPanel.add(jadCodePanel, BorderLayout.EAST);
        jTabbedpane.addTab("Jad", null, jadJPanel, "反编译");
        jTabbedpane.setMnemonicAt(0, KeyEvent.VK_0);
        // 增加待定页签
        JPanel varJPanel = new JPanel();
        jTabbedpane.addTab("todo", null, varJPanel, "待定");
        jTabbedpane.setMnemonicAt(1, KeyEvent.VK_1);

        jTabbedpane.setPreferredSize(new Dimension(9000, 700));
        setLayout(new GridLayout(1, 1));
        add(jTabbedpane);
    }
}
