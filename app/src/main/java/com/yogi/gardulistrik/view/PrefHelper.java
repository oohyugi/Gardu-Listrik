package com.yogi.gardulistrik.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.yogi.gardulistrik.api.mdl.LoginMdl;

/**
 * Created by yogi on 04/02/17.
 */
public class PrefHelper {


    public static String KEY_USER ="user";
    public static SharedPreferences getPref(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
    public static void savePref (Context context, String key, String value){
        getPref(context).edit().putString(key,value).commit();
    }
    public static String getPref(Context context, String key) {
        return getPref(context).getString(key, null);
    }

    public static void saveUser (Context context, LoginMdl mdl){
        getPref(context).edit().putString(KEY_USER,new Gson().toJson(mdl)).commit();
    }
    public static LoginMdl getUser(Context context){
        return new Gson().fromJson(getPref(context,KEY_USER),LoginMdl.class);

    }

}