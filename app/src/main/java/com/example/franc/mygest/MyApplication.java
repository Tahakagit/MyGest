package com.example.franc.mygest;

import android.app.Application;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;

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
