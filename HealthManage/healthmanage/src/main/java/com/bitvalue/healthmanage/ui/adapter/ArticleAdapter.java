package com.bitvalue.healthmanage.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.healthmanage.base.AppAdapter;
import com.bitvalue.healthmanage.util.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppApplication;
import com.bitvalue.healthmanage.http.bean.ArticleBean;


/***
 * 健康信息-添加文章-----文章选择Adapter
 */
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

        private ViewHolder() {
            super(R.layout.item_article);
            tv_name = findViewById(R.id.tv_name);
            tv_use_status = findViewById(R.id.tv_use_status);
        }

        @Override
        public void onBindView(int position) {
            articleBean = getItem(position);
            tv_name.setText(articleBean.title);
            tv_use_status.setOnClickListener(v -> {
                AppApplication.instance().getHomeActivity().switchSecondFragment(Constants.FRAGMENT_ARTICLE_DETAIL, getData().get(position));
//                    AppApplication.instance().getHomeActivity().switchSecondFragment(Constants.FRAGMENT_ARTICLE_DETAIL, articleBean);
            });
        }
    }
}
