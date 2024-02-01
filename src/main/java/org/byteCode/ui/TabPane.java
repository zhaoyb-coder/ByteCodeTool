package org.byteCode.ui;

import java.awt.*;
import java.awt.event.KeyEvent;

import javax.swing.*;

import org.byteCode.clint.Clint;
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

        // 增加监控页签
        JPanel watchJPanel = new JPanel();
        jTabbedpane.addTab("Watch", null, watchJPanel, "监控");
        jTabbedpane.setMnemonicAt(1, KeyEvent.VK_1);
        // jadJPanel划分为两个大的区域
        JScrollPane watchPkgPanel = new JScrollPane(JadPkgTree.watch());
        watchPkgPanel.setBackground(Color.WHITE);
        watchPkgPanel.setPreferredSize(new Dimension(200, 700));

        // 监控信息展示组件
        JTextArea watchText = new JTextArea();
        MainConfig.watchText = watchText;
        JButton watchBtn = new JButton("start watch");
        watchBtn.setPreferredSize(new Dimension(700, 30));
        watchBtn.addActionListener(e -> {
            // 调用watch方法，进行字节码插桩，然后等待请求调用
            MainConfig.watchText.setText("start watch! waiting..........");
            Clint.watch();
        });

        JScrollPane watchCodePanel = new JScrollPane();
        watchCodePanel.setViewportView(watchText);
        watchCodePanel.setBackground(Color.lightGray);
        watchCodePanel.setPreferredSize(new Dimension(700, 650));

        JPanel watchAllJPanel = new JPanel();
        watchAllJPanel.setPreferredSize(new Dimension(700, 700));
        watchAllJPanel.add(watchBtn, BorderLayout.SOUTH);
        watchAllJPanel.add(watchCodePanel, BorderLayout.NORTH);

        watchJPanel.add(watchPkgPanel, BorderLayout.WEST);
        watchJPanel.add(watchAllJPanel, BorderLayout.EAST);

        jTabbedpane.setPreferredSize(new Dimension(9000, 700));
        setLayout(new GridLayout(1, 1));
        add(jTabbedpane);
    }
}
