package com.bitvalue.health.ui.adapter;

import android.view.View;

import androidx.annotation.Nullable;

import com.bitvalue.health.api.requestbean.QuestionResultBean;
import com.bitvalue.health.api.responsebean.AnswerResultBean;
import com.bitvalue.healthmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 05/13
 * 问诊收集 adapter
 */
public class PrePiagnosisCollectionAdapter extends BaseQuickAdapter<QuestionResultBean.ListDTO, BaseViewHolder> {
  public PrePiagnosisCollectionAdapter(int layoutResId, @Nullable List<QuestionResultBean.ListDTO> data) {
    super(layoutResId, data);
  }

  @Override
  protected void convert(BaseViewHolder helper, QuestionResultBean.ListDTO item) {
    if (item == null) {
      return;
    }

    helper.getView(R.id.tv_time).setVisibility(View.GONE);
    helper.getView(R.id.tv_status).setVisibility(View.GONE);
//    if (item.name.contains("-")) {
//      String[] name = item.name.split("-");
//      helper.setText(R.id.tv_right_name,  name[1] );
//    } else {
//      helper.setText(R.id.tv_right_name, item.name);
//    }
    helper.setText(R.id.tv_right_name, item.name);

  }
}
