package com.test.themoviedb.Model;

import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by razan on 7/4/18.
 */

public class Movie {
  private int id;
  private String title;
  private String poster_path;
  private String description;
  private String genres;
  private static List<Genre> genreList = new ArrayList<Genre>();

  public Movie(int id, String title, String poster_path, String description, String genres) {
    this.id = id;
    this.title = title;
    this.poster_path = poster_path;
    this.description = description;
    this.genres = genres;
  }

  public Movie() {

  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getPoster_path() {
    return poster_path;
  }

  public void setPoster_path(String poster_path) {
    this.poster_path = poster_path;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getGenres() {
    return genres;
  }

  public void setGenres(String genres) {
    this.genres = genres;
  }

  public List<Genre> getGenreList() {
    return genreList;
  }

  public void setGenreList(List<Genre> genreList) {
    this.genreList = genreList;
  }

  @Override
  public String toString() {
    return "Movie{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", poster_path='" + poster_path + '\'' +
        ", description='" + description + '\'' +
        ", genres='" + genreList + '\'' +
        '}';
  }

  public static Movie jsonToObject(JSONObject jsonObject) {

    try {
      Movie result = new Movie();
      result.setId(jsonObject.getInt("id"));
      result.setTitle(jsonObject.getString("original_title"));
      result.setPoster_path(jsonObject.getString("poster_path"));
      result.setDescription(jsonObject.getString("overview"));
      //JSONObject genreObject = (JSONObject) jsonObject.get("genres");
      if (jsonObject.has("genres")){
        JSONArray genreArrayObject = jsonObject.getJSONArray("genres");

      for (int i = 0; i < genreArrayObject.length(); i++) {
        try {
          Genre genre = Genre.jsonToObject((JSONObject) genreArrayObject.get(i));
          genreList.add(genre);
        } catch (Exception e) {
        }
      }
    }
      return result;

    } catch (JSONException ex) {
      Logger.getLogger(Movie.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

}

