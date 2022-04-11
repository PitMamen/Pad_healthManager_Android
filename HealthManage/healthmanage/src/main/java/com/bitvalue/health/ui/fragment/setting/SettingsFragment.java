package com.bitvalue.health.ui.fragment.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.requestbean.PersonalDataBean;
import com.bitvalue.health.api.requestbean.ResetPasswordRequestBean;
import com.bitvalue.health.api.responsebean.LoginBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.contract.settingcontract.PersonalDataContract;
import com.bitvalue.health.presenter.PersonalDataPersenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.activity.LoginHealthActivity;
import com.bitvalue.health.util.ActivityManager;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.DataUtil;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.SharedPreManager;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.hjq.toast.ToastUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 个人设置界面
 */

public class SettingsFragment extends BaseFragment<PersonalDataPersenter> implements PersonalDataContract.PersonalDataView {


    @BindView(R.id.ll_taps)
    LinearLayout ll_tps_layout;
    @BindView(R.id.layout_plans)
    RelativeLayout layout_plans;
    @BindView(R.id.layout_personal_data)
    RelativeLayout layout_person;
    @BindView(R.id.tv_title)
    TextView tv_name;
    @BindView(R.id.tv_type)
    TextView tv_type;
    @BindView(R.id.ll_moddify_pwd_content)
    LinearLayout ll_modify_pwd_layout;
    @BindView(R.id.et_name)
    EditText ed_name;
    @BindView(R.id.et_phone_numb)
    EditText ed_phoneNumber;
    @BindView(R.id.et_input_old_pwd)
    EditText ed_input_old_pwd;
    @BindView(R.id.input_new_pwd)
    EditText ed_input_new_pwd;
    @BindView(R.id.comfirm_new_pwd)
    EditText ed_comfirm_pwd;
    @BindView(R.id.tv_commit)
    TextView btn_commit;
    @BindView(R.id.layout_fix_pwd)
    RelativeLayout rl_fix_pwd;
    @BindView(R.id.img_reset_back)
    ImageView img_back;
    @BindView(R.id.tv_acount)
    TextView tv_acount;


    private HomeActivity homeActivity;
    private LoginBean loginBean;
    private String local_savepwd;

    public static SettingsFragment getInstance(boolean is_need_toast) {
        SettingsFragment newsFragment = new SettingsFragment();
        Bundle bundle = new Bundle();//TODO 参数可改
        bundle.putBoolean("is_need_toast", is_need_toast);
        newsFragment.setArguments(bundle);
        return newsFragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) getActivity();
    }

    @Override
    public void initView(View view) {
        loginBean = SharedPreManager.getObject(Constants.KYE_USER_BEAN, LoginBean.class, homeActivity);
        local_savepwd = SharedPreManager.getString(Constants.KEY_PSD);
        if (loginBean == null) {
            tv_name.setText("设置");
        } else {
            tv_name.setText(loginBean.getUser().user.userName);
            tv_type.setText(loginBean.getAccount().roleName.equals("casemanager") ? "个案管理师" : "医生");
        }
        ed_name.setText(loginBean.getUser().user.userName);
        ed_phoneNumber.setText(loginBean.getUser().user.phone);
        tv_acount.setText(loginBean.getUser().userName);
    }


    @OnClick({R.id.layout_personal_data, R.id.layout_fix_pwd, R.id.layout_plans, R.id.tv_commit, R.id.img_reset_back})
    public void onViewClick(View view) {
        switch (view.getId()) {
            //个人信息
            case R.id.layout_personal_data:

                DataUtil.showNormalDialog(homeActivity, "温馨提示", "确定退出登录吗？", "确定", "取消", new DataUtil.OnNormalDialogClicker() {
                    @Override
                    public void onPositive() {
                        mPresenter.logoutAcount();

                    }

                    @Override
                    public void onNegative() {

                    }
                });
                break;

            //修改用户信息
            case R.id.layout_fix_pwd:
                ll_tps_layout.setVisibility(View.GONE);
                ll_modify_pwd_layout.setVisibility(View.VISIBLE);
                img_back.setVisibility(View.VISIBLE);
                break;

            //返回
            case R.id.img_reset_back:
                ll_tps_layout.setVisibility(View.VISIBLE);
                ll_modify_pwd_layout.setVisibility(View.GONE);
                img_back.setVisibility(View.GONE);
                break;


            //提交修改密码
            case R.id.tv_commit:
                if (EmptyUtil.isEmpty(ed_phoneNumber.getText().toString().trim())) {
                    ToastUtils.show("姓名不能为空!");
                    return;
                }

                if (EmptyUtil.isEmpty(ed_phoneNumber.getText().toString().trim())) {
                    ToastUtils.show("手机号不能为空!");
                    return;
                }
                boolean isMatch = DataUtil.isValidPhoneNumber(ed_phoneNumber.getText().toString().trim());
                if (ed_phoneNumber.getText().toString().trim().length() != 11 || !isMatch) {
                    ToastUtils.show("号码格式错误!");
                    return;
                }


                if (!EmptyUtil.isEmpty(local_savepwd)) {
                    if (!local_savepwd.equals(ed_input_old_pwd.getText().toString().trim())) {
                        ToastUtils.show("原密码错误!");
                        return;
                    }
                }


                if (EmptyUtil.isEmpty(ed_input_old_pwd.getText().toString().trim())) {
                    ToastUtils.show("原密码不能为空!");
                    return;
                }

                if (EmptyUtil.isEmpty(ed_input_new_pwd.getText().toString().trim())) {
                    ToastUtils.show("新密码不能为空");
                    return;
                }

                if (EmptyUtil.isEmpty(ed_comfirm_pwd.getText().toString().trim())) {
                    ToastUtils.show("请确认密码");
                    return;
                }

                if (!ed_input_new_pwd.getText().toString().trim().equals(ed_comfirm_pwd.getText().toString().trim())) {
                    ToastUtils.show("密码不一致,请重新输入!");
                    return;
                }
                ResetPasswordRequestBean requestBean = new ResetPasswordRequestBean();
                requestBean.setName(ed_name.getText().toString().trim());
                requestBean.setNewpassword(ed_input_new_pwd.getText().toString().trim());
                requestBean.setPassword(ed_input_old_pwd.getText().toString().trim());
                requestBean.setPhone(ed_phoneNumber.getText().toString().trim());
                mPresenter.resetPassword(requestBean);
                break;

            default:
                break;

        }
    }


    @Override
    protected PersonalDataPersenter createPresenter() {
        return new PersonalDataPersenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_settings;
    }

    @Override
    public void getPersonalDocDataSuccess(PersonalDataBean docDataBean) {

    }

    @Override
    public void getPersonalDocDataFail(String messagefail) {

    }

    @Override
    public void logoutAcountSuccess() {
        getActivity().runOnUiThread(() -> {
            ToastUtil.toastShortMessage("退出成功");
            startLoginActivity(false);
        });

    }

    @Override
    public void logoutAcountFail(String failMessage) {
        getActivity().runOnUiThread(() -> ToastUtil.toastShortMessage(failMessage));
    }

    @Override
    public void resetPasswordSuccess(String successMessage) {
        // TODO: 2022/4/7 如果修改成功 清除原有的 账号信息  退出应用 重新进入登录界面
        getActivity().runOnUiThread(() -> {
            ToastUtils.show("操作成功!");
            startLoginActivity(true);
        });
    }

    @Override
    public void resetPasswordFail(String failMessage) {
        getActivity().runOnUiThread(() -> ToastUtils.show("操作失败:" + failMessage));
    }

    private void startLoginActivity(boolean isModifyPwd){
        Intent intent = new Intent(Application.instance(), LoginHealthActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Application.instance().startActivity(intent);
        SharedPreManager.putString(Constants.KEY_TOKEN, "");
        if (isModifyPwd){
            SharedPreManager.putString(Constants.KEY_PSD, "");
        }
        ActivityManager.getInstance().finishAllActivities(LoginHealthActivity.class);
    }



}
