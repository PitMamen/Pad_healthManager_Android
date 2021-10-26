package com.bitvalue.healthmanage.ui.fragment.function.setting;

import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitvalue.healthmanage.util.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppApplication;
import com.bitvalue.healthmanage.base.AppFragment;
import com.bitvalue.healthmanage.http.glide.GlideApp;
import com.bitvalue.healthmanage.http.model.ApiResult;
import com.bitvalue.healthmanage.http.api.LogoutApi;
import com.bitvalue.healthmanage.http.api.PersonalDataApi;
import com.bitvalue.healthmanage.http.bean.TaskDetailBean;
import com.bitvalue.healthmanage.widget.manager.ActivityManager;
import com.bitvalue.healthmanage.ui.activity.main.HomeActivity;
import com.bitvalue.healthmanage.ui.activity.main.LoginHealthActivity;
import com.bitvalue.healthmanage.widget.manager.SharedPreManager;
import com.bitvalue.healthmanage.util.DataUtil;
import com.bitvalue.sdk.collab.helper.CustomAnalyseMessage;
import com.bitvalue.sdk.collab.helper.CustomHealthDataMessage;
import com.bitvalue.sdk.collab.helper.CustomMessage;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/***
 * 设置   个人信息界面
 */
public class PersonalDataFragment extends AppFragment {
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
    private TaskDetailBean taskDetailBean;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_personal_data;
    }

    @Override
    protected void initView() {
        tv_title.setText("个人信息");
        EventBus.getDefault().register(this);
        homeActivity = (HomeActivity) getActivity();
    }

    @Override
    protected void initData() {
        getDetail();
    }

    private void getDetail() {
        PersonalDataApi personalDataApi = new PersonalDataApi();
        EasyHttp.get(this).api(personalDataApi).request(new HttpCallback<ApiResult<PersonalDataApi>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(ApiResult<PersonalDataApi> result) {
                super.onSucceed(result);
//                //增加判空
                if (result == null) {
                    return;
                }
                if (result.getCode() == 0) {
                    if (null == result.getData()) {
                        return;
                    }
                    PersonalDataApi personalDataResult = result.getData();

                    if (null != personalDataResult.avatarUrl) {
                        GlideApp.with(img_head)
                                .load(personalDataResult.avatarUrl)
                                .transform(new RoundedCorners((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                        20, AppApplication.instance().getResources().getDisplayMetrics())))
                                .into(img_head);
                    }

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

                } else {
                    ToastUtil.toastShortMessage(result.getMessage());
                }
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
    }

    private void setData() {
//        tv_hospital.setText(taskDetailBean.hospital);
//        tv_type.setText(taskDetailBean.visitType);
//        tv_time.setText(taskDetailBean.visitTime);
//        tv_result.setText(taskDetailBean.diagnosis);
    }

    private void processPhotos() {
        for (int i = 0; i < taskDetailBean.healthImages.size(); i++) {
//            photos.add(taskDetailBean.healthImages.get(i).fileUrl.replace("218.77.104.74:8008", "192.168.1.122"));
            photos.add(taskDetailBean.healthImages.get(i).fileUrl);
        }
    }

    @OnClick({R.id.tv_logout, R.id.layout_back, R.id.tv_name})
    public void onClick(View view) {
        switch (view.getId()) {
//            退出登录
            case R.id.tv_logout:
                DataUtil.showNormalDialog(homeActivity, "温馨提示", "确定退出登录吗？", "确定", "取消", new DataUtil.OnNormalDialogClicker() {
                    @Override
                    public void onPositive() {
                        logOut();
                    }

                    @Override
                    public void onNegative() {

                    }
                });
                break;
            case R.id.layout_back:
                backPress();
                break;
            case R.id.tv_name:
//                ClientsResultBean.UserInfoDTO child = new ClientsResultBean.UserInfoDTO();
////                child.userId = 31111;
//                child.chatType = InputLayoutUI.CHAT_TYPE_VIDEO;
//                child.userId = "109";//0198  userid 109;0196  userid  121;0199  userid  110
//                homeActivity.switchSecondFragment(Constants.FRAGMENT_CHAT, child);
                break;
        }
    }

    private void logOut() {
        LogoutApi logoutApi = new LogoutApi();
        EasyHttp.get(this).api(logoutApi).request(new HttpCallback<ApiResult<String>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(ApiResult<String> result) {
                super.onSucceed(result);
                //增加判空
                if (result == null) {
                    return;
                }
                if (result.getCode() == 0) {
                    ToastUtil.toastShortMessage("退出登录成功");
                    Intent intent = new Intent(AppApplication.instance(), LoginHealthActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    AppApplication.instance().startActivity(intent);
                    SharedPreManager.putString(Constants.KEY_TOKEN, "");
//                    SharedPreManager.putObject(Constants.KYE_USER_BEAN, null);
                    // 进行内存优化，销毁除登录页之外的所有界面
                    ActivityManager.getInstance().finishAllActivities(LoginHealthActivity.class);
                } else {
                    ToastUtil.toastShortMessage(result.getMessage());
                }
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
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

    public static class NewMsgData {
        public ArrayList<String> userIds;
        public String msgType;
        public String id;
//        public String msgType;
    }

    private void backPress() {
        if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
            homeActivity.getSupportFragmentManager().popBackStack();
        }
    }
}
