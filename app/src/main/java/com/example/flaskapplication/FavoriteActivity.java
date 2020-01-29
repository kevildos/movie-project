package com.example.flaskapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FavoriteActivity extends AppCompatActivity {

    private OkHttpClient client;
    private Button sendButton;
    private RecyclerView recyclerView2;
    private MovieAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private int clickedPosition;
    private MovieAdapter.MovieHolder clickedHolder;
    public static String devUrl = "http://10.0.2.2:5000/";
    public static String hostUrl = "https://movie-api-stage.herokuapp.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        //Init recycler view
        recyclerView2 = (RecyclerView) findViewById(R.id.recyclerView2);
        recyclerView2.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView2.setLayoutManager(layoutManager);
        // Specify adapter
        mAdapter = new MovieAdapter(getLayoutInflater(), new MyOnLongClickListener());
        recyclerView2.setAdapter(mAdapter);

        // Initialize client and requests
        client = new OkHttpClient();

        generateMovies();
    }

    private void deleteMovie(String name, final int itemPosition) {
        Request request = new Request.Builder().url(hostUrl + "movie/" + name)
                .addHeader("Content-Type", "application/json")
                .delete().build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                System.out.println("Successfully removed movie from favorites");
                System.out.println(response);
                mAdapter.removeItem(itemPosition);
            }
        });
    }

    private void generateMovies() {
        Request request = new Request.Builder().url(hostUrl + "movie").build();
            makeRequest(request);
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
                                JSONArray jsonArray = new JSONArray(jsonString);
                                System.out.println("jsonArray created");
                                System.out.println(jsonArray);
                                for (int index = 0; index < jsonArray.length(); index++) {
                                    mAdapter.addItem(new JSONObject(jsonArray.get(index).toString()));
                                    //recyclerView2.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    throw new IOException("Unexpected Code: " + response);
                }

            }
        });
    }

    class MyOnLongClickListener implements View.OnLongClickListener{

        @Override
        public boolean onLongClick(View view) {
            int itemPosition = recyclerView2.getChildLayoutPosition(view);
            clickedHolder = (MovieAdapter.MovieHolder) recyclerView2.findContainingViewHolder(view);
            String name = clickedHolder.movieName.getText().toString();
            deleteMovie(name, itemPosition);
            Toast.makeText(FavoriteActivity.this, "Removed " + name
                    + " from Favorites", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

}
