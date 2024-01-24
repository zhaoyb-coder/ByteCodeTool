package org.byteCode.config;

import org.byteCode.ClassObj;

import java.lang.instrument.Instrumentation;

/**
 * @author zhaoyubo
 * @title MainConfig
 * @description 全局配置类，记录运行时信息
 * @create 2024/1/24 14:19
 **/
public class MainConfig {

    public volatile static Instrumentation inst = null;

    public volatile static ClassObj classObj = null;

    public volatile static String mainPkg =  "";
}
