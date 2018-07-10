package com.test.themoviedb.Model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Genre {
  public Genre(Integer id, String name) {
    this.id = id;
    this.name = name;
  }


  public Genre(){}

  private Integer id;

  private String name;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public static Genre jsonToObject(JSONObject jsonObject) {
    try {
      Genre result = new Genre();
      result.setId(jsonObject.getInt("id"));
      result.setName(jsonObject.getString("name"));
      return result;

    } catch (JSONException ex) {
      Logger.getLogger(Movie.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  @Override
  public String toString() {
    return "{id=" + id + ", name="+name+"}";
  }
}