package com.app.translation.Database;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPref {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public final static String MyPref = "MyPref";
    public final static String User = "User";

    public MyPref(Context context){
        sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setUser(String data) {
        editor.putString(User, data);
        editor.commit();
    }

    public String getUser() {
        return sharedPreferences.getString(User, "");
    }

    public void remove(){
        editor.remove(User);
        editor.clear();
        editor.apply();
    }
}
