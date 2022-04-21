package com.bitvalue.health.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.requestbean.QuestionResultBean;
import com.bitvalue.health.base.AppAdapter;
import com.bitvalue.health.util.Constants;
import com.bitvalue.healthmanage.R;

public class QuestionAdapter extends AppAdapter<QuestionResultBean.ListDTO> {

    private QuestionResultBean.ListDTO questionBean;

    public QuestionAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends AppAdapter<QuestionResultBean.ListDTO>.ViewHolder {

        private final TextView tv_name,tv_use_status;
        private LinearLayout ll_preview_qeution;

        private ViewHolder() {
            super(R.layout.item_question);
            tv_name = findViewById(R.id.tv_name);
            tv_use_status = findViewById(R.id.tv_use_status);
            ll_preview_qeution = findViewById(R.id.ll_preview_qeution);
        }

        @Override
        public void onBindView(int position) {
            questionBean = getItem(position);
            tv_name.setText(questionBean.name);
            ll_preview_qeution.setOnClickListener(v -> Application.instance().getHomeActivity().switchSecondFragment(Constants.FRAGMENT_QUESTION_DETAIL,getData().get(position)));
        }
    }
}
