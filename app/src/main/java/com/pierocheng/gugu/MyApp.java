package com.pierocheng.gugu;

import android.app.Application;

import com.iflytek.cloud.SpeechUtility;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import cn.bmob.v3.Bmob;

/**
 * Created by Redfire on 2016/5/1.
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 应用程序入口处调用,避免手机内存过小，杀死后台进程,造成SpeechUtility对象为null
        // 设置你申请的应用appid
        SpeechUtility.createUtility(this, "appid=56ed5de5");
        Bmob.initialize(this, "d47f4da77a838b88e8c4f535f3a5314d");
        //.init(ImageLoaderConfiguration.createDefault(MainActivity.this));
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
    }
}
