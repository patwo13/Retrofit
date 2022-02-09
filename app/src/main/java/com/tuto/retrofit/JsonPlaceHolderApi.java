package com.tuto.retrofit;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface JsonPlaceHolderApi {

    //@Query(x) need to be the same as parameter in url
    //Retrofit automatically add ? and & in the url
    //Use Integer to make it nullable
    @GET("posts")
    /*Call<List<Post>> getPosts(
            @Query("userId") Integer userId,
            @Query("_sort") String sort,
            @Query("_order") String order);*/
    Call<List<Post>> getPosts(
            @Query("userId") Integer[] userId,
            @Query("_sort") String sort,
            @Query("_order") String order);

    @GET("posts")
    //1st string "userId", 2nd is the value
    Call<List<Post>> getPosts(@QueryMap Map<String, String> parameters);

    @GET("posts/{id}/comments")
    Call<List<Comment>> getComments(@Path("id") int postId); //{x} need to be the same as in @Path("x")

    @GET
    Call<List<Comment>> getComments(@Url String url);
}
