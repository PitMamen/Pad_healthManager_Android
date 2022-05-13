package com.bitvalue.health.util.customview.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bitvalue.health.api.responsebean.AnswerResultBean;
import com.bitvalue.health.ui.adapter.PrediagnosisDataAdapter;
import com.bitvalue.health.util.InputMethodUtils;
import com.bitvalue.health.util.customview.WrapRecyclerView;
import com.bitvalue.healthmanage.R;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 05/13
 * 预诊资料 dialog
 */
public class PrediagnosisDataDialog extends Dialog {
    private Context mcontext;
    private WrapRecyclerView wrapRecyclerViewList;
    private PrediagnosisDataAdapter adapter;
    private List<AnswerResultBean.RecordsDTO> datas;
    private TextView tv_dialog_title;

    public PrediagnosisDataDialog(@NonNull Context context) {
        super(context);
        mcontext = context;
    }

    public PrediagnosisDataDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected PrediagnosisDataDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_medicalrecordfolder_layout);
        setCanceledOnTouchOutside(true);
        initView();
//        this.setOnDismissListener(dialog -> {    //当前dialog 一旦隐藏 则隐藏键盘
//            // TODO Auto-generated method stub
//            InputMethodUtils.hideSoftInput(mcontext);
//        });
    }


    private void initView() {
        wrapRecyclerViewList = findViewById(R.id.list_medicalfolder);
        tv_dialog_title = findViewById(R.id.tv_dialog_title);
        tv_dialog_title.setText("问诊小结");
        LinearLayoutManager datareviewrecordlayoutManager = new LinearLayoutManager(mcontext);
        wrapRecyclerViewList.setLayoutManager(datareviewrecordlayoutManager);
        adapter = new PrediagnosisDataAdapter(R.layout.item_medicalrecord_folder_layout, datas);
        wrapRecyclerViewList.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            if (onItemClickLisenner != null) {
                onItemClickLisenner.OnClickPosition(datas.get(position));
            }
            dismiss();
        });
    }

    public void setData(List<AnswerResultBean.RecordsDTO> listData) {
        datas = listData;
        if (datas != null && datas.size() > 0) {
            adapter.setNewData(datas);
        }
    }

    private PrediagnosisDataDialog.OnItemClickLisenner onItemClickLisenner;

    public PrediagnosisDataDialog setItemClickLisenner(PrediagnosisDataDialog.OnItemClickLisenner lisenner) {
        this.onItemClickLisenner = lisenner;
        return this;
    }


    public interface OnItemClickLisenner {
        void OnClickPosition(AnswerResultBean.RecordsDTO position);
    }


}
