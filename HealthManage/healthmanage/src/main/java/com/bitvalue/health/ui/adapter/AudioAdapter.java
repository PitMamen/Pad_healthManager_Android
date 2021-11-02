package com.bitvalue.health.ui.adapter;


import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.bitvalue.health.api.requestbean.UploadFileApi;
import com.bitvalue.health.callback.OnItemDelete;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.component.AudioPlayer;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 语音列表
 */

public class AudioAdapter extends BaseQuickAdapter<UploadFileApi, BaseViewHolder> {

    private OnItemDelete onItemDelete;
    private boolean isNoDelete;

    public AudioAdapter(@LayoutRes int layoutResId, @Nullable List<UploadFileApi> data) {
        super(layoutResId, data);
    }

    public void setIsNoDelete(boolean isNoDelete){
        this.isNoDelete = isNoDelete;
    }

    @Override
    protected void convert(BaseViewHolder holder, UploadFileApi uploadFileApi) {
        if (null == uploadFileApi) {
            return;
        }
        if (uploadFileApi.duration > 0) {
            holder.setText(R.id.tv_duration, uploadFileApi.duration / 1000 + "\"");
        } else {
            holder.setText(R.id.tv_duration, "点击播放");
        }

        ImageView audioPlayImage = holder.getView(R.id.audio_play_iv);

        LinearLayout audio_view = holder.getView(R.id.audio_view);

        audio_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AudioPlayer.getInstance().isPlaying()) {
                    AudioPlayer.getInstance().stopPlay();
                    return;
                }
                if (TextUtils.isEmpty(uploadFileApi.fileLinkUrl) && TextUtils.isEmpty(uploadFileApi.path)) {
                    ToastUtil.toastLongMessage("语音文件存在问题");
                    return;
                }
                audioPlayImage.setImageResource(com.bitvalue.sdk.collab.R.drawable.play_voice_message);
                final AnimationDrawable animationDrawable = (AnimationDrawable) audioPlayImage.getDrawable();
                animationDrawable.start();
                String playUrl = TextUtils.isEmpty(uploadFileApi.fileLinkUrl) ? uploadFileApi.path : uploadFileApi.fileLinkUrl;
                AudioPlayer.getInstance().startPlay(playUrl, new AudioPlayer.Callback() {
                    @Override
                    public void onCompletion(Boolean success) {
                        audioPlayImage.post(new Runnable() {
                            @Override
                            public void run() {
                                animationDrawable.stop();
                                audioPlayImage.setImageResource(com.bitvalue.sdk.collab.R.drawable.voice_msg_playing_3);
                            }
                        });
                    }
                });

            }
        });

        ImageView img_delete = holder.getView(R.id.img_delete);
        if (isNoDelete){
            img_delete.setVisibility(View.GONE);
        }
        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (onItemDelete != null){
                    onItemDelete.onItemDelete(position);
                }
            }
        });
    }
    public OnItemDelete getOnItemDelete() {
        return onItemDelete;
    }

    public void setOnItemDelete(OnItemDelete onItemDelete) {
        this.onItemDelete = onItemDelete;
    }
}
