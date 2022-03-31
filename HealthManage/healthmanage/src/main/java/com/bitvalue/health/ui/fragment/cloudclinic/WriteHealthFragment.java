package com.bitvalue.health.ui.fragment.cloudclinic;

import static com.bitvalue.health.util.Constants.FRAGMENT_HEALTH_HISTORY_PREVIEW;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.health.api.requestbean.GetHistoryApi;
import com.bitvalue.health.api.responsebean.LoginBean;
import com.bitvalue.health.api.responsebean.SaveCaseApi;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.contract.cloudcliniccontract.WriterCaseHistoryContract;
import com.bitvalue.health.presenter.cloudclinicpersenter.WriterCaseHistoryPersenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.fragment.chat.ChatFragment;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.DataUtil;
import com.bitvalue.health.util.SharedPreManager;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.helper.CustomCaseHistoryMessage;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.hjq.toast.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 书写病历 界面
 */
public class WriteHealthFragment extends BaseFragment<WriterCaseHistoryPersenter> implements WriterCaseHistoryContract.WriterCaseHistoryView {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.et_main)
    EditText et_main;

    @BindView(R.id.et_history)
    EditText et_history;

    @BindView(R.id.et_history_before)
    EditText et_history_before;

    @BindView(R.id.et_body)
    EditText et_body;

    @BindView(R.id.et_result)
    EditText et_result;

    @BindView(R.id.et_conclusion)
    EditText et_conclusion;

    private HomeActivity homeActivity;
    private ArrayList<String> mIds = new ArrayList<>();
    private GetHistoryApi getHistoryApi = new GetHistoryApi();
    private SaveCaseApi saveCaseApi = new SaveCaseApi();
    private int oldId;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) getActivity();
    }

    @Override
    public void initView(View rootView) {
        tv_title.setText(getString(R.string.writer_medical_record));
        mIds = getArguments().getStringArrayList(Constants.MSG_IDS);
        saveCaseApi = new SaveCaseApi();
        saveCaseApi.userId = mIds.get(0);
        saveCaseApi.appointmentId = getArguments().getString(Constants.PLAN_ID);
    }

    /**
     * 返回、取消、预览、提交按钮的点击事件监听
     * @param view
     */
    @OnClick({R.id.layout_back, R.id.tv_cancel, R.id.tv_preview, R.id.tv_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                /**
                 * 取消
                 */
            case R.id.tv_cancel:
                if (ifHasData()) {
                    backPress();
                } else {
                    if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        homeActivity.getSupportFragmentManager().popBackStack();
                    }
                }
                break;
            /***
             * 病历预览
             */
            case R.id.tv_preview:
                getData();
                ChatFragment.NewMsgData msgData = new ChatFragment.NewMsgData();
                msgData.saveCaseApi = saveCaseApi;
                homeActivity.switchSecondFragment(FRAGMENT_HEALTH_HISTORY_PREVIEW, msgData);
                break;
            /**
             * 提交病历
             */
            case R.id.tv_commit:
                if (ifReady()) {
                    if (oldId != 0) {
                        saveCaseApi.id = oldId;
                    }
                   mPresenter.commitCaseHistory(saveCaseApi);
                }
                break;
        }
    }

    private void getData() {
        saveCaseApi.chiefComplaint = et_main.getText().toString();
        saveCaseApi.presentIllness = et_history.getText().toString();
        saveCaseApi.diagnosis = et_result.getText().toString();
        saveCaseApi.suggestion = et_conclusion.getText().toString();
        saveCaseApi.pastIllness = et_history_before.getText().toString();
        saveCaseApi.generalInspection = et_body.getText().toString();
    }

    //判断数据是否为空
    private boolean ifHasData() {
        boolean hasData = false;
        if (et_main.getText().toString().isEmpty() && et_history.getText().toString().isEmpty() && et_result.getText().toString().isEmpty()
                && et_conclusion.getText().toString().isEmpty() && et_history_before.getText().toString().isEmpty() && et_body.getText().toString().isEmpty()) {
            hasData = false;
        } else {
            hasData = true;
        }
        return hasData;
    }

    //退回上一个界面 并且弹出对话框提示
    private void backPress() {
        DataUtil.showNormalDialog(homeActivity, "温馨提示", "您已创建并书写了病历，取消后您当前所创建的内容将消失，是否取消本次书写？", "确定", "取消", new DataUtil.OnNormalDialogClicker() {
            @Override
            public void onPositive() {
                if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    homeActivity.getSupportFragmentManager().popBackStack();
                }
            }

            @Override
            public void onNegative() {

            }
        });


    }

    @Override
    public void initData() {
        getHistoryApi.userId = mIds.get(0);
        getHistoryApi.appointmentId = saveCaseApi.appointmentId;
        mPresenter.qryUserMedicalCase(getHistoryApi);
    }

    @Override
    protected WriterCaseHistoryPersenter createPresenter() {
        return new WriterCaseHistoryPersenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_write_health;
    }


    //书写病历时 需要对用户输入的信息进行提示 如果没有在主述中填写 则不能提交病历
    private boolean ifReady() {
        if (et_main.getText().toString().isEmpty()) {
            ToastUtil.toastShortMessage("请输入主述");
            return false;
        } else {
            saveCaseApi.chiefComplaint = et_main.getText().toString();
        }

        if (et_history.getText().toString().isEmpty()) {
            ToastUtil.toastShortMessage("请输入现病史");
            return false;
        } else {
            saveCaseApi.presentIllness = et_history.getText().toString();
        }

        if (et_result.getText().toString().isEmpty()) {
            ToastUtil.toastShortMessage("请输入初步诊断");
            return false;
        } else {
            saveCaseApi.diagnosis = et_result.getText().toString();
        }

        if (et_conclusion.getText().toString().isEmpty()) {
            ToastUtil.toastShortMessage("请输入处理意见");
            return false;
        } else {
            saveCaseApi.suggestion = et_conclusion.getText().toString();
        }

        saveCaseApi.pastIllness = et_history_before.getText().toString();
        saveCaseApi.generalInspection = et_body.getText().toString();

        return true;
    }


    /**
     * 查询病历成功
     * @param listCaseBean
     */
    @Override
    public void qryCaseMedicalSuccess(ArrayList<SaveCaseApi> listCaseBean) {
         getActivity().runOnUiThread(() -> {
             SaveCaseApi saveCaseApi = listCaseBean.get(0);
             oldId = saveCaseApi.id;
             et_main.setText(saveCaseApi.chiefComplaint);
             et_history.setText(saveCaseApi.presentIllness);
             et_history_before.setText(saveCaseApi.pastIllness);
             et_body.setText(saveCaseApi.generalInspection);
             et_result.setText(saveCaseApi.diagnosis);
             et_conclusion.setText(saveCaseApi.suggestion);
         });
    }

    /**
     * 查询病历失败
     * @param Failmessage
     */
    @Override
    public void qryCaseMedicalFail(String Failmessage) {
        Log.e(TAG, "qryCaseMedicalFail: "+Failmessage );
        getActivity().runOnUiThread(() -> ToastUtils.show("获取病历失败"));
    }


    /**
     * 提交病历成功
     * @param listCaseBean
     */
    @Override
    public void commitCaseHistorySuccess(SaveCaseApi listCaseBean) {
        CustomCaseHistoryMessage message = new CustomCaseHistoryMessage();
        message.title = "病历记录";
        message.msgDetailId = listCaseBean.id + "";
        message.userId = mIds.get(0);
        message.content = listCaseBean.diagnosis;
        message.suggestion = listCaseBean.suggestion;
        message.appointmentId = saveCaseApi.appointmentId;
        //这个属性区分消息类型 HelloChatController中onDraw方法去绘制布局
        message.setType("CustomCaseHistoryMessage");
        message.setDescription("病历记录消息");
        EventBus.getDefault().post(message);
        if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
            homeActivity.getSupportFragmentManager().popBackStack();
        }
    }

    /**
     * 提交病历失败
     * @param FailMessage
     */
    @Override
    public void commitCaseHistoryFail(String FailMessage) {
        Log.e(TAG, "commitCaseHistoryFail: "+FailMessage );
        getActivity().runOnUiThread(() -> ToastUtils.show("提交病历失败"));
    }
}
