package com.bitvalue.health.util.customview.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bitvalue.health.api.requestbean.SummaryBean;
import com.bitvalue.health.api.responsebean.DataReViewRecordResponse;
import com.bitvalue.health.ui.adapter.MedicalRecordAdapter;
import com.bitvalue.health.util.InputMethodUtils;
import com.bitvalue.health.util.customview.WrapRecyclerView;
import com.bitvalue.healthmanage.R;
import java.util.List;

/**
 * @author created by bitvalue
 * @data : 05/13
 * 病历夹 dialog
 */
public class MedicalRecordFolderDialog extends Dialog {
    private Context mcontext;
    private WrapRecyclerView wrapRecyclerViewList;
    private List<SummaryBean> datas;
    private MedicalRecordAdapter adapter;

    public MedicalRecordFolderDialog(@NonNull Context context) {
        super(context);
        mcontext = context;
        setCancelable(true);
    }

    public MedicalRecordFolderDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MedicalRecordFolderDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_medicalrecordfolder_layout);
        setCanceledOnTouchOutside(true);
        initView();
        this.setOnDismissListener(dialog -> {    //当前dialog 一旦隐藏 则隐藏键盘
            // TODO Auto-generated method stub
            InputMethodUtils.hideSoftInput(mcontext);
        });
    }


    private void initView() {
        wrapRecyclerViewList = findViewById(R.id.list_medicalfolder);
        LinearLayoutManager datareviewrecordlayoutManager = new LinearLayoutManager(mcontext);
        wrapRecyclerViewList.setLayoutManager(datareviewrecordlayoutManager);
        adapter = new MedicalRecordAdapter(R.layout.item_medicalrecord_folder_layout, datas);
        wrapRecyclerViewList.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            if (onItemClickLisenner != null) {
                onItemClickLisenner.OnClickPosition(datas.get(position));
            }
            dismiss();
        });
    }

    public void setData(List<SummaryBean> listData) {
        datas = listData;
        if (datas != null && datas.size() > 0) {
            adapter.setNewData(datas);
        }
    }

    private OnItemClickLisenner onItemClickLisenner;

    public MedicalRecordFolderDialog setItemClickLisenner(OnItemClickLisenner lisenner) {
        this.onItemClickLisenner = lisenner;
        return this;
    }


    public interface OnItemClickLisenner {
        void OnClickPosition(SummaryBean position);
    }

}
