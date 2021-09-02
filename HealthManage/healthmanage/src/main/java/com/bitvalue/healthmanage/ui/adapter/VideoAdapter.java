package com.bitvalue.healthmanage.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppAdapter;
import com.bitvalue.healthmanage.http.response.ArticleBean;
import com.bitvalue.healthmanage.http.response.VideoResultBean;

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

        private final TextView tv_name;

        private ViewHolder() {
            super(R.layout.item_add_video);
//            tv_use_status = findViewById(R.id.tv_use_status);
            tv_name = findViewById(R.id.tv_name);
        }

        @Override
        public void onBindView(int position) {
            paperBean = getItem(position);
            tv_name.setText(paperBean.title);
        }
    }
}
