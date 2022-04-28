package com.bitvalue.health.util.customview;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bitvalue.healthmanage.R;

/**
 * @author created by bitvalue
 * @data : 04/27
 *
 * 问诊小结 dialog
 */
public class SummaryDialog extends Dialog {
    public EditText ed_input;
    private TextView btn_comfirm;
    private TextView btn_cancel;
    private TextView tv_docName;
    private TextView tv_summary_time;
    private LinearLayout ll_bottom_button;
    private OnButtonClickListener onClickBottomListener;
    private Context mContext;


    public SummaryDialog(@NonNull Context context) {
        super(context);
        mContext = context;
        setCancelable(true);
    }

    public SummaryDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SummaryDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_summary_layout);
        setCanceledOnTouchOutside(true);
        initView();
        initClicklisenter();
        this.setOnDismissListener(dialog -> {    //当前dialog 一旦隐藏 则隐藏键盘
            // TODO Auto-generated method stub
            InputMethodManager inputMgr = (InputMethodManager) mContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMgr.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
        });
    }


    public void setDocNameAndSummaryTime(String docName, String time) {
        this.tv_docName.setText(docName);
        this.tv_summary_time.setText(time);
    }

    public void setEditeTextString(String data) {
        ed_input.setText(String.valueOf(data));
        ed_input.setTextColor(mContext.getColor(R.color.black60));
        ed_input.setEnabled(false); //不可编辑
        ed_input.setFocusableInTouchMode(false);
    }

    public void setVisibleBotomButton(boolean show) {
        ll_bottom_button.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }


    public void showKeyboard() {
        if (ed_input != null) {
            //设置可获得焦点
            ed_input.setFocusable(true);
            ed_input.setFocusableInTouchMode(true);
            //请求获得焦点
            ed_input.requestFocus();
            //调用系统输入法
            InputMethodManager inputManager = (InputMethodManager) ed_input
                    .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(ed_input, 0);
        }
    }


    private void initView() {
        ed_input = findViewById(R.id.ed_input);
        btn_comfirm = findViewById(R.id.btn_comfirm);
        btn_cancel = findViewById(R.id.btn_cancel);
        tv_docName = findViewById(R.id.tv_docname);
        tv_summary_time = findViewById(R.id.tv_summray_time);
        ll_bottom_button = findViewById(R.id.ll_bottom_button);
    }

    private void initClicklisenter() {
        btn_comfirm.setOnClickListener(v -> {
            if (onClickBottomListener != null) {
                onClickBottomListener.onPositiveClick();
            }
        });

        btn_cancel.setOnClickListener(v -> {
            if (onClickBottomListener != null) {
                onClickBottomListener.onNegtiveClick();
            }
        });
    }

    public String getInputString() {
        return ed_input.getText().toString();
    }


    public SummaryDialog setOnclickListener(OnButtonClickListener onclickListener) {
        this.onClickBottomListener = onclickListener;
        return this;
    }




    public interface OnButtonClickListener {
        /**
         * 点击确定按钮事件
         */
        void onPositiveClick();

        /**
         * 点击取消按钮事件
         */
        void onNegtiveClick();
    }

}
