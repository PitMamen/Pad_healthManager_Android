package com.bitvalue.healthmanage.ui.fragment;

import android.os.Bundle;
import android.view.View;
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
import com.bitvalue.healthmanage.ui.adapter.interfaz.OnItemDelete;
import com.bitvalue.healthmanage.ui.contacts.bean.MainRefreshObj;
import com.bitvalue.healthmanage.util.DensityUtil;
import com.bitvalue.healthmanage.util.MUtils;
import com.bitvalue.sdk.collab.modules.chat.layout.input.InputLayoutUI;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;

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

    @BindView(R.id.contact_list)
    RecyclerView contact_list;

    private boolean is_need_toast;
    private HomeActivity homeActivity;
    private ArrayList<String> mIds = new ArrayList<>();
    private List<VideoClientsResultBean> videoClientsResultBeans = new ArrayList<>();
    private VideoPatientQuickAdapter videoPatientQuickAdapter;
    private VideoClientsApi videoClientsApi;

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
                ClientsResultBean.UserInfoDTO userInfoDTO = new ClientsResultBean.UserInfoDTO();
                userInfoDTO.userId = videoClientsResultBean.userInfo.userId;
                userInfoDTO.userName = videoClientsResultBean.userInfo.userName;
                userInfoDTO.chatType = InputLayoutUI.CHAT_TYPE_VIDEO;
                homeActivity.switchSecondFragment(Constants.FRAGMENT_CHAT, userInfoDTO);
            }
        });
        contact_list.setAdapter(videoPatientQuickAdapter);
    }

    @Override
    protected void initView() {
        homeActivity = (HomeActivity) getActivity();
        EventBus.getDefault().register(this);
        initPatientListView();

        videoClientsApi = new VideoClientsApi();
        getMyClients();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MainRefreshObj mainRefreshObj) {
        videoClientsResultBeans.clear();
        getMyClients();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @OnClick({R.id.tv_no_data, R.id.tv_wait, R.id.tv_end})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_no_data:
                getMyClients();
                break;

            case R.id.tv_wait:
                tv_wait.setTextColor(homeActivity.getResources().getColor(R.color.white));
                tv_wait.setBackgroundResource(0);

                tv_end.setTextColor(homeActivity.getResources().getColor(R.color.main_blue));
                tv_end.setBackgroundResource(R.drawable.shape_bg_white_solid_2);

//                homeActivity.switchSecondFragment(Constants.FRAGMENT_VIDEO_PATIENT_DATA,"");

                videoClientsApi.attendanceStatus = "";
                getMyClients();
                break;

            case R.id.tv_end:
                tv_end.setTextColor(homeActivity.getResources().getColor(R.color.white));
                tv_end.setBackgroundResource(0);

                tv_wait.setTextColor(homeActivity.getResources().getColor(R.color.main_blue));
                tv_wait.setBackgroundResource(R.drawable.shape_bg_white_solid_1);

                videoClientsApi.attendanceStatus = "3";
                getMyClients();
                break;
        }
    }


    private void getMyClients() {
        EasyHttp.get(this).api(videoClientsApi).request(new HttpCallback<HttpData<ArrayList<VideoClientsResultBean>>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(HttpData<ArrayList<VideoClientsResultBean>> result) {
                super.onSucceed(result);
                videoClientsResultBeans.clear();
                videoClientsResultBeans = result.getData();
                if (null == videoClientsResultBeans || videoClientsResultBeans.size() == 0) {
                    ToastUtil.toastShortMessage("暂无客户数据");
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
