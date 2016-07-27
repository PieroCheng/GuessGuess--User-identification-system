package com.pierocheng.gugu.camera;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;

import com.pierocheng.gugu.util.CamParaUtil;
import com.pierocheng.gugu.util.FileUtil;
import com.pierocheng.gugu.util.ImageUtil;
import java.io.IOException;
import java.util.List;

public class CameraInterface {
    private static final String TAG = "yanzi";
    private Camera mCamera;
    private Camera.Parameters mParams;
    private boolean isPreviewing = false;
    private float mPreviwRate = -1f;
    private static CameraInterface mCameraInterface;
    public Bitmap rotaBitmap;
    int mCurrentCamIndex = 0;


    public interface CamOpenOverCallback {
        public void cameraHasOpened();
    }

    private CameraInterface() {

    }

    public static synchronized CameraInterface getInstance() {
        if (mCameraInterface == null) {
            mCameraInterface = new CameraInterface();
        }
        return mCameraInterface;
    }

    /**camera
     * **/
    public void doOpenCamera(CamOpenOverCallback callback) {
        Log.i(TAG, "Camera open....");
        mCamera = openFrontFacingCameraGingerbread();
        Log.i(TAG, "Camera open over....");
        callback.cameraHasOpened();
    }

    /**
     * @param  holder
     * @param previewRate
     * **/
    public void doStartPreview(SurfaceHolder holder, float previewRate) {
        Log.i(TAG, "doStartPreview...");
        if (isPreviewing) {
            mCamera.stopPreview();
            return;
        }
        if (mCamera != null) {

            mParams = mCamera.getParameters();
            mParams.setPictureFormat(PixelFormat.JPEG);//�������պ�洢��ͼƬ��ʽ
            CamParaUtil.getInstance().printSupportPictureSize(mParams);
            CamParaUtil.getInstance().printSupportPreviewSize(mParams);
            //����PreviewSize��PictureSize
            Size pictureSize = CamParaUtil.getInstance().getPropPictureSize(
                    mParams.getSupportedPictureSizes(), previewRate, 800);
            mParams.setPictureSize(pictureSize.width, pictureSize.height);
            Size previewSize = CamParaUtil.getInstance().getPropPreviewSize(
                    mParams.getSupportedPreviewSizes(), previewRate, 800);
            mParams.setPreviewSize(previewSize.width, previewSize.height);

            mCamera.setDisplayOrientation(270);

            CamParaUtil.getInstance().printSupportFocusMode(mParams);
            List<String> focusModes = mParams.getSupportedFocusModes();
            if (focusModes.contains("continuous-video")) {
                mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            mCamera.setParameters(mParams);

            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();//����Ԥ��
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            isPreviewing = true;
            mPreviwRate = previewRate;

            mParams = mCamera.getParameters(); //����getһ��
            Log.i(TAG, "111:PreviewSize--With = " + mParams.getPreviewSize().width
                    + "Height = " + mParams.getPreviewSize().height);
            Log.i(TAG, "222:PictureSize--With = " + mParams.getPictureSize().width
                    + "Height = " + mParams.getPictureSize().height);
        }
    }

    public void doStopCamera() {
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            isPreviewing = false;
            mPreviwRate = -1f;
            mCamera.release();
            mCamera = null;
        }
    }


    public void doTakePicture() {
        if (isPreviewing && (mCamera != null)) {
            mCamera.takePicture(mShutterCallback, null, mJpegPictureCallback);
        }
    }

    ShutterCallback mShutterCallback = new ShutterCallback()
            //���Ű��µĻص������������ǿ����������Ʋ��š����ꡱ��֮��Ĳ�����Ĭ�ϵľ������ꡣ
    {
        public void onShutter() {
            // TODO Auto-generated method stub
            Log.i(TAG, "myShutterCallback:onShutter...");
        }
    };
    PictureCallback mRawCallback = new PictureCallback()
            // �����δѹ��ԭ���ݵĻص�,����Ϊnull
    {

        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            Log.i(TAG, "myRawCallback:onPictureTaken...");

        }
    };
    PictureCallback mJpegPictureCallback = new PictureCallback()
            //��jpegͼ�����ݵĻص�,����Ҫ��һ���ص�
    {
        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            Log.i(TAG, "myJpegCallback:onPictureTaken...");
            Bitmap b = null;
            if (null != data) {
                b = BitmapFactory.decodeByteArray(data, 0, data.length);//data���ֽ����ݣ����������λͼ
                mCamera.stopPreview();
                isPreviewing = false;
            }
            if (null != b) {
                rotaBitmap = ImageUtil.getRotateBitmap(b, 90.0f);
                FileUtil.saveBitmapForUser(rotaBitmap);
            }
            //mCamera.startPreview();
            isPreviewing = true;
        }
    };

    private Camera openFrontFacingCameraGingerbread() {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();

        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    cam = Camera.open(camIdx);
                    mCurrentCamIndex = camIdx;
                } catch (RuntimeException e) {
                    Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }

        return cam;
    }
}
