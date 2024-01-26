package org.byteCode.config;

import java.lang.instrument.Instrumentation;

import javax.swing.*;

import org.byteCode.ClassObj;

/**
 * @author zhaoyubo
 * @title MainConfig
 * @description 全局配置类，记录运行时信息
 * @create 2024/1/24 14:19
 **/
public class MainConfig {

    public volatile static Instrumentation inst = null;

    public volatile static ClassObj classObj = null;

    public volatile static String mainPkg = "";

    public volatile static JTextArea jadText = null;

    public static final int HTTP_PORT = 10086;
}
