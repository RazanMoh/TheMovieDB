package com.test.themoviedb.helpers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.themoviedb.Model.Movie;
import com.test.themoviedb.R;

import java.util.ArrayList;

/**
 * Created by razan on 7/9/18.
 */

public class ListAdapter extends BaseAdapter {
  Context context;
  ArrayList<Movie> movieArrayList;

  public ListAdapter(Context context, ArrayList<Movie> movieArrayList) {
    this.context=context;
    this.movieArrayList = movieArrayList;
  }

  public int getCount() {
    return movieArrayList.size();
  }

  @Override
  public Object getItem(int i) {
    return null;
  }

  @Override
  public long getItemId(int i) {
    return i;
  }

  public View getView(int position, View convertView, ViewGroup parent) {

    LayoutInflater inflater = LayoutInflater.from(context);
    View rowView= inflater.inflate(R.layout.single_movie_item, parent, false);
    ImageView movie_poster = (ImageView) rowView.findViewById(R.id.movie_poster);
    TextView movie_name = (TextView) rowView.findViewById(R.id.movie_name);
    TextView movie_description = (TextView) rowView.findViewById(R.id.movie_description);
    Glide.with(context).load("https://image.tmdb.org/t/p/w500"+movieArrayList.get(position).getPoster_path()).into(movie_poster);
    movie_name.setText(movieArrayList.get(position).getTitle());
    if (movieArrayList.get(position).getDescription().equals("null"))
      movie_description.setVisibility(View.INVISIBLE);
    else
    movie_description.setText(movieArrayList.get(position).getDescription());

    return (rowView);
  }
}