package com.bitvalue.healthmanage.ui.fragment;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.http.model.HttpData;
import com.bitvalue.healthmanage.http.request.GetHistoryApi;
import com.bitvalue.healthmanage.http.request.SaveCaseApi;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.widget.DataUtil;
import com.bitvalue.sdk.collab.helper.CustomCaseHistoryMessage;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class WriteHealthFragment extends AppFragment {

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
    private SaveCaseApi saveCaseApi;
    private ArrayList<String> mIds = new ArrayList<>();
    private GetHistoryApi getHistoryApi;
    private int oldId;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_write_health;
    }

    @Override
    protected void initView() {
        tv_title.setText("病历书写");
        mIds = getArguments().getStringArrayList(Constants.MSG_IDS);
        homeActivity = (HomeActivity) getActivity();
        saveCaseApi = new SaveCaseApi();
//        saveCaseApi.userId = SharedPreManager.getObject(Constants.KYE_USER_BEAN, LoginBean.class, AppApplication.instance()).getUser().user.userId + "";
        saveCaseApi.userId = mIds.get(0);
        saveCaseApi.appointmentId = getArguments().getString(Constants.PLAN_ID);
    }

    @OnClick({R.id.layout_back, R.id.tv_cancel, R.id.tv_preview, R.id.tv_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
            case R.id.tv_cancel:
                if (ifHasData()) {
                    backPress();
                } else {
                    if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        homeActivity.getSupportFragmentManager().popBackStack();
                    }
                }
                break;
            case R.id.tv_preview:
                getData();
                ChatFragment.NewMsgData msgData = new ChatFragment.NewMsgData();
                msgData.saveCaseApi = saveCaseApi;
                homeActivity.switchSecondFragment(Constants.FRAGMENT_HEALTH_HISTORY_PREVIEW, msgData);
                break;
            case R.id.tv_commit:
                if (ifReady()) {
                    commitCase();
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
    protected void initData() {
        getCaseData();
    }

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

    private void getCaseData() {
        getHistoryApi = new GetHistoryApi();
        getHistoryApi.userId = mIds.get(0);
        getHistoryApi.appointmentId = saveCaseApi.appointmentId;
        EasyHttp.post(this).api(getHistoryApi).request(new HttpCallback<HttpData<List<SaveCaseApi>>>(this) {


            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(HttpData<List<SaveCaseApi>> result) {
                super.onSucceed(result);
                if (result.getCode() == 0) {
                    if (null == result.getData() || result.getData().size() == 0) {
                        return;
                    }
                    SaveCaseApi saveCaseApi = result.getData().get(0);

                    oldId = saveCaseApi.id;

                    et_main.setText(saveCaseApi.chiefComplaint);

                    et_history.setText(saveCaseApi.presentIllness);
                    et_history_before.setText(saveCaseApi.pastIllness);
                    et_body.setText(saveCaseApi.generalInspection);
                    et_result.setText(saveCaseApi.diagnosis);
                    et_conclusion.setText(saveCaseApi.suggestion);
                }
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
    }

    private void commitCase() {
        if (oldId != 0) {
            saveCaseApi.id = oldId;
        }
        EasyHttp.post(this).api(saveCaseApi).request(new HttpCallback<HttpData<SaveCaseApi>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(HttpData<SaveCaseApi> result) {
                super.onSucceed(result);
                if (result.getCode() == 0) {
                    if (null == result.getData()) {
                        return;
                    }
                    CustomCaseHistoryMessage message = new CustomCaseHistoryMessage();
                    message.title = "病历记录";
                    message.msgDetailId = result.getData().id + "";
                    message.userId = mIds.get(0);
                    message.content = result.getData().diagnosis;
                    message.suggestion = result.getData().suggestion;
                    message.appointmentId = saveCaseApi.appointmentId;
                    //这个属性区分消息类型 HelloChatController中onDraw方法去绘制布局
                    message.setType("CustomCaseHistoryMessage");
                    message.setDescription("病历记录消息");
                    EventBus.getDefault().post(message);
                    if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        homeActivity.getSupportFragmentManager().popBackStack();
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
