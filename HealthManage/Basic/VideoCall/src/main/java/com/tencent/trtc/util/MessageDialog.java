package com.tencent.trtc.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tencent.trtc.videocall.R;

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
        if (txt_negative != null ) {
            txt_negative.setText(txt);
        }
    }

    public void setPositiveTxt(String txt) {
        if (txt_positive != null ) {
            txt_positive.setText(txt);
        }
    }

    public interface OnExecuteClickListener {
        void onNegativeClick();
        void onPositiveClick();
    }

    public MessageDialog(@NonNull Context context) {
        super(context, R.style.DialogExecutor);
        this.context = context;
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
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();

        int width;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            windowManager.getDefaultDisplay().getRealMetrics(dm);
            width= dm.widthPixels; // 宽度（PX）
        } else {
            windowManager.getDefaultDisplay().getMetrics(dm);
            width = dm.widthPixels;
        }

        lp.width = width * 2 / 5; // 宽度
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);

        txt_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MessageDialog.this.getOnExecuteClickListener() != null) {
                    MessageDialog.this.getOnExecuteClickListener().onPositiveClick();
                }
            }
        });

        txt_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MessageDialog.this.getOnExecuteClickListener() != null) {
                    MessageDialog.this.getOnExecuteClickListener().onNegativeClick();
                }
            }
        });
    }


}
