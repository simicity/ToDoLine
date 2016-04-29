package com.simicity.simi.todoline;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by simi on 4/27/16.
 */
public class TodoApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
            .setDefaultFontPath("fonts/NotoSans-Regular.ttf")
            .setFontAttrId(R.attr.fontPath)
            .build()
        );
    }
}
