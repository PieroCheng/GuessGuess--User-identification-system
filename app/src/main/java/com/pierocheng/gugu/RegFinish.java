package com.pierocheng.gugu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by pierocheng on 16/6/13.
 */
public class RegFinish extends Activity {
    private ImageView latch,menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regfinish);
        init();
        latchAnimation();
    }

    private void latchAnimation() {
        TranslateAnimation tr = new TranslateAnimation(1,1, Animation.RELATIVE_TO_SELF,300);
        tr.setDuration(1200);
        tr.setInterpolator(new BounceInterpolator());
        tr.setFillAfter(true);
        tr.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Toast.makeText(RegFinish.this, "成功完成一个用户的完整注册!", Toast.LENGTH_SHORT).show();
                AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
                alphaAnimation.setDuration(1000);
                alphaAnimation.setFillAfter(true);
                menu.startAnimation(alphaAnimation);
                menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RegFinish.this,CoinHome.class);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        latch.startAnimation(tr);
    }

    private void init() {
        latch= (ImageView) findViewById(R.id.reglatch);
        menu= (ImageView) findViewById(R.id.iv_reg_menu);
    }
}
