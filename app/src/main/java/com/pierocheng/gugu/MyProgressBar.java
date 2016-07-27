package com.pierocheng.gugu;

import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by Redfire on 2016/5/1.
 */
public class MyProgressBar extends FrameLayout {
    private boolean running;
    private int progress = 0;
    private static final int MAX_PROGRESS = 10000;

    private ClipDrawable clip;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0x123)
                clip.setLevel(progress);
        }
    };

    public MyProgressBar(Context context) {
        super(context);
        Init(context);
    }

    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init(context);
    }

    public MyProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Init(context);
    }

    public void Init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.touch_picture, this);

        ImageView iv = (ImageView) view.findViewById(R.id.iv_touchedfinger);


        clip = (ClipDrawable) iv.getDrawable();



    }

    public int getLevel() {
    return clip.getLevel();
    }

    public void stop() {
        progress = 0;
        running = false;
        handler.sendEmptyMessage(0x123);
    }

    public void start() {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                running = true;
                while (running) {
                    handler.sendEmptyMessage(0x123);
                    if (progress == MAX_PROGRESS)
                        break;
                    progress += 100;
                    try {
                        Thread.sleep(18);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }
}
