package com.bitvalue.health.ui.fragment.cloudclinic;

import static com.bitvalue.health.util.Constants.EVENT_MES_TYPE_CLOUDCLINC;
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

import com.bitvalue.health.api.eventbusbean.MsgRemindObj;
import com.bitvalue.health.api.eventbusbean.VideoRefreshObj;
import com.bitvalue.health.api.responsebean.ClientsResultBean;
import com.bitvalue.health.api.responsebean.VideoClientsResultBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.callback.OnItemClickCallback;
import com.bitvalue.health.contract.cloudcliniccontract.CloudClinicContract;
import com.bitvalue.health.presenter.cloudclinicpersenter.CloudClinicPersenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.CloudClinicAdapter;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.DataUtil;
import com.bitvalue.health.util.DensityUtil;
import com.bitvalue.health.util.MUtils;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.modules.chat.layout.input.InputLayoutUI;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.tencent.imsdk.message.Message;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author created by bitvalue
 * @data : 10/27
 * <p>
 * 云门诊 界面
 */
public class CloudClinicFragment extends BaseFragment<CloudClinicPersenter> implements CloudClinicContract.CloudClinicView, OnItemClickCallback {

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
    private int type = 0;  //用于区分 当前是点击的待就诊 还是 已结束
    private int newCount = 0;
    private HomeActivity homeActivity;
    private CloudClinicAdapter cloudClinicAdapter;
    private List<V2TIMConversation> TIMConversationList;
    private ArrayList<VideoClientsResultBean> tempListVideoClientsResultBeans = new ArrayList<>();


    @Override
    protected CloudClinicPersenter createPresenter() {
        return new CloudClinicPersenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_video_contacts;
    }

    //初始化当前Fragment实例
    public static CloudClinicFragment getInstance(boolean is_need_toast) {
        CloudClinicFragment contactsFragment = new CloudClinicFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_need_toast", is_need_toast);
        contactsFragment.setArguments(bundle);
        return contactsFragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }

    //初始化控件
    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        EventBus.getDefault().register(this);
        mPresenter.getConversationList(0, GET_CONVERSATION_COUNT);  // 获取聊天对话框
        mPresenter.listennerIMNewMessage();//监听IM新消息
        contact_list.setLayoutManager(new LinearLayoutManager(homeActivity));
        contact_list.addItemDecoration(MUtils.spaceDivider(
                DensityUtil.dip2px(homeActivity, homeActivity.getResources().getDimension(R.dimen.qb_px_3)), false));
        cloudClinicAdapter = new CloudClinicAdapter(R.layout.item_video_patient, tempListVideoClientsResultBeans, this);
        contact_list.setAdapter(cloudClinicAdapter);
    }


    /***
     * Eventbus 消息订阅处理
     * @param mainRefreshObj
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMessage(VideoRefreshObj mainRefreshObj) {
        Log.e(TAG, "onEventMessage:-----");
        cloudClinicAdapter.updateList(tempListVideoClientsResultBeans);
        cloudClinicAdapter.setNewData(tempListVideoClientsResultBeans);

        newCount = 0;
        setMsgCount();
        layout_pot.setVisibility(View.GONE);
        mPresenter.qryMedicalPatients(type == 0 ? "" : "4");

        EventBus.getDefault().post(new MsgRemindObj(EVENT_MES_TYPE_CLOUDCLINC, 0));
    }


    //点击事件
    @OnClick({R.id.tv_no_data, R.id.tv_wait, R.id.tv_end})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_no_data:
                break;

//                待就诊
            case R.id.tv_wait:
                tv_wait.setTextColor(homeActivity.getResources().getColor(R.color.white));
                tv_wait.setBackgroundResource(0);

                tv_end.setTextColor(homeActivity.getResources().getColor(R.color.main_blue));
                tv_end.setBackgroundResource(R.drawable.shape_bg_white_solid_2);

                newCount = 0;
                layout_pot.setVisibility(View.GONE);
                EventBus.getDefault().post(new MsgRemindObj(EVENT_MES_TYPE_CLOUDCLINC, 0));
                type = 0;
                mPresenter.qryMedicalPatients("");

                break;

//                已结束
            case R.id.tv_end:
                type = 1;
                tv_end.setTextColor(homeActivity.getResources().getColor(R.color.white));
                tv_end.setBackgroundResource(0);

                tv_wait.setTextColor(homeActivity.getResources().getColor(R.color.main_blue));
                tv_wait.setBackgroundResource(R.drawable.shape_bg_white_solid_1);

                mPresenter.qryMedicalPatients("4");
                break;
        }
    }


    /**
     * 成功获取对话框之后 开始请求接口 获取数据
     *
     * @param v2TIMConversationResult
     */
    @Override
    public void getConversationSuccess(V2TIMConversationResult v2TIMConversationResult) {
        Log.e(TAG, "getConversationSuccess--------------- ");
        TIMConversationList = v2TIMConversationResult.getConversationList();
        tempListVideoClientsResultBeans.clear();
        mPresenter.qryMedicalPatients("");
    }

    @Override
    public void getConversationfail(int code, String message) {

    }


    /***
     * 监听IM新消息回调
     * @param v2TIMMessage
     */
    @Override
    public void onNewMessage(V2TIMMessage v2TIMMessage) {
        String s = new Gson().toJson(v2TIMMessage);
        Log.d("httpVIDEO", s);
        if (null != v2TIMMessage) {
            Message message = v2TIMMessage.getMessage();
            if (!message.getGroupID().isEmpty()) {//区分健康咨询消息，这里不关注健康咨询消息
                return;
            }
            for (int i = 0; i < tempListVideoClientsResultBeans.size(); i++) {
                if (message.getSenderUserID().equals(tempListVideoClientsResultBeans.get(i).userInfo.userId + "")) {
                    tempListVideoClientsResultBeans.get(i).hasNew = true;
                    tempListVideoClientsResultBeans.get(i).newMsgNum++;
                    newCount++;

                    setMsgCount();
                    if (null != layout_pot) {
                        layout_pot.setVisibility(View.VISIBLE);
                    }
                    EventBus.getDefault().post(new MsgRemindObj(EVENT_MES_TYPE_CLOUDCLINC, newCount));
                }
            }

            if (null != cloudClinicAdapter) {
                cloudClinicAdapter.setNewData(tempListVideoClientsResultBeans);
            }
        }
    }


    /***
     * 获取云门诊就诊列表查询 成功
     * @param result
     */

    @Override
    public void qryMedicalPatientsSuccess(ArrayList<VideoClientsResultBean> result) {
        getActivity().runOnUiThread(() -> {
            tempListVideoClientsResultBeans.clear();
            tempListVideoClientsResultBeans = result;
            tv_no_data.setVisibility(tempListVideoClientsResultBeans.size() > 0 ? View.GONE : View.VISIBLE);
            if (null != result && result.size() > 0) {
                cloudClinicAdapter.updateList(tempListVideoClientsResultBeans);
                //初始化所有的新消息
                for (int i = 0; i < tempListVideoClientsResultBeans.size(); i++) {
                    tempListVideoClientsResultBeans.get(i).newMsgNum = DataUtil.getUnreadCount(true,
                            tempListVideoClientsResultBeans.get(i).userInfo.userId + "", TIMConversationList);
                    if (tempListVideoClientsResultBeans.get(i).newMsgNum > 0) {
                        tempListVideoClientsResultBeans.get(i).hasNew = true;
                    }
                }

                //计算消息总数
                for (int i = 0; i < tempListVideoClientsResultBeans.size(); i++) {
                    newCount = newCount + tempListVideoClientsResultBeans.get(i).newMsgNum;
                }
                //展示消息总数
                setMsgCount();
                if (newCount > 0) {
                    layout_pot.setVisibility(View.VISIBLE);
                    EventBus.getDefault().post(new MsgRemindObj(EVENT_MES_TYPE_CLOUDCLINC, newCount));
                }
            }else {
                Log.e(TAG, "暂无数据-----" );
            }
            cloudClinicAdapter.setNewData(tempListVideoClientsResultBeans);
        });
    }

    /***
     * 获取云门诊就诊列表查询 成功    但是data = null  做相应的显示
     * @param
     */
    @Override
    public void qryMedicalPatientsNotData() {
        Log.e(TAG, "qryMedicalPatientsNotData: ");
    }

    /**
     * /***
     * * 获取云门诊就诊列表查询 失败
     * * @param result
     *
     * @param failMessage
     */
    @Override
    public void qryMedicalPatientsFail(String failMessage) {

    }


    /**
     * listview 列表item的点击事件
     *
     * @param position
     * @param newMessage
     */
    @Override
    public void onItemClick(VideoClientsResultBean position, int newMessage) {
        VideoClientsResultBean videoClientsResultBean = position;

        if (videoClientsResultBean.attendanceStatus == 1) {
            ToastUtils.show("患者未到就诊状态，现处于待就诊状态");
            return;
        }

        ClientsResultBean.UserInfoDTO userInfoDTO = new ClientsResultBean.UserInfoDTO();
        userInfoDTO.userId = videoClientsResultBean.userInfo.userId + "";
//                userInfoDTO.userId = 109 + "";
        userInfoDTO.userName = videoClientsResultBean.userInfo.userName;
        userInfoDTO.chatType = InputLayoutUI.CHAT_TYPE_VIDEO;
        if (videoClientsResultBean.attendanceStatus == 4) {
            userInfoDTO.noInput = true;
        }
        userInfoDTO.planId = videoClientsResultBean.id;//预约id
        homeActivity.switchSecondFragment(Constants.FRAGMENT_CHAT, userInfoDTO);  //切换至聊天界面

        //点击的那个如果是有新信息的，给置成没新信息的状态
        newCount = newCount - newMessage;
        setMsgCount();
        if (newCount <= 0) {
            layout_pot.setVisibility(View.GONE);
            EventBus.getDefault().post(new MsgRemindObj(EVENT_MES_TYPE_CLOUDCLINC, 0));
        }

        cloudClinicAdapter.setNewData(tempListVideoClientsResultBeans);
    }


    private void setMsgCount() {
        tv_new_count.setText(newCount > 99 ? newCount + "+" : newCount + "");
    }
}
