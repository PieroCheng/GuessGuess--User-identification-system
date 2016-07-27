package com.pierocheng.gugu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.pierocheng.gugu.util.CircleImageView;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;

/**
 * Created by PieroCheng on 2016/5/11.
 */
public class HomePage extends Activity implements View.OnClickListener {
    public final static String SER_KEY = "com.PieroCheng.ser";
    public final static String SER_REG = "com.PieroCheng.reg";
    public final static String SER_NAME = "com.PieroCheng.name";
    private String IDSER;
    private Boolean ISREG10, ISREG1, ISREG2, ISREG3, ISREG4, ISREG5, ISREG6, ISREG7, ISREG8, ISREG9;

    private Intent intent;
    private Bundle mBundle;
    private Bundle mBundle1;
    private Bundle mBundle2;

    private CircleImageView avatar1, avatar2, avatar3, avatar4, avatar5, avatar6, avatar7, avatar8, avatar9, avatar10;//头像
    private ImageView lock1, lock2, lock3, lock4, lock5, lock6, lock7, lock8, lock9, lock10;//头像
    private TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10;//文字

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        init();//用于实例化对象
        //initUser();
        /** 为了实现类似游戏存档的效果（有则不为空，没有则为空，但仍占他自己位置），故不使用ListView 和 Adapter **/
        /** 若今后有更大的存储需求，则改为用listView显示 **/
        Queue<String> queue = new LinkedList<String>();//用队列结构来定义10个ID
        queue.offer("33394e3ede");
        queue.offer("roElAAAJ");
        queue.offer("ReZVKKKP");
        queue.offer("NBV8YYYm");
        queue.offer("iZuLTTTU");
        queue.offer("kXgdWWWX");
        queue.offer("HuAXDDDK");
        queue.offer("GNqT999A");
        queue.offer("SdXDkkkF");
        queue.offer("PvG1gggz");

        int i;
        for (i = 1; i <= 10; i++) {
            initUser(queue.poll(), i);
        }

    }

    private void initUser(String poll, final int i) {
        BmobQuery<UserBmob> query = new BmobQuery<UserBmob>();//用于查询是否有该用户
        query.getObject(this, poll, new GetListener<UserBmob>() {
            @Override
            public void onSuccess(UserBmob userBmob) {
                IDSER = userBmob.getObjectId();
                if (!userBmob.getNameBmob().equals("nul")) {
                    DisplayImageOptions options = new DisplayImageOptions.Builder().
                            cacheInMemory(true)
                            .cacheOnDisc(true)
                            .bitmapConfig(Bitmap.Config.RGB_565)
                            .build();
                    switch (i) {
                        case 1:
                            ISREG1 = false;
                            tv1.setText(userBmob.getNameBmob());
                            lock1.setImageDrawable(getResources().getDrawable(R.drawable.lock_user));
                            ImageLoader.getInstance().displayImage(userBmob.getImgURL(), avatar1, options);
                            break;
                        case 2:
                            ISREG2 = false;
                            tv2.setText(userBmob.getNameBmob());
                            lock2.setImageDrawable(getResources().getDrawable(R.drawable.lock_user));
                            ImageLoader.getInstance().displayImage(userBmob.getImgURL(), avatar2, options);
                            break;
                        case 3:
                            ISREG3 = false;
                            tv3.setText(userBmob.getNameBmob());
                            lock3.setImageDrawable(getResources().getDrawable(R.drawable.lock_user));
                            ImageLoader.getInstance().displayImage(userBmob.getImgURL(), avatar3, options);
                            break;
                        case 4:
                            ISREG4 = false;
                            tv4.setText(userBmob.getNameBmob());
                            lock4.setImageDrawable(getResources().getDrawable(R.drawable.lock_user));
                            ImageLoader.getInstance().displayImage(userBmob.getImgURL(), avatar4, options);
                            break;
                        case 5:
                            ISREG5 = false;
                            tv5.setText(userBmob.getNameBmob());
                            lock5.setImageDrawable(getResources().getDrawable(R.drawable.lock_user));
                            ImageLoader.getInstance().displayImage(userBmob.getImgURL(), avatar5, options);
                            break;
                        case 6:
                            ISREG6 = false;
                            tv6.setText(userBmob.getNameBmob());
                            lock6.setImageDrawable(getResources().getDrawable(R.drawable.lock_user));
                            ImageLoader.getInstance().displayImage(userBmob.getImgURL(), avatar6, options);
                            break;
                        case 7:
                            ISREG7 = false;
                            tv7.setText(userBmob.getNameBmob());
                            lock7.setImageDrawable(getResources().getDrawable(R.drawable.lock_user));
                            ImageLoader.getInstance().displayImage(userBmob.getImgURL(), avatar7, options);
                            break;
                        case 8:
                            ISREG8 = false;
                            tv8.setText(userBmob.getNameBmob());
                            lock8.setImageDrawable(getResources().getDrawable(R.drawable.lock_user));
                            ImageLoader.getInstance().displayImage(userBmob.getImgURL(), avatar8, options);
                            break;
                        case 9:
                            ISREG9 = false;
                            tv9.setText(userBmob.getNameBmob());
                            lock9.setImageDrawable(getResources().getDrawable(R.drawable.lock_user));
                            ImageLoader.getInstance().displayImage(userBmob.getImgURL(), avatar9, options);
                            break;
                        case 10:
                            ISREG10 = false;
                            tv10.setText(userBmob.getNameBmob());
                            lock10.setImageDrawable(getResources().getDrawable(R.drawable.lock_user));
                            ImageLoader.getInstance().displayImage(userBmob.getImgURL(), avatar10, options);
                            break;
                    }
                } else {
                    switch (i) {
                        case 1:
                            ISREG1 = true;
                            break;
                        case 2:
                            ISREG2 = true;
                            break;
                        case 3:
                            ISREG3 = true;
                            break;
                        case 4:
                            ISREG4 = true;
                            break;
                        case 5:
                            ISREG5 = true;
                            break;
                        case 6:
                            ISREG6 = true;
                            break;
                        case 7:
                            ISREG7 = true;
                            break;
                        case 8:
                            ISREG8 = true;
                            break;
                        case 9:
                            ISREG9 = true;
                            break;
                        case 10:
                            ISREG10 = true;
                            break;
                    }
                }
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(HomePage.this, "请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        mBundle = new Bundle();
        mBundle1 = new Bundle();
        mBundle2 = new Bundle();
        switch (v.getId()) {
            case R.id.base_user1:
                mBundle.putSerializable(SER_KEY, "33394e3ede");
                if (ISREG1) {
                    intent = new Intent(HomePage.this, RegName.class);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }else {
                    mBundle1.putSerializable(SER_NAME,tv1.getText().toString());
                    mBundle2.putSerializable(SER_REG,ISREG1);
                    intent = new Intent(HomePage.this,Touch.class);
                    intent.putExtras(mBundle);
                    intent.putExtras(mBundle1);
                    intent.putExtras(mBundle2);
                    startActivity(intent);
                }
                break;
            case R.id.base_user2:
                mBundle.putSerializable(SER_KEY, "roElAAAJ");
                if (ISREG2) {
                    intent = new Intent(HomePage.this, RegName.class);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }else {
                    mBundle1.putSerializable(SER_NAME,tv2.getText().toString());
                    intent = new Intent(HomePage.this,Touch.class);
                    mBundle2.putSerializable(SER_REG,ISREG2);
                    intent.putExtras(mBundle);
                    intent.putExtras(mBundle1);
                    intent.putExtras(mBundle2);
                    startActivity(intent);
                }
                break;
            case R.id.base_user3:
                mBundle.putSerializable(SER_KEY, "ReZVKKKP");
                if (ISREG3) {
                    intent = new Intent(HomePage.this, RegName.class);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }else {
                    mBundle1.putSerializable(SER_NAME,tv3.getText().toString());
                    mBundle2.putSerializable(SER_REG,ISREG3);
                    intent = new Intent(HomePage.this,Touch.class);
                    intent.putExtras(mBundle);
                    intent.putExtras(mBundle1);
                    intent.putExtras(mBundle2);
                    startActivity(intent);
                }
                break;
            case R.id.base_user4:
                mBundle.putSerializable(SER_KEY, "NBV8YYYm");
                if (ISREG4) {
                    intent = new Intent(HomePage.this, RegName.class);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }else {
                    mBundle1.putSerializable(SER_NAME,tv4.getText().toString());
                    mBundle2.putSerializable(SER_REG,ISREG4);
                    intent = new Intent(HomePage.this,Touch.class);
                    intent.putExtras(mBundle);
                    intent.putExtras(mBundle1);
                    intent.putExtras(mBundle2);
                    startActivity(intent);
                }
                break;
            case R.id.base_user5:
                mBundle.putSerializable(SER_KEY, "iZuLTTTU");
                if (ISREG5) {
                    intent = new Intent(HomePage.this, RegName.class);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }else {
                    mBundle1.putSerializable(SER_NAME,tv5.getText().toString());
                    mBundle2.putSerializable(SER_REG,ISREG5);
                    intent = new Intent(HomePage.this,Touch.class);
                    intent.putExtras(mBundle);
                    intent.putExtras(mBundle1);
                    intent.putExtras(mBundle2);
                    startActivity(intent);
                }
                break;
            case R.id.base_user6:
                mBundle.putSerializable(SER_KEY, "kXgdWWWX");
                if (ISREG6) {
                    intent = new Intent(HomePage.this, RegName.class);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }else {
                    mBundle1.putSerializable(SER_NAME,tv6.getText().toString());
                    mBundle2.putSerializable(SER_REG,ISREG6);
                    intent = new Intent(HomePage.this,Touch.class);
                    intent.putExtras(mBundle);
                    intent.putExtras(mBundle1);
                    intent.putExtras(mBundle2);
                    startActivity(intent);
                }
                break;
            case R.id.base_user7:
                mBundle.putSerializable(SER_KEY, "HuAXDDDK");
                if (ISREG7) {
                    intent = new Intent(HomePage.this, RegName.class);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }else {
                    mBundle1.putSerializable(SER_NAME,tv7.getText().toString());
                    mBundle2.putSerializable(SER_REG,ISREG7);
                    intent = new Intent(HomePage.this,Touch.class);
                    intent.putExtras(mBundle);
                    intent.putExtras(mBundle1);
                    intent.putExtras(mBundle2);
                    startActivity(intent);
                }
                break;
            case R.id.base_user8:
                mBundle.putSerializable(SER_KEY, "GNqT999A");
                if (ISREG8) {
                    intent = new Intent(HomePage.this, RegName.class);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }else {
                    mBundle1.putSerializable(SER_NAME,tv8.getText().toString());
                    mBundle2.putSerializable(SER_REG,ISREG8);
                    intent = new Intent(HomePage.this,Touch.class);
                    intent.putExtras(mBundle);
                    intent.putExtras(mBundle1);
                    intent.putExtras(mBundle2);
                    startActivity(intent);
                }
                break;
            case R.id.base_user9:
                mBundle.putSerializable(SER_KEY, "SdXDkkkF");
                if (ISREG9) {
                    intent = new Intent(HomePage.this, RegName.class);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }else {
                    mBundle1.putSerializable(SER_NAME,tv9.getText().toString());
                    mBundle2.putSerializable(SER_REG,ISREG9);
                    intent = new Intent(HomePage.this,Touch.class);
                    intent.putExtras(mBundle);
                    intent.putExtras(mBundle1);
                    intent.putExtras(mBundle2);
                    startActivity(intent);
                }
                break;
            case R.id.base_user10:
                mBundle.putSerializable(SER_KEY, "PvG1gggz");
                if (ISREG10) {
                    intent = new Intent(HomePage.this, RegName.class);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }else {
                    mBundle1.putSerializable(SER_NAME,tv10.getText().toString());
                    mBundle2.putSerializable(SER_REG,ISREG10);
                    intent = new Intent(HomePage.this,Touch.class);
                    intent.putExtras(mBundle);
                    intent.putExtras(mBundle1);
                    intent.putExtras(mBundle2);
                    startActivity(intent);
                }
                break;
        }
    }

    private void init() {
        avatar1 = (CircleImageView) findViewById(R.id.avatar1);
        avatar2 = (CircleImageView) findViewById(R.id.avatar2);
        avatar3 = (CircleImageView) findViewById(R.id.avatar3);
        avatar4 = (CircleImageView) findViewById(R.id.avatar4);
        avatar5 = (CircleImageView) findViewById(R.id.avatar5);
        avatar6 = (CircleImageView) findViewById(R.id.avatar6);
        avatar7 = (CircleImageView) findViewById(R.id.avatar7);
        avatar8 = (CircleImageView) findViewById(R.id.avatar8);
        avatar9 = (CircleImageView) findViewById(R.id.avatar9);
        avatar10 = (CircleImageView) findViewById(R.id.avatar10);

        lock1 = (ImageView) findViewById(R.id.lock_user1);
        lock2 = (ImageView) findViewById(R.id.lock_user2);
        lock3 = (ImageView) findViewById(R.id.lock_user3);
        lock4 = (ImageView) findViewById(R.id.lock_user4);
        lock5 = (ImageView) findViewById(R.id.lock_user5);
        lock6 = (ImageView) findViewById(R.id.lock_user6);
        lock7 = (ImageView) findViewById(R.id.lock_user7);
        lock8 = (ImageView) findViewById(R.id.lock_user8);
        lock9 = (ImageView) findViewById(R.id.lock_user9);
        lock10 = (ImageView) findViewById(R.id.lock_user10);

        tv1 = (TextView) findViewById(R.id.name_user1);
        tv2 = (TextView) findViewById(R.id.name_user2);
        tv3 = (TextView) findViewById(R.id.name_user3);
        tv4 = (TextView) findViewById(R.id.name_user4);
        tv5 = (TextView) findViewById(R.id.name_user5);
        tv6 = (TextView) findViewById(R.id.name_user6);
        tv7 = (TextView) findViewById(R.id.name_user7);
        tv8 = (TextView) findViewById(R.id.name_user8);
        tv9 = (TextView) findViewById(R.id.name_user9);
        tv10 = (TextView) findViewById(R.id.name_user10);

        findViewById(R.id.base_user1).setOnClickListener(this);
        findViewById(R.id.base_user2).setOnClickListener(this);
        findViewById(R.id.base_user3).setOnClickListener(this);
        findViewById(R.id.base_user4).setOnClickListener(this);
        findViewById(R.id.base_user5).setOnClickListener(this);
        findViewById(R.id.base_user6).setOnClickListener(this);
        findViewById(R.id.base_user7).setOnClickListener(this);
        findViewById(R.id.base_user8).setOnClickListener(this);
        findViewById(R.id.base_user9).setOnClickListener(this);
        findViewById(R.id.base_user10).setOnClickListener(this);
    }
}
