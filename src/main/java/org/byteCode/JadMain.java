package org.byteCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.benf.cfr.reader.api.CfrDriver;
import org.benf.cfr.reader.api.OutputSinkFactory;
import org.smartboot.http.common.utils.StringUtils;

/**
 * @author zhaoyubo
 * @title JadMain
 * @description 反编译核心类
 * @create 2024/1/21 16:25
 **/
public class JadMain {

    public static String decompile(String classFilePath, String methodName) {
        return decompile(classFilePath, methodName, false);
    }

    /**
     * @param classFilePath
     * @param className
     * @param hideUnicode
     * @return
     */
    public static String decompile(String classFilePath, String className, boolean hideUnicode) {
        final StringBuilder result = new StringBuilder(8192);

        OutputSinkFactory mySink = new OutputSinkFactory() {
            @Override
            public List<SinkClass> getSupportedSinks(SinkType sinkType, Collection<SinkClass> collection) {
                return Arrays.asList(SinkClass.STRING, SinkClass.DECOMPILED, SinkClass.DECOMPILED_MULTIVER,
                    SinkClass.EXCEPTION_MESSAGE);
            }

            @Override
            public <T> Sink<T> getSink(final SinkType sinkType, SinkClass sinkClass) {
                return sinkable -> {
                    if (sinkType == SinkType.PROGRESS) {
                        return;
                    }
                    result.append(sinkable);
                };
            }
        };

        HashMap<String, String> options = new HashMap<>();
        options.put("showversion", "false");
        options.put("hideutf", String.valueOf(hideUnicode));
        if (!StringUtils.isBlank(className)) {
            options.put("jarfilter", className);
        }

        CfrDriver driver = new CfrDriver.Builder().withOptions(options).withOutputSink(mySink).build();
        List<String> toAnalyse = new ArrayList<>();
        toAnalyse.add(classFilePath);
        driver.analyse(toAnalyse);
        // 去除无用信息
        result.replace(0, result.lastIndexOf("package"), "");
        return result.toString();
    }
}
