package com.bitvalue.healthmanage.ui.fragment;

import android.view.View;
import android.widget.TextView;

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

public class MedicineGuideFragment extends AppFragment implements BGANinePhotoLayout.Delegate {

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

    @BindView(R.id.tv_attention_content)
    TextView tv_attention_content;

    @BindView(R.id.tv_end_content)
    TextView tv_end_content;

    @BindView(R.id.tv_bad_content)
    TextView tv_bad_content;

    @BindView(R.id.tv_not_content)
    TextView tv_not_content;

    private HomeActivity homeActivity;
    private CustomHealthDataMessage customHealthDataMessage;
    private ArrayList<String> photos = new ArrayList<>();
    private TaskDetailBean taskDetailBean;
    private boolean isAttentionVisible;
    private boolean isEndVisible;
    private boolean isBadVisible;
    private boolean isNotVisible;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_medicine_guide;
    }

    @Override
    protected void initView() {
        tv_title.setText("用药指导");
        EventBus.getDefault().register(this);
        homeActivity = (HomeActivity) getActivity();
    }

    @Override
    protected void initData() {
//        customHealthDataMessage = (CustomHealthDataMessage) getArguments().getSerializable(Constants.DATA_MSG);
//        getDetail();
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
                if (result == null || result.getData() == null) {
                    return;
                }
                if (result.getCode() == 0) {
                    taskDetailBean = result.getData();
                    if (null == taskDetailBean) {
                        return;
                    }
                    setData();
                    processPhotos();
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
        tv_name.setText(taskDetailBean.hospital);
        tv_gender.setText(taskDetailBean.visitType);
        tv_age.setText(taskDetailBean.visitTime);
        tv_conclusion.setText(taskDetailBean.diagnosis);
        tv_disease.setText(taskDetailBean.diagnosis);
    }

    private void processPhotos() {
        for (int i = 0; i < taskDetailBean.healthImages.size(); i++) {
//            photos.add(taskDetailBean.healthImages.get(i).fileUrl.replace("218.77.104.74:8008", "192.168.1.122"));
            photos.add(taskDetailBean.healthImages.get(i).fileUrl);
        }
    }

    @OnClick({R.id.layout_back, R.id.layout_attention, R.id.layout_end_condition, R.id.layout_bad, R.id.layout_not})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                backPress();
                break;
            case R.id.layout_attention:
                if (!isAttentionVisible) {
                    tv_attention_content.setVisibility(View.VISIBLE);
                    isAttentionVisible = true;
                } else {
                    tv_attention_content.setVisibility(View.GONE);
                    isAttentionVisible = false;
                }

                break;
            case R.id.layout_end_condition:
                if (!isEndVisible) {
                    tv_end_content.setVisibility(View.VISIBLE);
                    isEndVisible = true;
                } else {
                    tv_end_content.setVisibility(View.GONE);
                    isEndVisible = false;
                }
                break;
            case R.id.layout_bad:
                if (!isBadVisible) {
                    tv_bad_content.setVisibility(View.VISIBLE);
                    isBadVisible = true;
                } else {
                    tv_bad_content.setVisibility(View.GONE);
                    isBadVisible = false;
                }
                break;
            case R.id.layout_not:
                if (!isNotVisible) {
                    tv_not_content.setVisibility(View.VISIBLE);
                    isNotVisible = true;
                } else {
                    tv_not_content.setVisibility(View.GONE);
                    isNotVisible = false;
                }
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
