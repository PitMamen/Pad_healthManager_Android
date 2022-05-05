package com.bitvalue.health.util.customview.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bitvalue.healthmanage.R;

/**
 * @author created by bitvalue
 * @data : 04/28
 * 编辑自定义快捷用语 dialog
 */
public class QuickReplyDialog extends Dialog {
  public EditText ed_input;
  private TextView btn_comfirm;
  private TextView btn_cancel;
  private QuickReplyDialog.OnButtonClickListener onClickBottomListener;
  private Context mContext;

  public QuickReplyDialog(@NonNull Context context) {
    super(context);
    mContext = context;
    setCancelable(true);
  }

  public QuickReplyDialog(@NonNull Context context, int themeResId) {
    super(context, themeResId);
  }

  protected QuickReplyDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
    super(context, cancelable, cancelListener);
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_custom_reply_layout);
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



  private void initView() {
    ed_input = findViewById(R.id.ed_input);
    btn_comfirm = findViewById(R.id.btn_comfirm);
    btn_cancel = findViewById(R.id.btn_cancel);
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


  public QuickReplyDialog setOnclickListener(QuickReplyDialog.OnButtonClickListener onclickListener) {
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
