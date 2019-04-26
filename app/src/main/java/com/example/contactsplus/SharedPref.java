package com.example.contactsplus;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPref {

        static void setToPref(Context context, String tag, String str){
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(tag, str);
            editor.apply();
        }

        static String getFromPref(Context context, String tag){
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            return settings.getString(tag,"false");
        }


}
