package com.pierocheng.gugu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by pierocheng on 16/5/30.
 */
public class CoinHome extends Activity {
    public final static String SER_KEY = "com.PieroCheng.ser";
    public final static String SER_NAME = "com.PieroCheng.name";
    public final static String SER_REG = "com.PieroCheng.reg";
    private ImageView register, verify, iv_coinhome, iv_coinhome_later;
    private Timer timer;
    private int timerEnd;
    private List<UserBmob> userlist;
    private HorizontalListView lv_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coinhome);
        lv_user = (HorizontalListView) findViewById(R.id.lv_user);
        register = (ImageView) findViewById(R.id.register_coinhome);
        verify = (ImageView) findViewById(R.id.verify_coinhome);
        iv_coinhome = (ImageView) findViewById(R.id.iv_coinhome);
        iv_coinhome_later = (ImageView) findViewById(R.id.iv_coinhome_later);

        TranslateAnimation tr = new TranslateAnimation(1,1,Animation.RELATIVE_TO_SELF,-30);
        tr.setDuration(3000);
        tr.setInterpolator(new BounceInterpolator());
        tr.setRepeatCount(200);
        verify.startAnimation(tr);

        lv_user.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle mBundle1 = new Bundle();
                Bundle mBundle2 = new Bundle();
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(SER_KEY, userlist.get(i).getObjectId());
                mBundle1.putSerializable(SER_NAME, userlist.get(i).getNameBmob());
                mBundle.putSerializable(SER_REG, false);
                Intent intent = new Intent(CoinHome.this, Touch.class);
                intent.putExtras(mBundle1);
                intent.putExtras(mBundle2);
                intent.putExtras(mBundle);
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                startActivity(intent);


            }
        });
        register.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        timerEnd = 3;
                        Timer();
                        register.startAnimation(animationSet());
                        ScaleAnimation scaleAnimation1 = new ScaleAnimation(1f, 1.4f, 1f, 1.4f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);//让verify变大
                        scaleAnimation1.setInterpolator(new AccelerateDecelerateInterpolator());
                        scaleAnimation1.setDuration(3000);
                        scaleAnimation1.setFillAfter(true);
                        verify.startAnimation(scaleAnimation1);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (timerEnd == 2) {
                            timer.cancel();
                            AnimationSet animationSet = new AnimationSet(true);
                            TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, 300);
                            translateAnimation.setDuration(2000);

                            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    iv_coinhome.setVisibility(View.GONE);
                                    iv_coinhome_later.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            animationSet.addAnimation(translateAnimation);
                            iv_coinhome.startAnimation(animationSet);

                            lv_user.setVisibility(View.VISIBLE);
                            final List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
                            BmobQuery<UserBmob> query = new BmobQuery<UserBmob>();
                            query.findObjects(CoinHome.this, new FindListener<UserBmob>() {
                                @Override
                                public void onSuccess(List<UserBmob> list) {
                                    userlist = list;
                                    for (int i = 0; i < list.size(); i++) {
                                        Map<String, Object> listem = new HashMap<String, Object>();
                                        listem.put("avator", list.get(i).getImgURL());
                                        listems.add(listem);

                                    }
                                    SimpleAdapter simpleAdapter = new SimpleAdapter(CoinHome.this, listems, R.layout.simple_item, new String[]{"avator"}, new int[]{R.id.iv_item});
                                    simpleAdapter.setViewBinder(new CustomViewBInder());
                                    lv_user.setAdapter(simpleAdapter);

                                }

                                @Override
                                public void onError(int i, String s) {
                                    Toast.makeText(CoinHome.this, s, Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else if (timerEnd == 1){
                            Intent intent = new Intent(CoinHome.this,RegName.class);
                            startActivity(intent);
                        }else {
                            timer.cancel();
                            register.clearAnimation();
                            verify.clearAnimation();
                        }
                        break;

                }
                return true;
            }
        });
    }

    private AnimationSet animationSet() {
        AnimationSet as = new AnimationSet(true);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);//让register旋转
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0.1f, 1, 0.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);//并变小
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0.0f);//最终让其消失

        //设置加速减速效果
        rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());

        //动画时间
        rotateAnimation.setDuration(3000);
        scaleAnimation.setDuration(3000);
        alphaAnimation.setDuration(3000);

        as.addAnimation(rotateAnimation);
        as.addAnimation(scaleAnimation);
        as.addAnimation(alphaAnimation);

        as.setFillAfter(true);
        return as;
    }

    private void Timer() {// 定义按钮的点击监听器
        // 定义Handler
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //handler处理消息
                if (msg.what > 0.8) {
                    timerEnd = 1;
                } else if (msg.what <= 0) {
                    //在handler里可以更改UI组件
                    timer.cancel();
                    timerEnd = 2;
                }
            }
        };
        // 定义计时器
        timer = new Timer();
        // 定义计划任务，根据参数的不同可以完成以下种类的工作：在固定时间执行某任务，在固定时间开始重复执行某任务，重复时间间隔可控，在延迟多久后执行某任务，在延迟多久后重复执行某任务，重复时间间隔可控
        timer.schedule(new TimerTask() {
            int i = 1;

            // TimerTask 是个抽象类,实现的是Runable类
            @Override
            public void run() {
                //定义一个消息传过去
                Message msg = new Message();
                msg.what = i--;
                handler.sendMessage(msg);
            }
        }, 0, 1000);
    }
}
