package com.pierocheng.gugu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.iflytek.cloud.FaceRequest;
import com.iflytek.cloud.RequestListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.pierocheng.gugu.camera.CameraInterface;
import com.pierocheng.gugu.camera.preview.CameraSurfaceView;
import com.pierocheng.gugu.util.DisplayUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * Created by PieroCheng on 2016/5/9.
 */
public class Verify extends Activity implements CameraInterface.CamOpenOverCallback, View.OnClickListener {
    public final static String SER_FINVER = "com.PieroCheng.FINVER";
    public final static String SER_COUNTVER = "com.PieroCheng.COUNTVER";
    public final static String SER_KEY = "com.PieroCheng.ser";
    public final static String SER_NAME = "com.PieroCheng.name";
    public final static String SER_CAMISTRUE = "com.PieroCheng.CAMISTRUE";

    private Intent intent;
    private Bundle mBundle, mBundle1, mBundle2, mBundle3, mBundle4;

    private int ISCAPTRUE = 0;
    private File file;
    private String name;
    private Boolean CAMERAISTURE = false;

    private CameraSurfaceView surfaceView = null;
    private ImageButton shutterBtn;
    private float previewRate = -1f;
    private byte[] mImageData = null;
    private Toast mToast;
    // 进度对话框
    private ProgressDialog mProDialog;
    // FaceRequest对象，集成了人脸识别的各种功能
    private FaceRequest mFaceRequest;

    private static final File parentPath = Environment.getExternalStorageDirectory();//储存文件根目录
    private static String storagePath = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在程序入口处传入appid，初始化SDK
        //SpeechUtility.createUtility(this, "appid=" + getString(R.string.app_id));
        setContentView(R.layout.capture);
        mProDialog = new ProgressDialog(this);
        mProDialog.setCancelable(true);
        mProDialog.setTitle("请稍后");
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        mProDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                // cancel进度框时,取消正在进行的操作
                if (null != mFaceRequest) {
                    mFaceRequest.cancel();
                }
            }
        });

        mFaceRequest = new FaceRequest(this);


        Thread openThread = new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                CameraInterface.getInstance().doOpenCamera(Verify.this);
            }
        };

        openThread.start();
        setContentView(R.layout.capture);
        initUI();
        initViewParams();

    }


    private void initUI() {
        surfaceView = (CameraSurfaceView) findViewById(R.id.camera_surfaceview);
        shutterBtn = (ImageButton) findViewById(R.id.btn_shutter);

        shutterBtn.setOnClickListener(this);
    }

    private void initViewParams() {
        ViewGroup.LayoutParams params = surfaceView.getLayoutParams();
        Point p = DisplayUtil.getScreenMetrics(this);
        params.width = p.x;
        params.height = p.y;
        previewRate = DisplayUtil.getScreenRate(this); //默认全屏的比例预览
        surfaceView.setLayoutParams(params);

        //手动设置拍照ImageButton的大小为120dip×120dip,原图片大小是64×64
        ViewGroup.LayoutParams p2 = shutterBtn.getLayoutParams();
        p2.width = DisplayUtil.dip2px(this, 80);
        p2.height = DisplayUtil.dip2px(this, 80);
        shutterBtn.setLayoutParams(p2);
    }


    private void deleteFile() {
        String path = initPathForFind();//找到该文件夹
        String filepath = path + "/1.jpg";//找到该文件
        File file = new File(filepath);//用于检测文件是否存在
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    public void onClick(View v) {
        CameraInterface.getInstance().doTakePicture();
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setMessage("(ง •̀_•́)ง照的不错，决定是这张吗？");

        builder.setNegativeButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
            }
        });

        builder.setPositiveButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String path = initPathForFind();//找到该文件夹
                String filepath = path + "/1.jpg";//找到该文件
                file = new File(filepath);//用于检测文件是否存在和之后上传至Bmob
                if (file.exists()) {
                    Bitmap bm = BitmapFactory.decodeFile(filepath);
                    /**图片目前没有设置保存的功能，未来或需要加上（用于设置头像）
                     * */
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    //可根据流量及网络状况对图片进行压缩
                    bm.compress(Bitmap.CompressFormat.PNG, 30, baos);
                    mImageData = baos.toByteArray();
                    /**未来要设置时限跳转的动画效果
                     * */
                    /** 接下来直接向网上注册该用户 **/
                    name = (String) getIntent().getSerializableExtra(FingerAreaActivity.SER_NAME);
                    verifyOnline(name);
                } else showTip("没有找到该图片");
            }
        });
        builder.createDialogCmarea().show();
    }


    private void forNextStep(Boolean CAMERAISTURE) {
        intent = new Intent(Verify.this, IsvActivity.class);
        mBundle = new Bundle();
        mBundle1 = new Bundle();
        mBundle2 = new Bundle();
        mBundle3 = new Bundle();
        mBundle4 = new Bundle();
        mBundle.putSerializable(SER_KEY, getIntent().getSerializableExtra(FingerAreaActivity.SER_KEY));
        mBundle1.putSerializable(SER_NAME, getIntent().getSerializableExtra(FingerAreaActivity.SER_NAME));
        mBundle2.putSerializable(SER_COUNTVER, getIntent().getSerializableExtra(FingerAreaActivity.SER_COUNTVER));
        mBundle3.putSerializable(SER_FINVER, getIntent().getSerializableExtra(FingerAreaActivity.SER_FINVER));
        mBundle4.putSerializable(SER_CAMISTRUE, CAMERAISTURE);
        intent.putExtras(mBundle);
        intent.putExtras(mBundle1);
        intent.putExtras(mBundle2);
        intent.putExtras(mBundle3);
        intent.putExtras(mBundle4);
        startActivity(intent);
    }

    private void verifyOnline(String name) {
        String mAuthid = name;
        if (TextUtils.isEmpty(mAuthid)) {
            showTip("authid不能为空");
            return;
        }

        if (null != mImageData) {
            mProDialog.setMessage("验证中...");
            mProDialog.show();
            // 设置用户标识，格式为6-18个字符（由字母、数字、下划线组成，不得以数字开头，不能包含空格）。
            // 当不设置时，云端将使用用户设备的设备ID来标识终端用户。
            mFaceRequest.setParameter(SpeechConstant.AUTH_ID, mAuthid);
            mFaceRequest.setParameter(SpeechConstant.WFR_SST, "verify");
            mFaceRequest.sendRequest(mImageData, mRequestListener);
        } else {
            showTip("请选择图片后再验证");
        }
    }

    private void verify(JSONObject obj) throws JSONException {
        int ret = obj.getInt("ret");
        if (ret != 0) {
            showTip("请检查网络");
            return;
        }
        if ("success".equals(obj.get("rst"))) {
            if (obj.getBoolean("verf")) {
                //showTip("通过验证，欢迎回来！");
                CAMERAISTURE = true;
                deleteFile();//由于是静态赋给CameraInterface存储目录，所以为了防止内存溢出，我们要将缓存图片删除
                forNextStep(CAMERAISTURE);
            } else {
                //showTip("验证不通过");
                deleteFile();//由于是静态赋给CameraInterface存储目录，所以为了防止内存溢出，我们要将缓存图片删除
                forNextStep(CAMERAISTURE);
            }
        } else {
            showTip("检查网络");
        }
    }

    private RequestListener mRequestListener = new RequestListener() {

        @Override
        public void onEvent(int eventType, Bundle params) {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            if (null != mProDialog) {
                mProDialog.dismiss();
            }

            try {
                String result = new String(buffer, "utf-8");
                Log.d("FaceDemo", result);
                JSONObject object = new JSONObject(result);
                String type = object.optString("sst");
                if ("verify".equals(type)) {
                    verify(object);
                }
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (null != mProDialog) {
                mProDialog.dismiss();
            }
        }
    };

    @Override
    public void cameraHasOpened() {
        SurfaceHolder holder = surfaceView.getSurfaceHolder();
        CameraInterface.getInstance().doStartPreview(holder, previewRate);
    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    private static String initPathForFind() {
        if (storagePath.equals("")) {
            storagePath = parentPath.getAbsolutePath() + "/" + "gugu";
            File f = new File(storagePath);
            if (!f.exists()) {
                f.mkdir();
            }
        }
        return storagePath;
    }

    @Override
    public void finish() {
        if (null != mProDialog) {
            mProDialog.dismiss();
        }
        super.finish();
    }


}
