package com.test.themoviedb.Model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by razan on 7/5/18.
 */

public class Cast {
  private int id;
  private String name;
  private String birthday;
  private String gender;//1= female , 2= male
  private String profile_path;

  public Cast() {

  }

  public Cast(int id, String name, String birthday, String gender, String profile_path) {
    this.id = id;
    this.name = name;
    this.birthday = birthday;
    this.gender = gender;
    this.profile_path = profile_path;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBirthday() {
    return birthday;
  }

  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getProfile_path() {
    return profile_path;
  }

  public void setProfile_path(String profile_path) {
    this.profile_path = profile_path;
  }

  public static Cast jsonToObject(JSONObject jsonObject) {
    try {
      Cast result = new Cast();
      result.setId(jsonObject.getInt("id"));
      result.setName(jsonObject.getString("name"));

      if(jsonObject.has("birthday"))
      result.setBirthday(jsonObject.getString("birthday"));

      if(jsonObject.getInt("gender")==0)
        result.setGender("not set");
      if(jsonObject.getInt("gender")==1) //if is better for refactoring rather than else if
        result.setGender("Female");
      if(jsonObject.getInt("gender")==2) //if is better for refactoring rather than else if
        result.setGender("male");

      result.setProfile_path(jsonObject.getString("profile_path"));

      return result;

    } catch (JSONException ex) {
      Logger.getLogger(Movie.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }
}
