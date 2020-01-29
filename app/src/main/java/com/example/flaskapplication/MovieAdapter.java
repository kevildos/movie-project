package com.example.flaskapplication;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter {

    private LayoutInflater inflater;
    private List<JSONObject> jsonObjects = new ArrayList<>();
    private View.OnLongClickListener mOnLongClickListener;

    public MovieAdapter(LayoutInflater inflater, View.OnLongClickListener myOnLongClickListener) {
        this.inflater = inflater;
        this.mOnLongClickListener = myOnLongClickListener;
    }

    public static class MovieHolder extends RecyclerView.ViewHolder {

        TextView movieName;
        TextView movieDate;
        TextView movieDesc;

        public MovieHolder(@NonNull View itemView) {
            super(itemView);

            movieName = itemView.findViewById(R.id.movieName);
            movieDate = itemView.findViewById(R.id.movieDate);
            movieDesc = itemView.findViewById(R.id.movieDesc);
        }
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.movie_new_view,parent,false);
        v.setOnLongClickListener(mOnLongClickListener);
        return new MovieHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MovieHolder movieHolder = (MovieHolder)holder;
        try {
            movieHolder.movieName.setText(jsonObjects.get(position).getString("original_title"));
            movieHolder.movieDate.setText(jsonObjects.get(position).getString("release_date"));
            movieHolder.movieDesc.setText(jsonObjects.get(position).getString("overview"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonObjects.size();
    }

    public void addItem(JSONObject jsonObject){
        jsonObjects.add(jsonObject);
        notifyDataSetChanged();
    }

    public void removeItem(int position){
        jsonObjects.remove(position);
        notifyItemRemoved(position);
    }

    public void removeAllItems(){
        jsonObjects = new ArrayList<>();
    }


}
