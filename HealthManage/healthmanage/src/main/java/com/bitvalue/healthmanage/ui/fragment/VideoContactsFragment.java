package com.bitvalue.healthmanage.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.http.model.HttpData;
import com.bitvalue.healthmanage.http.request.VideoClientsApi;
import com.bitvalue.healthmanage.http.response.ClientsResultBean;
import com.bitvalue.healthmanage.http.response.VideoClientsResultBean;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.ui.adapter.VideoPatientQuickAdapter;
import com.bitvalue.healthmanage.ui.contacts.bean.VideoRefreshObj;
import com.bitvalue.healthmanage.util.DensityUtil;
import com.bitvalue.healthmanage.util.MUtils;
import com.bitvalue.sdk.collab.TUIKit;
import com.bitvalue.sdk.collab.base.IMEventListener;
import com.bitvalue.sdk.collab.modules.chat.layout.input.InputLayoutUI;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.tencent.imsdk.message.Message;
import com.tencent.imsdk.v2.V2TIMMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 视频问诊联系人列表
 */
public class VideoContactsFragment extends AppFragment {

    @BindView(R.id.tv_no_data)
    TextView tv_no_data;

    @BindView(R.id.tv_wait)
    TextView tv_wait;

    @BindView(R.id.tv_end)
    TextView tv_end;

    @BindView(R.id.tv_new_count)
    TextView tv_new_count;

    @BindView(R.id.layout_pot)
    LinearLayout layout_pot;

    @BindView(R.id.contact_list)
    RecyclerView contact_list;

    private boolean is_need_toast;
    private HomeActivity homeActivity;
    private ArrayList<String> mIds = new ArrayList<>();
    private List<VideoClientsResultBean> videoClientsResultBeans = new ArrayList<>();
    private VideoPatientQuickAdapter videoPatientQuickAdapter;
    private VideoClientsApi videoClientsApi;
    private int newCount = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video_contacts;
    }

    public static VideoContactsFragment getInstance(boolean is_need_toast) {
        VideoContactsFragment contactsFragment = new VideoContactsFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_need_toast", is_need_toast);
        contactsFragment.setArguments(bundle);
        return contactsFragment;
    }

    private void initPatientListView() {
        contact_list.setLayoutManager(new LinearLayoutManager(getAttachActivity()));
        contact_list.addItemDecoration(MUtils.spaceDivider(
                DensityUtil.dip2px(getAttachActivity(), getAttachActivity().getResources().getDimension(R.dimen.qb_px_3)), false));
        videoPatientQuickAdapter = new VideoPatientQuickAdapter(R.layout.item_video_patient, videoClientsResultBeans);
        videoPatientQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                VideoClientsResultBean videoClientsResultBean = videoClientsResultBeans.get(position);

                if (videoClientsResultBean.attendanceStatus == 1) {
                    ToastUtils.show("患者未到就诊状态，现处于待就诊状态");
                    return;
                }

                ClientsResultBean.UserInfoDTO userInfoDTO = new ClientsResultBean.UserInfoDTO();
                userInfoDTO.userId = videoClientsResultBean.userInfo.userId + "";
                userInfoDTO.userName = videoClientsResultBean.userInfo.userName;
                userInfoDTO.chatType = InputLayoutUI.CHAT_TYPE_VIDEO;
                if (videoClientsResultBean.attendanceStatus == 4) {
                    userInfoDTO.noInput = true;
                }
                userInfoDTO.planId = videoClientsResultBean.id;//预约id
                homeActivity.switchSecondFragment(Constants.FRAGMENT_CHAT, userInfoDTO);

                //将点击的那个置成已点击的状态
                for (int i = 0; i < videoClientsResultBeans.size(); i++) {
                    videoClientsResultBeans.get(i).isClicked = false;
                    if (videoClientsResultBeans.get(i).userInfo.userId == videoClientsResultBean.userInfo.userId) {
                        videoClientsResultBeans.get(i).isClicked = true;
                    }
                }

                //点击的那个如果是有新信息的，给置成没新信息的状态
                if (videoClientsResultBeans.get(position).hasNew) {
                    videoClientsResultBeans.get(position).hasNew = false;
                    newCount = newCount - videoClientsResultBeans.get(position).newMsgNum;
                    tv_new_count.setText(newCount + "");
                    if (newCount == 0) {
                        layout_pot.setVisibility(View.GONE);
                    }
                }

                videoPatientQuickAdapter.setNewData(videoClientsResultBeans);
            }
        });
        contact_list.setAdapter(videoPatientQuickAdapter);
    }

    @Override
    protected void initView() {
        TUIKit.addIMEventListener(mIMEventListener);
        homeActivity = (HomeActivity) getActivity();
        EventBus.getDefault().register(this);
        initPatientListView();

        videoClientsApi = new VideoClientsApi();
        videoClientsApi.attendanceStatus = "";
        getMyClients(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(VideoRefreshObj mainRefreshObj) {
        videoClientsResultBeans.clear();

        newCount = 0;

        tv_new_count.setText(newCount + "");
        layout_pot.setVisibility(View.GONE);
        getMyClients(false);
    }

    // 监听做成静态可以让每个子类重写时都注册相同的一份。
    private IMEventListener mIMEventListener = new IMEventListener() {
        @Override
        public void onNewMessage(V2TIMMessage v2TIMMessage) {
            super.onNewMessage(v2TIMMessage);
            String s = new Gson().toJson(v2TIMMessage);
            Log.d("httpVIDEO", s);
            if (null != v2TIMMessage) {
                Message message = v2TIMMessage.getMessage();
                if (!message.getGroupID().isEmpty()) {//区分健康咨询消息，这里不关注健康咨询消息
                    return;
                }
                for (int i = 0; i < videoClientsResultBeans.size(); i++) {
                    if (message.getSenderUserID().equals(videoClientsResultBeans.get(i).userInfo.userId + "")) {
                        videoClientsResultBeans.get(i).hasNew = true;
                        videoClientsResultBeans.get(i).newMsgNum++;
                        newCount++;

                        tv_new_count.setText(newCount + "");
                        layout_pot.setVisibility(View.VISIBLE);
                    }
                }

                videoPatientQuickAdapter.setNewData(videoClientsResultBeans);
            }
        }
    };

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @OnClick({R.id.tv_no_data, R.id.tv_wait, R.id.tv_end})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_no_data:
                getMyClients(true);
                break;

            case R.id.tv_wait:
                tv_wait.setTextColor(homeActivity.getResources().getColor(R.color.white));
                tv_wait.setBackgroundResource(0);

                tv_end.setTextColor(homeActivity.getResources().getColor(R.color.main_blue));
                tv_end.setBackgroundResource(R.drawable.shape_bg_white_solid_2);

                newCount = 0;
                layout_pot.setVisibility(View.GONE);

                videoClientsApi.attendanceStatus = "";
                getMyClients(false);

                //TODO 做的入口数据调试
//                CustomPatientDataMessage customPatientDataMessage = new CustomPatientDataMessage();
//                if (videoClientsResultBeans.size() == 0) {
//                    return;
//                }
//                customPatientDataMessage.userId = videoClientsResultBeans.get(0).userInfo.userId + "";
//                homeActivity.switchSecondFragment(Constants.FRAGMENT_VIDEO_PATIENT_DATA, customPatientDataMessage);
                break;

            case R.id.tv_end:
                tv_end.setTextColor(homeActivity.getResources().getColor(R.color.white));
                tv_end.setBackgroundResource(0);

                tv_wait.setTextColor(homeActivity.getResources().getColor(R.color.main_blue));
                tv_wait.setBackgroundResource(R.drawable.shape_bg_white_solid_1);

                videoClientsApi.attendanceStatus = "4";
                getMyClients(false);
                break;
        }
    }


    private void getMyClients(boolean needToast) {
        EasyHttp.get(this).api(videoClientsApi).request(new HttpCallback<HttpData<ArrayList<VideoClientsResultBean>>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(HttpData<ArrayList<VideoClientsResultBean>> result) {
                super.onSucceed(result);
                videoClientsResultBeans.clear();
                if (null == result.getData()) {
                    return;
                }
                videoClientsResultBeans = result.getData();
                if (null == videoClientsResultBeans || videoClientsResultBeans.size() == 0) {
                    if (needToast) {
                        ToastUtil.toastShortMessage("暂无客户数据");
                    }
                    contact_list.setVisibility(View.GONE);
                    tv_no_data.setVisibility(View.VISIBLE);
                    return;
                } else {
                    contact_list.setVisibility(View.VISIBLE);
                    tv_no_data.setVisibility(View.GONE);
                }
                videoPatientQuickAdapter.setNewData(videoClientsResultBeans);
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
    }


    @Override
    protected void initData() {

    }
}
