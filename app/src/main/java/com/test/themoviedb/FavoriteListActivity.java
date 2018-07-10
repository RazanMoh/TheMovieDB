package com.test.themoviedb;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.test.themoviedb.Model.Movie;
import com.test.themoviedb.helpers.BottomNavigationViewHelper;
import com.test.themoviedb.helpers.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class FavoriteListActivity extends AppCompatActivity {
  private static final int ACTIVITY_NUM = 2;
  private JSONArray favorite_movies_array = null;
  private ArrayList<Movie> movieArrayList;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_favorite_list);
    setupBottomNavigationView();
    boolean checkConnection= checkConnection();
    if(checkConnection)
      getFavoriteMovie();
  }

  private boolean checkConnection() {
    ConnectionDetector connectionDetector = new ConnectionDetector(getApplicationContext());

    if (!connectionDetector.isConnectingToInternet()) {
      Toast.makeText(FavoriteListActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_LONG).show();
      final AlertDialog alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom)).create();
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
    }

    else {
      return true;
    }
  }

  private void setupBottomNavigationView() {
    BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
    BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
    BottomNavigationViewHelper.enableNavigation(getApplicationContext(), this,bottomNavigationView);
    Menu menu = bottomNavigationView.getMenu();
    MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
    menuItem.setChecked(true);
  }

  private void getFavoriteMovie() {

    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

    StringRequest sr=new StringRequest(Request.Method.GET, "https://api.themoviedb.org/3/account/7950698/favorite/movies?api_key=5d88184b27e82945677b274b6572beec&session_id=46b162a0cda56b85e80a112d0f3f8edf133157fe&language=en-US&sort_by=created_at.desc&page=1", new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {

        try {
          JSONObject jsonObject = new JSONObject(response);
          favorite_movies_array = jsonObject.getJSONArray("results");


          movieArrayList = new ArrayList<Movie>();

          for (int i = 0; i < favorite_movies_array.length(); i++) {
            try {
              Movie movie = Movie.jsonToObject((JSONObject) favorite_movies_array.get(i));
              movieArrayList.add(movie);

            } catch (Exception e) {}
          }

          ListAdapter adapter = new ListAdapter(getApplicationContext(), movieArrayList);
          ListView LatestMoviesListView = (ListView) findViewById(R.id.favorite_movies_list);
          LatestMoviesListView.setAdapter(adapter);

          LatestMoviesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

              Bundle bundle = new Bundle();
              bundle.putInt("Movie_ID",  movieArrayList.get(i).getId());
              Intent intent = new Intent(FavoriteListActivity.this, MovieInformationActivity.class);
              intent.putExtras(bundle);
              startActivity(intent);

            }
          });

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

  class ListAdapter extends BaseAdapter {
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
      View rowView= inflater.inflate(R.layout.single_movie_item_favorite, parent, false);
      ImageView movie_poster = (ImageView) rowView.findViewById(R.id.movie_poster);
      TextView movie_name = (TextView) rowView.findViewById(R.id.movie_name);
      Glide.with(context).load("https://image.tmdb.org/t/p/w500"+movieArrayList.get(position).getPoster_path()).into(movie_poster);
      movie_name.setText(movieArrayList.get(position).getTitle());
      return (rowView);
    }
  }
}
