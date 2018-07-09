package com.test.themoviedb.helpers;

/**
 * Created by razan on 7/5/18.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.test.themoviedb.CastInformationActivity;
import com.test.themoviedb.Model.Cast;
import com.test.themoviedb.R;

import java.util.ArrayList;


public class RecyclerViewAdapterCast extends RecyclerView.Adapter<RecyclerViewAdapterCast.ViewHolder>{

  private static final String TAG = "RecyclerViewAdapter";

  private ArrayList<Cast> casts = new ArrayList<>();
  private Context mContext;

  public RecyclerViewAdapterCast(Context context, ArrayList<Cast> casts ) {
    this.casts = casts;
    mContext = context;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_cast, parent, false);
    ViewHolder holder = new ViewHolder(view);
    return holder;
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, final int position) {

    Glide.with(mContext).load("https://image.tmdb.org/t/p/w500"+casts.get(position).getProfile_path()).into(holder.image);

   holder.imageName.setText(casts.get(position).getName());

    holder.parentLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("Cast_ID",  casts.get(position).getId());
        Intent intent = new Intent(mContext, CastInformationActivity.class);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);

      }
    });
  }

  @Override
  public int getItemCount() {
    return casts.size();
  }


  public class ViewHolder extends RecyclerView.ViewHolder{

    ImageView image;
    TextView imageName;
    RelativeLayout parentLayout;

    public ViewHolder(View itemView) {
      super(itemView);
      image = itemView.findViewById(R.id.cast_image);
      imageName = itemView.findViewById(R.id.cast_name);
      parentLayout = itemView.findViewById(R.id.parent_layout);
    }
  }
}
