package org.byteCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhaoyubo
 * @title ClassObj
 * @description class信息封装
 * @create 2024/1/22 17:31
 **/
public class ClassObj implements Serializable {

    private static final long serialVersionUID = -5540205557206208721L;
    private String jarPath;

    private List<String> className;

    public String getJarPath() {
        return jarPath;
    }

    public void setJarPath(String jarPath) {
        this.jarPath = jarPath;
    }

    public List<String> getClassName() {
        return className;
    }

    public void setClassName(List<String> className) {
        this.className = className;
    }
}
