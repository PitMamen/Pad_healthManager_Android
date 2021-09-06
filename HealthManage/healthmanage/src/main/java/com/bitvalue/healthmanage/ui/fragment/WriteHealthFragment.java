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
import com.bitvalue.healthmanage.http.request.TaskDetailApi;
import com.bitvalue.healthmanage.http.response.ArticleBean;
import com.bitvalue.healthmanage.http.response.PlanDetailResult;
import com.bitvalue.healthmanage.http.response.QuestionResultBean;
import com.bitvalue.healthmanage.http.response.TaskPlanDetailBean;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.ui.adapter.HealthPlanDetailAdapter;
import com.bitvalue.sdk.collab.helper.CustomHealthDataMessage;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.layout.WrapRecyclerView;

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

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_write_health;
    }

    @Override
    protected void initView() {
        tv_title.setText("病例书写");

        homeActivity = (HomeActivity) getActivity();
    }

    @OnClick({R.id.layout_back, R.id.tv_cancel, R.id.tv_preview, R.id.tv_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    homeActivity.getSupportFragmentManager().popBackStack();
                }
                break;
            case R.id.tv_cancel:
                break;
            case R.id.tv_preview:
                homeActivity.switchSecondFragment(Constants.FRAGMENT_HEALTH_HISTORY_PREVIEW, "");
                break;
            case R.id.tv_commit:

                break;
        }
    }

    @Override
    protected void initData() {

    }

    private void getPlanData() {
        GetPlanDetailApi getPlanDetailApi = new GetPlanDetailApi();
        EasyHttp.get(this).api(getPlanDetailApi).request(new HttpCallback<HttpData<PlanDetailResult>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(HttpData<PlanDetailResult> result) {
                super.onSucceed(result);
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
    }
}
