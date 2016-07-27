package com.pierocheng.gugu;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Redfire on 2016/4/26.
 */
public class FingerAreaActivity extends Activity {
    public final static String SER_FINREG = "com.PieroCheng.FINREG";
    public final static String SER_FINVER = "com.PieroCheng.FINVER";
    public final static String SER_COUNTREG = "com.PieroCheng.COUNTREG";
    public final static String SER_COUNTVER = "com.PieroCheng.COUNTVER";
    public final static String SER_KEY = "com.PieroCheng.ser";
    public final static String SER_REG = "com.PieroCheng.reg";
    public final static String SER_NAME = "com.PieroCheng.name";
    public final static String SER_ADDRESS = "com.PieroCheng.address";

    private Intent intent;
    private Bundle mBundle, mBundle1, mBundle2, mBundle3, mBundle4;

    private float max_size = 0;
    private MyProgressBar pb_finger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.touchsize_layout);
        pb_finger = (MyProgressBar) findViewById(R.id.pb_finger);

        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setMessage("长按屏幕中心图案开始记录按压面积");
        builder.setPositiveButton("", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                //设置你的操作事项
            }
        });

        builder.createDialog().show();
        pb_finger.setOnTouchListener(new View.OnTouchListener() {
            @Override

            public boolean onTouch(View v, final MotionEvent event) {

                int i = event.getAction();
                switch (i)

                {
                    case MotionEvent.ACTION_UP:

                        pb_finger.stop();
                        break;
                    case MotionEvent.ACTION_DOWN:
                        pb_finger.start();
                        break;
                    case MotionEvent.ACTION_MOVE:

                        float size = event.getSize();
                        if (size > max_size)
                            max_size = size;
                        Log.v("size", Float.toString(size));
                        if (pb_finger.getLevel() == 10000) {
                            if (getIntent().getSerializableExtra(Touch.SER_REG).equals(true)) {
                                intent = new Intent(FingerAreaActivity.this, Capture.class);
                                mBundle = new Bundle();
                                mBundle1 = new Bundle();
                                mBundle2 = new Bundle();
                                mBundle3 = new Bundle();
                                mBundle4 = new Bundle();
                                mBundle.putSerializable(SER_FINREG, max_size);//传指纹
                                mBundle1.putSerializable(SER_KEY, getIntent().getSerializableExtra(Touch.SER_KEY));//传BMOB ID
                                mBundle2.putSerializable(SER_ADDRESS, getIntent().getSerializableExtra(Touch.SER_ADDRESS));//传注册地址
                                mBundle3.putSerializable(SER_COUNTREG, getIntent().getSerializableExtra(Touch.SER_COUNTREG));//传点击次数
                                mBundle4.putSerializable(SER_NAME, getIntent().getSerializableExtra(Touch.SER_NAME));//传注册姓名
                                intent.putExtras(mBundle);
                                intent.putExtras(mBundle1);
                                intent.putExtras(mBundle2);
                                intent.putExtras(mBundle3);
                                intent.putExtras(mBundle4);
                                startActivity(intent);

                            } else {
                                intent = new Intent(FingerAreaActivity.this, Verify.class);
                                mBundle = new Bundle();
                                mBundle1 = new Bundle();
                                mBundle2 = new Bundle();
                                mBundle3 = new Bundle();
                                mBundle.putSerializable(SER_FINVER, max_size);
                                mBundle1.putSerializable(SER_KEY, getIntent().getSerializableExtra(Touch.SER_KEY));
                                mBundle2.putSerializable(SER_NAME, getIntent().getSerializableExtra(Touch.SER_NAME));//传注册姓名
                                mBundle3.putSerializable(SER_COUNTVER, getIntent().getSerializableExtra(Touch.SER_COUNTVER));
                                intent.putExtras(mBundle);
                                intent.putExtras(mBundle1);
                                intent.putExtras(mBundle2);
                                intent.putExtras(mBundle3);
                                startActivity(intent);
                            }
                        }
                        break;
                }
                return true;
            }

        });
    }
}
