package com.test.themoviedb.helpers;

/**
 * Created by razan on 7/5/18.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.test.themoviedb.Model.Movie;
import com.test.themoviedb.MovieInformationActivity;
import com.test.themoviedb.R;
import com.test.themoviedb.latestMoviesActivity;

import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

  private static final String TAG = "RecyclerViewAdapter";

  private ArrayList<Movie> movies = new ArrayList<>();
  private Context mContext;

  public RecyclerViewAdapter(Context context, ArrayList<Movie> movies ) {
    this.movies = movies;
    mContext = context;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
    ViewHolder holder = new ViewHolder(view);
    return holder;
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, final int position) {
    Log.d(TAG, "onBindViewHolder: called.");

    Glide.with(mContext).load("https://image.tmdb.org/t/p/w500"+movies.get(position).getPoster_path()).into(holder.image);

   holder.imageName.setText(movies.get(position).getTitle());

    holder.parentLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("Movie_ID",  movies.get(position).getId());
        Intent intent = new Intent(mContext, MovieInformationActivity.class);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);

      }
    });
  }

  @Override
  public int getItemCount() {
    return movies.size();
  }


  public class ViewHolder extends RecyclerView.ViewHolder{

    ImageView image;
    TextView imageName;
    RelativeLayout parentLayout;

    public ViewHolder(View itemView) {
      super(itemView);
      image = itemView.findViewById(R.id.movie_poster);
      imageName = itemView.findViewById(R.id.movie_name);
      parentLayout = itemView.findViewById(R.id.parent_layout);
    }
  }
}
