package com.chernov.android.android_googlemap_weather_api;

import android.support.v4.app.Fragment;

public class MainActivity extends SingleFragmentActivity {
    @Override
    public Fragment createFragment() {
        return new MainFragment();
    }
}