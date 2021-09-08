package com.bitvalue.healthmanage.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppAdapter;
import com.bitvalue.healthmanage.app.AppApplication;
import com.bitvalue.healthmanage.http.response.ArticleBean;

public class SearchPatientAdapter extends AppAdapter<ArticleBean> {

    private ArticleBean articleBean;

    public SearchPatientAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends AppAdapter<ArticleBean>.ViewHolder {

        private final TextView tv_name, tv_describe;

        private ViewHolder() {
            super(R.layout.item_patient_log);
            tv_name = findViewById(R.id.tv_name);
            tv_describe = findViewById(R.id.tv_describe);
        }

        @Override
        public void onBindView(int position) {
            articleBean = getItem(position);
            tv_name.setText(articleBean.title);
//            tv_use_status.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    AppApplication.instance().getHomeActivity().switchSecondFragment(Constants.FRAGMENT_ARTICLE_DETAIL, getData().get(position));
//                    AppApplication.instance().getHomeActivity().switchSecondFragment(Constants.FRAGMENT_ARTICLE_DETAIL, articleBean);
//                }
//            });
        }
    }
}
