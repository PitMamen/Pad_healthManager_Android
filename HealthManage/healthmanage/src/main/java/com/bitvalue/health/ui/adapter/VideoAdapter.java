package com.bitvalue.health.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.health.api.responsebean.VideoResultBean;
import com.bitvalue.health.base.AppAdapter;
import com.bitvalue.health.ui.activity.VideoPlayActivity;
import com.bitvalue.healthmanage.R;

public class VideoAdapter extends AppAdapter<VideoResultBean.ListDTO> {

    private VideoResultBean.ListDTO paperBean;

    public VideoAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends AppAdapter<VideoResultBean.ListDTO>.ViewHolder {

        private final TextView tv_name, tv_use_status;

        private ViewHolder() {
            super(R.layout.item_add_video);
//            tv_use_status = findViewById(R.id.tv_use_status);
            tv_name = findViewById(R.id.tv_name);
            tv_use_status = findViewById(R.id.tv_use_status);
        }

        @Override
        public void onBindView(int position) {
            paperBean = getItem(position);
            tv_name.setText(paperBean.title);
            tv_use_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new VideoPlayActivity.Builder()
                            .setVideoTitle(getData().get(position).title)
                            .setVideoSource(getData().get(position).vedioUrl)
                            .start(getContext());
                }
            });
        }
    }
}
