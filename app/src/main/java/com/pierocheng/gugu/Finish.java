package com.pierocheng.gugu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;

/**
 * Created by pierocheng on 16/5/25.
 */
public class Finish extends Activity {
    //private TextView re, toure, aerare, camre, voire;
    private String key, name;
    private UserBmob userBmobF;
    private ImageView latch, menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finish);
        userBmobF = new UserBmob();
        init();
        verify();
    }

    private void latchAnimation() {
        TranslateAnimation tr = new TranslateAnimation(1, 1, Animation.RELATIVE_TO_SELF, -105);
        tr.setInterpolator(new DecelerateInterpolator());
        tr.setDuration(1000);
        tr.setFillAfter(true);
        latch.startAnimation(tr);
        tr.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                latch.setPadding(latch.getPaddingLeft(), latch.getPaddingTop() - 210, latch.getPaddingRight(), latch.getPaddingBottom());
                RotateAnimation rotateAnimation = new RotateAnimation(0, 30, 350, 350);
                rotateAnimation.setFillAfter(true);
                rotateAnimation.setDuration(800);
                latch.startAnimation(rotateAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void verify() {
        key = (String) getIntent().getSerializableExtra(IsvActivity.SER_KEY);//传进用户ID,以便从Bmob上获取之前注册的信息
        name = (String) getIntent().getSerializableExtra(IsvActivity.SER_NAME);//传进用户名
        userBmobF.setComboBmobVER((Integer) getIntent().getSerializableExtra(IsvActivity.SER_COUNTVER));//传进点击计数,之后用作比较
        userBmobF.setFingerAeraBmobVER((Float) getIntent().getSerializableExtra(IsvActivity.SER_FINVER));//传进按压手指面积,之后用作比较
        userBmobF.setCameraBack((Boolean) getIntent().getSerializableExtra(IsvActivity.SER_CAMISTRUE));//传进人脸识别结果
        userBmobF.setListernerBack((Boolean) getIntent().getSerializableExtra(IsvActivity.SER_VOICEISTRUE));//传进声音识别结果
        BmobQuery<UserBmob> query = new BmobQuery<UserBmob>();
        query.getObject(this, key, new GetListener<UserBmob>() {
            @Override
            public void onSuccess(UserBmob userBmob) {
                userBmobF.setComboBmobREG(userBmob.getComboBmobREG());//把注册时云端的点击次数下载下来
                userBmobF.setFingerAeraBmobREG(userBmob.getFingerAeraBmobREG());//把注册时云端的手指面积下载下来
                gpa();//通过加权平均数的方法来计算是否通过验证
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(Finish.this, "请检查您的网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void gpa() {
        /** gpa算法为: 点击次数(0.3) 手指面积(0.3) 人脸识别(0.7) 声音识别(0.7) **/
        double i = 0;
        if (Math.abs(userBmobF.getComboBmobVER() - userBmobF.getComboBmobREG()) < 7) {
            i = i + 0.3;
            // toure.setText("Good");
        }
        if (Math.abs(userBmobF.getFingerAeraBmobVER() - userBmobF.getComboBmobREG()) < 0.5) {
            i = i + 0.3;
            // aerare.setText("Good");
        }
        if (userBmobF.getCameraBack()) {
            i = i + 0.7;
            // camre.setText("Good");
        }
        if (userBmobF.getListernerBack()) {
            i = i + 0.7;
            // voire.setText("Good");
        }
        if (i >= 0.6) {
            Toast.makeText(Finish.this, "通过" + i, Toast.LENGTH_SHORT).show();
            latchAnimation();

        } else
            Toast.makeText(Finish.this, "不通过" + i, Toast.LENGTH_SHORT).show();
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setFillAfter(true);
        menu.startAnimation(alphaAnimation);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Finish.this,CoinHome.class);
                startActivity(intent);
            }
        });
    }

    private void init() {
        //re = (TextView) findViewById(R.id.result);
        //toure = (TextView) findViewById(R.id.touchResult);
        //aerare = (TextView) findViewById(R.id.aeraResult);
        //camre = (TextView) findViewById(R.id.cameraResult);
        //voire = (TextView) findViewById(R.id.voiceResult);
        latch = (ImageView) findViewById(R.id.latch);
        menu = (ImageView) findViewById(R.id.iv_menu);
    }
}
