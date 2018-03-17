package com.example.franc.mygest;

import android.app.Application;

import java.math.BigDecimal;

import io.realm.Realm;

/**
 * Created by franc on 08/11/2017.
 */

public class MyApplication extends Application {

    public static final BigDecimal MONEY_PRECISION = new BigDecimal(1000);

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(getApplicationContext());

    }
}
