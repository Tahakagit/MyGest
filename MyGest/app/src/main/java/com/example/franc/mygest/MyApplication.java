package com.example.franc.mygest;

import android.app.Application;
import android.provider.Settings;

import io.realm.Realm;

/**
 * Created by franc on 08/11/2017.
 */

public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(getApplicationContext());

    }
}
