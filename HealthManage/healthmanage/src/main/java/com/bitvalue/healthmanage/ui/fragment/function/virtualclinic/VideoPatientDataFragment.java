package com.bitvalue.healthmanage.ui.fragment.function.virtualclinic;

import android.view.View;
import android.widget.TextView;

import com.bitvalue.healthmanage.util.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.http.bean.PatientDataResult;
import com.bitvalue.healthmanage.http.model.ApiResult;
import com.bitvalue.healthmanage.http.api.PatientDataApi;
import com.bitvalue.healthmanage.ui.activity.main.HomeActivity;
import com.bitvalue.healthmanage.ui.activity.media.ImagePreviewActivity;
import com.bitvalue.healthmanage.base.AppFragment;
import com.bitvalue.sdk.collab.helper.CustomAnalyseMessage;
import com.bitvalue.sdk.collab.helper.CustomMessage;
import com.bitvalue.sdk.collab.helper.CustomPatientDataMessage;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import okhttp3.Call;

public class VideoPatientDataFragment extends AppFragment implements BGANinePhotoLayout.Delegate {
    @BindView(R.id.npl_item_moment_photos)
    BGANinePhotoLayout ninePhotoLayout;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_gender)
    TextView tv_gender;

    @BindView(R.id.tv_age)
    TextView tv_age;

    @BindView(R.id.tv_conclusion)
    TextView tv_conclusion;

    @BindView(R.id.tv_disease)
    TextView tv_disease;

    private HomeActivity homeActivity;
    private CustomPatientDataMessage customPatientDataMessage;
    private ArrayList<String> photos = new ArrayList<>();
    private PatientDataResult patientDataResult;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video_patient_data;
    }

    @Override
    protected void initView() {
        tv_title.setText("患者资料详情");
        EventBus.getDefault().register(this);
        homeActivity = (HomeActivity) getActivity();
    }

    @Override
    protected void initData() {
        customPatientDataMessage = (CustomPatientDataMessage) getArguments().getSerializable(Constants.DATA_MSG);
        getDetail();
    }

//    查看患者上传资料
    private void getDetail() {
        PatientDataApi patientDataApi = new PatientDataApi();
        patientDataApi.userId = customPatientDataMessage.userId;
        EasyHttp.post(this).api(patientDataApi).request(new HttpCallback<ApiResult<List<PatientDataResult>>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(ApiResult<List<PatientDataResult>> result) {
                super.onSucceed(result);
                //增加判空
                if (result == null || result.getData() == null) {
                    return;
                }
                if (result.getCode() == 0) {
                    if (result.getData().size() == 0) {
                        return;
                    }
                    patientDataResult = result.getData().get(0);
                    if (null == patientDataResult) {
                        return;
                    }
                    setData();
                    processPhotos();

                    ninePhotoLayout.setDelegate(VideoPatientDataFragment.this);
                    ninePhotoLayout.setData(photos);
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
        tv_name.setText(patientDataResult.userInfo.userName);
        tv_gender.setText(patientDataResult.userInfo.userSex);
        tv_age.setText(patientDataResult.userInfo.userAge + "岁");
        tv_conclusion.setText(patientDataResult.diagnosis);
        tv_disease.setText(patientDataResult.dept);
    }

    private void processPhotos() {
        for (int i = 0; i < patientDataResult.healthImagesList.size(); i++) {
            photos.add(patientDataResult.healthImagesList.get(i).fileUrl);
//            photos.add(patientDataResult.healthImagesList.get(i).fileUrl.replace("218.77.104.74:8008", "192.168.1.122"));
        }
    }

    @OnClick({R.id.layout_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                backPress();
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

    @Override
    public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
        ImagePreviewActivity.start(homeActivity, model);
    }

    @Override
    public void onClickExpand(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
        ninePhotoLayout.setIsExpand(true);
        ninePhotoLayout.flushItems();
    }
}
