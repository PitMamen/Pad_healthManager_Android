package com.bitvalue.health.ui.fragment.healthmanage;

import static com.bitvalue.health.util.Constants.FRAGMENT_INTERESTSUSER_APPLY;
import static com.bitvalue.health.util.Constants.FRAGMENT_INTERESTSUSER_APPLY_BYDOC;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bitvalue.health.api.ApiResult;
import com.bitvalue.health.api.eventbusbean.NotifyactionObj;
import com.bitvalue.health.api.requestbean.filemodel.SystemRemindObj;
import com.bitvalue.health.api.responsebean.LoginBean;
import com.bitvalue.health.api.responsebean.TaskDeatailBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.callback.OnRightClickCallBack;
import com.bitvalue.health.contract.doctorfriendscontract.NeedDealWithContract;
import com.bitvalue.health.presenter.docfriendpersenter.DocFrienPersenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.AlreadyDealithAdapter;
import com.bitvalue.health.ui.adapter.NeedDealithQuickAdapter;
import com.bitvalue.health.util.ClickUtils;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.DensityUtil;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.GsonUtils;
import com.bitvalue.health.util.MUtils;
import com.bitvalue.health.util.RSAEncrypt;
import com.bitvalue.health.util.SharedPreManager;
import com.bitvalue.health.util.customview.WrapRecyclerView;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.modules.search.SearchMoreMsgListActivity;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.OnHttpListener;
import com.hjq.toast.ToastUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author created by bitvalue
 * @data : 10/27
 * <p>
 * 待办界面
 */
public class NeedDealWithFragment extends BaseFragment<DocFrienPersenter> implements NeedDealWithContract.NeedDealWithView, OnRightClickCallBack {


    @BindView(R.id.rl_needdeal)
    RelativeLayout rl_needdealwith;

    @BindView(R.id.rl_already_dealwith)
    RelativeLayout rl_already_deal;

    @BindView(R.id.tv_need_deal)
    TextView tv_needdeal_with;  //待办

    @BindView(R.id.tv_already_deal)
    TextView tv_already_deal;  //已办

    @BindView(R.id.rl_default_view)
    RelativeLayout rl_default_layout;


    @BindView(R.id.rl_needdeal_refresh)
    SmartRefreshLayout rl_needdeal_refresh; //所有的上下拉刷新

    @BindView(R.id.list_newly_discharged_patient)
    WrapRecyclerView list_allneeddealwith;  //所有的待办列表


//    @BindView(R.id.layout_search_result)
//    SmartRefreshLayout smartsearch_RefreshLayout; //搜索出来的上下拉刷新
//
//    @BindView(R.id.search_allpatient)
//    WrapRecyclerView list_search_needdealwith;  //搜索出来的待办列表


    /**
     * 已办
     */
    @BindView(R.id.rl_already_refresh)
    SmartRefreshLayout already_RefreshLayout; //所有的上下拉刷新
    @BindView(R.id.list_alreadylist)
    WrapRecyclerView list_alreadyPatient;


    @BindView(R.id.layout_search_unregister)
    SmartRefreshLayout search_already_RefreshLayout;
    @BindView(R.id.unregister_search_allpatient)
    WrapRecyclerView search_alreadyPatient;


    @BindView(R.id.ll_needdeal_patient)
    LinearLayout ll_needDealWithlayoout;
    @BindView(R.id.ll_already_deal)
    LinearLayout ll_already_layout;


    private HomeActivity homeActivity;

    private List<TaskDeatailBean> NeedDealWithList = new ArrayList<>();
    private List<TaskDeatailBean> AlradDealWithList = new ArrayList<>();
    private NeedDealithQuickAdapter needDealWithQuickAdapter;
    private AlreadyDealithAdapter AlreadyDealithAdapter;
    private LoginBean loginBean;
    private boolean isNeedbuttonClick = true;
    private Timer timer;

    @Override
    protected DocFrienPersenter createPresenter() {
        return new DocFrienPersenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_needdealwith_layout;
    }

    //初始化当前Fragment的实例
    public static NeedDealWithFragment getInstance(boolean is_need_toast) {
        NeedDealWithFragment contactsFragment = new NeedDealWithFragment();
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

    //初始化控件 和 Adapter
    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        loginBean = SharedPreManager.getObject(Constants.KYE_USER_BEAN, LoginBean.class, homeActivity);
        initListView();
        EventBus.getDefault().register(this);
    }


    //获取数据
    @Override
    public void initData() {
        super.initData();
        getNeedDealWithData();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getNeedDealWithData();
            }
        }, 0, 1 * 60 * 1000);  //一分钟执行一次

    }





    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getNeedDealWithData();
            getAlradydata();
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timer!=null){
            timer.cancel();
            timer.purge();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    //获取待办列表
    private void getNeedDealWithData() {
        mPresenter.getMyTaskDetail(0, 9, String.valueOf(loginBean.getUser().user.userId));
    }

    //获取已办列表
    private void getAlradydata() {
        mPresenter.getMyAlreadyDealTaskDetail(1, 9, String.valueOf(loginBean.getUser().user.userId));
    }


    @OnClick({R.id.rl_needdeal, R.id.rl_already_dealwith})
    public void OnClick(View view) {
        switch (view.getId()) {
            //待办
            case R.id.rl_needdeal:
                isNeedbuttonClick = true;
                tv_needdeal_with.setTextColor(homeActivity.getColor(R.color.white));
                rl_needdealwith.setBackground(homeActivity.getDrawable(R.drawable.needdealwith_select));

                tv_already_deal.setTextColor(homeActivity.getColor(R.color.black));
                rl_already_deal.setBackground(homeActivity.getDrawable(R.drawable.shape_needdealwith_unselect));
                ll_already_layout.setVisibility(View.GONE);
                ll_needDealWithlayoout.setVisibility(View.VISIBLE);

                if (needDealWithQuickAdapter != null) {
                    rl_default_layout.setVisibility(NeedDealWithList != null && NeedDealWithList.size() > 0 ? View.GONE : View.VISIBLE);
                    if (NeedDealWithList != null) {
                        needDealWithQuickAdapter.setNewData(NeedDealWithList);
                    }
                }
                break;

            //已办
            case R.id.rl_already_dealwith:
                isNeedbuttonClick = false;
                tv_already_deal.setTextColor(homeActivity.getColor(R.color.white));
                rl_already_deal.setBackground(homeActivity.getDrawable(R.drawable.needdealwith_select));

                tv_needdeal_with.setTextColor(homeActivity.getColor(R.color.black));
                rl_needdealwith.setBackground(homeActivity.getDrawable(R.drawable.shape_needdealwith_unselect));

                ll_already_layout.setVisibility(View.VISIBLE);
                ll_needDealWithlayoout.setVisibility(View.GONE);

                if (AlreadyDealithAdapter != null) {
                    rl_default_layout.setVisibility(AlradDealWithList != null && AlradDealWithList.size() > 0 ? View.GONE : View.VISIBLE);
//                    if (AlradDealWithList != null) {
//                        AlreadyDealithAdapter.setNewData(AlradDealWithList);
//                    }

                    if (!ClickUtils.isFastClick()) {
                        getAlradydata();
                    }
                }
                break;
        }
    }


    //初始化listView 及设置 Adapter
    private void initListView() {
        list_allneeddealwith.setLayoutManager(new LinearLayoutManager(Objects.requireNonNull(getActivity())));
//        list_allneeddealwith.addItemDecoration(MUtils.spaceDivider(DensityUtil.dip2px(homeActivity, homeActivity.getResources().getDimension(R.dimen.qb_px_3)), false));
        needDealWithQuickAdapter = new NeedDealithQuickAdapter(R.layout.item_need_dealwith_layout, NeedDealWithList, loginBean.getAccount().roleName.equals("casemanager"), this);
        list_allneeddealwith.setAdapter(needDealWithQuickAdapter);


        list_alreadyPatient.setLayoutManager(new LinearLayoutManager(Objects.requireNonNull(getActivity())));
//        list_alreadyPatient.addItemDecoration(MUtils.spaceDivider(DensityUtil.dip2px(homeActivity, homeActivity.getResources().getDimension(R.dimen.qb_px_3)), false));
        AlreadyDealithAdapter = new AlreadyDealithAdapter(R.layout.item_need_dealwith_layout, AlradDealWithList, this);
        list_alreadyPatient.setAdapter(AlreadyDealithAdapter);
    }


    private void hideShowList() {
        if (isNeedbuttonClick) {
            ll_needDealWithlayoout.setVisibility(View.VISIBLE);
            ll_already_layout.setVisibility(View.GONE);
        } else {
            ll_needDealWithlayoout.setVisibility(View.GONE);
            ll_already_layout.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 查询我的任务 成功回调
     *
     * @param taskDeatailBeanList
     */
    @Override
    public void getMyTaskDetailSuccess(List<TaskDeatailBean> taskDeatailBeanList) {
        homeActivity.runOnUiThread(() -> {
            hideShowList();
            NeedDealWithList = taskDeatailBeanList;
            homeActivity.showOrHideImageBoll(NeedDealWithList != null && taskDeatailBeanList.size() > 0);  //如果有待办任务 通知左侧状态栏 视图更新
            needDealWithQuickAdapter.setNewData(NeedDealWithList);
            if (NeedDealWithList != null && NeedDealWithList.size() > 0) {
                rl_default_layout.setVisibility(View.GONE);
            } else {
                rl_default_layout.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 查询我的任务 失败回调
     *
     * @param failMessage
     */
    @Override
    public void getMyTaskDetailFail(String failMessage) {
        homeActivity.runOnUiThread(() -> ToastUtils.show("获取待办列表失败!"));
    }


    /**
     * 查询已办 任务 成功回调
     *
     * @param taskDeatailBeanList
     */
    @Override
    public void getMyAlreadyDealTaskDetailSuccess(List<TaskDeatailBean> taskDeatailBeanList) {
        homeActivity.runOnUiThread(() -> {
            hideShowList();
            AlradDealWithList = taskDeatailBeanList;
            AlreadyDealithAdapter.setNewData(AlradDealWithList);
            if (AlradDealWithList != null && AlradDealWithList.size() > 0) {
                rl_default_layout.setVisibility(View.GONE);
            } else {
                rl_default_layout.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 查询已办 任务 失败回调
     *
     * @param failMessage
     */
    @Override
    public void getMyAlreadyDealTaskDetailFail(String failMessage) {
//        homeActivity.runOnUiThread(() -> ToastUtils.show("获取已办列表失败!"));
    }


    /**
     * 点击权益申请 跳转至 权益申请界面
     *
     * @param taskDeatailBean
     */
    @Override
    public void OnItemClick(TaskDeatailBean taskDeatailBean) {
        // TODO: 2022/2/22 这里要区分 是个案管理师登录的还是医生登录的
        if (loginBean != null) {
            homeActivity.switchSecondFragment(loginBean.getAccount().roleName.equals("casemanager") ? FRAGMENT_INTERESTSUSER_APPLY : FRAGMENT_INTERESTSUSER_APPLY_BYDOC, taskDeatailBean);  //个案师
        }
    }


    /**
     * 发送提醒
     *
     * @param taskDeatailBean
     */
    @Override
    public void OnRemindClick(TaskDeatailBean taskDeatailBean) {
        if (EmptyUtil.isEmpty(taskDeatailBean) || EmptyUtil.isEmpty(taskDeatailBean.getTaskDetail()) || taskDeatailBean.getTaskDetail().getUserInfo() == null) {
            Log.e(TAG, "医生发送提醒 taskDeatailBean.getTaskDetail() == null");
            return;
        }
        String jsonString = GsonUtils.ModelToJson(taskDeatailBean.getTaskDetail());
        sendSystemRemind(jsonString, String.valueOf(taskDeatailBean.getTaskDetail().getUserInfo().getUserId()));
    }


    //发送通知
    private void sendSystemRemind(String jsonString, String userID) {
        SystemRemindObj systemRemindObj = new SystemRemindObj();
        systemRemindObj.remindType = "videoRemind";
        systemRemindObj.userId = userID;
        systemRemindObj.eventType = 5;
        systemRemindObj.infoDetail = jsonString;
        EasyHttp.post(homeActivity).api(systemRemindObj).request(new OnHttpListener<ApiResult<String>>() {
            @Override
            public void onSucceed(ApiResult<String> result) {
                ToastUtils.show("操作成功!");
            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }


    /**
     * 更新待办数据 重新请求接口
     *
     * @param notifyactionObj
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateNeedDelaWith(NotifyactionObj notifyactionObj) {
        Log.e(TAG, "更新待办处理----");
        getNeedDealWithData();
    }




}
