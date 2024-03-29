package com.bitvalue.health.ui.fragment.cloudclinic;

import static com.bitvalue.health.util.Constants.ACOUNT_SERVICE;
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

import com.bitvalue.health.api.eventbusbean.VideoRefreshObj;
import com.bitvalue.health.api.requestbean.AllocatedPatientRequest;
import com.bitvalue.health.api.requestbean.RequestNewLeaveBean;
import com.bitvalue.health.api.responsebean.LoginBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.callback.OnItemClick;
import com.bitvalue.health.contract.cloudcliniccontract.CloudClinicContract;
import com.bitvalue.health.presenter.cloudclinicpersenter.CloudClinicPersenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.AllPatientAdapter;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.DensityUtil;
import com.bitvalue.health.util.MUtils;
import com.bitvalue.health.util.SharedPreManager;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.modules.conversation.ConversationLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

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

    @BindView(R.id.rl_status_refresh)
    SmartRefreshLayout smartRefreshLayout;

    @BindView(R.id.contact_list)
    RecyclerView contact_list;

    @BindView(R.id.versationlist_layout)
    ConversationLayout conversationLayout;

    private int type = 0;  //用于区分 当前是点击的待就诊 还是 已结束
    private int newCount = 0;
    private HomeActivity homeActivity;
    private AllPatientAdapter allPatientAdapter;
    private AllocatedPatientRequest requestNewLeaveBean = new AllocatedPatientRequest();
    private int pageNo = 1;
    private final int pageSize = 20;
    private int currentPage = 0;
    private List<NewLeaveBean.RowsDTO> patientList = new ArrayList<>();  //所有患者列表
    List<NewLeaveBean.RowsDTO> tempPatientList = new ArrayList<>();
    private boolean convasersionInited = false;
    LoginBean loginBean;

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
    public void onResume() {
        super.onResume();
        if (loginBean != null) {
            if (loginBean.getAccount().roleName.equals(ACOUNT_SERVICE)) {
                initConversation();
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            if (loginBean != null) {
                if (loginBean.getAccount().roleName.equals(ACOUNT_SERVICE)) {
                    initConversation();
                }
            }
        }
    }

    private void initConversation() {
        requestNewLeaveBean.pageNo = pageNo;
        requestNewLeaveBean.pageSize = pageSize;
        conversationLayout.initDefault();
        mPresenter.qryMedicalPatients(requestNewLeaveBean);
        conversationLayout.getConversationList().setOnItemClickListener((view12, position, messageInfo) -> {
            NewLeaveBean.RowsDTO info = new NewLeaveBean.RowsDTO();
            info.setUserName(messageInfo.getTitle());
            info.setUserId(messageInfo.getId());
            info.isShowCollection = false;  //咨询界面跳转过去  聊天界面底部不显示 预诊收集 按钮
            info.isShowSendRemind = false;
            homeActivity.switchSecondFragment(Constants.FRAGMENT_CHAT, info);
        });
        conversationLayout.setVisibility(smartRefreshLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
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
        loginBean = SharedPreManager.getObject(Constants.KYE_USER_BEAN, LoginBean.class, homeActivity);
        mPresenter.getConversationList(0, GET_CONVERSATION_COUNT);  // 获取聊天对话框
//        mPresenter.listennerIMNewMessage();//监听IM新消息

        smartRefreshLayout.setVisibility(View.GONE);
        initAllPatientList();
    }


    private void initAllPatientList() {
        contact_list.setLayoutManager(new LinearLayoutManager(homeActivity));
//        contact_list.addItemDecoration(MUtils.spaceDivider(DensityUtil.dip2px(homeActivity, homeActivity.getResources().getDimension(R.dimen.qb_px_3)), false));
        allPatientAdapter = new AllPatientAdapter(R.layout.item_video_patient, patientList, this);
        contact_list.setAdapter(allPatientAdapter);
        allPatientAdapter.setOnItemClickListener((adapter, view, position) -> {
//            homeActivity.switchSecondFragment(Constants.FRAGMENT_DETAIL, patientList.get(position));
            homeActivity.switchSecondFragment(Constants.DATA_REVIEW, patientList.get(position).getUserId());
        });

        //        上下拉刷新 最外层的
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {

            //上拉刷新
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                // TODO: 2021/12/8 加载下一页
                Log.e(TAG, "onLoadMore222: " + pageNo + " currentPage: " + currentPage);
                if (currentPage == pageNo) {
                    pageNo = 1;
                    Log.e(TAG, "无更多数据");
                    smartRefreshLayout.finishLoadMore();
                    smartRefreshLayout.finishRefresh();
                    return;
                }
                pageNo++;
                requestNewLeaveBean.pageNo = pageNo;
                mPresenter.qryMedicalPatients(requestNewLeaveBean);
                smartRefreshLayout.finishLoadMore();
                smartRefreshLayout.finishRefresh();
            }

            //下拉刷新
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                Log.e(TAG, "onLoadMore222: " + pageNo);
                if (pageNo > 1) {
                    pageNo--;
                } else {
                    smartRefreshLayout.finishRefresh();
                    return;
                }
                requestNewLeaveBean.pageNo = pageNo;
                mPresenter.qryMedicalPatients(requestNewLeaveBean);
                smartRefreshLayout.finishRefresh();
            }
        });


    }


    /***
     * Eventbus 消息订阅处理
     * @param mainRefreshObj
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMessage(VideoRefreshObj mainRefreshObj) {
        requestNewLeaveBean.pageNo = pageNo;
        requestNewLeaveBean.pageSize = pageSize;
        conversationLayout.initDefault();
        mPresenter.qryMedicalPatients(requestNewLeaveBean);
        conversationLayout.getConversationList().setOnItemClickListener((view12, position, messageInfo) -> {
            NewLeaveBean.RowsDTO info = new NewLeaveBean.RowsDTO();
            info.setUserName(messageInfo.getTitle());
            info.setUserId(messageInfo.getId());
            info.isShowCollection = false;  //咨询界面跳转过去  聊天界面底部不显示 预诊收集 按钮
            info.isShowSendRemind = false;
            homeActivity.switchSecondFragment(Constants.FRAGMENT_CHAT, info);
        });
        conversationLayout.setVisibility(smartRefreshLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }


    //点击事件
    @OnClick({R.id.tv_wait, R.id.tv_end})
    public void onClick(View view_) {
        switch (view_.getId()) {

//                咨询患者
            case R.id.tv_wait:
                tv_wait.setTextColor(homeActivity.getResources().getColor(R.color.white));
                tv_wait.setBackgroundResource(0);

                tv_end.setTextColor(homeActivity.getResources().getColor(R.color.main_blue));
                tv_end.setBackgroundResource(R.drawable.shape_bg_white_solid_2);

                newCount = 0;
                layout_pot.setVisibility(View.GONE);
                smartRefreshLayout.setVisibility(View.GONE);
                conversationLayout.setVisibility(View.VISIBLE);
//                EventBus.getDefault().post(new MsgRemindObj(EVENT_MES_TYPE_CLOUDCLINC, 0));
                type = 0;

                break;

//                所有患者
            case R.id.tv_end:
                convasersionInited = false;
                smartRefreshLayout.setVisibility(View.VISIBLE);
                conversationLayout.setVisibility(View.GONE);
                tv_end.setTextColor(homeActivity.getResources().getColor(R.color.white));
                tv_end.setBackgroundResource(0);

                tv_wait.setTextColor(homeActivity.getResources().getColor(R.color.main_blue));
                tv_wait.setBackgroundResource(R.drawable.shape_bg_white_solid_1);
                if (allPatientAdapter != null) {
                    if (tempPatientList.size() != 0 && (tempPatientList.size() == patientList.size())) {
                        allPatientAdapter.setNewData(tempPatientList);
                    } else {
                        requestNewLeaveBean.pageNo = pageNo;
                        requestNewLeaveBean.pageSize = pageSize;
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
//        List<V2TIMConversation> list = v2TIMConversationResult.getConversationList();
//        if (list!=null){
//            Log.e(TAG, "getConversationSuccess: "+list.size() );
//
//        }
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
        getActivity().runOnUiThread(() -> hideDialog());
    }


    @Override
    public void onItemClick(Object object, boolean isCheck) {

    }
}
