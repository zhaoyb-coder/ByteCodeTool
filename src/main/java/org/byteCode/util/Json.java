package org.byteCode.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author zhaoyubo
 * @title Json
 * @description 处理JSON工具类
 * @create 2024/1/31 13:13
 **/
public class Json {
    public static ObjectMapper mapper = new ObjectMapper();

    public static byte[] toBytes(Object obj) {
        try {
            return mapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T readValue(Object source, Class<T> target) {
        try {
            return mapper.readValue(source.toString().getBytes(), target);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
