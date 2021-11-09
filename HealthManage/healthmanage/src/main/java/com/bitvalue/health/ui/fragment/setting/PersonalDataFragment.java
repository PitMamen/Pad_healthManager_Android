package com.bitvalue.health.ui.fragment.setting;

import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.requestbean.PersonalDataBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.contract.settingcontract.PersonalDataContract;
import com.bitvalue.health.presenter.PersonalDataPersenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.activity.LoginHealthActivity;
import com.bitvalue.health.util.ActivityManager;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.DataUtil;
import com.bitvalue.health.util.SharedPreManager;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.helper.CustomAnalyseMessage;
import com.bitvalue.sdk.collab.helper.CustomHealthDataMessage;
import com.bitvalue.sdk.collab.helper.CustomMessage;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonalDataFragment extends BaseFragment<PersonalDataPersenter> implements PersonalDataContract.PersonalDataView {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.img_head)
    ImageView img_head;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_complete_info)
    TextView tv_complete_info;

    @BindView(R.id.tv_hospital)
    TextView tv_hospital;

    @BindView(R.id.tv_depart)
    TextView tv_depart;

    @BindView(R.id.tv_level)
    TextView tv_level;

    @BindView(R.id.tv_good)
    TextView tv_good;

    @BindView(R.id.tv_good_detail)
    TextView tv_good_detail;

    @BindView(R.id.tv_intro_detail)
    TextView tv_intro_detail;

    @BindView(R.id.tv_logout)
    TextView tv_logout;

    private HomeActivity homeActivity;
    private CustomHealthDataMessage customHealthDataMessage;
    private ArrayList<String> photos = new ArrayList<>();


    @Override
    public void initView(View view) {
        tv_title.setText(getString(R.string.person_imformation));
        EventBus.getDefault().register(this);
        homeActivity = (HomeActivity) getActivity();
    }

    @Override
    public void initData() {
        mPresenter.getPersonalData();
    }

    @Override
    protected PersonalDataPersenter createPresenter() {
        return new PersonalDataPersenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_personal_data;
    }


    @OnClick({R.id.tv_logout, R.id.layout_back, R.id.tv_name})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_logout:  //退出登录
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
            case R.id.layout_back:
                backPress();
                break;
            case R.id.tv_name:  //
//                ClientsResultBean.UserInfoDTO child = new ClientsResultBean.UserInfoDTO();
////                child.userId = 31111;
//                child.chatType = InputLayoutUI.CHAT_TYPE_VIDEO;
//                child.userId = "109";//0198  userid 109;0196  userid  121;0199  userid  110
//                homeActivity.switchSecondFragment(Constants.FRAGMENT_CHAT, child);
                break;
        }
    }

    /**
     * 处理订阅消息
     *
     * @param message
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CustomMessage message) {//不用区分类型，全部直接转换成json发送消息出去
        if (message instanceof CustomAnalyseMessage) {
            backPress();
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    private void backPress() {
        if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
            homeActivity.getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void getPersonalDocDataSuccess(PersonalDataBean docDataBean) {
        getActivity().runOnUiThread(() -> {
            PersonalDataBean personalDataResult = docDataBean;
            if (null != personalDataResult.avatarUrl) {
                Glide.with(img_head)
                        .load(personalDataResult.avatarUrl)
                        .transform(new RoundedCorners((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                20, Application.instance().getResources().getDisplayMetrics())))
                        .into(img_head);
            }

            setMessageInfo(personalDataResult);

        });
    }

    private void setMessageInfo(@NonNull PersonalDataBean personalDataResult) {
        if (null != personalDataResult.userName) {
            tv_name.setText(personalDataResult.userName);
        }

        if (null != personalDataResult.hospitalName) {
            tv_hospital.setText(personalDataResult.hospitalName);
        }

        if (null != personalDataResult.departmentName) {
            tv_depart.setText(personalDataResult.departmentName);
        }

        if (null != personalDataResult.professionalTitle) {
            tv_level.setText(personalDataResult.professionalTitle);
        }

        if (null != personalDataResult.expertInDiseaseWord) {
            tv_good.setText(personalDataResult.expertInDiseaseWord);
        }

        if (null != personalDataResult.expertInDisease) {
            tv_good_detail.setText(personalDataResult.expertInDisease);
        }

        if (null != personalDataResult.doctorBrief) {
            tv_intro_detail.setText(personalDataResult.doctorBrief);
        }
    }

    @Override
    public void getPersonalDocDataFail(String messagefail) {

        getActivity().runOnUiThread(() -> ToastUtil.toastShortMessage(messagefail));

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
