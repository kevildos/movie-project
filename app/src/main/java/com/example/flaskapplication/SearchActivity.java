package com.example.flaskapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {

    private OkHttpClient client;
    private Button sendButton;
    private RecyclerView recyclerView;
    private MovieAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private MovieAdapter.MovieHolder clickedHolder;
    public static String devUrl2 = "http://10.0.2.2:5000/";
    public static String hostUrl = "https://movie-api-stage.herokuapp.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Init recycler view
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // Specify adapter
        mAdapter = new MovieAdapter(getLayoutInflater(), new MyOnLongClickListener());
        recyclerView.setAdapter(mAdapter);

        // Initialize urls
        String flaskUrl = "https://movie-api-stage.herokuapp.com/";

        // Initialize client and requests
        client = new OkHttpClient();

        sendButton = findViewById(R.id.buttonSend);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateMovies();

            }
        });

    }

    private void postMovie(String name, String date, String desc) {
        JSONObject json = new JSONObject();
        try {
            json.put("original_title", name);
            json.put("release_date", date);
            json.put("overview", desc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody reqBody = RequestBody.create(json.toString(), JSON);
        Request request = new Request.Builder().url(hostUrl + "movie")
                .addHeader("Content-Type", "application/json")
                .post(reqBody).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                System.out.println("Successfully added movie to favorites");
                System.out.println(response);
            }
        });
    }


    private void generateMovies() {
        List<Request> requests = new ArrayList<>();
        for ( int i = 0; i < 3; i++) {
            requests.add( new Request.Builder().url(getMovieURL(-1)).build());
        }

        for ( int i = 0; i < 3; i++) {
            makeRequest(requests.get(i));
        }
    }


    private String getMovieURL(int position) {
        if (position == -1) position = new Random().nextInt(667872);
        System.out.printf("This is the position %d \n", position);
        return "https://api.themoviedb.org/3/movie/"+ position + "?api_key=61aebd228d4d977c1309e254c1315de5";
    }

    private void makeRequest(final Request request) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    System.out.println("Success!");
                    final String jsonString = response.body().string();
                    System.out.println(jsonString);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mAdapter.addItem(new JSONObject(jsonString));
                                recyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    //throw new IOException("Unexpected Code: " + response);
                    System.err.println("Unexpected Code: " + response);
                    makeRequest(new Request.Builder().url(getMovieURL(-1)).build());
                }

            }
        });
    }


    class MyOnLongClickListener implements View.OnLongClickListener{

        @Override
        public boolean onLongClick(View view) {
            clickedHolder = (MovieAdapter.MovieHolder) recyclerView.findContainingViewHolder(view);
            String name = clickedHolder.movieName.getText().toString();
            String date = clickedHolder.movieDate.getText().toString();
            String desc = clickedHolder.movieDesc.getText().toString();
            postMovie(name, date, desc);
            Toast.makeText(SearchActivity.this, "Added " + name
                    + " to Favorites", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
}
