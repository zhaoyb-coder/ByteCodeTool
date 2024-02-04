package org.byteCode.clint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.byteCode.config.MainConfig;
import org.byteCode.message.WatchMsg;
import org.byteCode.util.Json;
import org.smartboot.http.client.HttpClient;

/**
 * @author zhaoyubo
 * @title Clint
 * @description 发送请求客户端
 * @create 2024/1/31 16:27
 **/
public class Clint {

    public static volatile List<WatchMsg> result = new ArrayList<>();

    public static void watch() {
        HttpClient httpClient = new HttpClient("127.0.0.1", MainConfig.HTTP_PORT);
        CountDownLatch cd = new CountDownLatch(1);
        Map<String, String> params = new HashMap<>();
        params.put("method", Json.toJson(MainConfig.watchMethod));
        httpClient.post("/watch").body().formUrlencoded(params).onSuccess(response -> {
            try {
                result = Arrays.asList(Json.readValue(response.body(), WatchMsg[].class));
                cd.countDown();
            } catch (Exception ex) {
                ex.printStackTrace();
                cd.countDown();
            }
        }).onFailure(Throwable::printStackTrace).done();
        try {
            cd.await();
            MainConfig.watchText.setText("");
            MainConfig.watchText.setText(Json.format(result));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
