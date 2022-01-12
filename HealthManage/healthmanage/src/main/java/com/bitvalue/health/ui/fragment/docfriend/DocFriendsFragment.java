package com.bitvalue.health.ui.fragment.docfriend;

import static com.bitvalue.health.util.Constants.GET_CONVERSATION_COUNT;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.api.responsebean.ClientsResultBean;
import com.bitvalue.health.api.responsebean.LoginBean;
import com.bitvalue.health.api.responsebean.VideoClientsResultBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.contract.doctorfriendscontract.DoctorFriendsContract;
import com.bitvalue.health.presenter.docfriendpersenter.DocFrienPersenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.VideoDocQuickAdapter;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.DensityUtil;
import com.bitvalue.health.util.MUtils;
import com.bitvalue.health.util.SharedPreManager;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.modules.chat.layout.input.InputLayoutUI;
import com.hjq.toast.ToastUtils;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMMessage;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;

/**
 * @author created by bitvalue
 * @data : 10/27
 * <p>
 * 医生好友界面
 */
public class DocFriendsFragment extends BaseFragment<DocFrienPersenter> implements DoctorFriendsContract.DoctorFriendsView {


    @BindView(R.id.tv_no_data)
    TextView tv_no_data;

    @BindView(R.id.tv_wait)
    TextView tv_wait;

    @BindView(R.id.tv_end)
    TextView tv_end;

//    @BindView(R.id.tv_new_count)
//    TextView tv_new_count;

    @BindView(R.id.layout_pot)
    LinearLayout layout_pot;

    @BindView(R.id.contact_list)
    RecyclerView contact_list;


    private HomeActivity homeActivity;

    private List<VideoClientsResultBean> videoClientsResultBeans = new ArrayList<>();
    private VideoDocQuickAdapter videoPatientQuickAdapter;
    private int newCount = 0;

    @Override
    protected DocFrienPersenter createPresenter() {
        return new DocFrienPersenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_doc_contacts;
    }

    //初始化当前Fragment的实例
    public static DocFriendsFragment getInstance(boolean is_need_toast) {
        DocFriendsFragment contactsFragment = new DocFriendsFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_need_toast", is_need_toast);
        contactsFragment.setArguments(bundle);
        return contactsFragment;
    }


    //初始化控件 和 Adapter
    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        mPresenter.listennerIMNewMessage();//开始监听新消息
        initListView();
        mPresenter.getConversationList(0, GET_CONVERSATION_COUNT);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }

    //获取数据
    @Override
    public void initData() {
        super.initData();
        generateData();
    }


    //这里由于后台暂无接口，所以模拟数据
    private void generateData() {
        List<VideoClientsResultBean.UserInfoDTO> userInfoDTOList = new ArrayList<>();
        userInfoDTOList.add(new VideoClientsResultBean.UserInfoDTO("张文博", 174));
        userInfoDTOList.add(new VideoClientsResultBean.UserInfoDTO("向侠", 174));
        userInfoDTOList.add(new VideoClientsResultBean.UserInfoDTO("吴汉江", 109));
        userInfoDTOList.add(new VideoClientsResultBean.UserInfoDTO("朱兆夫", 110));
        userInfoDTOList.add(new VideoClientsResultBean.UserInfoDTO("周智广", 111));
        userInfoDTOList.add(new VideoClientsResultBean.UserInfoDTO("黄添隆", 170));
        userInfoDTOList.add(new VideoClientsResultBean.UserInfoDTO("刘傥", 171));
        userInfoDTOList.add(new VideoClientsResultBean.UserInfoDTO("朱威宏", 172));
        userInfoDTOList.add(new VideoClientsResultBean.UserInfoDTO("宋德业", 173));
        userInfoDTOList.add(new VideoClientsResultBean.UserInfoDTO("毛新展", 174));
        userInfoDTOList.add(new VideoClientsResultBean.UserInfoDTO("熊羽", 174));

        for (int i = 0; i < userInfoDTOList.size(); i++) {
            LoginBean loginBean = SharedPreManager.getObject(Constants.KYE_USER_BEAN, LoginBean.class, homeActivity);
            if (loginBean == null) {
                return;
            }
            if (userInfoDTOList.get(i).userId != loginBean.getUser().user.userId) {
                VideoClientsResultBean videoClientsResultBean = new VideoClientsResultBean();
                videoClientsResultBean.id = "1440574800199196673";
                videoClientsResultBean.yljgdm = "444885559";
                videoClientsResultBean.patientId = 162;
                videoClientsResultBean.departmentCode = "1030200";
                videoClientsResultBean.doctorId = "109";
                videoClientsResultBean.appointmentTime = 1632326400000l;
                videoClientsResultBean.appointmentPeriodTime = "SW";
                videoClientsResultBean.seeTime = "08:30-09:00";
                videoClientsResultBean.appointmentType = 0;
                videoClientsResultBean.attendanceStatus = 3;
                videoClientsResultBean.updateTime = 1632650899000l;

                videoClientsResultBean.userInfo = new VideoClientsResultBean.UserInfoDTO();
                videoClientsResultBean.userInfo.userSex = "男";
                videoClientsResultBean.userInfo.phone = "13574111026";
                videoClientsResultBean.userInfo.userName = userInfoDTOList.get(i).userName;
                videoClientsResultBean.userInfo.userId = userInfoDTOList.get(i).userId;
                videoClientsResultBean.userInfo.userAge = 29 + i;
                videoClientsResultBeans.add(videoClientsResultBean);
            }
        }

        videoPatientQuickAdapter.setNewData(videoClientsResultBeans);
    }


    //初始化listView 及设置 Adapter
    private void initListView() {
        contact_list.setLayoutManager(new LinearLayoutManager(Objects.requireNonNull(getActivity())));
        contact_list.addItemDecoration(MUtils.spaceDivider(
                DensityUtil.dip2px(homeActivity, homeActivity.getResources().getDimension(R.dimen.qb_px_3)), false));
        videoPatientQuickAdapter = new VideoDocQuickAdapter(R.layout.item_video_patient, videoClientsResultBeans);
        videoPatientQuickAdapter.setOnItemClickListener((adapter, view, position) -> {
            VideoClientsResultBean videoClientsResultBean = videoClientsResultBeans.get(position);

            if (videoClientsResultBean.attendanceStatus == 1) {
                ToastUtils.show("患者未到就诊状态，现处于待就诊状态");
                return;
            }

            ClientsResultBean.UserInfoDTO userInfoDTO = new ClientsResultBean.UserInfoDTO();
            userInfoDTO.userId = videoClientsResultBean.userInfo.userId + "";
//                userInfoDTO.userId = 109 + "";
            userInfoDTO.userName = videoClientsResultBean.userInfo.userName;
            userInfoDTO.chatType = InputLayoutUI.CHAT_TYPE_DocFriends;    //区分是云门诊 还是健康管理   这里先定为 虚拟诊间  后续可能添加新的模块
            if (videoClientsResultBean.attendanceStatus == 4) {
                userInfoDTO.noInput = true;
            }
            userInfoDTO.planId = videoClientsResultBean.id;//预约id
//            homeActivity.switchSecondFragment(Constants.FRAGMENT_CHAT, userInfoDTO); //切换至聊天界面

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
//                    tv_new_count.setText(newCount + "");
                if (newCount == 0) {
                    layout_pot.setVisibility(View.GONE);
                }
            }

            videoPatientQuickAdapter.setNewData(videoClientsResultBeans);
        });
        contact_list.setAdapter(videoPatientQuickAdapter);
    }


    /****
     * 获取 聊天对话框 及 监听 新消息回调
     * @param v2TIMConversationResult
     */

    @Override
    public void getConversationSuccess(V2TIMConversationResult v2TIMConversationResult) {
        Log.e(TAG, "getConversationSuccess----------" );
    }

    /****
     * 获取 聊天对话框失败回调
     * @param
     */
    @Override
    public void getConversationfail(int code, String message) {
        Log.e(TAG, "getConversationfail-----------");
    }

    /**
     * IM新消息回调
     * @param v2TIMMessage
     */
    @Override
    public void onNewMessage(V2TIMMessage v2TIMMessage) {
        Log.e(TAG, "onNewMessage----------------" );
    }
}
