package org.byteCode.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.byteCode.ClassObj;
import org.byteCode.JadMain;
import org.byteCode.config.MainConfig;
import org.smartboot.http.client.HttpClient;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * @author zhaoyubo
 * @title JadPkgTree
 * @description <TODO description class purpose>
 * @create 2024/1/24 13:39
 **/
public class JadPkgTree {

    public static JTree tree = new JTree();

    public static JTree of() throws InterruptedException {
        DefaultMutableTreeNode root=new DefaultMutableTreeNode("代码目录");
        Set<String> lv1 = new HashSet<>();
        Map<String ,List<String>> classMap = new HashMap<>();
        HttpClient httpClient = new HttpClient("127.0.0.1", 8080);
        CountDownLatch cd =new CountDownLatch(1);
        httpClient.get("/all")
                .onSuccess(response -> {
                    try {
                        ClassObj clazz = new ObjectMapper().readValue(response.body().getBytes(), ClassObj.class);
                        MainConfig.classObj = clazz;
                        for (String fullName : clazz.getClassName()) {
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
                        tree =new JTree(root);
                        tree.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                // 如果在这棵树上点击了2次,即双击
                                if (e.getSource() == tree && e.getClickCount() == 2) {
                                    // 按照鼠标点击的坐标点获取路径
                                    TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                                    if (selPath != null)// 谨防空指针异常!双击空白处是会这样
                                    {
                                        System.out.println(selPath.getParentPath().getLastPathComponent());// 输出路径看一下
                                        // 获取这个路径上的最后一个组件,也就是双击的地方
                                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
                                        System.out.println(node.toString());// 输出这个组件toString()的字符串看一下
                                        //调用反编译
                                        System.out.println("clazz.getJarPath()"+clazz.getJarPath());
                                        String result = JadMain.decompile(clazz.getJarPath(), node.toString());
                                        System.out.println(result);
                                        //把这些反编译完成的代码展示到右侧的文本组件中
                                        MainConfig.jadText.setText(result);
                                    }
                                }

                            }
                        });
                        cd.countDown();
                    } catch (Exception ex) {
                        cd.countDown();
                    }
                })
                .onFailure(Throwable::printStackTrace)
                .done();
        cd.await();
        return tree;
    }

}
