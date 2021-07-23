package com.bitvalue.healthmanage.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.core.view.GravityCompat;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.aop.SingleClick;
import com.bitvalue.healthmanage.app.AppActivity;
import com.bitvalue.sdk.base.VerifyHelper;
import com.bitvalue.sdk.base.util.net.CheckCallBack;
import com.bitvalue.sdk.base.util.sp.SharedPreManager;
import com.hjq.toast.ToastUtils;

public class LoginActivity extends AppActivity {

    private ImageView img_remember;
    private EditText et_work_no;
    private EditText et_psd;
    private boolean isRememberPwd;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        img_remember = findViewById(R.id.img_remember);
        et_work_no = findViewById(R.id.et_work_no);
        et_psd = findViewById(R.id.et_psd);
        setOnClickListener(R.id.layout_remember, R.id.btn_login);

    }

    @Override
    protected void initData() {
        isRememberPwd = SharedPreManager.getBoolean(Constants.KEY_REMEMBER_PSD, false, this);
        updateIsRememberPwdImage();
    }

    @SingleClick
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_remember:
                isRememberPwd = isRememberPwd ? false : true;
                SharedPreManager.putBoolean(Constants.KEY_REMEMBER_PSD, isRememberPwd, this);
                updateIsRememberPwdImage();
                break;

            case R.id.btn_login:
                String userId = et_work_no.getText().toString();
                String passWord = et_psd.getText().toString();
                if (userId.isEmpty()){
                    ToastUtils.show("请输入工号");
                    return;
                }
                if (passWord.isEmpty()){
                    ToastUtils.show("请输入密码");
                    return;
                }
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                break;
        }
    }

    /**
     * 切换是否记住密码图标
     *
     * @param
     */
    private void updateIsRememberPwdImage() {
        if (isRememberPwd) {
            img_remember.setImageResource(R.drawable.remember_pwd_choice);
        } else {
            img_remember.setImageResource(R.drawable.unremember_pwd_choice);
        }
    }
}
