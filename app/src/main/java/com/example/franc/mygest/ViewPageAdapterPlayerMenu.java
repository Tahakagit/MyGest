package com.example.franc.mygest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by franc on 23/02/2018.
 */

public class ViewPageAdapterPlayerMenu extends FragmentPagerAdapter {
    static final int NUM_ITEMS = 10;
    FragmentManager fManager;
    ArrayList<Fragment> fragments;
    public ViewPageAdapterPlayerMenu(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        fManager = fm;
        this.fragments = fragments;

    }


    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return fragments.size();
    }
}
