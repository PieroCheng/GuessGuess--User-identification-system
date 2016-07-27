package com.pierocheng.gugu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.SpeakerVerifier;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.VerifierListener;
import com.iflytek.cloud.VerifierResult;

/**
 * Created by Redfire on 2016/5/5.
 */
public class Isvresactivity extends Activity {
    private SpeakerVerifier mVerifier;
    private String mAuthId = "test";
    // 文本声纹密码
    private String mTextPwd = "";
    private static final int PWD_TYPE_TEXT = 1;
    private int mPwdType = PWD_TYPE_TEXT;
    private AlertDialog mTextPwdSelectDialog;
    private Toast mToast;
    private TextView mShowPwdTextView;
    private TextView mShowMsgTextView;
    private TextView mShowRegFbkTextView;
    private ImageView iv_mic_res, iv_mic_res_anim;
    private AnimationSet animationSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setMessage("点击屏幕中心按钮，读出芝麻开门");
        builder.setPositiveButton("", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
            }
        });

        builder.createDialog().show();

        mVerifier = SpeakerVerifier.createVerifier(Isvresactivity.this, null);
        iv_mic_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTextView();

// 清空参数
                mTextPwd = "芝麻开门";
                mVerifier.setParameter(SpeechConstant.PARAMS, null);
                mVerifier.setParameter(SpeechConstant.ISV_AUDIO_PATH,
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/test.pcm");
                // 对于某些麦克风非常灵敏的机器，如nexus、samsung i9300等，建议加上以下设置对录音进行消噪处理
//                mVerifier.setParameter(SpeechConstant.AUDIO_SOURCE, "" + MediaRecorder.AudioSource.VOICE_RECOGNITION);
//                if (mPwdType == PWD_TYPE_TEXT) {
//                    // 文本密码注册需要传入密码
//                    if (TextUtils.isEmpty(mTextPwd)) {
//                        showTip("请获取密码后进行操作");
//                        return;
//                    }
                mVerifier.setParameter(SpeechConstant.ISV_PWD, mTextPwd);
                mShowPwdTextView.setText("请读出：" + mTextPwd);
                mShowMsgTextView.setText("训练 第" + 1 + "遍，剩余4遍");
//                }


                // 设置auth_id，不能设置为空
                mVerifier.setParameter(SpeechConstant.AUTH_ID, mAuthId);
                // 设置业务类型为注册
                mVerifier.setParameter(SpeechConstant.ISV_SST, "train");
                // 设置声纹密码类型
                mVerifier.setParameter(SpeechConstant.ISV_PWDT, "" + mPwdType);
                // 开始注册
                mVerifier.startListening(mRegisterListener);


            }
        });
    }

    private void init() {
        mAuthId = (String) getIntent().getSerializableExtra(Capture.SER_NAME);
        setContentView(R.layout.isvres_layout);
        iv_mic_res = (ImageView) findViewById(R.id.iv_mic_res);
        iv_mic_res_anim = (ImageView) findViewById(R.id.iv_mic_res_anim);
        mShowPwdTextView = (TextView) findViewById(R.id.showPwd);
        mShowMsgTextView = (TextView) findViewById(R.id.showMsg);
        mShowRegFbkTextView = (TextView) findViewById(R.id.showRegFbk);
        mToast = Toast.makeText(Isvresactivity.this, "", Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        animationSet = new AnimationSet(true);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        animationSet.addAnimation(rotateAnimation);
    }


    private void initTextView() {
        mTextPwd = null;
        mShowPwdTextView.setText("");
        mShowMsgTextView.setText("");
        mShowRegFbkTextView.setText("");
    }

    private VerifierListener mRegisterListener = new VerifierListener() {

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前正在说话，音量大小：" + volume);
            if (volume > 0 && volume <= 7)
                iv_mic_res.setImageResource(R.drawable.green1_3);
            else if (volume > 7 && volume <= 14)
                iv_mic_res.setImageResource(R.drawable.green2_3);
            else if (volume > 14)
                iv_mic_res.setImageResource(R.drawable.green_full);

        }

        @Override
        public void onResult(VerifierResult result) {
            ((TextView) findViewById(R.id.showMsg)).setText(result.source);

            if (result.ret == ErrorCode.SUCCESS) {
                switch (result.err) {
                    case VerifierResult.MSS_ERROR_IVP_GENERAL:
                        mShowMsgTextView.setText("内核异常");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_EXTRA_RGN_SOPPORT:
                        mShowRegFbkTextView.setText("训练达到最大次数");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_TRUNCATED:
                        mShowRegFbkTextView.setText("出现截幅");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_MUCH_NOISE:
                        mShowRegFbkTextView.setText("太多噪音");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_UTTER_TOO_SHORT:
                        mShowRegFbkTextView.setText("录音太短");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_TEXT_NOT_MATCH:
                        mShowRegFbkTextView.setText("训练失败，您所读的文本不一致");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_TOO_LOW:
                        mShowRegFbkTextView.setText("音量太低");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_NO_ENOUGH_AUDIO:
                        mShowMsgTextView.setText("音频长达不到自由说的要求");
                    default:
                        mShowRegFbkTextView.setText("");
                        break;
                }

                if (result.suc == result.rgn) {

                    mShowMsgTextView.setText("注册成功");

                    Intent intent = new Intent(Isvresactivity.this, RegFinish.class);
                    startActivity(intent);
                    if (PWD_TYPE_TEXT == mPwdType) {
//                        mResultEditText.setText("您的文本密码声纹ID：\n" + result.vid);
                    }

                } else {
                    int nowTimes = result.suc + 1;
                    int leftTimes = result.rgn - nowTimes;

                    if (PWD_TYPE_TEXT == mPwdType) {
                        mShowPwdTextView.setText("请读出：" + mTextPwd);
                    }

                    mShowMsgTextView.setText("训练 第" + nowTimes + "遍，剩余" + leftTimes + "遍");
                }

            } else {


                mShowMsgTextView.setText("注册失败，请重新开始。");
            }
        }

        // 保留方法，暂不用
        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle arg3) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }

        @Override
        public void onError(SpeechError error) {

            iv_mic_res_anim.clearAnimation();
            if (error.getErrorCode() == ErrorCode.MSP_ERROR_ALREADY_EXIST) {
                showTip("模型已存在，如需重新注册，请先删除");

            } else {
                showTip("onError Code：" + error.getPlainDescription(true));
            }
        }

        @Override
        public void onEndOfSpeech() {
            showTip("结束说话");
            iv_mic_res.setImageResource(R.drawable.with_no_circle);

            iv_mic_res_anim.clearAnimation();
        }

        @Override
        public void onBeginOfSpeech() {
            showTip("开始说话");

            iv_mic_res_anim.startAnimation(animationSet);
        }
    };

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

}
