package com.pierocheng.gugu;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Touch extends Activity implements View.OnClickListener {
    public final static String SER_COUNTREG = "com.PieroCheng.COUNTREG";
    public final static String SER_COUNTVER = "com.PieroCheng.COUNTVER";
    public final static String SER_KEY = "com.PieroCheng.ser";
    public final static String SER_REG = "com.PieroCheng.reg";
    public final static String SER_NAME = "com.PieroCheng.name";
    public final static String SER_ADDRESS = "com.PieroCheng.address";

    private Timer timer;
    private TextView timerText;
    private ImageView normal, wave1, wave2, wave3, wave4, wave5, wave6, wave7, wave8, wave9;
    private int timerFlag = 0;//定义一个timerFlag来判断第几次触碰屏幕，实现第一次计时开始，之后记录次数
    private int timerEnd = 0;
    private int count;
    private AnimationSet mAS1, mAS2, mAS3, mAS4, mAS5, mAS6, mAS7, mAS8, mAS9;//动画设置的变量
    private int OFFSET = 600;

    private Intent intent;
    private Bundle mBundle, mBundle1, mBundle2, mBundle3, mBundle4;

    /**
     * 目前设置的间断时间，不用就删
     **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.touch);
        Init();
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setMessage("10秒钟哟!手速杠杠的飞起来吧!");
        builder.setPositiveButton("", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
            }
        });

        builder.createDialog().show();
        normal.setOnClickListener(this);//图标的点击事件，美观起见，点击事件全部设在图标按钮上

        /**\\全部搞定
         * 1.定时，10m结束点击计时，修改delay\\delay已删
         * 2.波纹动画\\
         * 3.波纹动画的快慢，点击频率来定\\
         * **/
    }

    private void Init() {
        timerText = (TextView) findViewById(R.id.timer_layout);
        normal = (ImageView) findViewById(R.id.normal_layout);
        wave1 = (ImageView) findViewById(R.id.wave1_layout);
        wave2 = (ImageView) findViewById(R.id.wave2_layout);
        wave3 = (ImageView) findViewById(R.id.wave3_layout);
        wave4 = (ImageView) findViewById(R.id.wave4_layout);
        wave5 = (ImageView) findViewById(R.id.wave5_layout);
        wave6 = (ImageView) findViewById(R.id.wave6_layout);
        wave7 = (ImageView) findViewById(R.id.wave7_layout);
        wave8 = (ImageView) findViewById(R.id.wave8_layout);
        wave9 = (ImageView) findViewById(R.id.wave9_layout);

        mAS1 = initAnimationSet();
        mAS2 = initAnimationSet();
        mAS3 = initAnimationSet();
        mAS4 = initAnimationSet();
        mAS5 = initAnimationSet();
        mAS6 = initAnimationSet();
        mAS7 = initAnimationSet();
        mAS8 = initAnimationSet();
        mAS9 = initAnimationSet();
    }


    @Override
    public void onClick(View v) {
        if (timerFlag == 0) {
            Timer();
            timerFlag++;
        } else {
            mCount();
            switch (timerFlag) {
                case 1:
                    wave1.startAnimation(mAS1);
                    timerFlag++;
                    break;
                case 2:
                    wave2.startAnimation(mAS2);
                    timerFlag++;
                    break;
                case 3:
                    wave3.startAnimation(mAS3);
                    timerFlag++;
                    break;
                case 4:
                    wave4.startAnimation(mAS4);
                    timerFlag++;
                    break;
                case 5:
                    wave5.startAnimation(mAS5);
                    timerFlag++;
                    break;
                case 6:
                    wave6.startAnimation(mAS6);
                    timerFlag++;
                    break;
                case 7:
                    wave7.startAnimation(mAS7);
                    timerFlag++;
                    break;
                case 8:
                    wave8.startAnimation(mAS8);
                    timerFlag++;
                    break;
                case 9:
                    wave9.startAnimation(mAS9);
                    timerFlag = 1;
                    break;
            }
        }
    }

    private void mCount() {
        if (timerEnd == 0)
            count++;
        else {
            if (getIntent().getSerializableExtra(HomePage.SER_REG).equals(true)) {//若为注册
                intent = new Intent(Touch.this, FingerAreaActivity.class);
                mBundle = new Bundle();
                mBundle1 = new Bundle();
                mBundle2 = new Bundle();
                mBundle3 = new Bundle();
                mBundle4 = new Bundle();
                mBundle.putSerializable(SER_COUNTREG, count);//传点击
                mBundle1.putSerializable(SER_REG, true);//传是否注册（用于判断）
                mBundle2.putSerializable(SER_KEY, getIntent().getSerializableExtra(RegName.SER_KEY));//传BMOB ID
                mBundle3.putSerializable(SER_NAME, getIntent().getSerializableExtra(RegName.SER_NAME));//传注册名字
                mBundle4.putSerializable(SER_ADDRESS, getIntent().getSerializableExtra(RegName.SER_ADDRESS));//传注册地址
                intent.putExtras(mBundle);
                intent.putExtras(mBundle1);
                intent.putExtras(mBundle2);
                intent.putExtras(mBundle3);
                intent.putExtras(mBundle4);
                startActivity(intent);
            } else {//若不是注册
                intent = new Intent(Touch.this, FingerAreaActivity.class);
                mBundle = new Bundle();
                mBundle1 = new Bundle();
                mBundle2 = new Bundle();
                mBundle3 = new Bundle();
                mBundle.putSerializable(SER_COUNTVER, count);//传点击
                mBundle1.putSerializable(SER_REG, false);//传是否注册（用于判断）
                mBundle2.putSerializable(SER_KEY, getIntent().getSerializableExtra(CoinHome.SER_KEY));//传BMOB ID
                mBundle3.putSerializable(SER_NAME, getIntent().getSerializableExtra(CoinHome.SER_NAME));//传注册名字
                intent.putExtras(mBundle);
                intent.putExtras(mBundle1);
                intent.putExtras(mBundle2);
                intent.putExtras(mBundle3);
                startActivity(intent);
            }
        }
    }

    private void Timer() {// 定义按钮的点击监听器
        // 定义Handler
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //handler处理消息
                if (msg.what > 0) {
                    timerText.setText("" + msg.what);
                } else {
                    //在handler里可以更改UI组件
                    timerText.setText("-End-");
                    timer.cancel();
                    timerEnd++;
                }
            }
        };
        // 定义计时器
        timer = new Timer();
        // 定义计划任务，根据参数的不同可以完成以下种类的工作：在固定时间执行某任务，在固定时间开始重复执行某任务，重复时间间隔可控，在延迟多久后执行某任务，在延迟多久后重复执行某任务，重复时间间隔可控
        timer.schedule(new TimerTask() {
            int i = 10;

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


    private AnimationSet initAnimationSet() {
        AnimationSet as = new AnimationSet(true);
        ScaleAnimation sa = new ScaleAnimation(1f, 2.3f, 1f, 2.3f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(OFFSET * 3);
        AlphaAnimation aa = new AlphaAnimation(1, 0.1f);
        aa.setDuration(OFFSET * 3);
        as.addAnimation(sa);
        as.addAnimation(aa);
        return as;
    }

}
