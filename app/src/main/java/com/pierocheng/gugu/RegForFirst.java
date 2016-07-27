package com.pierocheng.gugu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by PieroCheng on 2016/5/15.
 */
public class RegForFirst extends Activity implements View.OnClickListener {
    private EditText et;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "d47f4da77a838b88e8c4f535f3a5314d");
        setContentView(R.layout.regforfirst);

        et = (EditText) findViewById(R.id.userforfirst);
        btn = (Button) findViewById(R.id.buttonforfirst);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        UserBmob userBmob = new UserBmob();
        userBmob.setNameBmob(et.getText().toString());
        userBmob.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(RegForFirst.this, "注册成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(RegForFirst.this, "111", Toast.LENGTH_LONG).show();
            }
        });
    }
}
