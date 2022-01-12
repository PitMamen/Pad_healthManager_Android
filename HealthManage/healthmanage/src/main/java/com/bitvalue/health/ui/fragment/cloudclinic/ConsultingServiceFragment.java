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
import com.bitvalue.health.api.requestbean.RequestNewLeaveBean;
import com.bitvalue.health.api.responsebean.ClientsResultBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.api.responsebean.VideoClientsResultBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.callback.OnItemClick;
import com.bitvalue.health.callback.OnItemClickCallback;
import com.bitvalue.health.contract.cloudcliniccontract.CloudClinicContract;
import com.bitvalue.health.presenter.cloudclinicpersenter.CloudClinicPersenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.AllPatientAdapter;
import com.bitvalue.health.ui.adapter.CloudClinicAdapter;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.DataUtil;
import com.bitvalue.health.util.DensityUtil;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.MUtils;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.modules.chat.layout.input.ChatLayout;
import com.bitvalue.sdk.collab.modules.chat.layout.input.InputLayoutUI;
import com.bitvalue.sdk.collab.modules.conversation.ConversationLayout;
import com.bitvalue.sdk.collab.modules.conversation.ConversationListLayout;
import com.bitvalue.sdk.collab.modules.conversation.base.ConversationInfo;
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
 * 咨询 界面
 */
public class ConsultingServiceFragment extends BaseFragment<CloudClinicPersenter> implements CloudClinicContract.CloudClinicView, OnItemClick {


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
    @BindView(R.id.versationlist_layout)
    ConversationLayout conversationLayout;

    private int type = 0;  //用于区分 当前是点击的待就诊 还是 已结束
    private int newCount = 0;
    private HomeActivity homeActivity;
    private CloudClinicAdapter allPatientAdapter;
    private RequestNewLeaveBean requestNewLeaveBean = new RequestNewLeaveBean();
    private int pageNo = 1;
    private int pageSize = 30;
    private List<NewLeaveBean.RowsDTO> patientList = new ArrayList<>();  //所有患者列表
    List<NewLeaveBean.RowsDTO> tempPatientList = new ArrayList<>();
    private boolean convasersionInited = false;
    @Override
    protected CloudClinicPersenter createPresenter() {
        return new CloudClinicPersenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_video_contacts;
    }

    //初始化当前Fragment实例
    public static ConsultingServiceFragment getInstance(boolean is_need_toast) {
        ConsultingServiceFragment contactsFragment = new ConsultingServiceFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_need_toast", is_need_toast);
        contactsFragment.setArguments(bundle);
        return contactsFragment;
    }


    @Override
    public void onPause() {
        super.onPause();
        convasersionInited = false;
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

        contact_list.setVisibility(View.GONE);

        contact_list.setLayoutManager(new LinearLayoutManager(homeActivity));
        contact_list.addItemDecoration(MUtils.spaceDivider(
                DensityUtil.dip2px(homeActivity, homeActivity.getResources().getDimension(R.dimen.qb_px_3)), false));
        allPatientAdapter = new CloudClinicAdapter(R.layout.item_video_patient, patientList, this);
        contact_list.setAdapter(allPatientAdapter);
    }


    /***
     * Eventbus 消息订阅处理
     * @param mainRefreshObj
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMessage(VideoRefreshObj mainRefreshObj) {
        Log.e(TAG, "onEventMessage:-----");
        requestNewLeaveBean.setPageNo(pageNo);
        requestNewLeaveBean.setPageSize(pageSize);
        mPresenter.qryMedicalPatients(requestNewLeaveBean);
        conversationLayout.initDefault();
        conversationLayout.getConversationList().setOnItemClickListener((view12, position, messageInfo) -> {
            NewLeaveBean.RowsDTO info = new NewLeaveBean.RowsDTO();
            info.setUserName(messageInfo.getTitle());
            info.setUserId(messageInfo.getId());
            homeActivity.switchSecondFragment(Constants.FRAGMENT_CHAT,info);
        });
        conversationLayout.setVisibility(View.VISIBLE);
    }


    //点击事件
    @OnClick({ R.id.tv_wait, R.id.tv_end})
    public void onClick(View view) {
        switch (view.getId()) {

//                咨询患者
            case R.id.tv_wait:
                tv_wait.setTextColor(homeActivity.getResources().getColor(R.color.white));
                tv_wait.setBackgroundResource(0);

                tv_end.setTextColor(homeActivity.getResources().getColor(R.color.main_blue));
                tv_end.setBackgroundResource(R.drawable.shape_bg_white_solid_2);

                newCount = 0;
                layout_pot.setVisibility(View.GONE);
                contact_list.setVisibility(View.GONE);
                conversationLayout.setVisibility(View.VISIBLE);
//                EventBus.getDefault().post(new MsgRemindObj(EVENT_MES_TYPE_CLOUDCLINC, 0));
                type = 0;

                break;

//                所有患者
            case R.id.tv_end:
                convasersionInited = false;
                contact_list.setVisibility(View.VISIBLE);
                conversationLayout.setVisibility(View.GONE);
                tv_end.setTextColor(homeActivity.getResources().getColor(R.color.white));
                tv_end.setBackgroundResource(0);

                tv_wait.setTextColor(homeActivity.getResources().getColor(R.color.main_blue));
                tv_wait.setBackgroundResource(R.drawable.shape_bg_white_solid_1);
                if (allPatientAdapter != null) {
                    if (tempPatientList.size() == patientList.size()){
                        Log.e(TAG, "11111111111111" );
                        allPatientAdapter.setNewData(tempPatientList);
                    }else {
                        Log.e(TAG, "00000000000000" );
                        requestNewLeaveBean.setPageNo(pageNo);
                        requestNewLeaveBean.setPageSize(pageSize);
                        mPresenter.qryMedicalPatients(requestNewLeaveBean);
                    }

                }


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
//        TIMConversationList = v2TIMConversationResult.getConversationList();
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
    }


    /***
     * 获取云门诊就诊列表查询 成功
     * @param
     */

    @Override
    public void qryMedicalPatientsSuccess(List<NewLeaveBean.RowsDTO> itinfoDetailDTOList) {
        getActivity().runOnUiThread(() -> {
            Log.e(TAG, "更新UI-----");
            hideDialog();
            tempPatientList = itinfoDetailDTOList;
            patientList = tempPatientList;
            allPatientAdapter.setNewData(tempPatientList);
        });
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


    @Override
    public void onItemClick(Object object, boolean isCheck) {

    }
}
