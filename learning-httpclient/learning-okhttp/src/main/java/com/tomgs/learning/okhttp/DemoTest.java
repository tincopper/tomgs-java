package com.tomgs.learning.okhttp;

import okhttp3.*;
import org.junit.Test;

import java.io.IOException;

/**
 * DemoTest
 *
 * @author tomgs
 * @since 1.0
 */
public class DemoTest extends BaseMockServerTest {

    @Test
    public void testGet() throws IOException {
        String url = "http://wwww.baidu.com";
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        /*call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("onResponse: " + response.body().string());
            }
        });*/

        final Response response = call.execute();
        System.out.println(response.body().string());
    }

}
