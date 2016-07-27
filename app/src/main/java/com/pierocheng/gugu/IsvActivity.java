package com.pierocheng.gugu;

import android.app.Activity;
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
 * Created by Redfire on 2016/5/10.
 */
public class IsvActivity extends Activity {
    public final static String SER_FINVER = "com.PieroCheng.FINVER";
    public final static String SER_COUNTVER = "com.PieroCheng.COUNTVER";
    public final static String SER_KEY = "com.PieroCheng.ser";
    public final static String SER_NAME = "com.PieroCheng.name";
    public final static String SER_CAMISTRUE = "com.PieroCheng.CAMISTRUE";
    public final static String SER_VOICEISTRUE = "com.PieroCheng.VOICEISTRUE";

    private Intent intent;
    private Bundle mBundle, mBundle1, mBundle2, mBundle3, mBundle4, mBundle5;
    private Boolean VOICEISTRUE = false;


    private SpeakerVerifier mVerifier;
    private String mAuthId = "test";
    private String mTextPwd = "芝麻开门";
    private Toast mToast;
    private TextView tv_isv_textpwd;
    private ImageView iv_mic, iv_mic_anim;
    private AnimationSet animationSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.isv_layout);
        mAuthId = (String) getIntent().getSerializableExtra(Verify.SER_NAME);
        tv_isv_textpwd = (TextView) findViewById(R.id.tv_isv_textpwd);
        iv_mic = (ImageView) findViewById(R.id.iv_mic);
        iv_mic_anim = (ImageView) findViewById(R.id.iv_mic_anim);
        mToast = Toast.makeText(IsvActivity.this, "", Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        animationSet = new AnimationSet(true);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        animationSet.addAnimation(rotateAnimation);


        // 首先创建SpeakerVerifier对象
        mVerifier = SpeakerVerifier.createVerifier(this, null);
        mVerifier.setParameter(SpeechConstant.PARAMS, null);
        mVerifier.setParameter(SpeechConstant.ISV_AUDIO_PATH,
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/verify.pcm");
//        mVerifier.setParameter(SpeechConstant.AUDIO_SOURCE, "" + MediaRecorder.AudioSource.VOICE_RECOGNITION);
        mVerifier = SpeakerVerifier.getVerifier();
        mVerifier.setParameter(SpeechConstant.ISV_SST, "verify");
        mVerifier.setParameter(SpeechConstant.ISV_PWDT, "" + 1);
        mVerifier.setParameter(SpeechConstant.AUTH_ID, mAuthId);
        mVerifier.setParameter(SpeechConstant.ISV_PWD, mTextPwd);


        iv_mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((TextView) findViewById(R.id.tv_isv_textpwd)).setText("请读出："
                        + mTextPwd);
                mVerifier.startListening(mVerifyListener);

            }
        });


    }

    private VerifierListener mVerifyListener = new VerifierListener() {

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前正在说话，音量大小：" + volume);
            if (volume > 0 && volume <= 7)
                iv_mic.setImageResource(R.drawable.green1_3);
            else if (volume > 7 && volume <= 14)
                iv_mic.setImageResource(R.drawable.green2_3);
            else if (volume > 14)
                iv_mic.setImageResource(R.drawable.green_full);
        }


        @Override
        public void onResult(VerifierResult result) {
            tv_isv_textpwd.setText(result.source);

            if (result.ret == 0) {
                // 验证通过
                //tv_isv_textpwd.setText("验证通过");
                VOICEISTRUE = true;
                forNextStep(VOICEISTRUE);
            } else {
                forNextStep(VOICEISTRUE);
                // 验证不通过
                /*switch (result.err) {
                    case VerifierResult.MSS_ERROR_IVP_GENERAL:
                        tv_isv_textpwd.setText("内核异常");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_TRUNCATED:
                        tv_isv_textpwd.setText("出现截幅");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_MUCH_NOISE:
                        tv_isv_textpwd.setText("太多噪音");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_UTTER_TOO_SHORT:
                        tv_isv_textpwd.setText("录音太短");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_TEXT_NOT_MATCH:
                        tv_isv_textpwd.setText("验证不通过，您所读的文本不一致");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_TOO_LOW:
                        tv_isv_textpwd.setText("音量太低");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_NO_ENOUGH_AUDIO:
                        tv_isv_textpwd.setText("音频长达不到自由说的要求");
                        break;
                    default:
                        tv_isv_textpwd.setText("验证不通过");
                        break;
                }
                */
            }
        }

        private void forNextStep(Boolean voiceistrue) {
            intent = new Intent(IsvActivity.this, Finish.class);
            mBundle = new Bundle();
            mBundle1 = new Bundle();
            mBundle2 = new Bundle();
            mBundle3 = new Bundle();
            mBundle4 = new Bundle();
            mBundle5 = new Bundle();
            mBundle.putSerializable(SER_KEY, getIntent().getSerializableExtra(Verify.SER_KEY));
            mBundle1.putSerializable(SER_NAME, getIntent().getSerializableExtra(Verify.SER_NAME));
            mBundle2.putSerializable(SER_COUNTVER, getIntent().getSerializableExtra(Verify.SER_COUNTVER));
            mBundle3.putSerializable(SER_FINVER, getIntent().getSerializableExtra(Verify.SER_FINVER));
            mBundle4.putSerializable(SER_CAMISTRUE, getIntent().getSerializableExtra(Verify.SER_CAMISTRUE));
            mBundle5.putSerializable(SER_VOICEISTRUE, VOICEISTRUE);
            intent.putExtras(mBundle);
            intent.putExtras(mBundle1);
            intent.putExtras(mBundle2);
            intent.putExtras(mBundle3);
            intent.putExtras(mBundle4);
            intent.putExtras(mBundle5);
            startActivity(intent);
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


            switch (error.getErrorCode()) {
                case ErrorCode.MSP_ERROR_NOT_FOUND:
                    tv_isv_textpwd.setText("模型不存在，请先注册");
                    break;

                default:
                    showTip("onError Code：" + error.getPlainDescription(true));
                    break;
            }
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showTip("结束说话");
            iv_mic.setImageResource(R.drawable.with_no_circle);
            iv_mic_anim.clearAnimation();


        }

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");

            iv_mic_anim.startAnimation(animationSet);

        }
    };


    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }
}
