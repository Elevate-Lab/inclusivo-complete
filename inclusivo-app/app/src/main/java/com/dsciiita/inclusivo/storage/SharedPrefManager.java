package com.dsciiita.inclusivo.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.dsciiita.inclusivo.models.Company;
import com.dsciiita.inclusivo.models.User;
import com.dsciiita.inclusivo.models.UserCandidate;
import com.dsciiita.inclusivo.models.UserEmployee;
import com.google.gson.Gson;

public class SharedPrefManager {

    public static final String SHARED_PREF_NAME = "inclusivodb";
    public static final String PROFILE_PROGRESS_SHARED_PREF = "profileProgress";
    public static final String ONBOARDING_SHARED_PREF = "onboarding";

    public static SharedPrefManager mInstance;
    private Context mCtx;

    private SharedPrefManager(Context ctx){
        mCtx = ctx;
    }

    public static synchronized SharedPrefManager getInstance(Context ctx){
        if(mInstance==null){
            mInstance = new SharedPrefManager(ctx);
        }
        return mInstance;
    }

    public void saveToken(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("user", token).apply();
    }

    public String getToken() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("user", null);
    }


    public void saveCompanyID(int id){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt("companyID", id).apply();
    }

    public int getCompanyID(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("companyID", 1);
    }


    public void clear() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


    public boolean isEmployer(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isEmployer", false);
    }

    public void setEmployer(boolean isEmployer){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("isEmployer", isEmployer).apply();
    }

    public boolean isNew(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(ONBOARDING_SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isNew", true);
    }

    public void setNew(boolean isNew){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(ONBOARDING_SHARED_PREF, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("isNew", isNew).apply();
    }

    public void setProgress(int progress){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(PROFILE_PROGRESS_SHARED_PREF, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt("progress", progress).apply();
    }

    public int getProgress(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(PROFILE_PROGRESS_SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("progress", 0);
    }


}
