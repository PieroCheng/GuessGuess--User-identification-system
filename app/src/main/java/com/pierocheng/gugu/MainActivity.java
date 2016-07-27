package com.pierocheng.gugu;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class MainActivity extends Activity  {
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        img = (ImageView) findViewById(R.id.main_btn);
//        img.setOnClickListener(this);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent mainIntent = new Intent(MainActivity.this, HomePage.class);
               startActivity(mainIntent);
               finish();
            }

        }, 3000);
    }

//    @Override
//    public void onClick(View v) {
//        Intent intent = new Intent(this, HomePage.class);
//        startActivity(intent);
//    }
}
