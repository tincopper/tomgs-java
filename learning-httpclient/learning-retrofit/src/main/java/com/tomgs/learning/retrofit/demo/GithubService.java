package com.tomgs.learning.retrofit.demo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * GithubService
 *
 * @author tomgs
 * @since 1.0
 */
public interface GithubService {

    @GET("users/{user}")
    Call<ResponseBody> listRepos(@Path("user") String user);

}
