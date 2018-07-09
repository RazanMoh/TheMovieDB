package com.test.themoviedb;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
  private static int SplashTimeOut=1500;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    new Handler().postDelayed(new Runnable(){

      @Override
      public void run(){

        Intent HomeIntent=new Intent(MainActivity.this,latestMoviesActivity.class);
        startActivity(HomeIntent);
        finish();
      }
    },SplashTimeOut);
  }

}
