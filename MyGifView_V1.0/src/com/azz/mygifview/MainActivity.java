package com.azz.mygifview;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyGifView myGifView = (MyGifView) findViewById(R.id.myGifView);
        myGifView.setBackgroundColor(Color.parseColor("#000000"));
    }

}
