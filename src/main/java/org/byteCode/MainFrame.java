package org.byteCode;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.smartboot.http.client.HttpClient;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

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
                                String aClass = new ObjectMapper().readValue(response.body().getBytes(), String.class);
                                statusLabel.setText(aClass);
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
}
