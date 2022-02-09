package com.tuto.retrofit;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView textViewResult;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.text_view_result);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        /*
        * baseUrl - baseurl for the request
        * addConverterFactory - to define that what we want to use to pass the response
        * */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        //getPosts();
        //getComments();
        //createPost();
        updatePost();
        //deletePost();
    }

    private void getPosts(){

        Map<String, String> parameters = new HashMap<>();
        parameters.put("userId", "1");
        parameters.put("_sort", "id");
        parameters.put("_order", "desc");

        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(parameters);
        //Call<List<Post>> call = jsonPlaceHolderApi.getPosts(new Integer[]{2,3,5}, "id", "desc ");
        //Call<List<Post>> call = jsonPlaceHolderApi.getPosts(4, "id", "desc ");
        //Call<List<Post>> call = jsonPlaceHolderApi.getPosts(4, null, null); //can put null if dont want to use

        //the request need to be made on background, if not the page will be freeeze until response
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if(!response.isSuccessful()){ //status 200/300
                    textViewResult.setText("Code : "+response.code());
                    return;
                }

                List<Post> posts = response.body();

                for(Post post : posts){
                    String content ="";
                    content += "ID: "+post.getId() + "\n";
                    content += "User ID: "+post.getUserId() + "\n";
                    content += "Title: "+post.getTitle() + "\n";
                    content += "Text: "+post.getText() + "\n\n";

                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textViewResult.setText(t.getMessage()); //return error message
            }
        });
    }

    private void getComments(){

        //Call<List<Comment>> call = jsonPlaceHolderApi.getComments(3);
        Call<List<Comment>> call = jsonPlaceHolderApi.getComments("posts/3/comments");


        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if(!response.isSuccessful()){ //status 200/300
                    textViewResult.setText("Code : "+response.code());
                    return;
                }

                List<Comment> comments = response.body();
                for(Comment comment : comments){
                    String content ="";
                    content += "ID: "+comment.getId() + "\n";
                    content += "Post ID: "+comment.getPostId() + "\n";
                    content += "Name: "+comment.getName() + "\n";
                    content += "Email: "+comment.getEmail() + "\n";
                    content += "Text: "+comment.getText() + "\n\n";

                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    private void createPost(){
        Post post = new Post(23,"new Title", "New Text");

        Map<String, String> parameters = new HashMap<>();
        parameters.put("userId", "25");
        parameters.put("title", "New Title");


        //Call<Post> call = jsonPlaceHolderApi.createPosts(post);
        //Call<Post> call = jsonPlaceHolderApi.createPosts(23, "new Title", "New Text");
        Call<Post> call = jsonPlaceHolderApi.createPosts(parameters);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(!response.isSuccessful()){ //status 200/300
                    textViewResult.setText("Code : "+response.code());
                    return;
                }

                Post postResponse = response.body();

                String content ="";
                content += "Code : " +response.code() + "\n"; //get status code as jsonplaceholder will just fake post request
                content += "ID: "+postResponse.getId() + "\n";
                content += "User ID: "+postResponse.getUserId() + "\n";
                content += "Title: "+postResponse.getTitle() + "\n";
                content += "Text: "+postResponse.getText() + "\n\n";

                textViewResult.append(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    private void updatePost(){
        Post post = new Post(12, null, "new text");

        Call<Post> call = jsonPlaceHolderApi.putPost(5, post);
        //Call<Post> call = jsonPlaceHolderApi.patchPost(5, post);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(!response.isSuccessful()){ //status 200/300
                    textViewResult.setText("Code : "+response.code());
                    return;
                }

                Post postResponse = response.body();

                String content ="";
                content += "Code : " +response.code() + "\n"; //get status code as jsonplaceholder will just fake post request
                content += "ID: "+postResponse.getId() + "\n";
                content += "User ID: "+postResponse.getUserId() + "\n";
                content += "Title: "+postResponse.getTitle() + "\n";
                content += "Text: "+postResponse.getText() + "\n\n";

                textViewResult.append(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    private void deletePost(){
        Call<Void> call = jsonPlaceHolderApi.deletePost(5);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                textViewResult.setText("Code : "+response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
}
