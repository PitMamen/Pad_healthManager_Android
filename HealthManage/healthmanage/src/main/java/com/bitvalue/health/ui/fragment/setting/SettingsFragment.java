package com.bitvalue.health.ui.fragment.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.requestbean.PersonalDataBean;
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
import com.bitvalue.health.util.SharedPreManager;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 个人设置界面
 */

public class SettingsFragment extends BaseFragment<PersonalDataPersenter> implements PersonalDataContract.PersonalDataView {

    @BindView(R.id.layout_plans)
    RelativeLayout layout_plans;
    @BindView(R.id.layout_log)
    RelativeLayout layout_logs;
    @BindView(R.id.layout_personal_data)
    RelativeLayout layout_person;
    @BindView(R.id.tv_title)
    TextView tv_name;
    @BindView(R.id.tv_type)
    TextView tv_type;
    private HomeActivity homeActivity;

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
        LoginBean loginBean = SharedPreManager.getObject(Constants.KYE_USER_BEAN, LoginBean.class, homeActivity);
        if (loginBean == null) {
            tv_name.setText("设置");
        } else {
            tv_name.setText(loginBean.getUser().user.userName);
            tv_type.setText(loginBean.getAccount().roleName.equals("casemanager")?"个案管理师":"医生");
        }
    }


    @OnClick({R.id.layout_rtc, R.id.layout_personal_data, R.id.layout_log, R.id.layout_plans})
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

//                homeActivity.switchSecondFragment(Constants.FRAGMENT_PERSONAL_DATA, "");
                break;

            //套餐配置
//            case R.id.layout_plans:
//                homeActivity.switchSecondFragment(Constants.FRAGMENT_HEALTH_PLAN, "");
//                break;
//
//            //看诊记录
//            case R.id.layout_log:
//                homeActivity.switchSecondFragment(Constants.FRAGMENT_CHAT_LOG, "");
//                break;
//
//            //书写病历
//            case R.id.layout_rtc:
//                homeActivity.switchSecondFragment(Constants.FRAGMENT_MEDICINE_GUIDE, "");
//                break;


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
            Intent intent = new Intent(Application.instance(), LoginHealthActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Application.instance().startActivity(intent);
            SharedPreManager.putString(Constants.KEY_TOKEN, "");
            ActivityManager.getInstance().finishAllActivities(LoginHealthActivity.class);
        });

    }

    @Override
    public void logoutAcountFail(String failMessage) {
        getActivity().runOnUiThread(() -> ToastUtil.toastShortMessage(failMessage));
    }
}
