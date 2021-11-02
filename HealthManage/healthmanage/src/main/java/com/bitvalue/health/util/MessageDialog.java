package com.bitvalue.health.util;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.healthmanage.R;

/**
 * 智慧学习通用消息dialog
 */

public class MessageDialog extends Dialog {
    protected Context context;
    private TextView txt_negative, txt_positive, txt_title, txt_message;
    private OnExecuteClickListener onExecuteClickListener;

    public OnExecuteClickListener getOnExecuteClickListener() {
        return onExecuteClickListener;
    }

    public void setOnExecuteClickListener(OnExecuteClickListener onExecuteClickListener) {
        this.onExecuteClickListener = onExecuteClickListener;
    }

    public TextView getTxt_negative() {
        return txt_negative;
    }

    public void setNegativeTxt(String txt) {
        if (txt_negative != null) {
            txt_negative.setText(txt);
        }
    }

    public void setPositiveTxt(String txt) {
        if (txt_positive != null) {
            txt_positive.setText(txt);
        }
    }

    public void setIsSingle(boolean isSingle) {
        if (isSingle) {
            txt_negative.setVisibility(View.GONE);
        }
    }

    public interface OnExecuteClickListener {
        void onNegativeClick();

        void onPositiveClick();
    }

    public MessageDialog(@NonNull Context context) {
        super(context, R.style.DialogExecutor);
        initViews();
    }

    public void setTitle(String title) {
        if (TextUtils.isEmpty(title) || (txt_title == null)) {
            return;
        }

        txt_title.setText(title);
    }

    public void setMessage(String message) {
        if (TextUtils.isEmpty(message) || (txt_message == null)) {
            return;
        }

        txt_message.setText(message);
    }

    public void initViews() {
        setContentView(R.layout.layout_message_dialog);
        txt_negative = (TextView) findViewById(R.id.txt_negative);
        txt_positive = (TextView) findViewById(R.id.txt_positive);
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_message = (TextView) findViewById(R.id.txt_message);

        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
//        dialogWindow.setWindowAnimations(R.style.aniFallStyle);  //添加动画
        lp.width = Constants.screenWidth * 2 / 5; // 宽度
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);

        txt_positive.setOnClickListener(v -> {
            if (getOnExecuteClickListener() != null) {
                getOnExecuteClickListener().onPositiveClick();
            }
        });

        txt_negative.setOnClickListener(v -> {
            if (getOnExecuteClickListener() != null) {
                getOnExecuteClickListener().onNegativeClick();
            }
        });
    }


}
