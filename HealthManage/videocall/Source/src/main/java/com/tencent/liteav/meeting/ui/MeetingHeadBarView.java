package com.tencent.liteav.meeting.ui;

import android.content.Context;

import androidx.annotation.NonNull;

import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.liteav.demo.trtc.R;
import com.tencent.liteav.meeting.ui.utils.TimeUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 头部view
 *
 * @author guanyifeng
 */
public class MeetingHeadBarView extends RelativeLayout {
    private ImageView mHeadsetImg;
    private ImageView mCameraSwitchImg;
    private ImageView mUVCCameraImg;
    private TextView mTitleTv;
    private TextView mExitTv;
    private TextView mtv_time_down;
    private HeadBarCallback mHeadBarCallback;

    public MeetingHeadBarView(Context context) {
        this(context, null);
    }

    public MeetingHeadBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.trtcmeeting_head_bar, this);
        initView(this);
    }

    private void initView(@NonNull final View itemView) {
        mHeadsetImg = (ImageView) itemView.findViewById(R.id.img_headset);
        mCameraSwitchImg = (ImageView) itemView.findViewById(R.id.img_camera_switch);
        mUVCCameraImg = (ImageView) itemView.findViewById(R.id.img_uvccamera);
        mTitleTv = (TextView) itemView.findViewById(R.id.tv_title);
        mExitTv = (TextView) itemView.findViewById(R.id.tv_exit);
        mtv_time_down = (TextView) itemView.findViewById(R.id.tv_time_down);

        mHeadsetImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHeadBarCallback != null) {
                    mHeadBarCallback.onHeadSetClick();
                }
            }
        });

        mCameraSwitchImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHeadBarCallback != null) {
                    mHeadBarCallback.onSwitchCameraClick();
                }
            }
        });

        mUVCCameraImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHeadBarCallback != null) {
                    mHeadBarCallback.onUVCCameraClick();
                }
            }
        });

        mExitTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHeadBarCallback != null) {
                    mHeadBarCallback.onExitClick();
                }
            }
        });
    }

    public void setTitle(String text) {
        if (mTitleTv != null) {
            mTitleTv.setText(text);
        }
    }


    //倒计时显示
    public void setTimeDownCount(String time, boolean isRigthOnTimeOut) {
        if (mtv_time_down != null) {
            mtv_time_down.setText(time);
            mtv_time_down.setTextColor(getResources().getColor(isRigthOnTimeOut ? R.color.beauty_color_red : R.color.trtcmeeting_color_white));
        }
    }


    public void setHeadsetImg(boolean useSpeaker) {
        if (mHeadsetImg != null) {
            mHeadsetImg.setImageResource(useSpeaker ? R.drawable.trtcmeeting_ic_speaker : R.drawable.trtcmeeting_ic_headset);
        }
    }

    public void showUVCCamera(boolean enable) {
        if (mUVCCameraImg != null) {
            mUVCCameraImg.setVisibility(enable ? View.VISIBLE : View.GONE);
        }
    }

    public void setHeadBarCallback(HeadBarCallback headBarCallback) {
        mHeadBarCallback = headBarCallback;
    }


    public interface HeadBarCallback {
        void onHeadSetClick();

        void onSwitchCameraClick();

        void onUVCCameraClick();

        void onExitClick();
    }
}
