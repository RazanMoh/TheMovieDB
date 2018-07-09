package com.test.themoviedb;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.test.themoviedb.Model.Cast;
import com.test.themoviedb.Model.Movie;
import com.test.themoviedb.R;
import com.test.themoviedb.helpers.BottomNavigationViewHelper;
import com.test.themoviedb.helpers.RecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CastInformationActivity extends AppCompatActivity {
  private static final int ACTIVITY_NUM = 0;
  private JSONArray movies_array = null;
  private ArrayList<Movie> movieArrayList;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cast_information);
    Intent intent = getIntent();
    Bundle bundle = intent.getExtras();
    int cast_id= bundle.getInt("Cast_ID");

    getCast(cast_id);
    getMovies(cast_id);
    setupBottomNavigationView();

    ImageButton backArrow = findViewById(R.id.backArrow);
    backArrow.setOnClickListener(new View.OnClickListener(){
      public void onClick(View view) {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

      }
    });


  }

  private void getCast(int id) {

    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
    StringRequest sr=new StringRequest(Request.Method.GET, "https://api.themoviedb.org/3/person/"+id+"?api_key=5d88184b27e82945677b274b6572beec&language=en-US", new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {

        try {
          JSONObject jsonObject = new JSONObject(response);

          try {

            Cast cast = Cast.jsonToObject(jsonObject);

            ImageView cast_image = (ImageView) findViewById(R.id.cast_image);
            TextView cast_name = (TextView) findViewById(R.id.cast_name);
            TextView cast_gender = (TextView) findViewById(R.id.cast_gender);
            TextView cast_birthday = (TextView) findViewById(R.id.cast_birthday);
            Glide.with(getApplicationContext()).load("https://image.tmdb.org/t/p/w500"+cast.getProfile_path()).into(cast_image);
            cast_name.setText("Name: "+cast.getName());
            cast_gender.setText("Gender: "+cast.getGender());
            cast_birthday.setText("Birthday: "+cast.getBirthday());

          } catch (Exception e) {}

          return;

        } catch (JSONException e) {
          e.printStackTrace();
        }
      }


    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
      }
    });
    queue.add(sr);
  }

  private void getMovies(int id) {

    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
    Log.d("ggg","razan123"+id);
    StringRequest sr=new StringRequest(Request.Method.GET, "https://api.themoviedb.org/3/person/"+id+"/movie_credits?api_key=5d88184b27e82945677b274b6572beec&language=en-US", new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {

        try {
          JSONObject jsonObject = new JSONObject(response);

          movies_array = jsonObject.getJSONArray("cast");

          movieArrayList = new ArrayList<Movie>();

          for (int i = 0; i < movies_array.length(); i++) {
            try {
              Movie movie = Movie.jsonToObject((JSONObject) movies_array.get(i));
              movieArrayList.add(movie);

            } catch (Exception e) {}
          }

          initRecyclerView();

          return;

        } catch (JSONException e) {
          e.printStackTrace();
        }
      }


    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
      }
    });
    queue.add(sr);
  }

  private void initRecyclerView(){
    RecyclerView recyclerView = findViewById(R.id.similar_movies_view);
    RecyclerViewAdapter adapter = new RecyclerViewAdapter(getApplicationContext(), movieArrayList);
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
  }


  private void setupBottomNavigationView() {
    BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
    BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
    BottomNavigationViewHelper.enableNavigation(getApplicationContext(), this,bottomNavigationView);
    Menu menu = bottomNavigationView.getMenu();
    MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
    menuItem.setChecked(true);
  }

}
