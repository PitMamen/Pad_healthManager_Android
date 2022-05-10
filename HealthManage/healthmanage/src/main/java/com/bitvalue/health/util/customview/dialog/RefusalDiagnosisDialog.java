package com.bitvalue.health.util.customview.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bitvalue.health.util.InputMethodUtils;
import com.bitvalue.healthmanage.R;

/**
 * @author created by bitvalue
 * @data : 05/09
 * 拒绝问诊 理由填写 dialog
 */
public class RefusalDiagnosisDialog extends Dialog {
  public EditText ed_input;
  private TextView btn_comfirm;
  private TextView btn_cancel;
  private Spinner sp_reason;
  private Context mContext;
  private String[] reasonList = new String[]{"抱歉,没有接诊时间","不对症状,请选择其他医生","建议线下就诊","其他"};  //
  private RefusalDiagnosisDialog.OnClickBottomListener onClickBottomListener;
  private String selectedReason;
  private ArrayAdapter<String> spinnerzhuanbingAdapter;

  public RefusalDiagnosisDialog(@NonNull Context context) {
    super(context);
    mContext = context;
    setCancelable(true);
  }

  public RefusalDiagnosisDialog(@NonNull Context context, int themeResId) {
    super(context, themeResId);
  }

  public RefusalDiagnosisDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
    super(context, cancelable, cancelListener);
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.refusal_of_diagnosis_layout);
    setCanceledOnTouchOutside(false);
    initView();
    initClicklisenter();
    initSpinnerSpecial();
    this.setOnDismissListener(dialog -> {    //当前dialog 一旦隐藏 则隐藏键盘
      // TODO Auto-generated method stub
      InputMethodUtils.hideSoftInput(mContext);
    });
  }


  private void initSpinnerSpecial() {
    spinnerzhuanbingAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, reasonList);
    //设置下拉列表的风格,simple_spinner_dropdown_item是android系统自带的样式，等会自己定义改动
    spinnerzhuanbingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    sp_reason.setAdapter(spinnerzhuanbingAdapter);
    //将adapter 加入到spinner中
    sp_reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedReason = reasonList[position];
        sp_reason.setSelection(position);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });
  }
  private void initView(){
    sp_reason = findViewById(R.id.sp_reason);
    ed_input = findViewById(R.id.ed_input);
    btn_comfirm = findViewById(R.id.btn_comfirm);
    btn_cancel = findViewById(R.id.btn_cancel);
  }

  public String getInputReason() {
    return ed_input.getText().toString();
  }

  public String getSelectedReason(){
    return selectedReason;
  }



  private void initClicklisenter() {
    btn_comfirm.setOnClickListener(v -> {
      if (onClickBottomListener != null) {
        this.dismiss();
        onClickBottomListener.onPositiveClick();
      }
    });

    btn_cancel.setOnClickListener(v -> {
      if (onClickBottomListener != null) {
        this.dismiss();
        onClickBottomListener.onNegtiveClick();
      }
    });
  }


  public RefusalDiagnosisDialog setOnclickListener(RefusalDiagnosisDialog.OnClickBottomListener onclickListener) {
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
