package com.test.themoviedb.helpers;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.test.themoviedb.R;

/**
 * Created by razan on 7/9/18.
 */

public class HidActionBar extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Toast.makeText( getApplicationContext(), "piccc!!!" , Toast.LENGTH_LONG).show();

    setContentView(R.layout.popup_photo_full);
    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    //getSupportActionBar().hide();
    Bundle extras = getIntent().getExtras();

  }

  public void hideAction(View view, Context context, String url){
    new PhotoFullPopupWindow(context, R.layout.popup_photo_full, view ,url , null);

    //For hiding android actionbar
    // myActionBar.hide();

  }
}
