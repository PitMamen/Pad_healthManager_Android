package com.bitvalue.healthmanage.ui.fragment;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppApplication;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.http.model.HttpData;
import com.bitvalue.healthmanage.http.request.GetPlanDetailApi;
import com.bitvalue.healthmanage.http.request.SaveCaseApi;
import com.bitvalue.healthmanage.http.request.TaskDetailApi;
import com.bitvalue.healthmanage.http.response.ArticleBean;
import com.bitvalue.healthmanage.http.response.PlanDetailResult;
import com.bitvalue.healthmanage.http.response.QuestionResultBean;
import com.bitvalue.healthmanage.http.response.TaskPlanDetailBean;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.ui.adapter.HealthPlanDetailAdapter;
import com.bitvalue.sdk.collab.helper.CustomCaseHistoryMessage;
import com.bitvalue.sdk.collab.helper.CustomHealthDataMessage;
import com.bitvalue.sdk.collab.helper.CustomHealthMessage;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.layout.WrapRecyclerView;

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

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_write_health;
    }

    @Override
    protected void initView() {
        tv_title.setText("病例书写");
        mIds = getArguments().getStringArrayList(Constants.MSG_IDS);
        homeActivity = (HomeActivity) getActivity();
        saveCaseApi = new SaveCaseApi();
    }

    @OnClick({R.id.layout_back, R.id.tv_cancel, R.id.tv_preview, R.id.tv_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
            case R.id.tv_cancel:
                backPress();
                break;
            case R.id.tv_preview:
                if (ifReady()) {
                    ChatFragment.NewMsgData msgData = new ChatFragment.NewMsgData();
                    msgData.saveCaseApi = saveCaseApi;
                    homeActivity.switchSecondFragment(Constants.FRAGMENT_HEALTH_HISTORY_PREVIEW, msgData);
                }
                break;
            case R.id.tv_commit:
                if (ifReady()) {
                    commitCase();
                }
                break;
        }
    }

    private void backPress() {
        if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
            homeActivity.getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    protected void initData() {

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

    private void commitCase() {
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
                    //这个属性区分消息类型 HelloChatController中onDraw方法去绘制布局
                    message.setType("CustomCaseHistoryMessage");
                    message.setDescription("病历记录消息");
                    EventBus.getDefault().post(message);
                    backPress();
                }
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
    }
}
