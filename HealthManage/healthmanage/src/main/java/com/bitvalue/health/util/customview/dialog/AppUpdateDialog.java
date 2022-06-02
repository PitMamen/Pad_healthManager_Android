package com.bitvalue.health.util.customview.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.health.util.Constants;
import com.bitvalue.healthmanage.R;

/**
 * @author created by bitvalue
 * @data : 05/24
 *  app升级提醒dialog
 */
public class AppUpdateDialog extends Dialog {
    protected Context context;
    private TextView tv_newVersion, tv_update_content, tv_issuetime, txt_negative, txt_positive;
    private AppUpdateDialog.OnExecuteClickListener onExecuteClickListener;

    public AppUpdateDialog.OnExecuteClickListener getOnExecuteClickListener() {
        return onExecuteClickListener;
    }

    public AppUpdateDialog setOnExecuteClickListener(OnExecuteClickListener onExecuteClickListener) {
        this.onExecuteClickListener = onExecuteClickListener;
        return this;
    }


    public void setUpdateImformation(String newVersion, String issueTime, String update_content) {
        tv_issuetime.setText("发布时间:" + issueTime);
        tv_update_content.setText(update_content);
        tv_newVersion.setText("新版本:" + newVersion);
    }


    public interface OnExecuteClickListener {
        void onNegativeClick();

        void onPositiveClick();
    }

    public AppUpdateDialog(@NonNull Context context) {
        super(context, R.style.DialogExecutor);
        initViews();
    }


    public void initViews() {
        setContentView(R.layout.update_dialog);
        tv_newVersion = (TextView) findViewById(R.id.tv_update_version);
        tv_update_content = (TextView) findViewById(R.id.tv_update_content);
        tv_issuetime = (TextView) findViewById(R.id.tv_issue_time);
        txt_negative = (TextView) findViewById(R.id.tv_update_close);
        txt_positive = (TextView) findViewById(R.id.tv_update_update);

        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
//        dialogWindow.setWindowAnimations(R.style.dialog_style);  //添加动画
        lp.width = Constants.screenWidth * 2 / 5; // 宽度
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);

        txt_positive.setOnClickListener(v -> {
            if (getOnExecuteClickListener() != null) {
                dismiss();
                getOnExecuteClickListener().onPositiveClick();
            }
        });

        txt_negative.setOnClickListener(v -> {
            if (getOnExecuteClickListener() != null) {
                dismiss();
                getOnExecuteClickListener().onNegativeClick();
            }
        });
    }


}
