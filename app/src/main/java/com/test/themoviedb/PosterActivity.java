package com.test.themoviedb;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.test.themoviedb.R;

public class PosterActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_poster);
    Intent intent = getIntent();
    Bundle bundle = intent.getExtras();
    String poster_url= bundle.getString("poster_url");
    ImageView image = findViewById(R.id.poster);
    Glide.with(getApplicationContext()).load("https://image.tmdb.org/t/p/w500"+poster_url).into(image);

  }
}


