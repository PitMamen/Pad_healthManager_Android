package com.bitvalue.healthmanage.ui.activity.main;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bitvalue.healthmanage.util.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.base.AppActivity;
//import com.bitvalue.sdk.base.VerifyHelper;
//import com.bitvalue.sdk.base.util.net.CheckCallBack;
//import com.bitvalue.sdk.base.util.sp.SharedPreManager;
import com.bitvalue.healthmanage.app.AppApplication;
import com.bitvalue.healthmanage.http.model.ApiResult;
import com.bitvalue.healthmanage.http.api.LoginApi;
import com.bitvalue.healthmanage.http.bean.LoginBean;
import com.bitvalue.healthmanage.util.DemoLog;
import com.bitvalue.healthmanage.widget.manager.SharedPreManager;
import com.bitvalue.sdk.collab.TUIKit;
import com.bitvalue.sdk.collab.base.IUIKitCallBack;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.hjq.http.EasyConfig;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;

//import butterknife.OnClick;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class LoginHealthActivity extends AppActivity {

    @BindView(R.id.img_remember)
    ImageView img_remember;
    @BindView(R.id.et_work_no)
    EditText et_work_no;
    @BindView(R.id.et_psd)
    EditText et_psd;
    private boolean isRememberPwd;
    private boolean needToast;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        needToast = getIntent().getBooleanExtra(Constants.NEED_TOAST, false);
        isRememberPwd = SharedPreManager.getBoolean(Constants.KEY_REMEMBER_PSD, false, this);
        String psd = SharedPreManager.getString(Constants.KEY_PSD);
        String account = SharedPreManager.getString(Constants.KEY_ACCOUNT);
        if (null != psd && null != account && isRememberPwd) {
            et_psd.setText(psd);
            et_work_no.setText(account);
        }
        updateIsRememberPwdImage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (needToast) {
            ToastUtils.show("账号已过期，请重新登录");
        }
    }

    @OnClick({R.id.layout_remember, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_remember:
                isRememberPwd = !isRememberPwd;
                SharedPreManager.putBoolean(Constants.KEY_REMEMBER_PSD, isRememberPwd, this);
                updateIsRememberPwdImage();
                break;

            case R.id.btn_login:
                String userId = et_work_no.getText().toString();
                String passWord = et_psd.getText().toString();
                if (userId.isEmpty()) {
                    Toast.makeText(this, "请输入工号", Toast.LENGTH_LONG).show();
                    return;
                }
                if (passWord.isEmpty()) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_LONG).show();
                    return;
                }
                goLogin();
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
                .request(new HttpCallback<ApiResult<LoginBean>>(this) {

                    @Override
                    public void onStart(Call call) {
                        super.onStart(call);
                    }

                    @Override
                    public void onEnd(Call call) {
                        super.onEnd(call);
                    }

                    @Override
                    public void onSucceed(ApiResult<LoginBean> data) {
                        super.onSucceed(data);
                        if (data == null) {
                            return;
                        }
                        //10004 密码错误
                        if (data.getCode() == 10004) {
                            ToastUtil.toastShortMessage("密码错误");
                            hideDialog();
                            return;
                        }
                        if (data.getCode() == 0) {
                            LoginBean loginBean = data.getData();
                            if (loginBean != null) {
                                EasyConfig.getInstance().addHeader("Authorization", loginBean.getToken());
                                SharedPreManager.putString(Constants.KEY_TOKEN, loginBean.getToken());
                                if (isRememberPwd) {
                                    SharedPreManager.putString(Constants.KEY_PSD, et_psd.getText().toString());
                                    SharedPreManager.putString(Constants.KEY_ACCOUNT, et_work_no.getText().toString());
                                }
                                SharedPreManager.putObject(Constants.KYE_USER_BEAN, loginBean);
                                showDialog();
                                TUIKit.login(loginBean.getAccount().user.userId + "", loginBean.getAccount().user.userSig, new IUIKitCallBack() {
                                    @Override
                                    public void onError(String module, final int code, final String desc) {
                                        hideDialog();
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                DemoLog.e("TUIKit.login", "登录聊天失败" + ", errCode = " + code + ", errInfo = " + desc);
                                            }
                                        });
                                    }

                                    @Override
                                    public void onSuccess(Object data) {
                                        hideDialog();
                                        // 跳转到首页
                                        // HomeActivity.start(getContext(), MeFragment.class);
                                        SharedPreManager.putBoolean(Constants.KEY_IM_AUTO_LOGIN, true, AppApplication.instance());
                                        startActivity(new Intent(LoginHealthActivity.this, HomeActivity.class));
                                        finish();
                                    }
                                });

                            } else {
                                ToastUtil.toastShortMessage("工号或密码错误");
                                hideDialog();
                            }
                        } else {
                            ToastUtil.toastShortMessage("工号或密码错误");
                            hideDialog();
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        hideDialog();
                    }

                });
    }

    /**
     * 切换是否记住密码图标
     *
     * @param
     */
    private void updateIsRememberPwdImage() {
        img_remember.setImageResource(isRememberPwd ? R.drawable.remember_pwd_choice : R.drawable.unremember_pwd_choice);
    }
}
