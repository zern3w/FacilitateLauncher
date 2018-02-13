package android.facilitatelauncher;

import android.content.Intent;
import android.facilitatelauncher.activity.MainActivity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by puttipongtadang on 2/1/18.
 */

public class Launcher extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showApp();
    }

    private void showApp(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
