package org.byteCode.message;

import java.io.Serializable;

/**
 * @author zhaoyubo
 * @title WatchMsg
 * @description Watch 返回结果对象
 * @create 2024/1/31 11:20
 **/
public class WatchMsg implements Serializable {

    private static final long serialVersionUID = -5540205557206208721L;

    /** 是否可以返回 */
    private transient boolean wait = true;
    /** 类名 */
    private String className;
    /** 方法名 */
    private String methodName;
    /** 方法执行时间 */
    private String execTime;
    /** 方法入参 */
    private Object request;
    /** 方法返回值 */
    private Object response;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getExecTime() {
        return execTime;
    }

    public void setExecTime(long execTime) {
        this.execTime = execTime + "ms";
    }

    public Object getRequest() {
        return request;
    }

    public void setRequest(Object request) {
        this.request = request;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public boolean isWait() {
        return wait;
    }

    public void setWait(boolean wait) {
        this.wait = wait;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
