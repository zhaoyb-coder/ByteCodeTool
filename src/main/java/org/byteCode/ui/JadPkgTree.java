package org.byteCode.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.byteCode.ClassObj;
import org.byteCode.JadMain;
import org.byteCode.config.MainConfig;
import org.smartboot.http.client.HttpClient;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author zhaoyubo
 * @title JadPkgTree
 * @description <TODO description class purpose>
 * @create 2024/1/24 13:39
 **/
public class JadPkgTree {

    public static JTree tree = new JTree();

    public static JTree of() throws InterruptedException {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("代码目录");
        HttpClient httpClient = new HttpClient("127.0.0.1", 8080);
        CountDownLatch cd = new CountDownLatch(1);
        httpClient.get("/all").onSuccess(response -> {
            try {
                ClassObj clazz = new ObjectMapper().readValue(response.body().getBytes(), ClassObj.class);
                MainConfig.classObj = clazz;
                DefaultMutableTreeNode jTreeRoot = buildTree(clazz.getClassName());
                tree = new JTree(jTreeRoot);
                tree.expandRow(1);
                tree.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        // 如果在这棵树上点击了2次,即双击
                        if (e.getSource() == tree && e.getClickCount() == 2) {
                            // 按照鼠标点击的坐标点获取路径
                            TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                            if (selPath != null)// 谨防空指针异常!双击空白处是会这样
                            {
                                // 获取这个路径上的最后一个组件,也就是双击的地方
                                DefaultMutableTreeNode node = (DefaultMutableTreeNode)selPath.getLastPathComponent();
                                // 调用反编译
                                String result = JadMain.decompile(clazz.getJarPath(), node.toString());
                                // 把这些反编译完成的代码展示到右侧的文本组件中
                                MainConfig.jadText.setText(result);
                            }
                        }

                    }
                });
                cd.countDown();
            } catch (Exception ex) {
                cd.countDown();
            }
        }).onFailure(Throwable::printStackTrace).done();
        // 等待调用完成再返回结果
        cd.await();
        return tree;
    }

    public static DefaultMutableTreeNode buildTree(List<String> classNames) {
        Node root = new Node("代码目录", 0);
        for (String className : classNames) {
            String[] parts = className.split("\\.");

            Node currentNode = root;
            for (String part : parts) {
                Node childNode = findChildNode(currentNode, part);

                if (childNode == null) {
                    int nodeType = (part.equals(parts[parts.length - 1])) ? 2 : 1;
                    childNode = new Node(part, nodeType);
                    currentNode.addChildNode(childNode);
                }

                currentNode = childNode;
            }
        }

        optimizeTree(root);
        return convertToJTree(root);
    }

    public static DefaultMutableTreeNode convertToJTree(Node node) {
        DefaultMutableTreeNode jTreeNode = new DefaultMutableTreeNode(node.nodeName);
        for (Node childNode : node.childNodes) {
            jTreeNode.add(convertToJTree(childNode));
        }
        return jTreeNode;
    }

    public static Node findChildNode(Node node, String nodeName) {
        for (Node childNode : node.childNodes) {
            if (childNode.nodeName.equals(nodeName)) {
                return childNode;
            }
        }
        return null;
    }

    public static void optimizeTree(Node node) {
        if (node.nodeType == 1 && node.pkgNum == 1 && node.fileNum == 0) {
            Node childNode = node.childNodes.get(0);
            optimizeTree(childNode);
            node.childNodes = childNode.childNodes;
            node.pkgNum = childNode.pkgNum;
            node.nodeName = node.nodeName + "." + childNode.nodeName;
            node.nodeType = childNode.nodeType;
        }

        for (Node childNode : node.childNodes) {
            optimizeTree(childNode);
        }
    }

    static class Node {
        String nodeName;
        List<Node> childNodes;
        public int nodeType; // 1 for package, 2 for file
        Integer totalNum = 0;
        Integer pkgNum = 0;
        Integer fileNum = 0;

        public Node(String nodeName, int nodeType) {
            this.nodeName = nodeName;
            this.nodeType = nodeType;
            this.childNodes = new ArrayList<>();
            this.fileNum = 0;
        }

        public void addChildNode(Node childNode) {
            this.childNodes.add(childNode);
            if (childNode.nodeType == 1) {
                this.pkgNum += childNode.pkgNum + 1; // 加上当前节点自身
            }
            if (childNode.nodeType == 2) {
                this.fileNum += childNode.fileNum + 1; // 加上当前节点自身
            }
            this.totalNum += childNode.totalNum + 1; // 加上当前节点自身
        }
    }

}
