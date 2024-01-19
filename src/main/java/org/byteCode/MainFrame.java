package org.byteCode;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.smartboot.http.client.HttpClient;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
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

    private JPanel getTree(List<String> classList){
        JPanel panel=new JPanel();
        DefaultMutableTreeNode root=new DefaultMutableTreeNode("代码目录");
        DefaultMutableTreeNode node=null;
        DefaultMutableTreeNode childNode=null;
        Set<String> lv1 = new HashSet<>();
        Map<String ,String> classMap = new HashMap<>();
        for (String fullName : classList) {
            fullName = fullName.replace(".class","");
            String pkgName = fullName.substring(0,fullName.lastIndexOf("."));
            String className = fullName.substring(fullName.lastIndexOf(".")+1,fullName.length());
            lv1.add(pkgName);
            classMap.put(pkgName,className);
        }
        JTree tree=new JTree(root);
        panel.add(tree);
        panel.setVisible(true);
        return panel;
    }

    public static void main(String[] args) {
        String fullName = "com.exp.add.a.class";
        fullName = fullName.replace(".class","");
        String pkgName = fullName.substring(0,fullName.lastIndexOf("."));
        String className = fullName.substring(fullName.lastIndexOf(".")+1,fullName.length());
        System.out.println(pkgName);
        System.out.println(className);
    }
}
