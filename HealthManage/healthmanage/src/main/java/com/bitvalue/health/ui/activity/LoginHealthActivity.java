package com.bitvalue.health.ui.activity;

import static com.bitvalue.health.util.Constants.LOCAL_PRIVATER_KEY;
import static com.bitvalue.health.util.Constants.LOCAL_PUBLIC_KEY;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.requestbean.LoginReqBean;
import com.bitvalue.health.api.responsebean.LoginResBean;
import com.bitvalue.health.base.BaseActivity;
import com.bitvalue.health.contract.homecontract.LoginContract;
import com.bitvalue.health.presenter.homepersenter.LoginPersenter;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.RSAEncrypt;
import com.bitvalue.health.util.SharedPreManager;
import com.bitvalue.healthmanage.R;
import com.hjq.http.EasyConfig;
import com.hjq.toast.ToastUtils;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/***
 * 用户登录界面
 */
public class LoginHealthActivity extends BaseActivity<LoginPersenter> implements LoginContract.loginView {

    @BindView(R.id.img_remember)
    ImageView img_remember;
    @BindView(R.id.et_work_no)
    EditText et_work_no;
    @BindView(R.id.et_psd)
    EditText et_psd;
    private boolean isRememberPwd;
    private boolean needToast;


    @Override
    protected LoginPersenter createPresenter() {
        return new LoginPersenter();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
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
        try {
            Map<String, String> map_key = RSAEncrypt.genKeyPair(); // 生成 公钥和私钥 并保存
            if (map_key != null && map_key.size() > 0) {
                String publicKey = map_key.get("publicKey");
                String privaterkey = map_key.get("privateKey");
                SharedPreManager.putString(LOCAL_PUBLIC_KEY, publicKey);
                SharedPreManager.putString(LOCAL_PRIVATER_KEY, privaterkey);
                Log.d(TAG, "publicKey: " + publicKey + "  \n" + " privaterkey: " + privaterkey);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.e(TAG, "生成公钥私钥异常: " + e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (needToast) {
            ToastUtils.show("账号已过期，请重新登录");
        }
    }


    //各个子控件的点击事件
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
                if (isRememberPwd) {
                    SharedPreManager.putString(Constants.KEY_PSD, et_psd.getText().toString());
                    SharedPreManager.putString(Constants.KEY_ACCOUNT, et_work_no.getText().toString());
                }
                showDialog();
               String publicKey= SharedPreManager.getString(LOCAL_PUBLIC_KEY);
                Log.e(TAG, "222222: "+publicKey );
                mPresenter.login(new LoginReqBean(userId, passWord, 2,publicKey));
                break;
        }
    }

    /**
     * 切换是否记住密码图标,
     *
     * @param
     */
    private void updateIsRememberPwdImage() {
        img_remember.setImageResource(isRememberPwd ? R.drawable.remember_pwd_choice : R.drawable.unremember_pwd_choice);
    }


    //用户登录成功之后的回调
    @Override
    public void loginSuccess(Object object) {
        runOnUiThread(() -> hideDialog());
        LoginResBean resBean = (LoginResBean) object;
        EasyConfig.getInstance().addHeader("Authorization", resBean.getToken()); //这里也要给EasyHttp添加Token，其他地方有用到EasyHttp请求
        SharedPreManager.putObject(Constants.KYE_USER_BEAN, resBean);
        SharedPreManager.putString(Constants.KEY_TOKEN, resBean.getToken());
        SharedPreManager.putBoolean(Constants.KEY_IM_AUTO_LOGIN, true, Application.instance());
        Log.e(TAG, "loginSuccess: " + SharedPreManager.getString(Constants.KEY_TOKEN));
        startActivity(new Intent(LoginHealthActivity.this, HomeActivity.class));
        finish();

    }

    //用户登录失败之后的回调
    @Override
    public void loginFail(String failMessage) {
        runOnUiThread(() -> {
            showToast(failMessage);
            hideDialog();
        });

    }
}
