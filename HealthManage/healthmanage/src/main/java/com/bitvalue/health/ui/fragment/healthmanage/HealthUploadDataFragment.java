package com.bitvalue.health.ui.fragment.healthmanage;

import android.view.View;
import android.widget.TextView;

import com.bitvalue.health.api.responsebean.TaskDetailBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.contract.healthmanagercontract.HealthUploadDataContract;
import com.bitvalue.health.presenter.healthmanager.HealthUploadDataPresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.activity.media.ImagePreviewActivity;
import com.bitvalue.health.ui.fragment.chat.ChatFragment;
import com.bitvalue.health.util.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.helper.CustomAnalyseMessage;
import com.bitvalue.sdk.collab.helper.CustomHealthDataMessage;
import com.bitvalue.sdk.collab.helper.CustomMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;

/****
 * 健康计划中，上传患者资料界面
 */
public class HealthUploadDataFragment extends BaseFragment<HealthUploadDataPresenter> implements HealthUploadDataContract.View, BGANinePhotoLayout.Delegate {
    @BindView(R.id.npl_item_moment_photos)
    BGANinePhotoLayout ninePhotoLayout;

    @BindView(R.id.tv_hospital)
    TextView tv_hospital;

    @BindView(R.id.tv_time)
    TextView tv_time;

    @BindView(R.id.tv_type)
    TextView tv_type;

    @BindView(R.id.tv_result)
    TextView tv_result;

    @BindView(R.id.tv_title)
    TextView tv_title;

    private HomeActivity homeActivity;
    private CustomHealthDataMessage customHealthDataMessage;
    private ArrayList<String> photos = new ArrayList<>();
    private String currentID = "";


    @Override
    public void initView(View rootView) {
        tv_title.setText("患者资料上传");
        EventBus.getDefault().register(this);
        homeActivity = (HomeActivity) getActivity();
    }

    @Override
    public void initData() {
        //获取上个界面回传的数据
        customHealthDataMessage = (CustomHealthDataMessage) getArguments().getSerializable(Constants.DATA_MSG);
        //获取健康计划 接口请求
        mPresenter.queryHealthPlanContent(customHealthDataMessage.contentId, "OutsideInformation", customHealthDataMessage.userId);
    }

    @Override
    protected HealthUploadDataPresenter createPresenter() {
        return new HealthUploadDataPresenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_upload_data;
    }


    @OnClick({R.id.tv_send_analyse, R.id.layout_back})
    public void onClick(View view) {
        switch (view.getId()) {
            //点击健康评估 进入健康评估界面
            case R.id.tv_send_analyse:
                ChatFragment.NewMsgData msgData = new ChatFragment.NewMsgData();
                msgData.msgType = Constants.MSG_SINGLE;
                msgData.userIds = new ArrayList<>();
                msgData.userIds.add(customHealthDataMessage.userId);
                msgData.id = currentID;
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

    @Override
    public void querySuccess(TaskDetailBean taskDetailBean) {
        if (null == taskDetailBean) {
            return;
        }
        currentID = taskDetailBean.userGoodsId;

        getActivity().runOnUiThread(() -> {
            tv_hospital.setText(taskDetailBean.hospital);
            tv_type.setText(taskDetailBean.visitType);
            tv_time.setText(taskDetailBean.visitTime);
            tv_result.setText(taskDetailBean.diagnosis);

            for (int i = 0; i < taskDetailBean.healthImages.size(); i++) {
                photos.add(taskDetailBean.healthImages.get(i).fileUrl);
            }

            ninePhotoLayout.setDelegate(HealthUploadDataFragment.this);
            ninePhotoLayout.setData(photos);
        });

    }

    @Override
    public void queryFail(String failMessage) {

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
