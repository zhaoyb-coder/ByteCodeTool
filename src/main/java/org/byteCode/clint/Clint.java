package org.byteCode.clint;

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

    public static WatchMsg result = new WatchMsg();

    public static void watch() {
        System.out.println("clint----watch---`start`");
        HttpClient httpClient = new HttpClient("127.0.0.1", MainConfig.HTTP_PORT);
        CountDownLatch cd = new CountDownLatch(1);
        MainConfig.watchText.setText("等待中....");
        httpClient.get("/watch").onSuccess(response -> {
            try {
                System.out.println("clint----watch---start");
                System.out.println(response.body());
                result = Json.readValue(response.body(), WatchMsg.class);
                System.out.println("clint----watch---end");
                cd.countDown();
            } catch (Exception ex) {
                ex.printStackTrace();
                cd.countDown();
            }
        }).onFailure(Throwable::printStackTrace).done();
        try {
            cd.await();
            System.out.println("clint----success--" + Json.toJson(result));
            MainConfig.watchText.setText(Json.toJson(result));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
