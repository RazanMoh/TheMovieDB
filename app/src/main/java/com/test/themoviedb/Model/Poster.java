package com.test.themoviedb.Model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by razan on 7/4/18.
 */

public class Poster {
  private String file_path;
  private int height;
  private int width;

  public Poster(int id, String file_path, int height, int width) {
    this.file_path = file_path;
    this.height = height;
    this.width = width;

  }

  public Poster() {

  }

  public String getFile_path() {
    return file_path;
  }

  public void setFile_path(String file_path) {
    this.file_path = file_path;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public static Poster jsonToObject(JSONObject jsonObject) {
    try {
     Poster result = new Poster();
      result.setFile_path(jsonObject.getString("file_path"));
      result.setHeight(jsonObject.getInt("height"));
      result.setWidth(jsonObject.getInt("width"));

      return result;

    } catch (JSONException ex) {
      Logger.getLogger(Poster.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

}

