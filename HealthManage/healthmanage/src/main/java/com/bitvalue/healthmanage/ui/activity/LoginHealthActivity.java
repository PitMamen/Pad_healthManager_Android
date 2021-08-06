package com.bitvalue.healthmanage.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.aop.SingleClick;
import com.bitvalue.healthmanage.app.AppActivity;
//import com.bitvalue.sdk.base.VerifyHelper;
//import com.bitvalue.sdk.base.util.net.CheckCallBack;
//import com.bitvalue.sdk.base.util.sp.SharedPreManager;
import com.bitvalue.healthmanage.app.AppApplication;
import com.bitvalue.healthmanage.http.model.HttpData;
import com.bitvalue.healthmanage.http.request.LoginApi;
import com.bitvalue.healthmanage.http.response.LoginBean;
import com.bitvalue.healthmanage.util.DemoLog;
import com.bitvalue.healthmanage.util.SharedPreManager;
import com.bitvalue.healthmanage.util.signature.GenerateTestUserSig;
import com.bitvalue.sdk.collab.TUIKit;
import com.bitvalue.sdk.collab.base.IUIKitCallBack;
import com.hjq.http.EasyConfig;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;

//import butterknife.OnClick;
import okhttp3.Call;

public class LoginHealthActivity extends AppActivity {

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
                if (userId.isEmpty()) {
//                    ToastUtils.show("请输入工号");
                    Toast.makeText(this, "请输入工号", Toast.LENGTH_LONG).show();
                    return;
                }
                if (passWord.isEmpty()) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_LONG).show();
//                    ToastUtils.show("请输入密码");
                    return;
                }
                goLogin();
//                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                break;
        }
    }

    private void goLogin() {
        //登陆的时候 Authorization 先置空
        EasyConfig.getInstance().getHeaders().put("Authorization", "");
        EasyHttp.post(this)
                .api(new LoginApi()
                        .setUserName(et_work_no.getText().toString())
                        .setPassword(et_psd.getText().toString()))
                .request(new HttpCallback<HttpData<LoginBean>>(this) {

                    @Override
                    public void onStart(Call call) {
//                        mCommitView.showProgress();
                    }

                    @Override
                    public void onEnd(Call call) {
                    }

                    @Override
                    public void onSucceed(HttpData<LoginBean> data) {
                        // 更新 Token
//                        EasyConfig.getInstance().addParam("token", data.getData().getToken());
                        LoginBean loginBean = data.getData();
                        EasyConfig.getInstance().addHeader("Authorization", loginBean.getToken());
                        SharedPreManager.putString(Constants.KEY_TOKEN, loginBean.getToken());
                        SharedPreManager.putObject(Constants.KYE_USER_BEAN, loginBean);
//                        String userSig = GenerateTestUserSig.genTestUserSig(loginBean.getUser().userId + "");//要改，正常是后台计算秘钥，UserSig
                        TUIKit.login(loginBean.getAccount().user.userId + "", loginBean.getAccount().user.userSig, new IUIKitCallBack() {
                            @Override
                            public void onError(String module, final int code, final String desc) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        DemoLog.e("TUIKit.login", "登录聊天失败" + ", errCode = " + code + ", errInfo = " + desc);
                                    }
                                });
                            }

                            @Override
                            public void onSuccess(Object data) {
                                // 跳转到首页
                                // HomeActivity.start(getContext(), MeFragment.class);
                                SharedPreManager.putBoolean(Constants.KEY_IM_AUTO_LOGIN, true, AppApplication.instance());
                                startActivity(new Intent(LoginHealthActivity.this, HomeActivity.class));
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        postDelayed(() -> {
//                            mCommitView.showError(3000);
                        }, 1000);
                    }
                });
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
