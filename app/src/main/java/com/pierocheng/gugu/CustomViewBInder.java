package com.pierocheng.gugu;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Redfire on 2016/5/31.
 */
public class CustomViewBInder implements SimpleAdapter.ViewBinder {
    @Override
    public boolean setViewValue(View view, Object o, String s) {
        if (view instanceof ImageView ) {
            ImageView iv = (ImageView) view;
            DisplayImageOptions options = new DisplayImageOptions.Builder().
                    cacheInMemory(true)
                    .cacheOnDisc(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            ImageLoader.getInstance().displayImage(s, iv, options);

            return true;
        }
        return false;
    }
}
