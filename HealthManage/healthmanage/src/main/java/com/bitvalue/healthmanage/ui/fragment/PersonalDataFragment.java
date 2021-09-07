package com.bitvalue.healthmanage.ui.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.http.model.HttpData;
import com.bitvalue.healthmanage.http.request.TaskDetailApi;
import com.bitvalue.healthmanage.http.response.TaskDetailBean;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.ui.media.ImagePreviewActivity;
import com.bitvalue.sdk.collab.helper.CustomAnalyseMessage;
import com.bitvalue.sdk.collab.helper.CustomHealthDataMessage;
import com.bitvalue.sdk.collab.helper.CustomMessage;
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

public class PersonalDataFragment extends AppFragment implements BGANinePhotoLayout.Delegate {
    @BindView(R.id.npl_item_moment_photos)
    BGANinePhotoLayout ninePhotoLayout;

    @BindView(R.id.img_head)
    ImageView img_head;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.img_qr_code)
    TextView img_qr_code;

    @BindView(R.id.tv_complete_info)
    TextView tv_complete_info;

    @BindView(R.id.tv_title)
    TextView tv_title;

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
        customHealthDataMessage = (CustomHealthDataMessage) getArguments().getSerializable(Constants.DATA_MSG);
        getDetail();
    }

    private void getDetail() {
        TaskDetailApi taskDetailApi = new TaskDetailApi();
        taskDetailApi.contentId = customHealthDataMessage.contentId;
        taskDetailApi.planType = "OutsideInformation";
        taskDetailApi.userId = customHealthDataMessage.userId;
        EasyHttp.get(this).api(taskDetailApi).request(new HttpCallback<HttpData<TaskDetailBean>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(HttpData<TaskDetailBean> result) {
                super.onSucceed(result);
                //增加判空
                if (result == null || result.getData() ==null){
                    return;
                }
                if (result.getCode() == 0) {
                    taskDetailBean = result.getData();
                    if (null == taskDetailBean) {
                        return;
                    }
                    setData();
                    processPhotos();

                    ninePhotoLayout.setDelegate(PersonalDataFragment.this);
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
//        tv_hospital.setText(taskDetailBean.hospital);
//        tv_type.setText(taskDetailBean.visitType);
//        tv_time.setText(taskDetailBean.visitTime);
//        tv_result.setText(taskDetailBean.diagnosis);
    }

    private void processPhotos() {
        for (int i = 0; i < taskDetailBean.healthImages.size(); i++) {
//            photos.add(taskDetailBean.healthImages.get(i).fileUrl);
            photos.add(taskDetailBean.healthImages.get(i).fileUrl.replace("218.77.104.74:8008", "192.168.1.122"));
//            photos.add(taskDetailBean.healthImages.get(i).fileUrl.replace("192.168.1.122", "218.77.104.74:8008"));
//            photos.add(taskDetailBean.healthImages.get(i).previewFileUrl.replace("218.77.104.74:8008", "192.168.1.122"));
        }
    }

    @OnClick({R.id.tv_send_analyse, R.id.layout_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_send_analyse:
                ChatFragment.NewMsgData msgData = new ChatFragment.NewMsgData();
                msgData.msgType = Constants.MSG_SINGLE;
                msgData.userIds = new ArrayList<>();
                msgData.userIds.add(customHealthDataMessage.userId);
                homeActivity.switchSecondFragment(Constants.FRAGMENT_HEALTH_ANALYSE, msgData);
                break;
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
