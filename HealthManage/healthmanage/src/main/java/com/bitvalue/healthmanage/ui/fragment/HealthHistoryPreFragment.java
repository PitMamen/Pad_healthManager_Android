package com.bitvalue.healthmanage.ui.fragment;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.http.model.HttpData;
import com.bitvalue.healthmanage.http.request.GetPlanDetailApi;
import com.bitvalue.healthmanage.http.response.PlanDetailResult;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class HealthHistoryPreFragment extends AppFragment {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_time)
    TextView tv_time;

    @BindView(R.id.tv_type)
    TextView tv_type;

    @BindView(R.id.tv_main_detail)
    TextView tv_main_detail;

    @BindView(R.id.tv_history_detail)
    TextView tv_history_detail;

    @BindView(R.id.tv_history_before_detail)
    TextView tv_history_before_detail;

    @BindView(R.id.tv_body_detail)
    TextView tv_body_detail;

    @BindView(R.id.tv_result_detail)
    TextView tv_result_detail;

    @BindView(R.id.tv_conclusion_detail)
    TextView tv_conclusion_detail;

    @BindView(R.id.tv_doc_name_detail)
    TextView tv_doc_name_detail;

    private HomeActivity homeActivity;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_health_history_preview;
    }

    @Override
    protected void initView() {
        tv_title.setText("就诊病历");

        homeActivity = (HomeActivity) getActivity();
    }

    @OnClick({R.id.layout_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    homeActivity.getSupportFragmentManager().popBackStack();
                }
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
