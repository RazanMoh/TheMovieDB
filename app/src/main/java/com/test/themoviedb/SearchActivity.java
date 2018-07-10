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
import android.widget.SearchView;
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

public class SearchActivity extends AppCompatActivity {
  private static final int ACTIVITY_NUM = 1;
  private JSONArray search_movies_array = null;
  private ArrayList<Movie> movieArrayList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);

    setupBottomNavigationView();
    SearchView searchView = (SearchView) findViewById(R.id.search_movie);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        query = query.replaceAll(" ", "+").toLowerCase();

        return checkConnection(query);
      }

      @Override
      public boolean onQueryTextChange(String newText) {

            return false;
    }
  });

  }

  private boolean checkConnection(String query) {
    ConnectionDetector connectionDetector = new ConnectionDetector(getApplicationContext());

    if (!connectionDetector.isConnectingToInternet()) {
      Toast.makeText(SearchActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_LONG).show();
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
      searchMovie(query);
      return true;
    }
  }

  private void searchMovie(String keyWord) {

    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
    StringRequest sr=new StringRequest(Request.Method.GET, "https://api.themoviedb.org/3/search/movie?api_key=5d88184b27e82945677b274b6572beec&query="+keyWord, new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {

        try {
          JSONObject jsonObject = new JSONObject(response);
          search_movies_array = jsonObject.getJSONArray("results");

          movieArrayList = new ArrayList<Movie>();
          TextView searchError = (TextView) findViewById(R.id.search_movie_error);
          if(search_movies_array.length()==0){
            searchError.setVisibility(View.VISIBLE);
          }
          else {
            searchError.setVisibility(View.GONE);
          }

          for (int i = 0; i < search_movies_array.length(); i++) {
            try {
              Movie movie = Movie.jsonToObject((JSONObject) search_movies_array.get(i));
              movieArrayList.add(movie);

            } catch (Exception e) {}
          }
          ListAdapter adapter = new ListAdapter(getApplicationContext(), movieArrayList);
          ListView LatestMoviesListView = (ListView) findViewById(R.id.list_of_movies);
          LatestMoviesListView.setAdapter(adapter);

          LatestMoviesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

              Bundle bundle = new Bundle();
              bundle.putInt("Movie_ID",  movieArrayList.get(i).getId());
              Intent intent = new Intent(SearchActivity.this, MovieInformationActivity.class);
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
      return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

      LayoutInflater inflater = getLayoutInflater();
      View rowView= inflater.inflate(R.layout.single_movie_item, parent, false);
      ImageView movie_poster = (ImageView) rowView.findViewById(R.id.movie_poster);
      TextView movie_name = (TextView) rowView.findViewById(R.id.movie_name);
      TextView movie_description = (TextView) rowView.findViewById(R.id.movie_description);
      Glide.with(getApplicationContext()).load("https://image.tmdb.org/t/p/w500"+movieArrayList.get(position).getPoster_path()).into(movie_poster);
      movie_name.setText(movieArrayList.get(position).getTitle());
      movie_description.setText(movieArrayList.get(position).getDescription());

      return (rowView);
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
}
