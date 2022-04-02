package com.bitvalue.health.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.responsebean.ArticleBean;
import com.bitvalue.health.base.AppAdapter;
import com.bitvalue.health.util.Constants;
import com.bitvalue.healthmanage.R;

public class ArticleAdapter extends AppAdapter<ArticleBean> {

    private ArticleBean articleBean;

    public ArticleAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends AppAdapter<ArticleBean>.ViewHolder {

        private final TextView tv_name, tv_use_status;
        private  ImageView img_goto;

        private ViewHolder() {
            super(R.layout.item_article);
            tv_name = findViewById(R.id.tv_name);
            tv_use_status = findViewById(R.id.tv_use_status);
            img_goto = findViewById(R.id.img_goto);
        }

        @Override
        public void onBindView(int position) {
            articleBean = getItem(position);
            tv_name.setText(articleBean.title);

            tv_use_status.setOnClickListener(v -> Application.instance().getHomeActivity().switchSecondFragment(Constants.FRAGMENT_ARTICLE_DETAIL, getData().get(position)));
            img_goto.setOnClickListener(v -> Application.instance().getHomeActivity().switchSecondFragment(Constants.FRAGMENT_ARTICLE_DETAIL, getData().get(position)));
        }
    }
}
