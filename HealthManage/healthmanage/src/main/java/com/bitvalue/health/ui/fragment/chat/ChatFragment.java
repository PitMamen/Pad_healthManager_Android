package com.bitvalue.health.ui.fragment.chat;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import static com.bitvalue.health.util.Constants.FRAGMENT_ADD_PAPER;
import static com.bitvalue.health.util.Constants.FRAGMENT_ADD_QUESTION;
import static com.bitvalue.health.util.Constants.FRAGMENT_QUICKREPLY;
import static com.tencent.imsdk.v2.V2TIMConversation.V2TIM_C2C;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.ApiResult;
import com.bitvalue.health.api.eventbusbean.NotiflyUIObj;
import com.bitvalue.health.api.eventbusbean.NotifycationAlardyObj;
import com.bitvalue.health.api.eventbusbean.RefreshDataViewObj;
import com.bitvalue.health.api.requestbean.CallRequest;
import com.bitvalue.health.api.requestbean.QuestionResultBean;
import com.bitvalue.health.api.requestbean.QuickReplyRequest;
import com.bitvalue.health.api.requestbean.SaveRightsUseBean;
import com.bitvalue.health.api.requestbean.UpdateRightsRequestTimeRequestBean;
import com.bitvalue.health.api.requestbean.filemodel.SystemRemindObj;
import com.bitvalue.health.api.responsebean.CallResultBean;
import com.bitvalue.health.api.responsebean.DataReViewRecordResponse;
import com.bitvalue.health.api.responsebean.LoginBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.api.responsebean.AnswerResultBean;
import com.bitvalue.health.api.responsebean.QueryRightsRecordBean;
import com.bitvalue.health.api.responsebean.SaveCaseApi;
import com.bitvalue.health.api.responsebean.TaskDeatailBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.contract.healthmanagercontract.InterestsUseApplyByDocContract;
import com.bitvalue.health.model.planmodel.GetAnserListApi;
import com.bitvalue.health.model.planmodel.GetQuestionByKeyWordApi;
import com.bitvalue.health.presenter.healthmanager.InterestsUseApplyByDocPresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.DialogItemAdapter;
import com.bitvalue.health.ui.adapter.DialogItemAnswerAdapter;
import com.bitvalue.health.ui.adapter.MedicalRecordAdapter;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.DataUtil;
import com.bitvalue.health.util.DensityUtil;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.GsonUtils;
import com.bitvalue.health.util.SharedPreManager;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.health.util.chatUtil.CustomAnalyseMessage;
import com.bitvalue.health.util.chatUtil.CustomDoctorReceptionMessage;
import com.bitvalue.health.util.chatUtil.CustomRefuseVisitMessage;
import com.bitvalue.health.util.chatUtil.CustomVideoCallMessageController;
import com.bitvalue.health.util.chatUtil.CustomWenJuanMessage;
import com.bitvalue.health.util.customview.dialog.MedicalRecordFolderDialog;
import com.bitvalue.health.util.customview.dialog.PrePiagnosisCollectionDialog;
import com.bitvalue.health.util.customview.dialog.PrediagnosisDataDialog;
import com.bitvalue.health.util.customview.dialog.RefusalDiagnosisDialog;
import com.bitvalue.health.util.customview.dialog.SummaryDialog;
import com.bitvalue.health.util.customview.WrapRecyclerView;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.base.IUIKitCallBack;
import com.bitvalue.sdk.collab.component.TitleBarLayout;
import com.bitvalue.sdk.collab.helper.ChatLayoutHelper;
import com.bitvalue.sdk.collab.helper.CustomMessage;
import com.bitvalue.sdk.collab.helper.CustomVideoCallMessage;
import com.bitvalue.sdk.collab.modules.chat.base.ChatInfo;
import com.bitvalue.sdk.collab.modules.chat.layout.input.ChatLayout;
import com.bitvalue.sdk.collab.modules.chat.layout.input.InputLayout;
import com.bitvalue.sdk.collab.modules.chat.layout.message.MessageLayout;
import com.bitvalue.sdk.collab.modules.forward.ForwardSelectActivity;
import com.bitvalue.sdk.collab.modules.group.info.GroupInfo;
import com.bitvalue.sdk.collab.modules.group.info.StartGroupMemberSelectActivity;
import com.bitvalue.sdk.collab.modules.message.MessageInfo;
import com.bitvalue.sdk.collab.modules.message.MessageInfoUtil;
import com.bitvalue.sdk.collab.utils.PermissionUtils;
import com.bitvalue.sdk.collab.utils.TUIKitConstants;
import com.google.gson.Gson;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.http.listener.OnHttpListener;
import com.hjq.toast.ToastUtils;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMGroupAtInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/***
 * 聊天界面
 */
public class ChatFragment extends BaseFragment<InterestsUseApplyByDocPresenter> implements InterestsUseApplyByDocContract.View {

    @BindView(R.id.chat_layout)
    ChatLayout mChatLayout;  // 从布局文件中获取聊天面板组件
    private HomeActivity homeActivity;
    private TitleBarLayout mTitleBar;
    private ChatInfo mChatInfo;
    private NewLeaveBean.RowsDTO patientinfo;
    private int mForwardMode;

    private static final String TAG = ChatFragment.class.getSimpleName();
    private String planId;
    private ArrayList<String> userIDList = new ArrayList<>();
    private String userID;
    private TaskDeatailBean taskDeatailBean;
    private LoginBean loginBean;
    private static final int pagestart = 1;
    public static final int pageSize = 10;
    private int eventType = 5; //5  开始问诊  6 结束问诊
    private SummaryDialog summaryDialog;
    int[] location = new int[2];

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }

    @Override
    protected InterestsUseApplyByDocPresenter createPresenter() {
        return new InterestsUseApplyByDocPresenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.chat_fragment;
    }


    @Override
    public void initView(View view) {
        Bundle bundle = getArguments();
        loginBean = SharedPreManager.getObject(Constants.KYE_USER_BEAN, LoginBean.class, homeActivity);
        mChatInfo = (ChatInfo) bundle.getSerializable(Constants.CHAT_INFO);
        patientinfo = (NewLeaveBean.RowsDTO) bundle.getSerializable(Constants.USERINFO);
        if (mChatInfo == null || patientinfo == null) {
            return;
        }
        taskDeatailBean = patientinfo.taskDeatailBean;
        planId = patientinfo.getUserId();
        if (mChatInfo.getType() == V2TIM_C2C) {
            if (mChatInfo.isShowShortCut) {
                userIDList.clear();
                userIDList.add(mChatInfo.userId);
            }
            userID = mChatInfo.userId;
        } else {
            userIDList = mChatInfo.userIdList;
        }

        EventBus.getDefault().register(this);

        //单聊组件的默认UI和交互初始化
        mChatLayout.initDefault();
        initTitleBar();  //顶部TitleBar
        initChatLayout();  //聊天界面窗口
        leftIconClickLisentener();  //聊天界面 左方头像点击事件
        EndVisit(); //结束问诊
        docAccepteDiagnosis();// 接诊
        initbootomTipButton(patientinfo.getKsmc(), patientinfo.isShowCollection, patientinfo.isShowSendRemind); //底部 发送提醒、问卷、问诊等按钮
        initChatlayoutHelper();  //通过api设置ChatLayout各种属性的样例
        customControlListener(); //聊天界面底部更多中的 控件按钮

    }

    private void initChatlayoutHelper() {
        // TODO 通过api设置ChatLayout各种属性的样例
        ChatLayoutHelper helper = new ChatLayoutHelper(homeActivity);
        helper.setGroupId(mChatInfo.getId());
        helper.customizeChatLayout(mChatLayout);
    }


    private int getAtInfoType(List<V2TIMGroupAtInfo> atInfoList) {
        int atInfoType = 0;
        boolean atMe = false;
        boolean atAll = false;

        if (atInfoList == null || atInfoList.isEmpty()) {
            return V2TIMGroupAtInfo.TIM_AT_UNKNOWN;
        }

        for (V2TIMGroupAtInfo atInfo : atInfoList) {
            if (atInfo.getAtType() == V2TIMGroupAtInfo.TIM_AT_ME) {
                atMe = true;
                continue;
            }
            if (atInfo.getAtType() == V2TIMGroupAtInfo.TIM_AT_ALL) {
                atAll = true;
                continue;
            }
            if (atInfo.getAtType() == V2TIMGroupAtInfo.TIM_AT_ALL_AT_ME) {
                atMe = true;
                atAll = true;
                continue;
            }
        }

        if (atAll && atMe) {
            atInfoType = V2TIMGroupAtInfo.TIM_AT_ALL_AT_ME;
        } else if (atAll) {
            atInfoType = V2TIMGroupAtInfo.TIM_AT_ALL;
        } else if (atMe) {
            atInfoType = V2TIMGroupAtInfo.TIM_AT_ME;
        } else {
            atInfoType = V2TIMGroupAtInfo.TIM_AT_UNKNOWN;
        }

        return atInfoType;

    }

    private void updateAtInfoLayout() {
        int atInfoType = getAtInfoType(mChatInfo.getAtInfoList());
        switch (atInfoType) {
            case V2TIMGroupAtInfo.TIM_AT_ME:
                mChatLayout.getAtInfoLayout().setVisibility(VISIBLE);
                mChatLayout.getAtInfoLayout().setText(Application.instance().getString(R.string.ui_at_me));
                break;
            case V2TIMGroupAtInfo.TIM_AT_ALL:
                mChatLayout.getAtInfoLayout().setVisibility(VISIBLE);
                mChatLayout.getAtInfoLayout().setText(Application.instance().getString(R.string.ui_at_all));
                break;
            case V2TIMGroupAtInfo.TIM_AT_ALL_AT_ME:
                mChatLayout.getAtInfoLayout().setVisibility(VISIBLE);
                mChatLayout.getAtInfoLayout().setText(Application.instance().getString(R.string.ui_at_all_me));
                break;
            default:
                mChatLayout.getAtInfoLayout().setVisibility(GONE);
                break;

        }
    }

    @Override
    public void initData() {

    }


    @Override
    public void onDestroy() {
        if (mChatLayout != null) {
            mChatLayout.exitChat();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 处理订阅消息
     *
     * @param message
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventHandler(CustomMessage message) {//不用区分类型，全部直接转换成json发送消息出去
        Log.e(TAG, "接收消息");
        MessageInfo info = MessageInfoUtil.buildCustomMessage(new Gson().toJson(message), message.description, null);
        mChatLayout.sendMessage(info, false);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventGetTaskn(NotiflyUIObj bean) {
        if (bean.tradid.equals(taskDeatailBean.getTaskDetail().getTradeId())) {
            mTitleBar.getEndVisitText().setText("结束问诊");
        }
    }


    /**
     * 处理订阅消息
     *
     * @param message
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventHandler(MessageInfo message) {//不用区分类型，全部直接转换成json发送消息出去
        mChatLayout.sendMessage(message, false);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 结束问诊成功回调
     *
     * @param bean
     */
    @Override
    public void saveRightsUseRecordSuccess(SaveRightsUseBean bean) {
        homeActivity.runOnUiThread(() -> {
            ToastUtils.show("已结束问诊!");
            EventBus.getDefault().post(new NotifycationAlardyObj()); //通知更新最新数据
            EventBus.getDefault().post(new RefreshDataViewObj());  //通知医生端界面 更新 已结束 字样和 隐藏开始问诊控件
            if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                homeActivity.getSupportFragmentManager().popBackStack();
            }
        });
    }

    /**
     * 结束问诊失败回调
     *
     * @param
     */

    @Override
    public void saveRightsUseRecordFail(String failMessage) {
        homeActivity.runOnUiThread(() -> {
//            Log.e(TAG, "saveRightsUseRecordFail: " + failMessage);
            ToastUtils.show(failMessage);
        });
    }


    //保存问诊小结 结果成功回调
    @Override
    public void sendsummary_resultSuucess(DataReViewRecordResponse reViewRecordResponse) {
        homeActivity.runOnUiThread(() -> {
            if (null != summaryDialog) {
                summaryDialog.dismiss();

            }
        });
    }

    //保存问诊小结 结果失败回调
    @Override
    public void sendsummary_resultFail(String messageFail) {
        homeActivity.runOnUiThread(() -> {
            if (null != summaryDialog) {
                summaryDialog.dismiss();
            }
        });
    }

    /**
     * 获取问诊小结 成功回调
     *
     * @param reViewRecordResponse
     */
    @Override
    public void getSummaryListSuucess(List<DataReViewRecordResponse> reViewRecordResponse) {
        homeActivity.runOnUiThread(() -> {
            if (reViewRecordResponse != null && reViewRecordResponse.size() > 0) {
                MedicalRecordFolderDialog dialog = new MedicalRecordFolderDialog(homeActivity);
                dialog.setItemClickLisenner(position -> {
                    DataReViewRecordResponse summaBean = position;
                    summaryDialog = new SummaryDialog(homeActivity);
                    summaryDialog.setCanceledOnTouchOutside(true);
                    summaryDialog.show();
                    summaryDialog.setVisibleBotomButton(false);
                    summaryDialog.setEditeTextString(summaBean.getDealDetail());
                    summaryDialog.setDocNameAndSummaryTime(summaBean.getDealUserName(), TimeUtils.getTimeToDay(Long.parseLong(summaBean.getCreateTime())));
                }).show();
                dialog.setData(reViewRecordResponse);
            } else {
                ToastUtils.show("暂无问诊小结");
            }
        });


    }

    /**
     * 获取问诊小结失败回调
     *
     * @param messageFail
     */
    @Override
    public void getSummaryListFail(String messageFail) {

    }

    @Override
    public void saveCaseCommonWordsSuccess(String quickReplyResult) {
        homeActivity.runOnUiThread(() -> ToastUtils.show(quickReplyResult));
    }

    @Override
    public void saveCaseCommonWordsFail(String failMessage) {

    }

    /**
     * 拨打电话成功回调
     *
     * @param resultBean
     */
    @Override
    public void callSuccess(CallResultBean resultBean) {
        if (!EmptyUtil.isEmpty(resultBean)) {
            homeActivity.runOnUiThread(() -> {
                callPhone(resultBean.telX); //拨号  号码是接口返回的 虚拟号码
            });

        }
    }

    /**
     * 拨打电话失败回调
     *
     * @param
     */
    @Override
    public void callFail(String failMessage) {
        homeActivity.runOnUiThread(() -> ToastUtils.show(failMessage));
    }

    //如果是个案师账号 这里是预问诊收集   如果是医生账号 就是 预诊信息
    private void getQuestionListByKeyWord(String keyWord, String acountType) {
        //个案管理师 账号   请求问卷列表接口
        if (acountType.equals("casemanager")) {
            GetQuestionByKeyWordApi questionByKeyWordApi = new GetQuestionByKeyWordApi();
            questionByKeyWordApi.keyWord = keyWord;
            questionByKeyWordApi.start = pagestart;
            questionByKeyWordApi.pageSize = pageSize;
            EasyHttp.get(homeActivity).api(questionByKeyWordApi).request(new HttpCallback<ApiResult<QuestionResultBean>>(this) {
                @Override
                public void onSucceed(ApiResult<QuestionResultBean> result) {
                    super.onSucceed(result);
                    if (!EmptyUtil.isEmpty(result)) {
                        if (result.getCode() != 0) {
                            ToastUtils.show("请求失败!");
                            return;
                        }
                        if (!EmptyUtil.isEmpty(result.getData()) && !EmptyUtil.isEmpty(result.getData().list)) {
                            List<QuestionResultBean.ListDTO> list = result.getData().list;
                            PrePiagnosisCollectionDialog prePiagnosisCollectionDialog = new PrePiagnosisCollectionDialog(homeActivity);
                            prePiagnosisCollectionDialog.setItemClickLisenner(position -> {
                                QuestionResultBean.ListDTO questionBean = position;
                                    CustomWenJuanMessage questionMessage = new CustomWenJuanMessage();
                                    questionMessage.name = questionBean.name;
                                    questionMessage.url = questionBean.questUrl;
                                    questionMessage.id = questionBean.id;
                                    questionMessage.userId = planId;
                                    questionMessage.setType("CustomYuWenZhenMessage");
                                    questionMessage.setDescription("问卷");
                                    MessageInfo info = MessageInfoUtil.buildCustomMessage(new Gson().toJson(questionMessage), questionMessage.description, null);
                                    mChatLayout.sendMessage(info, false);   //发送点中的问卷
                            }).show();
                            prePiagnosisCollectionDialog.setData(list);
//                            DialogItemAdapter adapter = new DialogItemAdapter(homeActivity, list);
//                            AlertDialog alertDialog = new AlertDialog
//                                    .Builder(homeActivity)
//                                    .setSingleChoiceItems((ListAdapter) adapter, 0, (dialog, position) -> {
//                                        dialog.dismiss();
//                                        QuestionResultBean.ListDTO questionBean = list.get(position);
//                                        CustomWenJuanMessage questionMessage = new CustomWenJuanMessage();
//                                        questionMessage.name = questionBean.name;
//                                        questionMessage.url = questionBean.questUrl;
//                                        questionMessage.id = questionBean.id;
//                                        questionMessage.userId = planId;
//                                        questionMessage.setType("CustomYuWenZhenMessage");
//                                        questionMessage.setDescription("问卷");
//                                        MessageInfo info = MessageInfoUtil.buildCustomMessage(new Gson().toJson(questionMessage), questionMessage.description, null);
//                                        mChatLayout.sendMessage(info, false);   //发送点中的问卷
//                                        Log.e(TAG, "问卷: " + questionBean.name);
//                                    }).create();
//                            alertDialog.show();
//                            alertDialog.getWindow().setLayout(DensityUtil.dip2px(homeActivity, 400), LinearLayout.LayoutParams.WRAP_CONTENT);
                        } else {
                            ToastUtils.show("患者所属科室无相关问卷列表!");
                        }
                    }

                }

                @Override
                public void onFail(Exception e) {
                    super.onFail(e);
                    Log.e(TAG, "请求问卷列表接口: " + e.getMessage());

                }
            });
        } else {
            //医生账号  请求获取 答卷列表 接口
//            Log.d(TAG, "getQuestionListByKeyWord: " + planId);
            GetAnserListApi getAnserListApi = new GetAnserListApi();
            getAnserListApi.current = 1;
            getAnserListApi.size = 10;
            if (EmptyUtil.isEmpty(planId)) {
                ToastUtils.show("患者ID为空!");
                return;
            }
            getAnserListApi.userId = Integer.valueOf(planId);  //111   planId
            EasyHttp.get(homeActivity).api(getAnserListApi).request(new HttpCallback<ApiResult<AnswerResultBean>>(this) {
                @Override
                public void onSucceed(ApiResult<AnswerResultBean> result) {
                    super.onSucceed(result);
                    if (!EmptyUtil.isEmpty(result)) {
                        if (result.getCode() != 200) {
                            ToastUtils.show("请求失败!");
                            return;
                        }
                        if (!EmptyUtil.isEmpty(result.getData()) && !EmptyUtil.isEmpty(result.getData().getRecords())) {
                            List<AnswerResultBean.RecordsDTO> records = result.getData().getRecords();
                            PrediagnosisDataDialog prediagnosisDataDialog = new PrediagnosisDataDialog(homeActivity);
                            prediagnosisDataDialog.setItemClickLisenner(recordsDTO -> {
                                AnswerResultBean.RecordsDTO item = recordsDTO;
                                QuestionResultBean.ListDTO answerBean = new QuestionResultBean.ListDTO();
                                answerBean.questUrl = Constants.HOST_URL + "/r/" + item.getProjectKey() + "?userId=" + item.getUserId();  //自己拼接一个链接 跳转至答卷详情界面
                                    homeActivity.switchSecondFragment(Constants.FRAGMENT_QUESTION_DETAIL, answerBean);
                            }).show();
                            prediagnosisDataDialog.setData(records);
                        } else {
                            ToastUtils.show("该患者暂无预诊资料!");
                        }
                    }
                }

                @Override
                public void onFail(Exception e) {
                    super.onFail(e);
                }
            });
        }
    }


    /**
     * 聊天面板 底部5个小控件
     */
    private void initbootomTipButton(String deptName, boolean isShowdataCollection, boolean isShowSendRemind) {
        mChatLayout.getInputLayout().tv_datacollection.setText(loginBean.getAccount().roleName.equals("casemanager") ? getString(R.string.pre_diagnosis_collection) : getString(R.string.pre_diagnosis_data));
        mChatLayout.getInputLayout().tv_medicalfolder.setVisibility(!loginBean.getAccount().roleName.equals("casemanager") ? VISIBLE : GONE);
        mChatLayout.getInputLayout().tv_datacollection.setVisibility(isShowdataCollection ? VISIBLE : GONE); //如果是从咨询界面过来的 不显示预诊收集信息控件
        mChatLayout.getInputLayout().tv_sendremind.setVisibility(isShowSendRemind ? VISIBLE : GONE); //如果是从咨询界面过来的 不显示 预诊收集信息 和 上线提醒控件
        mChatLayout.getInputLayout().tv_datacollection.setOnClickListener(v -> {   //   预诊资料
            getQuestionListByKeyWord(deptName, loginBean.getAccount().roleName);   //点击获取问卷列表(根据科室名称模糊查询)
        });

        mChatLayout.getInputLayout().tv_sendremind.setOnClickListener(v -> {   //上线提醒
            if (EmptyUtil.isEmpty(taskDeatailBean) || EmptyUtil.isEmpty(taskDeatailBean.getTaskDetail())) {
                Log.e(TAG, "医生发送提醒 taskDeatailBean.getTaskDetail() == null");
                return;
            }
            ToastUtils.show("操作成功!");
            eventType = 5;  //开始问诊 提醒上线
            String jsonString = GsonUtils.ModelToJson(taskDeatailBean.getTaskDetail());
            sendSystemRemind(jsonString);
        });

        mChatLayout.getInputLayout().tv_sendquestion.setOnClickListener(v -> {    //发送问卷
            homeActivity.switchSecondFragment(FRAGMENT_ADD_QUESTION, planId);
        });

        mChatLayout.getInputLayout().tv_sendarticle.setOnClickListener(v -> {     //发送文章
            homeActivity.switchSecondFragment(FRAGMENT_ADD_PAPER, "");
        });
        mChatLayout.getInputLayout().tv_sendshortcut.setOnClickListener(v -> {   //  快捷回复
            homeActivity.switchSecondFragment(FRAGMENT_QUICKREPLY, String.valueOf(loginBean.getUser().user.userId));

        });

        mChatLayout.getInputLayout().tv_medicalfolder.setOnClickListener(v -> {  //病历夹
            mPresenter.getsummary_resultList(planId);  //获取问诊小结 列表

        });
    }

    /**
     * 结束问诊
     */
    private void EndVisit() {
        //结束问诊  如果是直接传给医生的权益任务 结束任务时需判断当前的权益是否已经开始生效,获取权益最新的状态
        mTitleBar.getEndVisitText().setOnClickListener(v -> {
            if (mTitleBar.getEndVisitText().getText().toString().equals("拒绝问诊")) {  //如果是拒绝问诊字眼 请求接口获取最新的权益状态 再根据返回的权益状态 弹框
                mPresenter.queryRightsRecord(1, 1000, taskDeatailBean.getTaskDetail().getRightsId(), String.valueOf(taskDeatailBean.getTaskDetail().getUserInfo().getUserId()), String.valueOf(taskDeatailBean.getTaskDetail().getId()));  //点击之前获取一下最新的权益状态
            } else {  // 直接结束问诊 弹问诊小结窗口
                summaryDialog = new SummaryDialog(homeActivity);
                summaryDialog.setOnclickListener(new SummaryDialog.OnButtonClickListener() {
                    @Override
                    public void onPositiveClick() {
                        String inputString = summaryDialog.getInputString();
                        if (EmptyUtil.isEmpty(inputString)) {
                            ToastUtils.show("问诊小结不能为空!");
                            return;
                        }
                        CustomAnalyseMessage customAnalyseMessage = new CustomAnalyseMessage();
                        customAnalyseMessage.title = "问诊小结";
                        customAnalyseMessage.content = inputString;
                        customAnalyseMessage.contentId = planId;
                        customAnalyseMessage.dealName = loginBean.getUser().user.userName;
                        customAnalyseMessage.time = TimeUtils.getCurrenTimeYMDHMS();
                        customAnalyseMessage.setType("CustomAnalyseMessage");
                        customAnalyseMessage.setDescription("问诊小结");
                        MessageInfo info = MessageInfoUtil.buildCustomMessage(new Gson().toJson(customAnalyseMessage), customAnalyseMessage.description, null);
                        mChatLayout.sendMessage(info, false);  //医生填写的 问诊小结 需要发送给患者
                        sendSummaryResult(inputString);  //问诊小结
                        endConsultation();//结束问诊
                        if (EmptyUtil.isEmpty(taskDeatailBean.getTaskDetail())) {
                            Log.e(TAG, "聊天结束问诊 taskDeatailBean.getTaskDetail() == null");
                            return;
                        }
                        eventType = 6; //结束问诊
                        String jsonString = GsonUtils.ModelToJson(taskDeatailBean.getTaskDetail());
                        sendSystemRemind(jsonString);  //发送短信通知
                    }

                    @Override
                    public void onNegtiveClick() {
                        summaryDialog.dismiss();
                    }
                }).show();
                summaryDialog.setDocNameAndSummaryTime(loginBean.getUser().user.userName, TimeUtils.getCurrenTimeYMDHMS());
            }
        });
    }

    /**
     * 接诊
     */
    private void docAccepteDiagnosis() {
        mTitleBar.getAcceptdiagnosisText().setOnClickListener(v -> DataUtil.showNormalDialog(homeActivity, "温馨提示", "确定接诊吗？", "确定", "取消", new DataUtil.OnNormalDialogClicker() {
            @Override
            public void onPositive() {
                //确定 请求接口 更新权益状态   并发送卡片给患者(自定义消息)
                UpdateRightsRequestTimeRequestBean updateRightsRequestTimeRequestBean = new UpdateRightsRequestTimeRequestBean();
                updateRightsRequestTimeRequestBean.execTime = TimeUtils.getCurrentTimeMinute();
                updateRightsRequestTimeRequestBean.tradeId = taskDeatailBean.getTaskDetail().getTradeId();
                updateRightsRequestTimeRequestBean.userId = String.valueOf(taskDeatailBean.getTaskDetail().getUserInfo().getUserId());
                mPresenter.updateRightsRequestTime(updateRightsRequestTimeRequestBean);
            }

            @Override
            public void onNegative() {

            }
        }));
    }


    /**
     * 结束问诊
     */
    private void endConsultation() {
        SaveRightsUseBean saveRightsUseBean = new SaveRightsUseBean();
        assert loginBean != null;
        saveRightsUseBean.deptName = loginBean.getUser().user.departmentName;
        saveRightsUseBean.execDept = String.valueOf(loginBean.getUser().user.departmentId); //这里传科室代码
        saveRightsUseBean.execFlag = 1;
        saveRightsUseBean.execUser = String.valueOf(loginBean.getUser().user.userId);
        saveRightsUseBean.statusDescribe = getString(R.string.end_consultation);
        saveRightsUseBean.execTime = TimeUtils.getTime_tosecond(taskDeatailBean.getExecTime());
        saveRightsUseBean.id = taskDeatailBean.getTaskDetail().getId();
        saveRightsUseBean.rightsId = taskDeatailBean.getTaskDetail().getRightsId();
        saveRightsUseBean.rightsName = taskDeatailBean.getTaskDetail().getRightsName();
        saveRightsUseBean.rightsType = taskDeatailBean.getTaskDetail().getRightsType();
        saveRightsUseBean.tradeId = taskDeatailBean.getTaskDetail().getTradeId();
        saveRightsUseBean.userId = taskDeatailBean.getTaskDetail().getUserId();
        saveRightsUseBean.taskId = String.valueOf(taskDeatailBean.getId());
        mPresenter.saveRightsUseRecord(saveRightsUseBean);   //请求接口 结束问诊
    }


    //发送问诊小结
    private void sendSummaryResult(String inputString) {
        DataReViewRecordResponse request = new DataReViewRecordResponse();
        request.setDealDetail(inputString);
        request.setDealResult("问诊小结");
        request.setDealType("SUMMARY");
        request.setUserId(taskDeatailBean.getTaskDetail().getUserInfo().getUserId() + "");
        request.setDealUserName(loginBean.getUser().user.userName);
        request.setDealUser(loginBean.getUser().user.userId + "");
        request.setTradeId(taskDeatailBean.getTaskDetail().getTradeId());
        mPresenter.sendsummary_result(request);  //发送问诊小结
    }


    //发送通知
    private void sendSystemRemind(String jsonString) {
        SystemRemindObj systemRemindObj = new SystemRemindObj();
        systemRemindObj.remindType = "videoRemind";
        systemRemindObj.userId = planId;
        systemRemindObj.eventType = eventType;
        systemRemindObj.infoDetail = jsonString;
        EasyHttp.post(homeActivity).api(systemRemindObj).request(new OnHttpListener<ApiResult<String>>() {
            @Override
            public void onSucceed(ApiResult<String> result) {
//                Log.e(TAG, "通知请求: " + result.getMessage());
            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }


    /***
     * 查询权益最新记录 成功回调
     * @param queryRightsRecordBean
     */
    @Override
    public void queryRightsRecordSuccess(QueryRightsRecordBean queryRightsRecordBean) {
        homeActivity.runOnUiThread(() -> {
            if (queryRightsRecordBean != null && queryRightsRecordBean.getRows().size() > 0) {
                QueryRightsRecordBean.RowsDTO rowsDTO = queryRightsRecordBean.getRows().get(0);
                if (rowsDTO.getExecFlag() == 0) {   //如果当前状态还是处于 确认时间 状态 则拒绝问诊
                    DataUtil.showNormalDialog(homeActivity, "温馨提示", "确定拒绝问诊吗？", "确定", "取消", new DataUtil.OnNormalDialogClicker() {
                        @Override
                        public void onPositive() {
                            RefusalDiagnosisDialog dialog = new RefusalDiagnosisDialog(homeActivity);
                            dialog.setOnclickListener(new RefusalDiagnosisDialog.OnClickBottomListener() {
                                @Override
                                public void onPositiveClick() {
                                    String reason = "";
                                    String selectReason = dialog.getSelectedReason();
                                    String inputReason = dialog.getInputReason();
                                    if (selectReason.equals("其他")) {
                                        if (EmptyUtil.isEmpty(inputReason)) {
                                            ToastUtils.show("请输入理由");
                                            return;
                                        }
                                        reason = inputReason;
                                    } else {
                                        reason = selectReason;
                                    }
                                    Log.e(TAG, "拒绝原因: " + reason);
                                    SaveRightsUseBean saveRightsUseBean = new SaveRightsUseBean();
                                    saveRightsUseBean.deptName = loginBean.getUser().user.departmentName;
                                    saveRightsUseBean.execDept = String.valueOf(loginBean.getUser().user.departmentId); //这里传科室代码
                                    saveRightsUseBean.execFlag = 3;
                                    saveRightsUseBean.execUser = loginBean.getUser().user.userName;
                                    saveRightsUseBean.execTime = TimeUtils.getTime_tosecond(taskDeatailBean.getExecTime());
                                    saveRightsUseBean.execUser = String.valueOf(loginBean.getUser().user.userId);
                                    saveRightsUseBean.id = taskDeatailBean.getTaskDetail().getId();
                                    saveRightsUseBean.rightsId = taskDeatailBean.getTaskDetail().getRightsId();
                                    saveRightsUseBean.rightsName = taskDeatailBean.getTaskDetail().getRightsName();
                                    saveRightsUseBean.rightsType = taskDeatailBean.getTaskDetail().getRightsType();
                                    saveRightsUseBean.statusDescribe = "拒绝问诊";
                                    saveRightsUseBean.remark = reason;
                                    saveRightsUseBean.tradeId = taskDeatailBean.getTaskDetail().getTradeId();
                                    saveRightsUseBean.userId = taskDeatailBean.getTaskDetail().getUserId();
                                    saveRightsUseBean.taskId = String.valueOf(taskDeatailBean.getId());
                                    Log.e(TAG, "拒绝问诊=== ");
                                    mPresenter.saveRightsUseRecord(saveRightsUseBean);
                                    //需要发送一条自定义消息给患者
                                    CustomRefuseVisitMessage customdoctorreceptionmessage = new CustomRefuseVisitMessage();
                                    customdoctorreceptionmessage.description = "医生拒诊";
                                    customdoctorreceptionmessage.content = "医生拒诊";
                                    customdoctorreceptionmessage.reason = reason;
                                    customdoctorreceptionmessage.tradeId = taskDeatailBean.getTaskDetail().getTradeId();
                                    customdoctorreceptionmessage.docName = loginBean.getUser().user.userName;
                                    customdoctorreceptionmessage.docId = String.valueOf(loginBean.getUser().user.userId);
                                    customdoctorreceptionmessage.type = "CustomDoctorRefuseMessage";
                                    MessageInfo info = MessageInfoUtil.buildCustomMessage(new Gson().toJson(customdoctorreceptionmessage), customdoctorreceptionmessage.description, null);
                                    mChatLayout.sendMessage(info, false);

                                    eventType = 10; //拒绝问诊的  发送通知 标识 暂定义为 10  还未确认
                                    String jsonString = GsonUtils.ModelToJson(saveRightsUseBean);  //这里发送提醒 调用接口参数和之前的发送通知不一样
                                    sendSystemRemind(jsonString);
                                    getFragmentManager().beginTransaction().remove(ChatFragment.this).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit(); //退出fragment  不返回上一个界面

                                }

                                @Override
                                public void onNegtiveClick() {
                                }
                            }).show();
                        }

                        @Override
                        public void onNegative() {

                        }
                    });
                } else {  // 该权益已经 开始 不能拒绝
                    mTitleBar.getEndVisitText().setText("结束问诊");  //  这里要更新 title
                    DataUtil.showNormalDialog(homeActivity, "温馨提示", "该工单已确认,无法拒诊!", "确定", "取消", new DataUtil.OnNormalDialogClicker() {
                        @Override
                        public void onPositive() {

                        }

                        @Override
                        public void onNegative() {

                        }
                    });
                }
            } else {
                // TODO: 2022/5/7 未获取到当前权益的最新状态 该做什么？？

            }
        });
    }


    /***
     * 查询权益使用记录 失败回调
     * @param faiMessage
     */
    @Override
    public void queryRightsRecordFail(String faiMessage) {
        homeActivity.runOnUiThread(() -> {
//            ToastUtils.show(faiMessage);
        });
    }


    /**
     * 医生点击接诊 更新权益状态的 成功回调
     *
     * @param isSuccess
     */
    @Override
    public void updateRightsRequestTimeSuccess(boolean isSuccess) {
        homeActivity.runOnUiThread(() -> {
            if (isSuccess) {
                mTitleBar.getAcceptdiagnosisText().setVisibility(GONE);
                mTitleBar.getEndVisitText().setText("结束问诊");
                //需要发送一条自定义消息给患者
                CustomDoctorReceptionMessage customDoctorReceptionMessage = new CustomDoctorReceptionMessage();
                customDoctorReceptionMessage.description = "医生接诊";
                customDoctorReceptionMessage.content = "医生接诊";
                customDoctorReceptionMessage.time = TimeUtils.getCurrentTimeMinute();
                customDoctorReceptionMessage.tradeId = taskDeatailBean.getTaskDetail().getTradeId();
                customDoctorReceptionMessage.docName = loginBean.getUser().user.userName;
                customDoctorReceptionMessage.docId = String.valueOf(loginBean.getUser().user.userId);
                customDoctorReceptionMessage.docdeptName = String.valueOf(loginBean.getUser().user.departmentName);
                customDoctorReceptionMessage.type = "CustomDoctorReceptionMessage";
                MessageInfo info = MessageInfoUtil.buildCustomMessage(new Gson().toJson(customDoctorReceptionMessage), customDoctorReceptionMessage.description, null);
                mChatLayout.sendMessage(info, false);
                EventBus.getDefault().post(new NotifycationAlardyObj()); //通知更新最新数据  接诊成功后 需更新待办列表
            }
        });
    }

    /**
     * 医生点击接诊 更新权益状态的 失败回调
     *
     * @param failMessage
     */
    @Override
    public void updateRightsRequestTimeFail(String failMessage) {

    }


    /**
     * 更多面板自定义控件 按钮及点击事件
     */
    private void customControlListener() {
        //新增的自定义控件点击回调
        mChatLayout.setOnCustomClickListener(new InputLayout.OnCustomClickListener() {
            //
//            //健康计划
            @Override
            public void onHealthPlanClick() {
                ChatFragment.NewMsgData msgData = new ChatFragment.NewMsgData();
                msgData.userIds = new ArrayList<>();
                msgData.userIds.add(mChatInfo.userId);//mChatInfo.getId()是健康管理群组聊天的groupId，userId才是每个页面需要的传参
                msgData.id = planId + "";//这里id设置为 planId
//                homeActivity.switchSecondFragment(Constants.FRAGMENT_HEALTH_PLAN_DETAIL, msgData);  //切换至健康计划界面
            }

            //健康评估
            @Override
            public void onHealthAnalyseClick() {
                ChatFragment.NewMsgData msgData = new ChatFragment.NewMsgData();
                msgData.msgType = Constants.MSG_SINGLE;
                msgData.userIds = new ArrayList<>();
                msgData.userIds.add(mChatInfo.userId);
                msgData.id = planId + "";//这里id设置为 planId
//                homeActivity.switchSecondFragment(Constants.FRAGMENT_HEALTH_ANALYSE, msgData); //切换至健康评估界面
            }

            //健康消息
            @Override
            public void onHealthMsgClick() {
                ChatFragment.NewMsgData msgData = new ChatFragment.NewMsgData();
                msgData.msgType = Constants.MSG_SINGLE;
                msgData.userIds = new ArrayList<>();
                msgData.userIds.add(mChatInfo.userId);
//                homeActivity.switchSecondFragment(Constants.FRAGMENT_SEND_MSG, msgData);//切换至健康消息界面
            }

            //健康档案
            @Override
            public void onHealthFilesClick() {
//                Intent intent = new Intent(homeActivity, HealthFilesActivity.class);
//                intent.putExtra(Constants.USER_ID, mChatInfo.userId);
//                homeActivity.startActivity(intent);
            }

            //
//
//            //视频问诊
            @Override
            public void onVideoCommunicate() {
                CustomVideoCallMessage message = new CustomVideoCallMessage();
                message.title = getString(R.string.video_visit);
                long currentTimeMillis = System.currentTimeMillis();
                if (EmptyUtil.isEmpty(planId)) {
                    ToastUtils.show("患者ID为空,不能进行视频通话!");
                    return;
                }
                String rooId = loginBean.getUser().user.userId + planId;
                message.msgDetailId = rooId;
                message.content = getString(R.string.click_to_access_the_video);
                message.timeStamp = currentTimeMillis;
                //这个属性区分消息类型 HelloChatController中onDraw方法去绘制布局
                message.setType("CustomVideoCallMessage");
                message.userId = new ArrayList<>();
                message.userId.add(mChatInfo.getId());//传入userid
                message.setDescription(getString(R.string.video_visit));
                message.id = planId + "";//这里id设置为视频看诊的预约id
                MessageInfo info = MessageInfoUtil.buildCustomMessage(new Gson().toJson(message), message.description, null);
                mChatLayout.sendMessage(info, false);
                CustomVideoCallMessageController.getPatientAppointmentById(message, true);


            }

            //            //书写病历
            @Override
            public void onWriteConsultConclusion() {
                ChatFragment.NewMsgData msgData = new ChatFragment.NewMsgData();
                msgData.userIds = new ArrayList<>();
                msgData.userIds.add(mChatInfo.getId());
                msgData.id = planId + "";
//                homeActivity.switchSecondFragment(Constants.FRAGMENT_WRITE_HEALTH, msgData);
            }

            // 拨打电话 
            @SuppressLint("WrongConstant")
            @Override
            public void onCallPhone() {
                // TODO: 2022/4/29 调用接口 准备拨号 获取本机号码
                TelephonyManager telephonyManager = (TelephonyManager) homeActivity.getSystemService(Context.TELEPHONY_SERVICE);
                if (!PermissionUtils.checkPermission(homeActivity, Manifest.permission.READ_PHONE_STATE)) {
                    Log.e(TAG, "手动添加权限----");
                    return;
                }
                String local_phone = telephonyManager.getLine1Number();
                if (taskDeatailBean != null && taskDeatailBean.getTaskDetail() != null && taskDeatailBean.getTaskDetail().getUserInfo() != null) {
                    String patientPhone = taskDeatailBean.getTaskDetail().getUserInfo().getPhone();
                    if (!EmptyUtil.isEmpty(patientPhone) && !EmptyUtil.isEmpty(local_phone)) {
                        if (local_phone.length() > 10) {
                            local_phone = local_phone.substring(3);
                        }
                        CallRequest callRequest = new CallRequest();
                        callRequest.distPhone = patientPhone;
                        callRequest.sourcePhone = local_phone;
                        mPresenter.callPhone(callRequest);
                    } else {
                        ToastUtils.show("未检测到本机号码!");
                    }
                    Log.e(TAG, "患者phone: " + patientPhone + " 本机号码： " + local_phone);
//                    callPhone(patientPhone);  //测试拨号
                }

            }
        });

    }


    //拨号
    private void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }


    /**
     * 左边头像事件点击处理
     */
    private void leftIconClickLisentener() {
        mChatLayout.getMessageLayout().setOnItemClickListener(new MessageLayout.OnItemLongClickListener() {
            @Override
            public void onMessageLongClick(View view, int position, MessageInfo messageInfo) {
                //因为adapter中第一条为加载条目，位置需减1
                mChatLayout.getMessageLayout().showItemPopMenu(position - 1, messageInfo, view);
            }

            /**
             * 点击左边头像 进入详情界面
             * @param view
             * @param position
             * @param messageInfo
             * @param isLeftIconClick  点击对方的头像
             */
            @Override
            public void onUserIconClick(View view, int position, MessageInfo messageInfo, boolean isLeftIconClick) {
                if (isLeftIconClick) {
                    homeActivity.switchSecondFragment(Constants.DATA_REVIEW, patientinfo.getUserId());
                }
            }
        });
    }


    private void initTitleBar() {
        //获取单聊面板的标题栏
        mTitleBar = mChatLayout.getTitleBar();
        mTitleBar.getEndVisitText().setVisibility(patientinfo.isConsultation ? VISIBLE : GONE); //如果是 问诊 顶部显示 “结束问诊” 字样 反之不显示
        mTitleBar.getEndVisitText().setText(taskDeatailBean != null && taskDeatailBean.getTaskDetail() != null && taskDeatailBean.getTaskDetail().getExecFlag() == 0 ? "拒绝问诊" : "结束问诊");
        mTitleBar.getLeftGroup().setVisibility(VISIBLE);//bug 381，隐藏返回键
        mTitleBar.getRightIcon().setVisibility(GONE);//沒有好友详情页面，隐藏

        mTitleBar.getAcceptdiagnosisText().setVisibility(taskDeatailBean != null && taskDeatailBean.getTaskDetail() != null && taskDeatailBean.getTaskDetail().getExecFlag() == 0 && taskDeatailBean.getTaskDetail().getRightsType().equals("textNum") ? VISIBLE : GONE);  //接诊按钮  只有图文咨询权益并且是还未确认时间的 才显示

        //单聊面板标记栏返回按钮点击事件，这里需要开发者自行控制
        mTitleBar.setOnLeftClickListener(v -> {
            //如果是  图文咨询 并且还未确认接诊的 点击返回 不再返回上一个fragment
            if (taskDeatailBean != null && taskDeatailBean.getTaskDetail().getRightsType().equals("textNum") && taskDeatailBean.getTaskDetail().getExecFlag() == 0) {
                getFragmentManager().beginTransaction().remove(ChatFragment.this).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit(); //退出fragment  不返回上一个界面
            } else {
                //正常返回上一个界面
                if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    homeActivity.getSupportFragmentManager().popBackStack();
                }
            }
        });
    }


    private void initChatLayout() {
        /*
         * 需要聊天的基本信息
         */
        mChatLayout.setChatInfo(mChatInfo);
        mChatInfo.setChatName(mChatInfo.getChatName());
        mChatLayout.getFlayout_tipmessage().setVisibility(patientinfo.isConsultation ? VISIBLE : GONE);  //如果是 问诊 顶部显示 “您已进入...” 字样  反之不显示
        mChatLayout.getInputLayout().ll_shortCutlayout.setVisibility(mChatInfo.isShowShortCut ? VISIBLE : GONE); //底部快捷回复布局
        mChatLayout.getInputLayout().setShowCallButton(!loginBean.getAccount().roleName.equals("casemanager") && taskDeatailBean.getTaskDetail().getRightsType().equals("telNum")); //只有医生并且是电话咨询权益才能拨打电话 个案师不显示 底部拨号按钮
        mChatLayout.getInputLayout().setShowVedioButton(!loginBean.getAccount().roleName.equals("casemanager") && taskDeatailBean.getTaskDetail().getRightsType().equals("videoNum")); //只有医生并且是电话咨询权益才能拨打电话 个案师不显示 底部拨号按钮
        //长按信息 添加信息 至快捷用语 回调  拿到需添加的信息后  请求接口
        mChatLayout.setAddQuickwordsListener(message -> {
            QuickReplyRequest request = new QuickReplyRequest();
            if (message.length() <= 70) {
                request.content = message;
                request.userId = String.valueOf(loginBean.getUser().user.userId);
                mPresenter.saveCaseCommonWords(request);
            } else {
                ToastUtils.show("操作失败!超出字数限制");
            }
        });
        mChatLayout.setForwardSelectActivityListener((mode, msgIds) -> {
            mForwardMode = mode;
            Intent intent = new Intent(Application.instance(), ForwardSelectActivity.class);
            intent.putExtra(ForwardSelectActivity.FORWARD_MODE, mode);
            startActivityForResult(intent, TUIKitConstants.FORWARD_SELECT_ACTIVTY_CODE);
        });
        mChatLayout.getInputLayout().setStartActivityListener(() -> {
            Intent intent = new Intent(Application.instance(), StartGroupMemberSelectActivity.class);
            GroupInfo groupInfo = new GroupInfo();
            groupInfo.setId(mChatInfo.getId());
            groupInfo.setChatName(mChatInfo.getChatName());
            intent.putExtra(TUIKitConstants.Group.GROUP_INFO, groupInfo);
            startActivityForResult(intent, 1);
        });
        mChatLayout.getInputLayout().setGoneInputMore(patientinfo.rightsType.equals("videoNum") || patientinfo.rightsType.equals("telNum"));  //如果是图文咨询的 聊天界面点击更多不显示视频问诊控件
        mChatLayout.getInputLayout().hideMoreShowSendbutton(patientinfo.rightsType.equals("videoNum") || patientinfo.rightsType.equals("telNum"));//如果是图文咨询 进入聊天界面 输入界面右端不显示加号按钮  直接显示发送字样
        mChatLayout.getInputLayout().setVisibility(VISIBLE);
        mChatLayout.getInputLayout().setChatType(mChatInfo.chatType);
        if (mChatInfo.getType() == V2TIMConversation.V2TIM_GROUP) {
            V2TIMManager.getConversationManager().getConversation(mChatInfo.getId(), new V2TIMValueCallback<V2TIMConversation>() {
                @Override
                public void onError(int code, String desc) {
                }

                @Override
                public void onSuccess(V2TIMConversation v2TIMConversation) {
                    if (v2TIMConversation == null) {
                        return;
                    }
                    mChatInfo.setAtInfoList(v2TIMConversation.getGroupAtInfoList());

                    final V2TIMMessage lastMessage = v2TIMConversation.getLastMessage();

                    updateAtInfoLayout();
                    mChatLayout.getAtInfoLayout().setOnClickListener(v -> {
                        final List<V2TIMGroupAtInfo> atInfoList = mChatInfo.getAtInfoList();
                        if (atInfoList == null || atInfoList.isEmpty()) {
                            mChatLayout.getAtInfoLayout().setVisibility(GONE);
                            return;
                        } else {
                            mChatLayout.getChatManager().getAtInfoChatMessages(atInfoList.get(atInfoList.size() - 1).getSeq(), lastMessage, new IUIKitCallBack() {
                                @Override
                                public void onSuccess(Object data) {
                                    mChatLayout.getMessageLayout().scrollToPosition((int) atInfoList.get(atInfoList.size() - 1).getSeq());
                                    LinearLayoutManager mLayoutManager = (LinearLayoutManager) mChatLayout.getMessageLayout().getLayoutManager();
                                    mLayoutManager.scrollToPositionWithOffset((int) atInfoList.get(atInfoList.size() - 1).getSeq(), 0);

                                    atInfoList.remove(atInfoList.size() - 1);
                                    mChatInfo.setAtInfoList(atInfoList);

                                    updateAtInfoLayout();
                                }

                                @Override
                                public void onError(String module, int errCode, String errMsg) {
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    public static class NewMsgData implements Serializable {
        public ArrayList<String> userIds;
        public String msgType;
        public String id;
        public String planId;
        public String groupID;
        public String appointmentId;

        public SaveCaseApi saveCaseApi;
    }


}
