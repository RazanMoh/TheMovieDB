package com.test.themoviedb;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
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
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.test.themoviedb.Model.Cast;
import com.test.themoviedb.Model.Movie;
import com.test.themoviedb.Model.Poster;
import com.test.themoviedb.helpers.BottomNavigationViewHelper;
import com.test.themoviedb.helpers.ConnectionDetector;
import com.test.themoviedb.helpers.RecyclerViewAdapter;
import com.test.themoviedb.helpers.RecyclerViewAdapterCast;
import com.test.themoviedb.helpers.RecyclerViewAdapterPoster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MovieInformationActivity extends AppCompatActivity {
  private static final int ACTIVITY_NUM = 0;
  private static CharSequence textA="";
  private JSONArray similar_movies_array = null, casts_array= null, posters_array=null;
  private ArrayList<Movie> movieArrayList;
  private ArrayList<Cast> castArrayList;
  private ArrayList<Poster> posterArrayList;
  private  static boolean checkFavorite =false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie_information);
    Intent intent = getIntent();
    Bundle bundle = intent.getExtras();
    int movie_id= bundle.getInt("Movie_ID");

   boolean checkConnection = checkConnection();
   if(checkConnection){
    getMovie(movie_id);
    getSimilarMovies(movie_id);
    getCasts(movie_id);
     getPosters(movie_id);
   }
    setupBottomNavigationView();
    initButtons(movie_id);

  }


  private boolean checkConnection() {
    ConnectionDetector connectionDetector = new ConnectionDetector(getApplicationContext());

    if (!connectionDetector.isConnectingToInternet()) {
      Toast.makeText(MovieInformationActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_LONG).show();
      final AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
      alertDialog.setTitle(getResources().getString(R.string.alert));
      alertDialog.setMessage(getResources().getString(R.string.internet_message));
      alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.enable_internet), new DialogInterface.OnClickListener() {

        public void onClick(DialogInterface dialog, int id) {
          Intent gpsOptionsIntent = new Intent(
              Settings.ACTION_WIFI_SETTINGS);
          startActivity(gpsOptionsIntent);
        }
      });
      alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
          dialog.dismiss();
        }
      });
      alertDialog.show();
      return false;
    } else {
      return true;
    }
  }

  private void initButtons(final int movie_id) {

    ImageButton backArrow = findViewById(R.id.backArrow);
    backArrow.setOnClickListener(new View.OnClickListener(){
      public void onClick(View view) {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
      }
    });

    final ImageButton favorite = findViewById(R.id.star);

    checkIfMovieIsFavorite(movie_id, favorite);

    favorite.setOnClickListener(new View.OnClickListener(){
      public void onClick(View view) {
        boolean checkConnection = checkConnection();
        if(checkConnection) {
          if (!checkFavorite)
            addAsFavorite(movie_id, favorite);
          else
            deleteFavorite(movie_id, favorite);
        }

      }
    });
  }

  private void getMovie(int id) {

    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
    StringRequest sr=new StringRequest(Request.Method.GET, "https://api.themoviedb.org/3/movie/%20"+id+"?api_key=5d88184b27e82945677b274b6572beec&language=en-US", new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {

        try {
          JSONObject jsonObject = new JSONObject(response);

            try {

              Movie movie = Movie.jsonToObject(jsonObject);

              ImageView movie_poster = (ImageView) findViewById(R.id.movie_poster);
              TextView movie_name = (TextView) findViewById(R.id.movie_name);
              TextView movie_description = (TextView) findViewById(R.id.movie_description);
              TextView movie_genres = (TextView) findViewById(R.id.movie_genres);
              Glide.with(getApplicationContext()).load("https://image.tmdb.org/t/p/w500"+movie.getPoster_path()).into(movie_poster);
              movie_name.setText(movie.getTitle());
              movie_description.setText(movie.getDescription());
              String genres="Genres:  ";
              int i=0;
              while(i<movie.getGenreList().size()){
                genres+=movie.getGenreList().get(i).getName();
                if(i+1!=movie.getGenreList().size()){
                  genres+= ", ";}
                i++;
              }
              movie_genres.setText(genres);

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

  private void setupBottomNavigationView() {
    BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
    BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
    BottomNavigationViewHelper.enableNavigation(getApplicationContext(), this,bottomNavigationView);
    Menu menu = bottomNavigationView.getMenu();
    MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
    menuItem.setChecked(true);
  }

  private void getSimilarMovies(int id) {

    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
    StringRequest sr=new StringRequest(Request.Method.GET, "https://api.themoviedb.org/3/movie/"+id+"/similar?api_key=5d88184b27e82945677b274b6572beec&language=en-US&page=1", new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {

        try {
          JSONObject jsonObject = new JSONObject(response);
          similar_movies_array = jsonObject.getJSONArray("results");

          movieArrayList = new ArrayList<Movie>();
          TextView similarMoviesError = (TextView) findViewById(R.id.similar_movies_error);
          if(similar_movies_array.length()==0){
            similarMoviesError.setVisibility(View.VISIBLE);
          }
          else {
            similarMoviesError.setVisibility(View.GONE);
          }

          for (int i = 0; i < similar_movies_array.length(); i++) {
            try {
              Movie movie = Movie.jsonToObject((JSONObject) similar_movies_array.get(i));
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

  private void getCasts(int id) {

    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
    StringRequest sr=new StringRequest(Request.Method.GET, "https://api.themoviedb.org/3/movie/"+id+"/casts?api_key=5d88184b27e82945677b274b6572beec&language=en-US&page=1", new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {

        try {
          JSONObject jsonObject = new JSONObject(response);
          casts_array = jsonObject.getJSONArray("cast");

          castArrayList = new ArrayList<Cast>();

          for (int i = 0; i < casts_array.length(); i++) {
            try {
              Cast cast = Cast.jsonToObject((JSONObject) casts_array.get(i));
              castArrayList.add(cast);

            } catch (Exception e) {}
          }

          initRecyclerViewCast();

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


  private void getPosters(int id) {

    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
    StringRequest sr=new StringRequest(Request.Method.GET, "https://api.themoviedb.org/3/movie/"+id+"/images?api_key=5d88184b27e82945677b274b6572beec&language=en-US%2Cnull", new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {

        try {
          JSONObject jsonObject = new JSONObject(response);
          posters_array = jsonObject.getJSONArray("posters");

          posterArrayList = new ArrayList<Poster>();
          TextView postersError = (TextView) findViewById(R.id.posters_error);
          if(posters_array.length()==0){
            postersError.setVisibility(View.VISIBLE);
          }
          else {
            postersError.setVisibility(View.GONE);
          }

          for (int i = 0; i < posters_array.length(); i++) {
            try {
              Poster poster = Poster.jsonToObject((JSONObject) posters_array.get(i));
              posterArrayList.add(poster);

            } catch (Exception e) {}
          }

          initRecyclerViewPoster();

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

  private void addAsFavorite(final int id, final ImageButton favorite) {
    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
    StringRequest sr = new StringRequest(Request.Method.POST,"https://api.themoviedb.org/3/account/7950698/favorite?api_key=5d88184b27e82945677b274b6572beec&session_id=46b162a0cda56b85e80a112d0f3f8edf133157fe", new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {
        try {
          JSONObject jsonObject = new JSONObject(response);
          int status_code= jsonObject.getInt("status_code");
          String status_message= jsonObject.getString("status_message");
          if(status_code==1) {
            favorite.setBackgroundResource(R.drawable.yellow_star);
            checkFavorite=true;

          }

          else if(status_code==12) {
            favorite.setBackgroundResource(R.drawable.yellow_star);
            checkFavorite=true;
          }
          else{
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.favorite_failed), Toast.LENGTH_LONG).show();
          }

        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {

      }
    }){
      @Override
      protected Map<String,String> getParams(){
        Map<String,String> params = new HashMap<String, String>();
        params.put("media_type","movie");
        params.put("media_id",id+"");
          params.put("favorite","true");
        return params;
      }

      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String,String> params = new HashMap<String, String>();

        params.put("Accept-Language","en");

        return params;
      }
    };
    queue.add(sr);

  }

  private void deleteFavorite(final int id, final ImageButton favorite) {
    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
    StringRequest sr = new StringRequest(Request.Method.POST,"https://api.themoviedb.org/3/account/7950698/favorite?api_key=5d88184b27e82945677b274b6572beec&session_id=46b162a0cda56b85e80a112d0f3f8edf133157fe", new Response.Listener<String>() {
        @Override
      public void onResponse(String response) {
        try {
          JSONObject jsonObject = new JSONObject(response);
          int status_code= jsonObject.getInt("status_code");
          String status_message= jsonObject.getString("status_message");
          if(status_code==13) {
            favorite.setBackgroundResource(R.drawable.star);
            checkFavorite=false;
          }
          else
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.favorite_failed), Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {

      }
    }){
      @Override
      protected Map<String,String> getParams(){
        Map<String,String> params = new HashMap<String, String>();
        params.put("media_type","movie");
        params.put("media_id",id+"");
        params.put("favorite","false");
        return params;
      }

      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String,String> params = new HashMap<String, String>();

        params.put("Accept-Language","en");

        return params;
      }
    };
    queue.add(sr);

  }

  private void initRecyclerView(){

    Log.d("razan", "initRecyclerView: init recyclerview."+movieArrayList.size());
    RecyclerView recyclerView = findViewById(R.id.similar_movies_view);
    RecyclerViewAdapter adapter = new RecyclerViewAdapter(getApplicationContext(), movieArrayList);
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
  }

  private void initRecyclerViewCast(){
    RecyclerView recyclerView = findViewById(R.id.cast_view);
    RecyclerViewAdapterCast adapter = new RecyclerViewAdapterCast(getApplicationContext(), castArrayList);
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
  }

  private void initRecyclerViewPoster(){
    RecyclerView recyclerView = findViewById(R.id.posters_view);
    RecyclerViewAdapterPoster adapter = new RecyclerViewAdapterPoster(getApplicationContext(), posterArrayList);
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
  }

  private void checkIfMovieIsFavorite(int id,final ImageButton favorite) {

    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
    StringRequest sr=new StringRequest(Request.Method.GET, "https://api.themoviedb.org/3/movie/"+id+"/account_states?api_key=5d88184b27e82945677b274b6572beec&session_id=46b162a0cda56b85e80a112d0f3f8edf133157fe", new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {

        try {
          JSONObject jsonObject = new JSONObject(response);
          boolean isFavorite= jsonObject.getBoolean("favorite");
          if(isFavorite) {
            favorite.setBackgroundResource(R.drawable.yellow_star);
            checkFavorite= true;
          }
          else{
            favorite.setBackgroundResource(R.drawable.star);
            checkFavorite= false;
          }
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

}
