package org.byteCode;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.smartboot.http.client.HttpClient;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zhaoyubo
 * @title MainFrame
 * @description <TODO description class purpose>
 * @create 2024/1/16 16:03
 **/
public class MainFrame {

    public static void out() {
        try{
            BeautyEyeLNFHelper.launchBeautyEyeLNF();
            UIManager.put("RootPane.setupButtonVisible", false);
            //实例化一个JFrame对象
            JFrame frame = new JFrame("ByteCodeTool");

            JLabel statusLabel = new JLabel("JLabel",JLabel.CENTER);
            frame.add(statusLabel);
            statusLabel.setBackground(Color.WHITE);
            // 创建登录按钮
            JButton allButton = new JButton("get all");
            allButton.setBounds(10, 80, 80, 25);
            statusLabel.add(allButton);
            allButton.addActionListener((e)-> {
                HttpClient httpClient = new HttpClient("127.0.0.1", 8080);
                httpClient.get("/all")
                        .onSuccess(response -> {
                            try {
                                List aClass = new ObjectMapper().readValue(response.body().getBytes(), List.class);
                                System.out.println(aClass.size());
                                System.out.println(aClass.get(0));
                                statusLabel.setText(aClass.size()+"");
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        })
                        .onFailure(Throwable::printStackTrace)
                        .done();
            });
            frame.setVisible(true);
            frame.pack();//使窗体可视
            frame.setBounds(400,300,400, 300);  				   //设置窗体显示位置和大小
        }catch(Exception e){
        }
    }

    private static JPanel getTree(List<String> classList){
        JPanel panel=new JPanel();
        DefaultMutableTreeNode root=new DefaultMutableTreeNode("代码目录");

        Set<String> lv1 = new HashSet<>();
        Map<String ,List<String>> classMap = new HashMap<>();
        for (String fullName : classList) {
            fullName = fullName.replace(".class","");
            String pkgName = fullName.substring(0,fullName.lastIndexOf("."));
            String className = fullName.substring(fullName.lastIndexOf(".")+1,fullName.length());
            lv1.add(pkgName);
            if(classMap.containsKey(pkgName)){
                List<String> classNameList = classMap.get(pkgName);
                classNameList.add(className);
                classMap.put(pkgName,classNameList);
            }else{
                List<String> classNameList = new ArrayList<>();
                classNameList.add(className);
                classMap.put(pkgName,classNameList);
            }
        }
        for (String s : lv1) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(s);
            List<String> strings = classMap.get(s);
            for (String string : strings) {
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(string);
                node.add(childNode);
            }
            root.add(node);
        }
        JTree tree=new JTree(root);
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 如果在这棵树上点击了2次,即双击
                if (e.getSource() == tree && e.getClickCount() == 2) {
                    // 按照鼠标点击的坐标点获取路径
                    TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                    if (selPath != null)// 谨防空指针异常!双击空白处是会这样
                    {
                        System.out.println(selPath);// 输出路径看一下
                        // 获取这个路径上的最后一个组件,也就是双击的地方
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
                        System.out.println(node.toString());// 输出这个组件toString()的字符串看一下
                    }
                }

            }
        });
        panel.add(tree);
        panel.setVisible(true);
        return panel;
    }

    public static void main(String[] args) throws Exception {
        String fullName = "";
        List<String> all = new ArrayList<>();
        all.add("com.exp.add.a.class");
        all.add("com.exp.add.b.class");
        all.add("com.exp.add.c.class");
        all.add("com.exp.del.1.class");
        all.add("com.exp.upt.2.class");
        all.add("com.exp.del.3.class");
        BeautyEyeLNFHelper.launchBeautyEyeLNF();
        UIManager.put("RootPane.setupButtonVisible", false);
        //实例化一个JFrame对象
        JFrame frame = new JFrame("ByteCodeTool");
        frame.add(getTree(all));
        frame.setVisible(true);
        frame.pack();//使窗体可视
        frame.setBounds(400,300,400, 300);
    }
}
