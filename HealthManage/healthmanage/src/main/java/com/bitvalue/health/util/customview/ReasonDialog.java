package com.bitvalue.health.util.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bitvalue.health.pickdatetime.bean.DateParams;
import com.bitvalue.healthmanage.R;

/**
 * @author created by bitvalue
 * @data : 04/14
 */
public class ReasonDialog extends Dialog {
    public EditText ed_input;
    private TextView btn_comfirm;
    private TextView btn_cancel;
    private OnClickBottomListener onClickBottomListener;

    public ReasonDialog(@NonNull Context context) {
        super(context);
    }

    public ReasonDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ReasonDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_reason_layout);
        setCanceledOnTouchOutside(false);
        initView();
        initClicklisenter();
    }

    private void initView() {
        ed_input = findViewById(R.id.ed_input);
        btn_comfirm = findViewById(R.id.btn_comfirm);
        btn_cancel = findViewById(R.id.btn_cancel);
    }


    public String getInputString() {
        return ed_input.getText().toString();
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


    public ReasonDialog setOnclickListener(ReasonDialog.OnClickBottomListener onclickListener) {
        this.onClickBottomListener = onclickListener;
        return this;
    }


    public interface OnClickBottomListener {
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
