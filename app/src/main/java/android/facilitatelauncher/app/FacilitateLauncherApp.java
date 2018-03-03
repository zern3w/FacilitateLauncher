package android.facilitatelauncher.app;

import android.app.Application;
import android.facilitatelauncher.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by puttipongtadang on 3/4/18.
 */

public class FacilitateLauncherApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initFont();
    }

    private void initFont() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/RSU_Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
