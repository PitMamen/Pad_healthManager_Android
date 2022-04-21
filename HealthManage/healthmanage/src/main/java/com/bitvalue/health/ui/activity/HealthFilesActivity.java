package com.bitvalue.health.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.api.ApiResult;
import com.bitvalue.health.api.requestbean.GetLogsApi;
import com.bitvalue.health.api.requestbean.PatientDataMoreApi;
import com.bitvalue.health.base.BaseAdapter;
import com.bitvalue.health.ui.adapter.HealthLogsAdapter;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.DataUtil;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.health.util.customview.WrapRecyclerView;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;


/****
 * 健康管理  中健康档案界面
 */
public class HealthFilesActivity extends AppActivity {

    @BindView(R.id.img_head)
    ImageView img_head;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_sex)
    TextView tv_sex;

    @BindView(R.id.tv_age)
    TextView tv_age;

    @BindView(R.id.tv_height)
    TextView tv_height;

    @BindView(R.id.tv_weight)
    TextView tv_weight;

    @BindView(R.id.tv_blood)
    TextView tv_blood;

    @BindView(R.id.tv_id_no)
    TextView tv_id_no;

    @BindView(R.id.tv_phone)
    TextView tv_phone;

    @BindView(R.id.tv_address)
    TextView tv_address;

    @BindView(R.id.tv_file_id)
    TextView tv_file_id;

    @BindView(R.id.tv_disease)
    TextView tv_disease;

    @BindView(R.id.tv_all)
    TextView tv_all;

    @BindView(R.id.tv_check)
    TextView tv_check;

    @BindView(R.id.tv_inside)
    TextView tv_inside;

    @BindView(R.id.tv_total)
    TextView tv_total;

    @BindView(R.id.tv_no_data)
    TextView tv_no_data;

    @BindView(R.id.list_health_log)
    WrapRecyclerView list_health_log;

    private HealthLogsAdapter mAdapter;
    private ArrayList<GetLogsApi.LogBean> logBeans = new ArrayList<>();
    private GetLogsApi getLogsApi;
    private int total;
    private String userId;
    private String idcardNum;//身份证号码
    private PatientDataMoreApi.PatientDataMoreResponse patientDataMoreResponse;
    private final String defaultmouth = "48";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_health_filse;
    }

    @Override
    protected void initView() {
        userId = getIntent().getStringExtra(Constants.USER_ID);
//        idcardNum = getIntent().getStringExtra(Constants.IDCARD_NUMBER);
        getLogsApi = new GetLogsApi();
        getLogsApi.timeHorizon = defaultmouth;
        getLogsApi.userId = userId;  //249  调试参数
//        getLogsApi.idNumber = "430202198802186610";
        initList(); //初始化各控件
        getPersonalData(); //加载患者详细数据
        getLogsData(); //加载患者的就诊记录
    }


    /***
     * 加载患者详细数据
     */
    private void getPersonalData() {
        PatientDataMoreApi patientDataMoreApi = new PatientDataMoreApi();
        patientDataMoreApi.userId = userId;
        EasyHttp.get(this).api(patientDataMoreApi).request(new HttpCallback<ApiResult<PatientDataMoreApi.PatientDataMoreResponse>>(this) {

            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(ApiResult<PatientDataMoreApi.PatientDataMoreResponse> result) {
                super.onSucceed(result);
                //增加判空
                if (result == null) {
                    return;
                }
                if (result.getCode() == 0) {
                    if (null == result.getData()) {
                        return;
                    }
                    patientDataMoreResponse = result.getData();
                    String curen = TimeUtils.getCurrenTime();
                    int finatime = Integer.valueOf(curen) - Integer.valueOf((patientDataMoreResponse.getBaseInfo().getBirthdayX().substring(0, 4)));
                    tv_name.setText(patientDataMoreResponse.getBaseInfo().getUserNameX());
                    tv_sex.setText(patientDataMoreResponse.getBaseInfo().getUserSexX());
                    tv_age.setText(finatime + "岁");
                    tv_height.setText(patientDataMoreResponse.getExternalInfo().getHeight() + "cm");
                    tv_weight.setText(patientDataMoreResponse.getExternalInfo().getWeight() + "kg");
                    tv_blood.setText(patientDataMoreResponse.getExternalInfo().getBloodType());
                    tv_file_id.setText(patientDataMoreResponse.getBaseInfo().getHealthRecordCode());

                    tv_id_no.setText(DataUtil.desensitizedIdNumber(patientDataMoreResponse.getBaseInfo().getIdentificationNoX()));
                    tv_phone.setText(DataUtil.desensitizedPhoneNumber(patientDataMoreResponse.getExternalInfo().getPhone()));
                    tv_address.setText(patientDataMoreResponse.getExternalInfo().getAddress());
                } else {
                    ToastUtil.toastShortMessage(result.getMessage());
                }
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.layout_close, R.id.layout_all, R.id.layout_check, R.id.layout_inside})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_close:
                finish();
                break;
            case R.id.layout_all:
                getLogsApi.recordType = "all";
                setNavUI(getLogsApi.recordType);
                getLogsData();
                break;
            case R.id.layout_check:
                getLogsApi.recordType = "menzhen";
                setNavUI(getLogsApi.recordType);
                getLogsData();
                break;
            case R.id.layout_inside:
                getLogsApi.recordType = "zhuyuan";
                setNavUI(getLogsApi.recordType);
                getLogsData();
                break;
        }
    }

    private void setNavUI(String recordType) {
        tv_all.setTextColor(getColor(R.color.text_desc_dark));
        tv_check.setTextColor(getColor(R.color.text_desc_dark));
        tv_inside.setTextColor(getColor(R.color.text_desc_dark));

        switch (recordType) {
            case "all":
                tv_all.setTextColor(getColor(R.color.main_blue));
                break;
            case "menzhen":
                tv_check.setTextColor(getColor(R.color.main_blue));
                break;
            case "zhuyuan":
                tv_inside.setTextColor(getColor(R.color.main_blue));
                break;
        }

    }


    /**
     * 初始化adapter
     */
    private void initList() {
        //参考AddQuestionFragment分页已调通
        mAdapter = new HealthLogsAdapter(this);
        mAdapter.setOnItemClickListener((recyclerView, itemView, position) -> {
            Intent intent = new Intent(HealthFilesActivity.this, MRDetailActivity.class);
            intent.putExtra(Constants.DOC_ID, Uri.encode(logBeans.get(position).getDocId()));
            intent.putExtra(Constants.INDEX_NAME, logBeans.get(position).getType());
            HealthFilesActivity.this.startActivity(intent);
        });
        list_health_log.setAdapter(mAdapter);
    }


    /**
     * 网络请求 获取患者看诊记录
     */
    private void getLogsData() {
        EasyHttp.post(this).api(getLogsApi).request(new HttpCallback<ApiResult<ArrayList<GetLogsApi.LogBean>>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(ApiResult<ArrayList<GetLogsApi.LogBean>> result) {
                super.onSucceed(result);
                //增加判空
                if (result == null || result.getData() == null) {
                    return;
                }
                if (result.getCode() == 0) {
                    logBeans = result.getData();
                    if (logBeans.size() > 0) {
                        tv_no_data.setVisibility(View.GONE);
                        list_health_log.setVisibility(View.VISIBLE);
                        mAdapter.setData(logBeans);
                    } else {
                        tv_no_data.setVisibility(View.VISIBLE);
                        list_health_log.setVisibility(View.GONE);
                    }

                    String str = "共<font color='#398CF7'>" + logBeans.size() + "</font>条";
                    tv_total.setText(Html.fromHtml(str));
                } else {
                    ToastUtil.toastShortMessage(result.getMessage());
                }
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
    }
}
