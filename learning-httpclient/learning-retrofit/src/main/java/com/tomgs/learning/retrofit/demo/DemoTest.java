package com.tomgs.learning.retrofit.demo;

import okhttp3.ResponseBody;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080/")
                .build();
        GithubService service = retrofit.create(GithubService.class);
        final Call<ResponseBody> listRepos = service.listRepos("1");
        System.out.println(listRepos);
        // 同步
        final Response<ResponseBody> execute = listRepos.execute();
        System.out.println(execute);
        if (execute.body() != null) {
            System.out.println(execute.body().string());
        }
        // 异步
        /*listRepos.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // do success something
                try {
                    System.out.println("onResponse: " + response.body().string());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // do failure something
                t.printStackTrace();
            }
        });*/
    }

}
