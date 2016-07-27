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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.FaceRequest;
import com.iflytek.cloud.RequestListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.pierocheng.gugu.util.DisplayUtil;
import com.pierocheng.gugu.camera.CameraInterface;
import com.pierocheng.gugu.camera.preview.CameraSurfaceView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;


/**
 * Created by PieroCheng on 2016/5/9.
 */
public class Capture extends Activity implements CameraInterface.CamOpenOverCallback, View.OnClickListener {
    public final static String SER_NAME = "com.PieroCheng.NAME";
    public final static String SER_KEY = "com.PieroCheng.ser";

    private Intent intent;
    private Bundle mBundle, mBundle1;
    private File file;
    private String name;
    private SurfaceHolder holder;

    private int ISCAPTRUE = 0;
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
        setContentView(R.layout.capture);
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setMessage("抬头，收下巴，来笑一个！");
        builder.setPositiveButton("", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                //设置你的操作事项
            }
        });
        builder.createDialog().show();

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
                CameraInterface.getInstance().doOpenCamera(Capture.this);
            }
        };

        openThread.start();
        setContentView(R.layout.capture);
        initUI();
        initViewParams();

        shutterBtn.setOnClickListener(this);
    }

    private void initUI() {
        surfaceView = (CameraSurfaceView) findViewById(R.id.camera_surfaceview);
        shutterBtn = (ImageButton) findViewById(R.id.btn_shutter);
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

    @Override
    public void cameraHasOpened() {
        holder = surfaceView.getSurfaceHolder();
        CameraInterface.getInstance().doStartPreview(holder, previewRate);
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
            public void onClick(DialogInterface dialog, int which) {
                String path = initPathForFind();//找到该文件夹
                String filepath = path + "/1.jpg";//找到该文件
                file = new File(filepath);//用于检测文件是否存在和之后上传至Bmob
                if (file.exists()) {
                    Bitmap bm = BitmapFactory.decodeFile(filepath);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    //可根据流量及网络状况对图片进行压缩
                    bm.compress(Bitmap.CompressFormat.PNG, 30, baos);
                    mImageData = baos.toByteArray();
                    /** 接下来直接向网上注册该用户 **/
                    name = (String) getIntent().getSerializableExtra(FingerAreaActivity.SER_NAME);
                    regOnline(name);//向讯飞上传图片
                    saveToBmob();//向Bmob上传到现在所缓存的数据,以及后续的操作
                } else showTip("没有找到该图片");
            }
        });


        builder.createDialogCmarea().show();
    }

    private void saveToBmob() {
        final BmobFile bmobfile = new BmobFile(file);//先将图像上传至Bmob服务器
        bmobfile.uploadblock(this, new UploadFileListener() {
            @Override
            public void onSuccess() {
                UserBmob userbmob = new UserBmob();
                userbmob.setNameBmob((String) getIntent().getSerializableExtra(FingerAreaActivity.SER_NAME));
                userbmob.setAddressBmob((String) getIntent().getSerializableExtra(FingerAreaActivity.SER_ADDRESS));
                userbmob.setComboBmobREG((Integer) getIntent().getSerializableExtra(FingerAreaActivity.SER_COUNTREG));//上传连续点击次数
                userbmob.setFingerAeraBmobREG((Float) getIntent().getSerializableExtra(FingerAreaActivity.SER_FINREG));//上传指纹的最大面积
                userbmob.setImgURL(bmobfile.getFileUrl(Capture.this));//关联上传图像和该用户的关系

                userbmob.save(Capture.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        deleteFile();//由于是静态赋给CameraInterface存储目录，所以为了防止内存溢出，我们要将缓存图片删除
                        intent = new Intent(Capture.this, Isvresactivity.class);
                        mBundle = new Bundle();
                        mBundle1 = new Bundle();
                        mBundle.putSerializable(SER_KEY, getIntent().getSerializableExtra(FingerAreaActivity.SER_KEY));
                        mBundle1.putSerializable(SER_NAME, name);
                        intent.putExtras(mBundle);
                        intent.putExtras(mBundle1);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(Capture.this, "网络错误，请检查！2", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(Capture.this, "网络错误，请检查！1", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteFile() {
        String path = initPathForFind();//找到该文件夹
        String filepath = path + "/1.jpg";//找到该文件
        File file = new File(filepath);//用于检测文件是否存在
        if (file.exists()) {
            file.delete();
        }
    }


    public void regOnline(String name) {
        String mAuthid = name;
        if (TextUtils.isEmpty(mAuthid))
            Toast.makeText(this, "用户id不能为空", Toast.LENGTH_LONG).show();
        if (null != mImageData) {
            mProDialog.setMessage("注册中...");
            mProDialog.show();
            // 设置用户标识，格式为6-18个字符（由字母、数字、下划线组成，不得以数字开头，不能包含空格）。
            // 当不设置时，云端将使用用户设备的设备ID来标识终端用户。
            mFaceRequest.setParameter(SpeechConstant.AUTH_ID, mAuthid);
            mFaceRequest.setParameter(SpeechConstant.WFR_SST, "reg");
            mFaceRequest.sendRequest(mImageData, mRequestListener);
        } else {
            Toast.makeText(this, "请选择图片后再注册", Toast.LENGTH_LONG).show();
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
                if ("reg".equals(type)) {
                    register(object);
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

            if (error != null) {
                switch (error.getErrorCode()) {
                    case ErrorCode.MSP_ERROR_ALREADY_EXIST:
                        showTip("authid已经被注册，请更换后再试");
                        break;

                    default:
                        showTip(error.getPlainDescription(true));
                        break;
                }
            }
        }
    };

    private void register(JSONObject obj) throws JSONException {
        int ret = obj.getInt("ret");
        if (ret != 0) {
            showTip("注册失败");
            return;
        }
        if ("success".equals(obj.get("rst"))) {
            showTip("注册成功");
        } else {
            showTip("注册失败");
        }
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
