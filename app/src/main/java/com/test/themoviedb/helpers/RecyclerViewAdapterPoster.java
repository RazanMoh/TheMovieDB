package com.test.themoviedb.helpers;

/**
 * Created by razan on 7/5/18.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.bumptech.glide.Glide;
import com.test.themoviedb.Model.Poster;
import com.test.themoviedb.PosterActivity;
import com.test.themoviedb.R;
import java.util.ArrayList;

public class RecyclerViewAdapterPoster extends RecyclerView.Adapter<RecyclerViewAdapterPoster.ViewHolder>{

  private static final String TAG = "RecyclerViewAdapter";
  private boolean isImageFitToScreen= false;

  private ArrayList<Poster> posters = new ArrayList<>();
  private Context mContext;

  public RecyclerViewAdapterPoster(Context context, ArrayList<Poster> posters ) {
    this.posters = posters;
    mContext = context;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_poster, parent, false);
    ViewHolder holder = new ViewHolder(view);
    return holder;
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, final int position) {

    Glide.with(mContext).load("https://image.tmdb.org/t/p/w500"+posters.get(position).getFile_path()).into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            HidActionBar hidActionBar=new HidActionBar();
            hidActionBar.hideAction(view,mContext,"https://image.tmdb.org/t/p/w500"+posters.get(position).getFile_path());


          }
        });
  }

  @Override
  public int getItemCount() {
    return posters.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder{

    ImageView image;
    RelativeLayout parentLayout;

    public ViewHolder(View itemView) {
      super(itemView);
      image = itemView.findViewById(R.id.poster);
      parentLayout = itemView.findViewById(R.id.parent_layout);
    }
  }
}