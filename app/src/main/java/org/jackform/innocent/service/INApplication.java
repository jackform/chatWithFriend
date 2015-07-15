package org.jackform.innocent.service;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import java.nio.charset.MalformedInputException;

/**
 * Created by jackform on 15-6-4.
 */
public class INApplication extends Application{
    private static INApplication mINApplication;
    private static Context context;

    static {
        //TODO load lib
        //System.loadLibrary("");
    }
    public static Context getAppContext() {
        return mINApplication;
    }
    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        context = this;
        mINApplication = this;

        //TODO some application's data initial
        //TODO start a service for send\receive network data
        Intent intent = new Intent(this,NetworkService.class);
        startService(intent);
    }
}

