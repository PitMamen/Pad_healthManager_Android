package com.bitvalue.health.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.api.ApiResult;
import com.bitvalue.health.api.requestbean.CommonConfig;
import com.bitvalue.health.api.requestbean.filemodel.HisDataCfxx;
import com.bitvalue.health.api.requestbean.filemodel.HisDataCommon;
import com.bitvalue.health.api.requestbean.filemodel.HisDataJyjgzb;
import com.bitvalue.health.api.requestbean.filemodel.HisDataSsxx;
import com.bitvalue.health.api.requestbean.filemodel.HisDataSysjc;
import com.bitvalue.health.api.requestbean.filemodel.HisDataXjjg;
import com.bitvalue.health.api.requestbean.filemodel.HisDataYmjg;
import com.bitvalue.health.api.requestbean.filemodel.HisDataYqjc;
import com.bitvalue.health.api.requestbean.filemodel.HisDataYzxx;
import com.bitvalue.health.api.requestbean.filemodel.HisDataZdxx;
import com.bitvalue.health.api.requestbean.filemodel.HisMzRecord;
import com.bitvalue.health.api.requestbean.filemodel.HisZyRecord;
import com.bitvalue.health.api.requestbean.filemodel.TbCisMain;
import com.bitvalue.health.api.responsebean.MRDetailResult;
import com.bitvalue.health.contract.healthfilescontract.MRDetailContract;
import com.bitvalue.health.model.healthfilesmodel.MRDetailRequestApi;
import com.bitvalue.health.presenter.healthfilepresenter.MRDetailPresenter;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.DataUtil;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.GsonUtils;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;

import org.xutils.common.util.LogUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;


/**
 * 患者数据列表界面 (含数据环形图)
 */
public class MRDetailActivity extends AppActivity implements View.OnClickListener, MRDetailContract.View {

    private static final String TAG = MRDetailActivity.class.getSimpleName();

    @BindView(R.id.close_iv)
    ImageView close_iv;
    @BindView(R.id.tv_menu_1_hbls)
    TextView tv_menu_1_hbls;
    @BindView(R.id.tv_menu_2_lcbx)
    TextView tv_menu_2_lcbx;
    @BindView(R.id.tv_menu_3_jcjy)
    TextView tv_menu_3_jcjy;
    @BindView(R.id.tv_menu_4_zdjg)
    TextView tv_menu_4_zdjg;
    @BindView(R.id.tv_menu_5_zlfa)
    TextView tv_menu_5_zlfa;
    @BindView(R.id.tv_menu_6_cyjl)
    TextView tv_menu_6_cyjl;
    @BindView(R.id.tv_menu_7_basy)
    TextView tv_menu_7_basy;
    @BindView(R.id.tv_menu_8_fy)
    TextView tv_menu_8_fy;
    @BindView(R.id.tv_menu_9_jy)
    TextView tv_menu_9_jy;

    @BindView(R.id.mr_detail_item_1_huanbinglishi)
    ScrollView mr_detail_item_1_huanbinglishi;
    @BindView(R.id.mr_detail_item_2_linchuangbiaoxian_menzhen)
    ScrollView mr_detail_item_2_linchuangbiaoxian_menzhen;
    @BindView(R.id.mr_detail_item_2_linchuangbiaoxian_zhuyuan)
    ScrollView mr_detail_item_2_linchuangbiaoxian_zhuyuan;
    @BindView(R.id.mr_detail_item_3_jianyanjiance)
    LinearLayout mr_detail_item_3_jianyanjiance;
    @BindView(R.id.sll_detail_item_3_jianyanjiance)
    ScrollView sll_detail_item_3_jianyanjiance;
    @BindView(R.id.mr_detail_item_4_zhenduanjieguo_menzhen)
    ScrollView mr_detail_item_4_zhenduanjieguo_menzhen;
    @BindView(R.id.mr_detail_item_5_zhiliaofangan_menzhen)
    ScrollView mr_detail_item_5_zhiliaofangan_menzhen;
    @BindView(R.id.mr_detail_item_5_zhiliaofangan_zhuyuan)
    LinearLayout mr_detail_item_5_zhiliaofangan_zhuyuan;
    @BindView(R.id.sll_detail_item_5_zhiliaofangan_zhuyuan)
    NestedScrollView sll_detail_item_5_zhiliaofangan_zhuyuan;
    @BindView(R.id.mr_detail_item_6_chuyuanjilu)
    ScrollView mr_detail_item_6_chuyuanjilu;
    @BindView(R.id.mr_detail_item_7_binganshouye)
    ScrollView mr_detail_item_7_binganshouye;
    @BindView(R.id.mr_detail_item_8_yf)
    ScrollView mr_detail_item_8_yf;
    @BindView(R.id.A01)
    TextView A01;
    @BindView(R.id.A02)
    TextView A02;
    @BindView(R.id.A46C)
    TextView A46C;
    @BindView(R.id.A47)
    TextView A47;
    @BindView(R.id.A49)
    TextView A49;
    @BindView(R.id.A48)
    TextView A48;

    @BindView(R.id.A11)
    TextView A11;
    @BindView(R.id.A12C)
    TextView A12C;
    @BindView(R.id.A13)
    TextView A13;
    @BindView(R.id.A14)
    TextView A14;
    @BindView(R.id.A16)
    TextView A16;
    @BindView(R.id.A15C)
    TextView A15C;
    @BindView(R.id.A19C)
    TextView A19C;
    @BindView(R.id.A18x01A18x05)
    TextView A18x01A18x05;
    @BindView(R.id.A17)
    TextView A17;
    @BindView(R.id.A22)
    TextView A22;
    @BindView(R.id.A23C)
    TextView A23C;
    @BindView(R.id.A20)
    TextView A20;
    @BindView(R.id.A38C)
    TextView A38C;
    @BindView(R.id.A21C)
    TextView A21C;
    @BindView(R.id.A24)
    TextView A24;
    @BindView(R.id.A25C)
    TextView A25C;
    @BindView(R.id.A26)
    TextView A26;

    @BindView(R.id.A27)
    TextView A27;
    @BindView(R.id.A28C)
    TextView A28C;
    @BindView(R.id.A29)
    TextView A29;
    @BindView(R.id.A30)
    TextView A30;
    @BindView(R.id.A31C)
    TextView A31C;
    @BindView(R.id.A32)
    TextView A32;
    @BindView(R.id.A33C)
    TextView A33C;
    @BindView(R.id.A34)
    TextView A34;
    @BindView(R.id.A35)
    TextView A35;

    @BindView(R.id.B11C)
    TextView B11C;
    @BindView(R.id.B12)
    TextView B12;
    @BindView(R.id.B13C)
    TextView B13C;
    @BindView(R.id.B14)
    TextView B14;
    @BindView(R.id.B21C)
    TextView B21C;
    @BindView(R.id.B21C2)
    TextView B21C2;
    @BindView(R.id.B21C3)
    TextView B21C3;
    @BindView(R.id.B21C4)
    TextView B21C4;
    @BindView(R.id.B15)
    TextView B15;
    @BindView(R.id.B16C)
    TextView B16C;
    @BindView(R.id.B17)
    TextView B17;
    @BindView(R.id.B20)
    TextView B20;
    @BindView(R.id.C02N)
    TextView C02N;
    @BindView(R.id.C01C)
    TextView C01C;

    //--------------数据显示->患病历史------------------------
    @BindView(R.id.tv_xbs)
    TextView tv_xbs;
    @BindView(R.id.tv_jws)
    TextView tv_jws;
    @BindView(R.id.tv_grs)
    TextView tv_grs;
    @BindView(R.id.tv_jzs)
    TextView tv_jzs;
    @BindView(R.id.tv_gms)
    TextView tv_gms;
    @BindView(R.id.tv_hys)
    TextView tv_hys;
    //--------------数据显示->临床表现-门诊------------------------
    @BindView(R.id.tv_zs)
    TextView tv_zs;
    @BindView(R.id.tv_zzms)
    TextView tv_zzms;
    //--------------数据显示->临床表现-住院------------------------
    @BindView(R.id.tv_ryzztz)
    TextView tv_ryzztz;
    @BindView(R.id.tv_hbz)
    TextView tv_hbz;
    //--------------数据显示->检查检验------------------------
    @BindView(R.id.ll_timeline_zone_jcjy)
    LinearLayout ll_timeline_zone_jcjy; //只有住院病历需要时间轴，门诊病历不需要显示
    @BindView(R.id.recyclerview_jcjy_timeline)
    RecyclerView recyclerview_jcjy_timeline;
    private RecyclerViewAdapter_jcjy_timeline mRecyclerViewAdapter_jcjy_timeline;

    @BindView(R.id.ll_tgjc_zone)
    LinearLayout ll_tgjc_zone; //只有门诊病历需要显示体格检查，住院病历不需要显示
    @BindView(R.id.ll_sysjy_zone)
    LinearLayout ll_sysjy_zone; //
    @BindView(R.id.ll_yqjc_zone)
    LinearLayout ll_yqjc_zone; //
    @BindView(R.id.tv_tgjc)
    TextView tv_tgjc;

    // 重要：实验室检验外层大的item recyclerview
    @BindView(R.id.recyclerview_jcjy_sysjy)
    RecyclerView recyclerview_jcjy_sysjy;
    private RecyclerViewAdapter_sysjy mRecyclerViewAdapter_sysjy;

    @BindView(R.id.recyclerview_jcjy_yqjc) //仪器检查
    RecyclerView recyclerview_jcjy_yqjc;
    private RecyclerViewAdapter_yqjc mRecyclerViewAdapter_yqjc;
    //--------------数据显示->诊断结果-门诊------------------------
    @BindView(R.id.tv_mzzdsm)
    TextView tv_mzzdsm;
    //--------------数据显示->诊断结果-住院------------------------
    @BindView(R.id.mr_detail_item_4_zhenduanjieguo_zhuyuan)
    LinearLayout mr_detail_item_4_zhenduanjieguo_zhuyuan;
    @BindView(R.id.zhenduanjieguo_zhuyuan_reclview)
    RecyclerView zhenduanjieguo_zhuyuan_reclview;
    //--------------数据显示->治疗方案-门诊------------------------
    @BindView(R.id.recyclerview_zlfa_cfxx_xiyao) //处方信息-用药-西药
            RecyclerView recyclerview_zlfa_cfxx_xiyao;
    private RecyclerViewAdapter_cfxx_xiyao mRecyclerViewAdapter_cfxx_xiyao;
    @BindView(R.id.recyclerview_zlfa_ssxx_menzhen) //手术信息
    RecyclerView recyclerview_zlfa_ssxx_menzhen;
    private RecyclerViewAdapter_ssxx_menzhen mRecyclerViewAdapter_ssxx_menzhen;
    //--------------数据显示->治疗方案-住院------------------------
    @BindView(R.id.ll_timeline_zone_zlfa)
    LinearLayout ll_timeline_zone_zlfa; //只有住院病历需要时间轴，门诊病历不需要显示
    @BindView(R.id.recyclerview_zlfa_timeline)
    RecyclerView recyclerview_zlfa_timeline;
    @BindView(R.id.item_zlfa_cfxx_xiyao_title)
    LinearLayout item_zlfa_cfxx_xiyao_title;
    private RecyclerViewAdapter_zlfa_timeline mRecyclerViewAdapter_zlfa_timeline;

    @BindView(R.id.recyclerview_zlfa_yzxx) //用药(从医嘱信息里面取)
    RecyclerView recyclerview_zlfa_yzxx;
    private RecyclerViewAdapter_yzxx mRecyclerViewAdapter_yzxx;
    @BindView(R.id.recyclerview_zlfa_ssxx_zhuyuan) //手术信息
    RecyclerView recyclerview_zlfa_ssxx_zhuyuan;
    private RecyclerViewAdapter_ssxx_zhuyuan mRecyclerViewAdapter_ssxx_zhuyuan;
    private RecyclerViewAdapter_zdxx_zhuyuan mRecyclerViewAdapter_zdxx_zhuyuan;
    //--------------数据显示->出院记录-只有住院病历有------------------------
    @BindView(R.id.tv_cyjl_ryzztz)
    TextView tv_cyjl_ryzztz;
    @BindView(R.id.tv_cyjl_hbz)
    TextView tv_cyjl_hbz;
    @BindView(R.id.tv_cyjl_ryzd)
    TextView tv_cyjl_ryzd;
    @BindView(R.id.tv_cyjl_zlgc)
    TextView tv_cyjl_zlgc;
    @BindView(R.id.tv_cyjl_cyzd)
    TextView tv_cyjl_cyzd;
    @BindView(R.id.tv_cyjl_cyqk)
    TextView tv_cyjl_cyqk;
    @BindView(R.id.tv_cyjl_cyyz)
    TextView tv_cyjl_cyyz;
    //=================费用=====================
    @BindView(R.id.piechart)
    PieChart piechart;

    @BindView(R.id.sfxxje1_tv)
    TextView sfxxje1_tv;
    @BindView(R.id.sfxxje2_tv)
    TextView sfxxje2_tv;
    @BindView(R.id.sfxxje3_tv)
    TextView sfxxje3_tv;
    @BindView(R.id.sfxxje4_tv)
    TextView sfxxje4_tv;
    @BindView(R.id.sfxxje5_tv)
    TextView sfxxje5_tv;
    @BindView(R.id.sfxxje6_tv)
    TextView sfxxje6_tv;
    @BindView(R.id.sfxxje7_tv)
    TextView sfxxje7_tv;
    @BindView(R.id.sfxxje8_tv)
    TextView sfxxje8_tv;
    @BindView(R.id.sfxxje9_tv)
    TextView sfxxje9_tv;
    @BindView(R.id.sfxxje10_tv)
    TextView sfxxje10_tv;
    @BindView(R.id.sfxxje11_tv)
    TextView sfxxje11_tv;
    @BindView(R.id.sfxxje12_tv)
    TextView sfxxje12_tv;
    @BindView(R.id.sfxxje13_tv)
    TextView sfxxje13_tv;
    @BindView(R.id.sfxxje14_tv)
    TextView sfxxje14_tv;
    @BindView(R.id.sfxxje15_tv)
    TextView sfxxje15_tv;

    @BindView(R.id.sfxxje_all_tv)
    TextView sfxxje_all_tv;
    @BindView(R.id.sfxxje_time_tv)
    TextView sfxxje_time_tv;
    //分享病历
    @BindView(R.id.share_iv)
    ImageView shareBtn;

    private MRDetailContract.Presenter presenter = new MRDetailPresenter(this);
    private String mPrivateCloudName; //数据来源
    private String mIndex; //记录类型，门诊还是住院
    private String mDocumentId; //病历ID，用来获取病历详情
    private String name, sex, age, diagnosis, time;
    private String providerId;
    private AlertDialog.Builder builder;
    private HisDataCommon mHisDataCommon;
    private HisMzRecord hisMzRecord;
    private HisZyRecord hisZyRecord;
    private List<HisDataCfxx> hisMzRecord_cfxx_xiyao_List; //门诊用药-西药 数据，hisMzRecord.getCfxx()中过滤出来的
    //住院病历 检查检验 时间轴的数据构造----------------------------begin
    private TreeMap<String, YqjcAndSysjcCombineData> mYqjcAndSysjcCombineDataMap;
    ;  //仪器检查和实验室检查的组合数据，用来显示时间轴数据
    private String[] mYqjcAndSysjcCombineData_keyArray; //将keySet转换为数组，方便使用使用position获取对应位置的值
    private int mJcjyCurrentPositon = 0; //检查检验当前时间轴位置
    private MRDetailResult mrDetailResult;


    /**
     * 仪器检查和实验室检查的组合数据，用来显示时间轴数据
     * 组合规则：相同日期的数据放在同一组合 //徐刚：coalesce(用实验室检验表中的【检验日期】，仪器检查的【检查时间】)
     */
    private class YqjcAndSysjcCombineData {
        //检查日期（时间轴日期）
        public String date;
        //仪器检查
        public List<HisDataYqjc> hisDataYqjcList; //实际上同一天有很多的仪器检查，同一天的仪器检查都合并到一起
        //实验室检查
        public List<HisDataSysjc> hisDataSysjcList; //重要！假设同一天也有很多的实验室检查数据，同一天的势函数检查数据也合并到一起


    }
    //住院病历 检查检验 时间轴的数据构造----------------------------end

    //住院病历 治疗方案 时间轴的数据构造----------------------------begin
    private TreeMap<String, YzxxAndSsxxCombineData> mYzxxAndSsxxCombineDataMap;  //医嘱信息和手术信息的组合数据，用来显示时间轴数据
    private String[] mYzxxAndSsxxCombineData_keyArray; //将keySet转换为数组，方便使用使用position获取对应位置的值
    private int mZlfaCurrentPositon = 0; //检查检验当前时间轴位置

    /**
     * 医嘱信息和手术信息的组合数据，用来显示时间轴数据
     * 组合规则：相同日期的数据放在同一组合 //徐刚：coalesce(医嘱信息表中的【医嘱下达时间】，手术信息表中的【手术起始时间】)
     */
    private class YzxxAndSsxxCombineData {
        //发生日期（时间轴日期）
        public Date date;
        //医嘱信息
        public List<HisDataYzxx> hisDataYzxxList; //用药信息放在了这里面，所以同一天医嘱信息可能有多条
        //手术信息
        public List<HisDataSsxx> hisDataSsxxList; //实际上同一天可以有多起手术，同一天的手术信息都合并到一起
    }

    //住院病历 治疗方案 时间轴的数据构造----------------------------end
    public static final int UPDATE_JIAN_YAN = 0x1;
    public static final int UPDATE_JIAN_CHA = 0x2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_JIAN_YAN:
                    sortSysjcByTime();
                    break;
                case UPDATE_JIAN_CHA:
                    sortYqjcByTime();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mr_detail;
    }

    @Override
    protected void initView() {
        setStatusColor(this, MRDetailActivity.this.getColor(R.color.white));
        setAndroidNativeLightStatusBar(this, true);
        close_iv.setOnClickListener(this);
        tv_menu_1_hbls.setOnClickListener(this);
        tv_menu_2_lcbx.setOnClickListener(this);
        tv_menu_3_jcjy.setOnClickListener(this);
        tv_menu_4_zdjg.setOnClickListener(this);
        tv_menu_5_zlfa.setOnClickListener(this);
        tv_menu_6_cyjl.setOnClickListener(this);
        tv_menu_7_basy.setOnClickListener(this);
        tv_menu_8_fy.setOnClickListener(this);
        tv_menu_9_jy.setOnClickListener(this);
        shareBtn.setOnClickListener(this);

        mPrivateCloudName = getIntent().getStringExtra("privateCloudName");
        mDocumentId = getIntent().getStringExtra("documentId");
        providerId = getIntent().getStringExtra("privateCloudId");
        name = getIntent().getStringExtra("name");
        sex = getIntent().getStringExtra("sex");
        age = getIntent().getStringExtra("age");
        diagnosis = getIntent().getStringExtra("diagnosis");
        time = getIntent().getStringExtra("time");
        tv_menu_2_lcbx.setVisibility(View.GONE);
        ll_tgjc_zone.setVisibility(View.GONE);
        //只展示住院的诊断信息
        if (CommonConfig.MR_INDEX_ZHUYUAN_ID.equals(mIndex)) {
            tv_menu_4_zdjg.setVisibility(View.VISIBLE);
        } else {
            tv_menu_4_zdjg.setVisibility(View.GONE);
        }
        //门诊无 个人信息页 切换第二页 电子病历  。2020.11.11 屏蔽电子病历，改为切换到检查
        if (CommonConfig.MR_INDEX_MENZHEN_ID.equals(mIndex)) {
            tv_menu_7_basy.setVisibility(View.GONE);
            showSelectFragment("jcjy");
        }

        //初始化入参
        String docId = getIntent().getStringExtra(Constants.DOC_ID);
        mIndex = getIntent().getStringExtra(Constants.INDEX_NAME);
        MRDetailRequestApi mrDetailRequestApi = new MRDetailRequestApi();
        mrDetailRequestApi.docId = docId;
        mrDetailRequestApi.indexName = mIndex;

//        presenter.getMRDetail(mrDetailRequestApi);
        getDetailData(mrDetailRequestApi);

        setupFeed(); //设置结果列表的RecyclerViewAdapter
//            }
    }

    private void getDetailData(MRDetailRequestApi mrDetailRequestApi) {
        EasyHttp.get(this).api(mrDetailRequestApi).request(new HttpCallback<ApiResult<MRDetailResult>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(ApiResult<MRDetailResult> result) {
                super.onSucceed(result);
                //增加判空
                if (result == null) {
                    return;
                }
                if (result.getCode() == 0) {
                    mrDetailResult = result.getData();
                    String toJson = new Gson().toJson(mrDetailResult);
                    if (null == mrDetailResult) {
                        return;
                    }
                    refreshMRDetail(new Gson().toJson(mrDetailResult.medicalRecord.source), new Gson().toJson(mrDetailResult.medicalRecord.medicalMain));
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_iv:
                finish();
                break;
            case R.id.tv_menu_1_hbls:
                showSelectFragment("hbls");
                break;

            case R.id.tv_menu_2_lcbx:
                showSelectFragment("lcbx");
                break;
            case R.id.tv_menu_3_jcjy:
                showSelectFragment("jcjy");
                break;

            case R.id.tv_menu_4_zdjg:
                showSelectFragment("zdjg");
                break;

            case R.id.tv_menu_5_zlfa:
                showSelectFragment("zlfa");
                break;

            case R.id.tv_menu_6_cyjl:
                showSelectFragment("cyjl");
                break;
            case R.id.tv_menu_7_basy:
                showSelectFragment("basy");
                break;
            case R.id.tv_menu_8_fy:
                showSelectFragment("fy");
                break;
            case R.id.tv_menu_9_jy:
                showSelectFragment("jy");
                break;
            case R.id.share_iv:
                shareToChatIm();//TODO 可能不需要分享
                break;
            default:
                break;
        }
    }


    private void shareToChatIm() {
        String zdmc = "";
        String timec = "";
        String xm = TextUtils.isEmpty(name) ? "" : name;
        String xb = TextUtils.isEmpty(sex) ? "" : sex;
        String nl = TextUtils.isEmpty(age) ? "" : age;

        if (TextUtils.isEmpty(diagnosis)) {
            zdmc = "";
        } else {
            zdmc = "诊断 ：" + diagnosis;
        }
        if (TextUtils.isEmpty(time)) {
            timec = "";
        } else {
            timec = "就诊时间 ：" + time;
        }
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo("cn.wildfirechat.chat", 0);
            if (packageInfo != null) {
                Intent intent = new Intent();
                ComponentName componentName = new ComponentName("cn.wildfirechat.chat", "cn.wildfire.chat.app.main.MainActivity");
                intent.setComponent(componentName);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("contact", "通讯录");
                intent.putExtra("share", "1");
                intent.putExtra("name", xm + "  " + xb + "  " + nl);
                intent.putExtra("zdmc", zdmc);
                intent.putExtra("time", timec);
                intent.putExtra("mPrivateCloudName", TextUtils.isEmpty(mPrivateCloudName) ? "" : mPrivateCloudName);
                intent.putExtra("privateCloudId", TextUtils.isEmpty(providerId) ? "" : providerId);
                intent.putExtra("mIndex", TextUtils.isEmpty(mIndex) ? "" : mIndex);
                intent.putExtra("mDocumentId", TextUtils.isEmpty(mDocumentId) ? "" : mDocumentId);
//                 intent.putExtra("token",MyApplication.getInstance().getToken());
                startActivity(intent);
            } else {
                Toast.makeText(this, "没有安装医疗协作APP", Toast.LENGTH_LONG).show();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    //显示fragment
    private void showSelectFragment(String type) {
        //blue_0489DF black_565656
        tv_menu_7_basy.setTextColor(ContextCompat.getColor(this, R.color.black_files));
        tv_menu_1_hbls.setTextColor(ContextCompat.getColor(this, R.color.black_files));
        tv_menu_2_lcbx.setTextColor(ContextCompat.getColor(this, R.color.black_files));
        tv_menu_3_jcjy.setTextColor(ContextCompat.getColor(this, R.color.black_files));
        tv_menu_4_zdjg.setTextColor(ContextCompat.getColor(this, R.color.black_files));
        tv_menu_5_zlfa.setTextColor(ContextCompat.getColor(this, R.color.black_files));
        tv_menu_6_cyjl.setTextColor(ContextCompat.getColor(this, R.color.black_files));
        tv_menu_8_fy.setTextColor(ContextCompat.getColor(this, R.color.black_files));
        tv_menu_9_jy.setTextColor(ContextCompat.getColor(this, R.color.black_files));

        tv_menu_7_basy.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_medical_7_no, 0, 0, 0);
        tv_menu_1_hbls.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_medical_1_no, 0, 0, 0);
//        tv_menu_2_lcbx.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_medical_2_no,0,0,0);
        tv_menu_3_jcjy.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_medical_3_no, 0, 0, 0);
        tv_menu_4_zdjg.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_medical_1_no, 0, 0, 0);
        tv_menu_5_zlfa.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_medical_5_no, 0, 0, 0);
        tv_menu_6_cyjl.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_medical_6_no, 0, 0, 0);
        tv_menu_8_fy.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_medical_8_no, 0, 0, 0);
        tv_menu_9_jy.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_medical_9_no, 0, 0, 0);

        mr_detail_item_7_binganshouye.setVisibility(View.GONE);
        mr_detail_item_1_huanbinglishi.setVisibility(View.GONE);
        mr_detail_item_2_linchuangbiaoxian_menzhen.setVisibility(View.GONE);
        mr_detail_item_2_linchuangbiaoxian_zhuyuan.setVisibility(View.GONE);
        mr_detail_item_3_jianyanjiance.setVisibility(View.GONE);
        mr_detail_item_4_zhenduanjieguo_menzhen.setVisibility(View.GONE);
        mr_detail_item_4_zhenduanjieguo_zhuyuan.setVisibility(View.GONE);
        mr_detail_item_5_zhiliaofangan_menzhen.setVisibility(View.GONE);
        mr_detail_item_5_zhiliaofangan_zhuyuan.setVisibility(View.GONE);
        mr_detail_item_6_chuyuanjilu.setVisibility(View.GONE);
        mr_detail_item_8_yf.setVisibility(View.GONE);

        if (TextUtils.isEmpty(type)) {
            return;
        }
        switch (type) {
            case "basy":
                tv_menu_7_basy.setTextColor(ContextCompat.getColor(this, R.color.blue_files));
                mr_detail_item_7_binganshouye.setVisibility(View.VISIBLE);
                tv_menu_7_basy.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_medical_7_yes, 0, 0, 0);
                break;
            case "hbls":
                tv_menu_1_hbls.setTextColor(ContextCompat.getColor(this, R.color.blue_files));
                mr_detail_item_1_huanbinglishi.setVisibility(View.VISIBLE);
                tv_menu_1_hbls.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_medical_1_yes, 0, 0, 0);
                break;
            case "lcbx":
                tv_menu_2_lcbx.setTextColor(ContextCompat.getColor(this, R.color.blue_files));
                if (CommonConfig.MR_INDEX_MENZHEN_ID.equals(mIndex)) {
                    mr_detail_item_2_linchuangbiaoxian_menzhen.setVisibility(View.VISIBLE);
                } else if (CommonConfig.MR_INDEX_ZHUYUAN_ID.equals(mIndex)) {
                    mr_detail_item_2_linchuangbiaoxian_zhuyuan.setVisibility(View.VISIBLE);
                }
//                tv_menu_2_lcbx.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_medical_2,0,0,0);
                break;
            case "jcjy":
                tv_menu_3_jcjy.setTextColor(ContextCompat.getColor(this, R.color.blue_files));
                mr_detail_item_3_jianyanjiance.setVisibility(View.VISIBLE);
                tv_menu_3_jcjy.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_medical_3_yes, 0, 0, 0);
                ll_sysjy_zone.setVisibility(View.GONE);
                ll_yqjc_zone.setVisibility(View.VISIBLE);
                handler.sendEmptyMessage(UPDATE_JIAN_CHA);
                break;
            case "zdjg":
                tv_menu_4_zdjg.setTextColor(ContextCompat.getColor(this, R.color.blue_files));
                if (CommonConfig.MR_INDEX_MENZHEN_ID.equals(mIndex)) {
                    mr_detail_item_4_zhenduanjieguo_menzhen.setVisibility(View.VISIBLE);
                } else if (CommonConfig.MR_INDEX_ZHUYUAN_ID.equals(mIndex)) {
                    mr_detail_item_4_zhenduanjieguo_zhuyuan.setVisibility(View.VISIBLE);
                }
                tv_menu_4_zdjg.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_medical_1_yes, 0, 0, 0);
                break;
            case "zlfa":
                tv_menu_5_zlfa.setTextColor(ContextCompat.getColor(this, R.color.blue_files));
                if (CommonConfig.MR_INDEX_MENZHEN_ID.equals(mIndex)) {
                    mr_detail_item_5_zhiliaofangan_menzhen.setVisibility(View.VISIBLE);
                } else if (CommonConfig.MR_INDEX_ZHUYUAN_ID.equals(mIndex)) {
                    mr_detail_item_5_zhiliaofangan_zhuyuan.setVisibility(View.VISIBLE);
                }
                tv_menu_5_zlfa.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_medical_5_yes, 0, 0, 0);
                break;
            case "cyjl":
                tv_menu_6_cyjl.setTextColor(ContextCompat.getColor(this, R.color.blue_files));
                mr_detail_item_6_chuyuanjilu.setVisibility(View.VISIBLE);
                tv_menu_6_cyjl.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_medical_6_yes, 0, 0, 0);
                break;
            case "fy":
                tv_menu_8_fy.setTextColor(ContextCompat.getColor(this, R.color.blue_files));
                mr_detail_item_8_yf.setVisibility(View.VISIBLE);
                tv_menu_8_fy.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_medical_8_yes, 0, 0, 0);
                break;
            case "jy":
                showDialog();//TODO 此处先修改成项目的loading
//                DialogUtil.showProgressDialog(this, getString(R.string.data_loding));
                tv_menu_9_jy.setTextColor(ContextCompat.getColor(this, R.color.blue_files));
                mr_detail_item_3_jianyanjiance.setVisibility(View.VISIBLE);
                tv_menu_9_jy.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_medical_9_yes, 0, 0, 0);
                ll_sysjy_zone.setVisibility(View.VISIBLE);
                ll_yqjc_zone.setVisibility(View.GONE);
                handler.sendEmptyMessage(UPDATE_JIAN_YAN);
                break;
        }
    }

    @Override
    public void refreshMRDetail(String sourceEntityJsonStr, String medicalMainStr) {
        if (TextUtils.isEmpty(sourceEntityJsonStr)) {
            return;
        }
        List<Float> floatList = new ArrayList<>();
        if (CommonConfig.MR_INDEX_MENZHEN_ID.equals(mIndex)) {

            //门诊没有首页，直接显示检查
            tv_menu_7_basy.setVisibility(View.GONE);
            mr_detail_item_7_binganshouye.setVisibility(View.GONE);

            tv_menu_3_jcjy.setTextColor(ContextCompat.getColor(this, R.color.blue_files));
            mr_detail_item_3_jianyanjiance.setVisibility(View.VISIBLE);
            tv_menu_3_jcjy.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_medical_3_yes, 0, 0, 0);
            ll_sysjy_zone.setVisibility(View.GONE);
            ll_yqjc_zone.setVisibility(View.VISIBLE);
            handler.sendEmptyMessage(UPDATE_JIAN_CHA);

            //如果是门诊病历，那么隐藏出院记录菜单
            tv_menu_6_cyjl.setVisibility(View.GONE);
            hisMzRecord = GsonUtils.getGson().fromJson(sourceEntityJsonStr, new TypeToken<HisMzRecord>() {
            }.getType());
            mRecyclerViewAdapter_yqjc.notifyDataSetChanged();
            mHisDataCommon = hisMzRecord;
//            //------------显示数据->患者信息---------------------
            //门诊无个人数据信息

            if (StringUtils.isEmpty(hisMzRecord.getXbs())) {
                tv_xbs.setText(getString(R.string.tip_no_data));
                tv_xbs.setTextColor(ContextCompat.getColor(this, R.color.text_desc_dark));
            } else {
                tv_xbs.setText(hisMzRecord.getXbs());
            }
            if (StringUtils.isEmpty(hisMzRecord.getJws())) {
                tv_jws.setText(getString(R.string.tip_no_data));
                tv_jws.setTextColor(ContextCompat.getColor(this, R.color.text_desc_dark));
            } else {
                tv_jws.setText(hisMzRecord.getJws());
                //------------显示数据->患病历史---------------------
            }
            if (StringUtils.isEmpty(hisMzRecord.getGrs())) {
                tv_grs.setText(getString(R.string.tip_no_data));
                tv_grs.setTextColor(ContextCompat.getColor(this, R.color.text_desc_dark));
            } else {
                tv_grs.setText(hisMzRecord.getGrs());
            }
            if (StringUtils.isEmpty(hisMzRecord.getJzs())) {
                tv_jzs.setText(getString(R.string.tip_no_data));
                tv_jzs.setTextColor(ContextCompat.getColor(this, R.color.text_desc_dark));
            } else {
                tv_jzs.setText(hisMzRecord.getJzs());
            }
            if (StringUtils.isEmpty(hisMzRecord.getGms())) {
                tv_gms.setText(getString(R.string.tip_no_data));
                tv_gms.setTextColor(ContextCompat.getColor(this, R.color.text_desc_dark));
            } else {
                tv_gms.setText(hisMzRecord.getGms());
            }
            if (StringUtils.isEmpty(hisMzRecord.getHys())) {
                tv_hys.setText(getString(R.string.tip_no_data));
                tv_hys.setTextColor(ContextCompat.getColor(this, R.color.text_desc_dark));
            } else {
                tv_hys.setText(hisMzRecord.getHys());
            }
            //------------显示数据->临床表现---------------------
            if (StringUtils.isEmpty(hisMzRecord.getZs())) {
                tv_zs.setText(getString(R.string.tip_no_data));
                tv_zs.setTextColor(ContextCompat.getColor(this, R.color.text_desc_dark));
            } else {
                tv_zs.setText(hisMzRecord.getZs());
            }
            if (StringUtils.isEmpty(hisMzRecord.getZzms())) {
                tv_zzms.setText(getString(R.string.tip_no_data));
                tv_zzms.setTextColor(ContextCompat.getColor(this, R.color.text_desc_dark));
            } else {
                tv_zzms.setText(hisMzRecord.getZzms());
            }
            //--------------数据显示->检查检验------------------------
            ll_timeline_zone_jcjy.setVisibility(View.GONE); //只有住院病历需要时间轴，门诊病历不需要显示
            ll_tgjc_zone.setVisibility(View.GONE); //只有门诊病历需要显示体格检查，住院病历不需要显示
            if (StringUtils.isEmpty(hisMzRecord.getTgjc())) {
                tv_tgjc.setText(getString(R.string.tip_no_data));
                tv_tgjc.setTextColor(ContextCompat.getColor(this, R.color.text_desc_dark));
            } else {
                tv_tgjc.setText(hisMzRecord.getTgjc());
            }
            //--------------数据显示->诊断结果------------------------
            if (StringUtils.isEmpty(hisMzRecord.getMzzdsm())) {
                tv_mzzdsm.setText(getString(R.string.tip_no_data));
                tv_mzzdsm.setTextColor(ContextCompat.getColor(this, R.color.text_desc_dark));
            } else {
                tv_mzzdsm.setText(hisMzRecord.getMzzdsm());
            }
            //--------------数据显示->治疗方案------------------------
            /**
             * 从门诊病历里的处方信息列表立面，筛选出中药和西药数据
             * 判断规则：
             *  1.是否是药品
             *  2.中药饮片处方字段getZyypcf()如果不为空，就认为是中药，否则为西药
             */
            sortCfxxByZhongyaoXiyao();

            //手术是公共信息，并且是一个列表，门诊病历直接全部罗列显示

            //-------------------费用-收费信息---------------------
            if (hisMzRecord.getSfxx() == null || hisMzRecord.getSfxx().size() == 0) {
                tv_menu_8_fy.setVisibility(View.GONE);
            } else {
                floatList = hisMzRecord.getSfxxList(hisMzRecord.getSfxx());
                getPieChart(floatList);
                sfxxje_time_tv.setText(TimeUtils.getTime(hisMzRecord.getSfxx().get(0).getStfsj(), TimeUtils.YY_MM_DD_FORMAT_3));
            }

        } else if (CommonConfig.MR_INDEX_ZHUYUAN_ID.equals(mIndex)) {
            //如果是住院病历，那么显示出院记录菜单
            tv_menu_6_cyjl.setVisibility(View.VISIBLE);
            hisZyRecord = GsonUtils.getGson().fromJson(sourceEntityJsonStr, new TypeToken<HisZyRecord>() {
            }.getType());
            mHisDataCommon = hisZyRecord;
            //------------显示数据->患者信息---------------------

            if (!TextUtils.isEmpty(medicalMainStr)) {
                TbCisMain medecalMain = GsonUtils.getGson().fromJson(medicalMainStr, new TypeToken<TbCisMain>() {
                }.getType());
                if (medecalMain != null) {
                    A02.setText(mrDetailResult.hospitalName);
                    A01.setText(medecalMain.getYljgdm());
                    A46C.setText(medecalMain.getYlfffsmc());
                    A47.setText(medecalMain.getJkkh());
                    A49.setText(medecalMain.getZycs());
                    A48.setText(medecalMain.getBah());
                    A11.setText(medecalMain.getXm());
                    if ("1".equals(medecalMain.getXb())) {
                        A12C.setText("男");
                    } else {
                        A12C.setText("女");
                    }
                    A13.setText(medecalMain.getCsny());
                    A14.setText(DataUtil.ConvertAge2Str(hisZyRecord.getNl()));
//                    A16.setText(hisZyRecord.getZyts() + "天");
                    A16.setText((null == hisZyRecord.getZyts()) ? ("0天") : (hisZyRecord.getZyts() + "天"));
                    A15C.setText(medecalMain.getGjmc());
                    A19C.setText(medecalMain.getMzmc());
                    A18x01A18x05.setText("-");
                    A17.setText(medecalMain.getXserytz() + "");
                    A22.setText(medecalMain.getCsd());
                    A23C.setText(medecalMain.getJg());
                    A20.setText(medecalMain.getSfz());
                    A38C.setText(medecalMain.getZybm() + "");
                    A21C.setText(medecalMain.getHyzkmc());
                    A24.setText(medecalMain.getHkdz());
                    A25C.setText(medecalMain.getHkyb());
                    A26.setText(medecalMain.getJzd());
                    A27.setText(medecalMain.getLxdh());
                    A28C.setText(medecalMain.getXzzyb());
                    A29.setText(medecalMain.getGzdw());
                    A30.setText(medecalMain.getGzdwdh());
                    A31C.setText(medecalMain.getGzdwyb());
                    A32.setText(medecalMain.getLxrxm());
                    A33C.setText(medecalMain.getLxrgxmc());
                    A34.setText(medecalMain.getLxrdz());
                    A35.setText(medecalMain.getLxrdh());

                    B11C.setText(medecalMain.getRylxmc());
//                    B12.setText(TimeUtils.getTime(hisZyRecord.getRysj(), TimeUtils.YY_MM_DD_FORMAT_3));
                    B12.setText((null != medecalMain.getRysj()) ? medecalMain.getRysj().substring(0, 10) : "");
                    B13C.setText(hisZyRecord.getRyksmc());
                    B14.setText(medecalMain.getRybq());
                    B21C.setText("-");
                    B21C2.setText("-");
                    B21C3.setText("-");
                    B21C4.setText("-");
                    B15.setText(TimeUtils.getTime(hisZyRecord.getCysj(), TimeUtils.YY_MM_DD_FORMAT_3));
                    B16C.setText(hisZyRecord.getCyksmc());
                    B17.setText(medecalMain.getCybq());
                    B20.setText((null == hisZyRecord.getZyts()) ? "0" : (hisZyRecord.getZyts() + ""));
                    C02N.setText(hisZyRecord.getZdmc());
                    C01C.setText(hisZyRecord.getZdbm0());
                }
            }

            //------------显示数据->患病历史---------------------
            if (StringUtils.isEmpty(hisZyRecord.getXbs())) {
                tv_xbs.setText(getString(R.string.tip_no_data));
                tv_xbs.setTextColor(ContextCompat.getColor(this, R.color.text_desc_dark));
            } else {
                tv_xbs.setText(hisZyRecord.getXbs());
            }
            if (StringUtils.isEmpty(hisZyRecord.getJws())) {
                tv_jws.setText(getString(R.string.tip_no_data));
                tv_jws.setTextColor(ContextCompat.getColor(this, R.color.text_desc_dark));
            } else {
                tv_jws.setText(hisZyRecord.getJws());
            }
            if (StringUtils.isEmpty(hisZyRecord.getGrs())) {
                tv_grs.setText(getString(R.string.tip_no_data));
                tv_grs.setTextColor(ContextCompat.getColor(this, R.color.text_desc_dark));
            } else {
                tv_grs.setText(hisZyRecord.getGrs());
            }
            if (StringUtils.isEmpty(hisZyRecord.getJzs())) {
                tv_jzs.setText(getString(R.string.tip_no_data));
                tv_jzs.setTextColor(ContextCompat.getColor(this, R.color.text_desc_dark));
            } else {
                tv_jzs.setText(hisZyRecord.getJzs());
            }
            if (StringUtils.isEmpty(hisZyRecord.getGms())) {
                tv_gms.setText(getString(R.string.tip_no_data));
                tv_gms.setTextColor(ContextCompat.getColor(this, R.color.text_desc_dark));
            } else {
                tv_gms.setText(hisZyRecord.getGms());
            }
            if (StringUtils.isEmpty(hisZyRecord.getHys())) {
                tv_hys.setText(getString(R.string.tip_no_data));
                tv_hys.setTextColor(ContextCompat.getColor(this, R.color.text_desc_dark));
            } else {
                tv_hys.setText(hisZyRecord.getHys());
            }
            //------------显示数据->临床表现---------------------
            if (StringUtils.isEmpty(hisZyRecord.getRyzztz())) {
                tv_ryzztz.setText(getString(R.string.tip_no_data));
                tv_ryzztz.setTextColor(ContextCompat.getColor(this, R.color.text_desc_dark));
            } else {
                tv_ryzztz.setText(hisZyRecord.getRyzztz()); //【入院时主要症状及体征】
            }
            if (StringUtils.isEmpty(hisZyRecord.getHbz())) {
                tv_hbz.setText(getString(R.string.tip_no_data));
                tv_hbz.setTextColor(ContextCompat.getColor(this, R.color.text_desc_dark));
            } else {
                tv_hbz.setText(hisZyRecord.getHbz()); //【合并症】
            }
            //--------------数据显示->检查检验------------------------
            ll_timeline_zone_jcjy.setVisibility(View.VISIBLE); //只有住院病历需要时间轴，门诊病历不需要显示
            ll_tgjc_zone.setVisibility(View.GONE); //只有门诊病历需要显示体格检查，住院病历不需要显示
            /**
             * 住院的检查检验数据需要按照时间轴来显示，由于【仪器检查】和【实验室数据】是独立的数组，
             * 所以需要合并数据，并且按照时间排序：用实验室检验表中的【检验日期】，仪器检查的【检查时间】来排序
             */
            //--------------数据显示->治疗方案------------------------
            /**
             * 住院的治疗方案数据需要按照时间轴来显示，由于【医嘱信息】和【手术信息】是独立的数组，
             * 所以需要合并数据，并且按照时间排序：用医嘱信息表中的【医嘱下达时间】，手术信息表的【手术起始时间】来排序
             */
            sortYzxxAndSsxxByTime();
            //--------------数据显示->出院记录-只有住院病历有------------------------
            //入院情况
            if (StringUtils.isEmpty(hisZyRecord.getRyzztz())) {
                tv_cyjl_ryzztz.setText(getString(R.string.tip_no_data));
                tv_cyjl_ryzztz.setTextColor(ContextCompat.getColor(this, R.color.text_desc_dark));
            } else {
                if ("-".equals(hisZyRecord.getRyzztz())) {
                    tv_cyjl_ryzztz.setText("无");
                } else {
                    tv_cyjl_ryzztz.setText(hisZyRecord.getRyzztz()); //【入院时主要症状及体征】
                }
            }
            if (StringUtils.isEmpty(hisZyRecord.getHbz())) {
                tv_cyjl_hbz.setText(getString(R.string.tip_no_data));
                tv_cyjl_hbz.setTextColor(ContextCompat.getColor(this, R.color.text_desc_dark));
            } else {
                if ("-".equals(hisZyRecord.getHbz())) {
                    tv_cyjl_hbz.setText("无");
                } else {
                    tv_cyjl_hbz.setText(hisZyRecord.getHbz()); //【合并症】
                }
            }
            if (StringUtils.isEmpty(hisZyRecord.getRyzd())) {
                tv_cyjl_ryzd.setText(getString(R.string.tip_no_data));
                tv_cyjl_ryzd.setTextColor(ContextCompat.getColor(this, R.color.text_desc_dark));
            } else {
                if ("-".equals(hisZyRecord.getRyzd())) {
                    tv_cyjl_ryzd.setText("无");
                } else {
                    tv_cyjl_ryzd.setText(hisZyRecord.getRyzd());
                }
            }
            if (StringUtils.isEmpty(hisZyRecord.getZlgc())) {
                tv_cyjl_zlgc.setText(getString(R.string.tip_no_data));
                tv_cyjl_zlgc.setTextColor(ContextCompat.getColor(this, R.color.text_desc_dark));
            } else {
                if ("-".equals(hisZyRecord.getZlgc())) {
                    tv_cyjl_zlgc.setText("无");
                } else {
                    tv_cyjl_zlgc.setText(hisZyRecord.getZlgc());
                }
            }
            //出院诊断：【出院时症状与体征】，【出院诊断】
            if (StringUtils.isEmpty(hisZyRecord.getCyszzytz()) && StringUtils.isEmpty(hisZyRecord.getCyzd())) {
                tv_cyjl_cyzd.setText(getString(R.string.tip_no_data));
                tv_cyjl_cyzd.setTextColor(ContextCompat.getColor(this, R.color.text_desc_dark));
            } else {
                if ("-".equals(hisZyRecord.getCyzd())) {
                    tv_cyjl_cyzd.setText("无");
                } else {
                    tv_cyjl_cyzd.setText(hisZyRecord.getCyzd());
                }
            }
            if (StringUtils.isEmpty(hisZyRecord.getZljgmc())) {
                tv_cyjl_cyqk.setText(getString(R.string.tip_no_data));
                tv_cyjl_cyqk.setTextColor(ContextCompat.getColor(this, R.color.text_desc_dark));
            } else {
                if ("-".equals(hisZyRecord.getZljgmc())) {
                    tv_cyjl_cyqk.setText("无");
                } else {
                    tv_cyjl_cyqk.setText(hisZyRecord.getZljgmc());
                }
            }
            if (StringUtils.isEmpty(hisZyRecord.getCyyz())) {
                tv_cyjl_cyyz.setText(getString(R.string.tip_no_data));
                tv_cyjl_cyyz.setTextColor(ContextCompat.getColor(this, R.color.text_desc_dark));
            } else {
                if ("-".equals(hisZyRecord.getCyyz())) {
                    tv_cyjl_cyyz.setText("无");
                } else {
                    tv_cyjl_cyyz.setText(hisZyRecord.getCyyz());
                }
            }

            //-------------------费用-收费信息---------------------
            if (hisZyRecord.getSfxx() == null || hisZyRecord.getSfxx().size() == 0) {
                tv_menu_8_fy.setVisibility(View.GONE);
            } else {
                floatList = hisZyRecord.getSfxxList(hisZyRecord.getSfxx());
                getPieChart(floatList);
                sfxxje_time_tv.setText(TimeUtils.getTime(hisZyRecord.getSfxx().get(0).getStfsj(), TimeUtils.YY_MM_DD_FORMAT_3));
            }

        }


        if (floatList != null && floatList.size() == 15) {
            sfxxje1_tv.setText(String.format("%.2f", floatList.get(0)));
            sfxxje2_tv.setText(String.format("%.2f", floatList.get(1)));
            sfxxje3_tv.setText(String.format("%.2f", floatList.get(2)));
            sfxxje4_tv.setText(String.format("%.2f", floatList.get(3)));
            sfxxje5_tv.setText(String.format("%.2f", floatList.get(4)));
            sfxxje6_tv.setText(String.format("%.2f", floatList.get(5)));
            sfxxje7_tv.setText(String.format("%.2f", floatList.get(6)));
            sfxxje8_tv.setText(String.format("%.2f", floatList.get(7)));
            sfxxje9_tv.setText(String.format("%.2f", floatList.get(8)));
            sfxxje10_tv.setText(String.format("%.2f", floatList.get(9)));
            sfxxje11_tv.setText(String.format("%.2f", floatList.get(10)));
            sfxxje12_tv.setText(String.format("%.2f", floatList.get(11)));
            sfxxje13_tv.setText(String.format("%.2f", floatList.get(12)));
            sfxxje14_tv.setText(String.format("%.2f", floatList.get(13)));
            if (floatList.get(14) > 0.1) {
                //其他费用
                sfxxje15_tv.setText(String.format("%.2f", floatList.get(14)));
            }

            float sfxxjeAll = 0f;
            for (int i = 0; i < floatList.size(); i++) {
                if (i != 14) {
                    sfxxjeAll += floatList.get(i);
                } else if (floatList.get(14) > 0.1) {
                    sfxxjeAll += floatList.get(i);
                }
            }
            BigDecimal bg = new BigDecimal(sfxxjeAll);
            float sfxxjeAllTemp = bg.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
            sfxxje_all_tv.setText(sfxxjeAllTemp + "");
        }
    }

    @Override
    public void showLoadDialog() {
        showDialog();
    }

    @Override
    public void dismissLoadDialog() {
        hideDialog();
    }

    @Override
    public void showTips(String tipsMsg) {
        ToastUtils.showShort(tipsMsg);
    }

    @Override
    public void setPresenter(MRDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    /**
     * 从门诊病历里的处方信息列表立面，筛选出中药和西药数据
     * 判断规则：
     * 1.是否是药品
     * 2.中药使用类别代码：1：西药 2：中成要 3：中草药
     */
    private void sortCfxxByZhongyaoXiyao() {
        hisMzRecord_cfxx_xiyao_List = new ArrayList<HisDataCfxx>();
        List<HisDataCfxx> cfxxList = hisMzRecord.getCfxx();
        if (cfxxList != null) {
            for (HisDataCfxx cfxx : cfxxList) {
                //1.是否是药品
                if ("1".equals(cfxx.getSfyp())) {
                    hisMzRecord_cfxx_xiyao_List.add(cfxx);
                }
            }
        }
    }

    @SuppressLint("NewApi")
    private void sortYqjcByTime() {
        int offset = sll_detail_item_3_jianyanjiance.getMeasuredHeight() - sll_detail_item_3_jianyanjiance.getHeight();
        sll_detail_item_3_jianyanjiance.scrollTo(0, offset);
        mYqjcAndSysjcCombineDataMap = new TreeMap<String, YqjcAndSysjcCombineData>(Comparator.reverseOrder());
        if (mHisDataCommon != null && mHisDataCommon.getYqjc().size() != 0) {
            List<HisDataYqjc> listYqjc = mHisDataCommon.getYqjc();
            if (!listYqjc.isEmpty() && listYqjc != null) {
                for (HisDataYqjc yqjc : listYqjc) {
//                    String time = TimeUtils.formatDate_YY_MM_DD_FORMAT_3(yqjc.getJcsj());
                    String time = TimeUtils.getTime(yqjc.getJcsj(), TimeUtils.YY_MM_DD_FORMAT_3);
                    YqjcAndSysjcCombineData combineData = mYqjcAndSysjcCombineDataMap.get(time);
                    if (combineData == null) {
                        combineData = new YqjcAndSysjcCombineData();
                        combineData.hisDataYqjcList = new ArrayList<>();
                        combineData.hisDataYqjcList.add(yqjc);
                    } else {
                        combineData.hisDataYqjcList.add(yqjc);
                    }
                    if (combineData.hisDataYqjcList.size() != 0) {
                        mYqjcAndSysjcCombineDataMap.put(time, combineData);
                    }
                }
                mYqjcAndSysjcCombineData_keyArray = new String[mYqjcAndSysjcCombineDataMap.size()];
                mYqjcAndSysjcCombineDataMap.keySet().toArray(mYqjcAndSysjcCombineData_keyArray);
                mJcjyCurrentPositon = 0; //当前时间轴初始位置
                //如果mYqjcAndSysjcCombineDataMap没有数据，那么将时间轴隐藏起来
                if (mYqjcAndSysjcCombineDataMap.size() == 0) {
                    ll_timeline_zone_jcjy.setVisibility(View.GONE);
                } else {
                    ll_timeline_zone_jcjy.setVisibility(View.VISIBLE);
                }
                mRecyclerViewAdapter_jcjy_timeline.notifyDataSetChanged();
                mRecyclerViewAdapter_yqjc.notifyDataSetChanged();
            } else {
                ll_timeline_zone_jcjy.setVisibility(View.GONE);
            }
        } else {
            ll_timeline_zone_jcjy.setVisibility(View.GONE);
        }
    }

    @SuppressLint("NewApi")
    private void sortSysjcByTime() {
        int offset = sll_detail_item_3_jianyanjiance.getMeasuredHeight() - sll_detail_item_3_jianyanjiance.getHeight();
        sll_detail_item_3_jianyanjiance.scrollTo(0, offset);
        mYqjcAndSysjcCombineDataMap = new TreeMap<String, YqjcAndSysjcCombineData>(Comparator.reverseOrder());
        if (null == mHisDataCommon){
            return;
        }
        List<HisDataSysjc> listSysjc = mHisDataCommon.getSysjc();
        if (!listSysjc.isEmpty() && listSysjc != null) {
            for (HisDataSysjc sysjc : listSysjc) {
//                String time = TimeUtils.formatDate_YY_MM_DD_FORMAT_3(sysjc.getJyrq());
                String time = TimeUtils.getTime(sysjc.getJyrq(), TimeUtils.YY_MM_DD_FORMAT_3);
                YqjcAndSysjcCombineData combineData = mYqjcAndSysjcCombineDataMap.get(time);
                if (combineData == null) {
                    combineData = new YqjcAndSysjcCombineData();
                    combineData.hisDataSysjcList = new ArrayList<>();
                    combineData.hisDataSysjcList.add(sysjc);
                } else {
                    if (combineData.hisDataSysjcList == null || combineData.hisDataSysjcList.size() == 0) {
                        combineData.hisDataSysjcList = new ArrayList<>();
                        combineData.hisDataSysjcList.add(sysjc);
                    } else {
                        combineData.hisDataSysjcList.add(sysjc);
                    }
                }
                if (combineData.hisDataSysjcList.size() != 0) {
                    mYqjcAndSysjcCombineDataMap.put(time, combineData);
                }
            }
            Log.d(TAG, "MAP SIZE *************" + mYqjcAndSysjcCombineDataMap.size());
            //将keySet转换为数组，方便使用使用position获取对应位置的值
            mYqjcAndSysjcCombineData_keyArray = new String[mYqjcAndSysjcCombineDataMap.size()];
            mYqjcAndSysjcCombineDataMap.keySet().toArray(mYqjcAndSysjcCombineData_keyArray);
            mJcjyCurrentPositon = 0; //当前时间轴初始位置
            //如果mYqjcAndSysjcCombineDataMap没有数据，那么将时间轴隐藏起来
            if (mYqjcAndSysjcCombineDataMap.size() == 0) {
                ll_timeline_zone_jcjy.setVisibility(View.GONE);
            } else {
                ll_timeline_zone_jcjy.setVisibility(View.VISIBLE);
            }
            mRecyclerViewAdapter_sysjy.notifyDataSetChanged();
            mRecyclerViewAdapter_yqjc.notifyDataSetChanged();
            mRecyclerViewAdapter_jcjy_timeline.notifyDataSetChanged();
            dismissLoadDialog();
        } else {
            dismissLoadDialog();
            ll_timeline_zone_jcjy.setVisibility(View.GONE);
        }
    }

    /**
     * 住院的治疗方案数据需要按照时间轴来显示，由于【医嘱信息】和【手术信息】是独立的数组，
     * 所以需要合并数据，并且按照时间排序：用医嘱信息表中的【医嘱下达时间】，手术信息表的【手术起始时间】来排序
     */
    @SuppressLint("NewApi")
    private void sortYzxxAndSsxxByTime() {
        mYzxxAndSsxxCombineDataMap = new TreeMap<String, YzxxAndSsxxCombineData>(Comparator.reverseOrder());
        //医嘱信息
        List<HisDataYzxx> listYzxx = hisZyRecord.getYzxx();
        if (null == listYzxx) {
            return;
        }
        if (listYzxx.size() != 0) {
            for (HisDataYzxx yzxx : listYzxx) {
//                String time = TimeUtils.formatDate_YY_MM_DD_FORMAT_3(yzxx.getYzxdsj());
                String time = TimeUtils.getTime(yzxx.getYzxdsj(), TimeUtils.YY_MM_DD_FORMAT_3);
                YzxxAndSsxxCombineData combineData = mYzxxAndSsxxCombineDataMap.get(time);
                if (combineData == null) {
                    combineData = new YzxxAndSsxxCombineData();
                    combineData.hisDataYzxxList = new ArrayList<>();
                    if (yzxx.getYzlx().equals("1")) {//医嘱信息判断是医嘱类型（yzll）1为药品
                        combineData.hisDataYzxxList.add(yzxx);
                    }
                } else {
                    if (yzxx.getYzlx().equals("1")) {
                        combineData.hisDataYzxxList.add(yzxx);
                    }
                }
                if (combineData.hisDataYzxxList.size() != 0) {
                    mYzxxAndSsxxCombineDataMap.put(time, combineData);
                }

                mYzxxAndSsxxCombineData_keyArray = new String[mYzxxAndSsxxCombineDataMap.size()];
                mYzxxAndSsxxCombineDataMap.keySet().toArray(mYzxxAndSsxxCombineData_keyArray);

                if (mYzxxAndSsxxCombineDataMap.size() == 0) {
                    item_zlfa_cfxx_xiyao_title.setVisibility(View.GONE);
                } else {
                    item_zlfa_cfxx_xiyao_title.setVisibility(View.VISIBLE);
                }
            }
        }

        //手术信息
        List<HisDataSsxx> listSsxx = hisZyRecord.getSsxx();
        if (null == listSsxx) {
            return;
        }
        if (listSsxx.size() != 0) {
            for (HisDataSsxx ssxx : listSsxx) {
//                String jyrq = TimeUtils.formatDate_YY_MM_DD_FORMAT_3(ssxx.getSsqssj());
                String jyrq = TimeUtils.getTime(ssxx.getSsqssj(), TimeUtils.YY_MM_DD_FORMAT_3);
                YzxxAndSsxxCombineData combineData = mYzxxAndSsxxCombineDataMap.get(jyrq);
                if (combineData == null) {
                    combineData = new YzxxAndSsxxCombineData();
                    combineData.hisDataSsxxList = new ArrayList<>();
                    combineData.hisDataSsxxList.add(ssxx);
                } else {
                    if (combineData.hisDataSsxxList == null || combineData.hisDataSsxxList.size() == 0) {
                        combineData.hisDataSsxxList = new ArrayList<>();
                        combineData.hisDataSsxxList.add(ssxx);
                    } else {
                        combineData.hisDataSsxxList.add(ssxx);
                    }
                }
                if (combineData.hisDataSsxxList.size() != 0) {
                    mYzxxAndSsxxCombineDataMap.put(jyrq, combineData);
                }
            }
            //将keySet转换为数组，方便使用使用position获取对应位置的值
            mYzxxAndSsxxCombineData_keyArray = new String[mYzxxAndSsxxCombineDataMap.size()];
            mYzxxAndSsxxCombineDataMap.keySet().toArray(mYzxxAndSsxxCombineData_keyArray);
            mZlfaCurrentPositon = 0; //当前时间轴初始位置
            //如果mYzxxAndSsxxCombineDataMap没有数据，那么将时间轴隐藏起来
            if (mYzxxAndSsxxCombineDataMap.size() == 0) {
                ll_timeline_zone_zlfa.setVisibility(View.GONE);
            } else {
                ll_timeline_zone_zlfa.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 设置结果列表的RecyclerViewAdapter
     */
    private void setupFeed() {
        //-------------------检查检验------------------------------
        //检查检验的时间轴
        final LinearLayoutManager linearLayoutManager_0 = new LinearLayoutManager(this);
        //配置布局，默认为vertical（垂直布局），下边这句将布局改为水平布局
        linearLayoutManager_0.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview_jcjy_timeline.setLayoutManager(linearLayoutManager_0);
        mRecyclerViewAdapter_jcjy_timeline = new RecyclerViewAdapter_jcjy_timeline(this);
        recyclerview_jcjy_timeline.setAdapter(mRecyclerViewAdapter_jcjy_timeline);

        //实验室检验
        final LinearLayoutManager linearLayoutManager_1 = new LinearLayoutManager(this);
        recyclerview_jcjy_sysjy.setLayoutManager(linearLayoutManager_1);
        mRecyclerViewAdapter_sysjy = new RecyclerViewAdapter_sysjy(this);
        recyclerview_jcjy_sysjy.setAdapter(mRecyclerViewAdapter_sysjy);

        //仪器检查
        final LinearLayoutManager linearLayoutManager_2 = new LinearLayoutManager(this);
        recyclerview_jcjy_yqjc.setLayoutManager(linearLayoutManager_2);
        mRecyclerViewAdapter_yqjc = new RecyclerViewAdapter_yqjc(this);
        recyclerview_jcjy_yqjc.setAdapter(mRecyclerViewAdapter_yqjc);
        //-------------------治疗方案-门诊------------------------------
        //用药-西药(从处方信息中取)
        final LinearLayoutManager linearLayoutManager_3 = new LinearLayoutManager(this);
        recyclerview_zlfa_cfxx_xiyao.setLayoutManager(linearLayoutManager_3);
        mRecyclerViewAdapter_cfxx_xiyao = new RecyclerViewAdapter_cfxx_xiyao(this);
        recyclerview_zlfa_cfxx_xiyao.setAdapter(mRecyclerViewAdapter_cfxx_xiyao);

        //手术信息--门诊
        final LinearLayoutManager linearLayoutManager_7 = new LinearLayoutManager(this);
        recyclerview_zlfa_ssxx_menzhen.setLayoutManager(linearLayoutManager_7);
        mRecyclerViewAdapter_ssxx_menzhen = new RecyclerViewAdapter_ssxx_menzhen(this);
        recyclerview_zlfa_ssxx_menzhen.setAdapter(mRecyclerViewAdapter_ssxx_menzhen);

        //-------------------治疗方案-住院------------------------------
        //治疗方案的时间轴

        final LinearLayoutManager linearLayoutManager_8 = new LinearLayoutManager(this);
        //配置布局，默认为vertical（垂直布局），下边这句将布局改为水平布局
        linearLayoutManager_8.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview_zlfa_timeline.setLayoutManager(linearLayoutManager_8);
        mRecyclerViewAdapter_zlfa_timeline = new RecyclerViewAdapter_zlfa_timeline(this);
        recyclerview_zlfa_timeline.setAdapter(mRecyclerViewAdapter_zlfa_timeline);

        //用药(从医嘱信息中取)
        final LinearLayoutManager linearLayoutManager_9 = new LinearLayoutManager(this);
        recyclerview_zlfa_yzxx.setLayoutManager(linearLayoutManager_9);
        mRecyclerViewAdapter_yzxx = new RecyclerViewAdapter_yzxx(this);
        recyclerview_zlfa_yzxx.setAdapter(mRecyclerViewAdapter_yzxx);

        //手术信息--住院
        final LinearLayoutManager linearLayoutManager_10 = new LinearLayoutManager(this);
        recyclerview_zlfa_ssxx_zhuyuan.setLayoutManager(linearLayoutManager_10);
        mRecyclerViewAdapter_ssxx_zhuyuan = new RecyclerViewAdapter_ssxx_zhuyuan(this);
        recyclerview_zlfa_ssxx_zhuyuan.setAdapter(mRecyclerViewAdapter_ssxx_zhuyuan);

        //门诊诊断
        final LinearLayoutManager linearLayoutManager_11 = new LinearLayoutManager(this);
        zhenduanjieguo_zhuyuan_reclview.setLayoutManager(linearLayoutManager_11);
        mRecyclerViewAdapter_zdxx_zhuyuan = new RecyclerViewAdapter_zdxx_zhuyuan(this);
        zhenduanjieguo_zhuyuan_reclview.setAdapter(mRecyclerViewAdapter_zdxx_zhuyuan);
    }

    /**
     * 设置可见文本水印
     */
//    private void setVisibleTextWatermarks() {
//        //页面显示可见水印，内容为当前登录账号所属机构
//        WatermarkText watermarkText = new WatermarkText(MyApplication.getInstance().getCUR_USERINFO().getOrgName())
//                .setPositionX(0.5)
//                .setPositionY(0.5)
//                .setTextColor(ContextCompat.getColor(this, R.color.black_999999)) // 吴中敏： #999999
//                .setTextFont(R.font.champagne)
//                //.setTextShadow(0.1f, 5, 5, Color.GRAY)
//                .setTextAlpha(128)
//                .setRotation(-35)
//                .setTextSize(12);
//
//        WatermarkBuilder.create(this, iv_water_mark_background)
//                .setTileMode(true)
//                .loadWatermarkText(watermarkText)
//                .getWatermark()
//                .setToImageView(iv_water_mark_background);
//    }

    /**
     * 设置不可见文本水印
     */
//    private void setInvisibleTextWatermarks() {
//        //页面包含不可见水印，内容为当前登录账号、所属科室、当前日期
//        String wmStr = MyApplication.getInstance().getCUR_USERINFO().getUserName()
//                + " " + MyApplication.getInstance().getCUR_USERINFO().getDepartmentName()
//                + " " + TimeUtils.getCurrentDateString_3();
//        WatermarkText watermarkText = new WatermarkText(wmStr);
//
//        WatermarkBuilder
//                .create(this, iv_water_mark_background)
//                .loadWatermarkText(watermarkText)
//                .setInvisibleWMListener(true, new BuildFinishListener<Bitmap>() {
//                    @Override
//                    public void onSuccess(Bitmap object) {
//                        LogUtils.d("---------------->Successfully create invisible watermark!");
//                        if (object != null) {
//                            iv_water_mark_background.setImageBitmap(object);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(String message) {
//                        LogUtils.e("---------------->Failed to create invisible watermark：" + message);
//                    }
//                });
//    }

    /**
     * 解析不可见文本水印 //FIXME 目前只能解析的背景图片，截屏图片还不知道能不能解析
     */
//    private void detectInvisibleTextWatermarks() {
//        WatermarkDetector.create(iv_water_mark_background, true)
//                .detect(new DetectFinishListener() {
//                    @Override
//                    public void onSuccess(DetectionReturnValue returnValue) {
//                        if (returnValue != null) {
//                            ToastUtil.showToastLong("Successfully detected invisible text: " + returnValue.getWatermarkString());
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(String message) {
//                        ToastUtil.showToastLong("Failed to detected invisible text: " + message);
//                    }
//                });
//    }


    //圆形图
    private void getPieChart(List<Float> piechartData) {
        if (piechartData == null || piechartData.size() == 0) {
            return;
        }
        LogUtil.e("收费数据===" + GsonUtils.ModelToJson(piechartData));
        piechart.setUsePercentValues(true);//设置占用百分比
        piechart.getDescription().setEnabled(false);//描述
        piechart.setExtraOffsets(0, 0, 0, 0);//上下左右偏移

        piechart.setDragDecelerationFrictionCoef(0.95f);//旋转助力系数

//        piechart.setCenterTextTypeface(tfLight);
        piechart.setCenterText("费用占比分析");//中间字
        piechart.setCenterTextColor(MRDetailActivity.this.getColor(R.color.text_main));
        piechart.setCenterTextSize(12f);

        piechart.setDrawHoleEnabled(true);//是否空心
        piechart.setHoleColor(MRDetailActivity.this.getColor(R.color.white));//空心颜色
//        piechart.setTransparentCircleColor(Color.WHITE);//透明环颜色
        piechart.setTransparentCircleAlpha(110);//

        piechart.setHoleRadius(80f);//空心半径
        piechart.setTransparentCircleRadius(50f);//透明环半径
        piechart.setDrawCenterText(true);//是否显示中心字

        piechart.setRotationAngle(-90);//旋转角度
        // enable rotation of the chart by touch
        piechart.setRotationEnabled(false);//是否旋转
        piechart.setHighlightPerTapEnabled(false);//item点击变化
        piechart.getLegend().setEnabled(false);//设置底部legend
        // entry label styling
        piechart.setEntryLabelColor(MRDetailActivity.this.getColor(R.color.green_c7));//比例字体颜色
//        piechart.setEntryLabelTypeface(tfRegular);
        piechart.setEntryLabelTextSize(12f);//比例字体大小


        //数据处理
        if (piechartData == null || piechartData.size() == 0) {
            return;
        }
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (int i = 0; i < piechartData.size(); i++) {
            entries.add(new PieEntry(piechartData.get(i)));
        }
        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setDrawIcons(false);
        ArrayList<Integer> colors = new ArrayList<>();
        // 01 = 住院费 **//02 = 诊疗费 **//03 = 治疗费 **//04 = 护理费//05 = 手术费 **//06 = 检查费//07 = 化验费 **//08 = 摄片费//09 = 透视费
        //10 = 输血费//11 = 输氧费//12 = 西药费 **//13 = 中成药费 **//14 = 中草药费 **//15 = 其它费用 **
        colors.add(getResources().getColor(R.color.blue_f1));
        colors.add(getResources().getColor(R.color.color_CF83FF));
        colors.add(getResources().getColor(R.color.orange_5d));
        colors.add(getResources().getColor(R.color.color_a9c9f9));

        colors.add(getResources().getColor(R.color.red_FF5651));
        colors.add(getResources().getColor(R.color.color_2da9eaf));
        colors.add(getResources().getColor(R.color.color_e216b0));
        colors.add(getResources().getColor(R.color.blue_e5));

        colors.add(getResources().getColor(R.color.red_FF0000));
        colors.add(getResources().getColor(R.color.color_aaffb34f));
        colors.add(getResources().getColor(R.color.green_9AC890));
        colors.add(getResources().getColor(R.color.color_0bb356));

        colors.add(getResources().getColor(R.color.green_c7));
        colors.add(getResources().getColor(R.color.color_fcb3a2));
        colors.add(getResources().getColor(R.color.text_desc_dark));

        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(piechart));
        data.setValueTextSize(11f);
        data.setValueTextColor(getResources().getColor(R.color.text_main));
        data.setDrawValues(false);//不显示占比
        piechart.setData(data);
        // undo all highlights
        piechart.highlightValues(null);
        piechart.invalidate();
    }


    //以下是适配器
    //-----RecyclerViewAdapter 检查检验--时间轴----------------------------------------------------------------------------------------------

    /**
     * Recyclerview适配器 检查检验 时间轴
     */
    class RecyclerViewAdapter_jcjy_timeline extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

        private Context mContext;
        View previous_iv_jcjy_timeline_bg; //上一个选择的item
        TextView previous_tv_jcjy_timeline_text; //上一个选择的item

        public RecyclerViewAdapter_jcjy_timeline(Context context) {
            mContext = context;

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_mr_detail_jcjy_timeline, parent, false);
            MyAPHolder holder = new MyAPHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            MyAPHolder holder = (MyAPHolder) viewHolder;
            bindViewHolder(position, holder);
        }


        //时间轴点击
        @Override
        public void onClick(View view) {
            MyAPHolder holder = (MyAPHolder) view.getTag();
            switch (view.getId()) {
                case R.id.item_mr_detail_jcjy_timeline:  //点击整个item                    I
                    previous_iv_jcjy_timeline_bg.setBackgroundResource(R.drawable.shape_circle_solid_gray);
                    previous_tv_jcjy_timeline_text.setTextColor(ContextCompat.getColor(MRDetailActivity.this, R.color.text_desc_dark));
                    holder.dot_v.setBackgroundResource(R.drawable.shape_circle_solid_blue);
                    holder.tv_jcjy_timeline_text.setTextColor(ContextCompat.getColor(MRDetailActivity.this, R.color.text_main));

                    previous_iv_jcjy_timeline_bg = holder.dot_v;
                    previous_tv_jcjy_timeline_text = holder.tv_jcjy_timeline_text;
                    mJcjyCurrentPositon = holder.position;
                    //通知实验室检查和仪器检查数据显示的adapter切换数据
                    int offset = sll_detail_item_3_jianyanjiance.getMeasuredHeight() - sll_detail_item_3_jianyanjiance.getHeight();
                    sll_detail_item_3_jianyanjiance.scrollTo(0, offset);
                    mRecyclerViewAdapter_sysjy.notifyDataSetChanged();
                    mRecyclerViewAdapter_yqjc.notifyDataSetChanged();
                    break;
            }
        }

        private void bindViewHolder(int position, MyAPHolder holder) {
            holder.item_mr_detail_jcjy_timeline.setOnClickListener(this);
            holder.item_mr_detail_jcjy_timeline.setTag(holder);
            holder.position = position;
            //时间轴的单个item的时间值
            holder.tv_jcjy_timeline_text.setText(mYqjcAndSysjcCombineData_keyArray[position]);
            if (position == 0) {
                previous_iv_jcjy_timeline_bg = holder.dot_v;
                previous_tv_jcjy_timeline_text = holder.tv_jcjy_timeline_text;
                //时间轴的单个item的背景
                holder.dot_v.setBackgroundResource(R.drawable.shape_circle_solid_blue);
                holder.tv_jcjy_timeline_text.setTextColor(ContextCompat.getColor(MRDetailActivity.this, R.color.text_main));
            } else {
                //时间轴的单个item的背景
                holder.dot_v.setBackgroundResource(R.drawable.shape_circle_solid_gray);
                holder.tv_jcjy_timeline_text.setTextColor(ContextCompat.getColor(MRDetailActivity.this, R.color.text_desc_dark));
            }
            dismissLoadDialog();
        }

        @Override
        public int getItemCount() {
            if (mYqjcAndSysjcCombineDataMap == null) {
                return 0;
            }
            Log.d(TAG, "SIZE:" + mYqjcAndSysjcCombineDataMap.size());
            return mYqjcAndSysjcCombineDataMap.size();
        }

        class MyAPHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.item_mr_detail_jcjy_timeline)
            LinearLayout item_mr_detail_jcjy_timeline;

            //时间轴的单个item的背景
            @BindView(R.id.dot_v)
            View dot_v;

            //时间轴的单个item的时间值
            @BindView(R.id.tv_jcjy_timeline_text)
            TextView tv_jcjy_timeline_text;

            public int position;

            public MyAPHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    //-----RecyclerViewAdapter 治疗方案--时间轴----------------------------------------------------------------------------------------------

    /**
     * Recyclerview适配器 治疗方案 时间轴
     */
    class RecyclerViewAdapter_zlfa_timeline extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

        private Context mContext;
        View previous_iv_jcjy_timeline_bg;
        TextView previous_tv_jcjy_timeline_text;

        public RecyclerViewAdapter_zlfa_timeline(Context context) {
            mContext = context;

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_mr_detail_jcjy_timeline, parent, false); //治疗方案的时间轴layout和检测检验时的时间轴公用一个layout，这里的命名就不改了
            MyAPHolder holder = new MyAPHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            MyAPHolder holder = (MyAPHolder) viewHolder;
            bindViewHolder(position, holder);
        }

        @Override
        public void onClick(View view) {
            MyAPHolder holder = (MyAPHolder) view.getTag();
            switch (view.getId()) {
                case R.id.item_mr_detail_jcjy_timeline:  //点击整个item                    I
                    previous_iv_jcjy_timeline_bg.setBackgroundResource(R.drawable.shape_circle_solid_gray);
                    previous_tv_jcjy_timeline_text.setTextColor(ContextCompat.getColor(MRDetailActivity.this, R.color.text_desc_dark));
                    holder.dot_v.setBackgroundResource(R.drawable.shape_circle_solid_blue);
                    holder.tv_jcjy_timeline_text.setTextColor(ContextCompat.getColor(MRDetailActivity.this, R.color.text_main));
                    previous_iv_jcjy_timeline_bg = holder.dot_v;
                    previous_tv_jcjy_timeline_text = holder.tv_jcjy_timeline_text;
                    mZlfaCurrentPositon = holder.position;
                    //通知两个数据显示的adapter切换数据
                    int offset = sll_detail_item_5_zhiliaofangan_zhuyuan.getMeasuredHeight() - sll_detail_item_5_zhiliaofangan_zhuyuan.getHeight();
                    sll_detail_item_5_zhiliaofangan_zhuyuan.scrollTo(0, offset);
                    mRecyclerViewAdapter_yzxx.notifyDataSetChanged();
                    mRecyclerViewAdapter_ssxx_zhuyuan.notifyDataSetChanged();
                    break;
            }
        }

        private void bindViewHolder(int position, MyAPHolder holder) {
            holder.item_mr_detail_jcjy_timeline.setOnClickListener(this);
            holder.item_mr_detail_jcjy_timeline.setTag(holder);
            holder.position = position;
            //时间轴的单个item的时间值
            if (mYzxxAndSsxxCombineData_keyArray == null || mYzxxAndSsxxCombineData_keyArray.length == 0) {
                ll_timeline_zone_zlfa.setVisibility(View.GONE);
                item_zlfa_cfxx_xiyao_title.setVisibility(View.GONE);
            } else {
                holder.tv_jcjy_timeline_text.setText(mYzxxAndSsxxCombineData_keyArray[position]);
                ll_timeline_zone_zlfa.setVisibility(View.VISIBLE);
                item_zlfa_cfxx_xiyao_title.setVisibility(View.VISIBLE);
            }
            if (position == 0) {
                previous_iv_jcjy_timeline_bg = holder.dot_v;
                previous_tv_jcjy_timeline_text = holder.tv_jcjy_timeline_text;
                //时间轴的单个item的背景
                holder.dot_v.setBackgroundResource(R.drawable.shape_circle_solid_blue);
                holder.tv_jcjy_timeline_text.setTextColor(ContextCompat.getColor(MRDetailActivity.this, R.color.text_main));
            } else {
                //时间轴的单个item的背景
                holder.dot_v.setBackgroundResource(R.drawable.shape_circle_solid_gray);
                holder.tv_jcjy_timeline_text.setTextColor(ContextCompat.getColor(MRDetailActivity.this, R.color.text_desc_dark));
            }
        }

        @Override
        public int getItemCount() {
            return mYzxxAndSsxxCombineDataMap == null ? 0 : mYzxxAndSsxxCombineDataMap.size();
        }

        class MyAPHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.item_mr_detail_jcjy_timeline)
            LinearLayout item_mr_detail_jcjy_timeline; //治疗方案的时间轴layout和检测检验时的时间轴公用一个layout，这里的命名就不改了
            //时间轴的单个item的背景
            @BindView(R.id.dot_v)
            View dot_v; //治疗方案的时间轴layout和检测检验时的时间轴公用一个layout，这里的命名就不改了
            //时间轴的单个item的时间值
            @BindView(R.id.tv_jcjy_timeline_text)
            TextView tv_jcjy_timeline_text; //治疗方案的时间轴layout和检测检验时的时间轴公用一个layout，这里的命名就不改了
            public int position;

            public MyAPHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    //-----RecyclerViewAdapter 实验室检验----------------------------------------------------------------------------------------------

    /**
     * Recyclerview适配器 实验室检验 包括检验细菌药敏三个表格
     * 根据时间轴
     */
    class RecyclerViewAdapter_sysjy extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

        private Context mContext;
        private final int VIEW_TYPE_EMPTY_VIEW = 99; //空数据时，显示空布局类型
        private boolean mIsEmptyData = true; //用来表示当前数据是否为空

        public RecyclerViewAdapter_sysjy(Context context) {
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            if (VIEW_TYPE_EMPTY_VIEW == viewType) {
                //空布局
                view = LayoutInflater.from(mContext).inflate(R.layout.item_mr_detail_recyclerview_empty, parent, false);
            } else {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_mr_detail_jcjy_syssjy, parent, false);
            }
            MyAPHolder holder = new MyAPHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            //如果是数据为空，啥也不干，直接显示空布局R.layout.item_mr_detail_recyclerview_empty就好
            if (mIsEmptyData == true) {
                //do nothing
            } else {
                MyAPHolder holder = (MyAPHolder) viewHolder;
                bindViewHolder(position, holder);
            }
        }

        @Override
        public void onClick(View view) {
            MyAPHolder holder = (MyAPHolder) view.getTag();
            switch (view.getId()) {
                case R.id.item_mr_detail_jcjy_yqjc:  //点击整个item                    I
                    break;
            }
        }

        private void bindViewHolder(int position, MyAPHolder holder) {
            YqjcAndSysjcCombineData entryData;
            if (CommonConfig.MR_INDEX_ZHUYUAN_ID.equals(mIndex)) {//住院
                entryData = mYqjcAndSysjcCombineDataMap.get(mYqjcAndSysjcCombineData_keyArray[mJcjyCurrentPositon]);
            } else {
                entryData = new YqjcAndSysjcCombineData();
                entryData.hisDataSysjcList = mHisDataCommon.getSysjc();
            }
            if (entryData.hisDataSysjcList.size() != 0) {
                HisDataSysjc sysjc = entryData.hisDataSysjcList.get(position);//获取对应时间轴的实验室检验数据
                // 检验结果指标
                holder.tv_bgdlbmc_1.setText(sysjc.getBgdlbmc());
                holder.tv_yybbmc_1.setText(sysjc.getYybbmc());
                holder.tv_bgbz_1.setText(sysjc.getBgbz());
                holder.tv_bgrq_1.setText(TimeUtils.parseString2StringWithLevel(sysjc.getBgrq()));
                holder.tv_cjrq_1.setText(TimeUtils.getTime(sysjc.getCjrq(), TimeUtils.YY_MM_DD_FORMAT_3));
                holder.tv_jyrq_1.setText(TimeUtils.getTime(sysjc.getJyrq(), TimeUtils.YY_MM_DD_FORMAT_3));

                holder.mRecyclerViewAdapter_jyjgzb = new RecyclerViewAdapter_jyjgzb(mContext, position);
                LinearLayoutManager linearLayoutManager_1 = new LinearLayoutManager(mContext);
                holder.recyclerview_jcjy_jyjgzb.setLayoutManager(linearLayoutManager_1);
                holder.recyclerview_jcjy_jyjgzb.setAdapter(holder.mRecyclerViewAdapter_jyjgzb);
                holder.recyclerview_jcjy_jyjgzb.setNestedScrollingEnabled(false);
                holder.mRecyclerViewAdapter_jyjgzb.notifyDataSetChanged();


                // 细菌结果
                holder.mRecyclerViewAdapter_xjjg = new RecyclerViewAdapter_xjjg(mContext, position);
                if (holder.mRecyclerViewAdapter_xjjg.GetItemCountFunc() > 0) {
                    LinearLayoutManager linearLayoutManager_2 = new LinearLayoutManager(mContext);
                    holder.recyclerview_jcjy_xjjg.setLayoutManager(linearLayoutManager_2);
                    holder.recyclerview_jcjy_xjjg.setAdapter(holder.mRecyclerViewAdapter_xjjg);
                    holder.recyclerview_jcjy_xjjg.setNestedScrollingEnabled(false);
                    holder.ll_xjjg_zone.setVisibility(View.VISIBLE);

                    holder.tv_bgdlbmc_2.setText(sysjc.getBgdlbmc());
                    holder.tv_yybbmc_2.setText(sysjc.getYybbmc());
                    holder.tv_bgbz_2.setText(sysjc.getBgbz());
                    holder.tv_bgrq_2.setText(TimeUtils.parseString2StringWithSlash(sysjc.getBgrq()));
                    holder.tv_cjrq_2.setText(TimeUtils.getTime(sysjc.getCjrq(), TimeUtils.YY_MM_DD_FORMAT_3));
                    holder.tv_jyrq_2.setText(TimeUtils.getTime(sysjc.getJyrq(), TimeUtils.YY_MM_DD_FORMAT_3));
                } else {
                    holder.ll_xjjg_zone.setVisibility(View.GONE);
                }

                // 药敏结果
                holder.mRecyclerViewAdapter_ymjg = new RecyclerViewAdapter_ymjg(mContext, position);
                if (holder.mRecyclerViewAdapter_ymjg.GetItemCountFunc() > 0) {
                    LinearLayoutManager linearLayoutManager_3 = new LinearLayoutManager(mContext);
                    holder.recyclerview_jcjy_ymjg.setLayoutManager(linearLayoutManager_3);
                    holder.recyclerview_jcjy_ymjg.setAdapter(holder.mRecyclerViewAdapter_ymjg);
                    holder.recyclerview_jcjy_ymjg.setNestedScrollingEnabled(false);
                    holder.ll_ymjg_zone.setVisibility(View.VISIBLE);

                    holder.tv_bgdlbmc_3.setText(sysjc.getBgdlbmc());
                    holder.tv_yybbmc_3.setText(sysjc.getYybbmc());
                    holder.tv_bgbz_3.setText(sysjc.getBgbz());
                    holder.tv_bgrq_3.setText(TimeUtils.parseString2StringWithSlash(sysjc.getBgrq()));
                    holder.tv_cjrq_3.setText(TimeUtils.getTime(sysjc.getCjrq(), TimeUtils.YY_MM_DD_FORMAT_3));
                    holder.tv_jyrq_3.setText(TimeUtils.getTime(sysjc.getJyrq(), TimeUtils.YY_MM_DD_FORMAT_3));
                } else {
                    holder.ll_ymjg_zone.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public int getItemCount() {
            int count = 0;
            if (CommonConfig.MR_INDEX_MENZHEN_ID.equals(mIndex)) {
                if (hisMzRecord == null || hisMzRecord.getSysjc() == null) {
                    count = 0;
                } else {
                    count = hisMzRecord.getSysjc().size();
                }
            } else if (CommonConfig.MR_INDEX_ZHUYUAN_ID.equals(mIndex)) {
                if (mYqjcAndSysjcCombineDataMap == null) {
                    count = 0;
                } else {
                    if (mYqjcAndSysjcCombineData_keyArray == null || mYqjcAndSysjcCombineData_keyArray.length == 0) {
                        count = 0;
                        dismissLoadDialog();
                    } else {
                        YqjcAndSysjcCombineData entryData = mYqjcAndSysjcCombineDataMap.get(mYqjcAndSysjcCombineData_keyArray[mJcjyCurrentPositon]);
                        if (entryData == null) {
                            count = 0;
                        } else {
                            if (entryData.hisDataSysjcList == null) {
                                count = 0;
                            } else {
                                count = entryData.hisDataSysjcList.size();
                            }
                        }
                    }
                }
            }

            if (count == 0) {
                mIsEmptyData = true;
                count = 1; //如果数据为空，那么count设置为1，用来显示空视图
            } else {
                mIsEmptyData = false;
            }
            return count;
        }

        @Override
        public int getItemViewType(int position) {
            if (mIsEmptyData == true) {
                //空布局的类型
                return VIEW_TYPE_EMPTY_VIEW;
            }

            return super.getItemViewType(position);
        }

        class MyAPHolder extends RecyclerView.ViewHolder {

            // 项目名称 取【报告单类别名称】
            @BindView(R.id.tv_bgdlbmc_1)
            TextView tv_bgdlbmc_1;
            @BindView(R.id.tv_bgdlbmc_2)
            TextView tv_bgdlbmc_2;
            @BindView(R.id.tv_bgdlbmc_3)
            TextView tv_bgdlbmc_3;
            // 标本名称 取【医院标本名称】
            @BindView(R.id.tv_yybbmc_1)
            TextView tv_yybbmc_1;
            @BindView(R.id.tv_yybbmc_2)
            TextView tv_yybbmc_2;
            @BindView(R.id.tv_yybbmc_3)
            TextView tv_yybbmc_3;
            // 检验报告备注
            @BindView(R.id.tv_bgbz_1)
            TextView tv_bgbz_1;
            @BindView(R.id.tv_bgbz_2)
            TextView tv_bgbz_2;
            @BindView(R.id.tv_bgbz_3)
            TextView tv_bgbz_3;
            // 报告日期
            @BindView(R.id.tv_bgrq_1)
            TextView tv_bgrq_1;
            @BindView(R.id.tv_bgrq_2)
            TextView tv_bgrq_2;
            @BindView(R.id.tv_bgrq_3)
            TextView tv_bgrq_3;
            // 采集日期
            @BindView(R.id.tv_cjrq_1)
            TextView tv_cjrq_1;
            @BindView(R.id.tv_cjrq_2)
            TextView tv_cjrq_2;
            @BindView(R.id.tv_cjrq_3)
            TextView tv_cjrq_3;
            // 检验日期
            @BindView(R.id.tv_jyrq_1)
            TextView tv_jyrq_1;
            @BindView(R.id.tv_jyrq_2)
            TextView tv_jyrq_2;
            @BindView(R.id.tv_jyrq_3)
            TextView tv_jyrq_3;

            // 实验室检验每一个item的三个子list
            @BindView(R.id.ll_jyjgzb_zone) // 检验结果列表区域
                    LinearLayout ll_jyjgzb_zone;
            @BindView(R.id.recyclerview_jcjy_jyjgzb) //。检验结果指标
            RecyclerView recyclerview_jcjy_jyjgzb;
            RecyclerViewAdapter_jyjgzb mRecyclerViewAdapter_jyjgzb;

            @BindView(R.id.ll_xjjg_zone) // 细菌结果列表区域
            LinearLayout ll_xjjg_zone;
            @BindView(R.id.recyclerview_jcjy_xjjg) //。细菌结果
            RecyclerView recyclerview_jcjy_xjjg;
            RecyclerViewAdapter_xjjg mRecyclerViewAdapter_xjjg;

            @BindView(R.id.ll_ymjg_zone) // 药敏结果列表区域
            LinearLayout ll_ymjg_zone;
            @BindView(R.id.recyclerview_jcjy_ymjg) //。药敏结果
            RecyclerView recyclerview_jcjy_ymjg;
            RecyclerViewAdapter_ymjg mRecyclerViewAdapter_ymjg;

            public MyAPHolder(View itemView) {
                super(itemView);
                //如果是数据为空，不做注解绑定view，因为是空布局文件，ButterKnife.bind会crash
                if (mIsEmptyData == true) {
                    //do nothing
                } else {
                    ButterKnife.bind(this, itemView);
                }
            }
        }
    }

    //-----RecyclerViewAdapter 检验结果指标----------------------------------------------------------------------------------------------

    /**
     * Recyclerview适配器 检验结果指标
     */
    class RecyclerViewAdapter_jyjgzb extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

        private Context mContext;
        private int mParentItemSysjcPostion; //父列表，实验室检查列表的postion
        private final int VIEW_TYPE_EMPTY_VIEW = 99; //空数据时，显示空布局类型 https://www.jianshu.com/p/8d6a8860088b
        private boolean mIsEmptyData = true; //用来表示当前数据是否为空

        public RecyclerViewAdapter_jyjgzb(Context context, int parentItemSysjcPostion) {
            mContext = context;
            mParentItemSysjcPostion = parentItemSysjcPostion;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;

            if (VIEW_TYPE_EMPTY_VIEW == viewType) {
                //空布局
                view = LayoutInflater.from(mContext).inflate(R.layout.item_mr_detail_recyclerview_empty, parent, false);
            } else {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_mr_detail_jcjy_jyjgzb, parent, false);
            }

            MyAPHolder holder = new MyAPHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            //如果是数据为空，啥也不干，直接显示空布局R.layout.item_mr_detail_recyclerview_empty就好
            if (mIsEmptyData == true) {
                //do nothing
            } else {
                MyAPHolder holder = (MyAPHolder) viewHolder;
                bindViewHolder(position, holder);
            }
        }

        @Override
        public void onClick(View view) {
            MyAPHolder holder = (MyAPHolder) view.getTag();
            switch (view.getId()) {
                case R.id.item_mr_detail_jcjy_jyjgzb:  //点击整个item                    I
                    break;
            }

        }

        private void bindViewHolder(int position, MyAPHolder holder) {
            holder.item_mr_detail_jcjy_jyjgzb.setOnClickListener(this);
            holder.item_mr_detail_jcjy_jyjgzb.setTag(holder);
            List<HisDataJyjgzb> jyjgzbList = null;
            if (CommonConfig.MR_INDEX_MENZHEN_ID.equals(mIndex)) {
                jyjgzbList = hisMzRecord.getSysjc().get(mParentItemSysjcPostion).getJyjgzb();
            } else if (CommonConfig.MR_INDEX_ZHUYUAN_ID.equals(mIndex)) {
                YqjcAndSysjcCombineData entryData = mYqjcAndSysjcCombineDataMap.get(mYqjcAndSysjcCombineData_keyArray[mJcjyCurrentPositon]);
                jyjgzbList = entryData.hisDataSysjcList.get(mParentItemSysjcPostion).getJyjgzb();
            }
            if (jyjgzbList.size() != 0) {
                // 序号
                holder.tv_sequence.setText("" + (position + 1));
                // 检测指标代码
                holder.tv_jczbdm.setText(jyjgzbList.get(position).getJczbdm());
                // 检测指标名称
                holder.tv_jczbmc.setText(jyjgzbList.get(position).getJczbmc());
                // 检测指标结果
                holder.tv_jczbjg.setText(jyjgzbList.get(position).getJczbjg());
                // 异常提示
                if (jyjgzbList.get(position).getYcts().equals("3")) {
                    Glide.with(mContext).load(R.drawable.icon_higher).into(holder.iv_ycts);
                } else if (jyjgzbList.get(position).getYcts().equals("4")) {
                    Glide.with(mContext).load(R.drawable.icon_lower).into(holder.iv_ycts);
                }
                // 计量单位
                holder.tv_jldw.setText(jyjgzbList.get(position).getJldw());
                // 参考值范围
                holder.tv_ckz.setText(jyjgzbList.get(position).getCkz());
            }
        }

        @Override
        public int getItemCount() {
            int count = 0;
            if (CommonConfig.MR_INDEX_MENZHEN_ID.equals(mIndex)) {
                if (hisMzRecord.getSysjc() != null
                        && hisMzRecord.getSysjc().size() != 0
                        && hisMzRecord.getSysjc().get(mParentItemSysjcPostion) != null
                        && hisMzRecord.getSysjc().get(mParentItemSysjcPostion).getJyjgzb() != null) {
                    count = hisMzRecord.getSysjc().get(mParentItemSysjcPostion).getJyjgzb().size();
                }
            } else if (CommonConfig.MR_INDEX_ZHUYUAN_ID.equals(mIndex)) {
                if (mYqjcAndSysjcCombineDataMap == null) {
                    count = 0;
                } else {
                    YqjcAndSysjcCombineData entryData = mYqjcAndSysjcCombineDataMap.get(mYqjcAndSysjcCombineData_keyArray[mJcjyCurrentPositon]);
                    if (entryData.hisDataSysjcList == null) {
                        count = 0;
                    } else {
                        if (entryData.hisDataSysjcList.get(mParentItemSysjcPostion).getJyjgzb() != null) {
                            if (entryData.hisDataSysjcList.get(mParentItemSysjcPostion).getJyjgzb().size() != 0) {
                                count = entryData.hisDataSysjcList.get(mParentItemSysjcPostion).getJyjgzb().size();
                            }
                        }
                    }
                }
            }
            if (count == 0) {
                mIsEmptyData = true;
                count = 1; //如果数据为空，那么count设置为1，用来显示空视图
            } else {
                mIsEmptyData = false;
            }
            return count;
        }


        @Override
        public int getItemViewType(int position) {
            if (mIsEmptyData == true) {
                //空布局的类型
                return VIEW_TYPE_EMPTY_VIEW;
            }
            return super.getItemViewType(position);
        }

        class MyAPHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.item_mr_detail_jcjy_jyjgzb)
            LinearLayout item_mr_detail_jcjy_jyjgzb;
            // 序号
            @BindView(R.id.tv_sequence)
            TextView tv_sequence;

            // 检测指标代码
            @BindView(R.id.tv_jczbdm)
            TextView tv_jczbdm;

            // 检测指标名称
            @BindView(R.id.tv_jczbmc)
            TextView tv_jczbmc;

            // 检测指标结果
            @BindView(R.id.tv_jczbjg)
            TextView tv_jczbjg;

            // 异常提示
            @BindView(R.id.tv_ycts)
            ImageView iv_ycts;

            // 计量单位
            @BindView(R.id.tv_jldw)
            TextView tv_jldw;

            // 参考值范围
            @BindView(R.id.tv_ckz)
            TextView tv_ckz;

            public MyAPHolder(View itemView) {
                super(itemView);

                //如果是数据为空，不做注解绑定view，因为是空布局文件，ButterKnife.bind会crash
                if (mIsEmptyData == true) {
                    //do nothing
                } else {
                    ButterKnife.bind(this, itemView);
                }
            }
        }
    }

    //-----RecyclerViewAdapter 细菌结果----------------------------------------------------------------------------------------------

    /**
     * Recyclerview适配器 细菌结果
     */
    class RecyclerViewAdapter_xjjg extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

        private Context mContext;
        private int mParentItemSysjcPostion; //父列表，实验室检查列表的postion
        private final int VIEW_TYPE_EMPTY_VIEW = 99; //空数据时，显示空布局类型 https://www.jianshu.com/p/8d6a8860088b
        private boolean mIsEmptyData = true; //用来表示当前数据是否为空

        public RecyclerViewAdapter_xjjg(Context context, int parentItemSysjcPostion) {
            Log.d("111", "RecyclerViewAdapter_xjjg");
            mContext = context;

            mParentItemSysjcPostion = parentItemSysjcPostion;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.d("111", "RecyclerViewAdapter_xjjg_onCreateViewHolder");
            View view = null;

            if (VIEW_TYPE_EMPTY_VIEW == viewType) {
                //空布局
                view = LayoutInflater.from(mContext).inflate(R.layout.item_mr_detail_recyclerview_empty, parent, false);
            } else {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_mr_detail_jcjy_xjjg, parent, false);
            }

            MyAPHolder holder = new MyAPHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            Log.d("111", "xj_onBindViewHolder");
            //如果是数据为空，啥也不干，直接显示空布局R.layout.item_mr_detail_recyclerview_empty就好
            if (mIsEmptyData == true) {
                //do nothing
            } else {
                MyAPHolder holder = (MyAPHolder) viewHolder;
                bindViewHolder(position, holder);
            }
        }

        @Override
        public void onClick(View view) {
            MyAPHolder holder = (MyAPHolder) view.getTag();
            switch (view.getId()) {
                case R.id.item_mr_detail_jcjy_xjjg:  //点击整个item                    I
                    break;
            }
        }

        private void bindViewHolder(int position, MyAPHolder holder) {
            Log.d("111", "xj_bindViewHolder" + mIndex);
            //LogUtils.d("5-----1>position=" + position);
            holder.item_mr_detail_jcjy_xjjg.setOnClickListener(this);
            holder.item_mr_detail_jcjy_xjjg.setTag(holder);

            List<HisDataXjjg> xjjgList = null;
            if (CommonConfig.MR_INDEX_MENZHEN_ID.equals(mIndex)) {
                Log.d("111", "***menzhen");
                xjjgList = hisMzRecord.getSysjc().get(mParentItemSysjcPostion).getXjjg();
            } else if (CommonConfig.MR_INDEX_ZHUYUAN_ID.equals(mIndex)) {
                Log.d("111", "****zhuyuan");
                YqjcAndSysjcCombineData entryDate = mYqjcAndSysjcCombineDataMap.get(mYqjcAndSysjcCombineData_keyArray[mJcjyCurrentPositon]);
                xjjgList = entryDate.hisDataSysjcList.get(mParentItemSysjcPostion).getXjjg();
                Log.d("111", "***********" + GsonUtils.ListTojson(xjjgList));
            }

            // 序号
            holder.tv_sequence.setText("" + (position + 1));
            // 检测指标代码
            holder.tv_xjdh.setText(xjjgList.get(position).getXjdh());
            // 细菌名称
            holder.tv_xjmc.setText(xjjgList.get(position).getXjmc());
            // 菌落计数
            holder.tv_jljs.setText(xjjgList.get(position).getJljs());
            // 培养基
            holder.tv_pyj.setText(xjjgList.get(position).getByj());
            // 培养时间
            holder.tv_pysj.setText(xjjgList.get(position).getBysj());
            // 培养条件
            holder.tv_pytj.setText(xjjgList.get(position).getPytj());
            // 发现方式
            holder.tv_fxfs.setText(xjjgList.get(position).getFxfs());
        }

        @Override
        public int getItemCount() {
            Log.d("111", "*****getItemCount");
            int count = GetItemCountFunc();
            //==========================空数据显示
            if (count == 0) {
                mIsEmptyData = true;
                count = 1; //如果数据为空，那么count设置为1，用来显示空视图
            } else {
                mIsEmptyData = false;
            }
            return count;
        }

        /**
         * 将列表数量的计算，独立成一个函数，是为了让这个函数在外层的实验室检验列表也能调用，
         * 以此来判断是否需要隐藏当前的(检验结果、细菌结果、药敏结果)列表区域，详情请参考调用此函数的地方
         *
         * @return int 列表数量
         */
        public int GetItemCountFunc() {
            Log.d("111", "GetItemCountFunc");
            int count = 0;

            if (CommonConfig.MR_INDEX_MENZHEN_ID.equals(mIndex)) {
                if (hisMzRecord.getSysjc() != null
                        && hisMzRecord.getSysjc().size() != 0
                        && hisMzRecord.getSysjc().get(mParentItemSysjcPostion) != null
                        && hisMzRecord.getSysjc().get(mParentItemSysjcPostion).getXjjg() != null) {
                    count = hisMzRecord.getSysjc().get(mParentItemSysjcPostion).getXjjg().size();
                }
            } else if (CommonConfig.MR_INDEX_ZHUYUAN_ID.equals(mIndex)) {
                if (mYqjcAndSysjcCombineDataMap == null) {
                    count = 0;
                } else {
                    YqjcAndSysjcCombineData entryData = mYqjcAndSysjcCombineDataMap.get(mYqjcAndSysjcCombineData_keyArray[mJcjyCurrentPositon]);

                    if (entryData.hisDataSysjcList == null) {
                        count = 0;
                    } else {
                        if (entryData.hisDataSysjcList.get(mParentItemSysjcPostion).getXjjg() != null) {
                            count = entryData.hisDataSysjcList.get(mParentItemSysjcPostion).getXjjg().size();
                        }
                    }
                }
            }
            Log.d("111", "GetItemCountFunc___count:" + count);
            return count;
        }

        @Override
        public int getItemViewType(int position) {

            if (mIsEmptyData == true) {
                //空布局的类型
                return VIEW_TYPE_EMPTY_VIEW;
            }

            return super.getItemViewType(position);
        }

        class MyAPHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.item_mr_detail_jcjy_xjjg)
            LinearLayout item_mr_detail_jcjy_xjjg;

            // 序号
            @BindView(R.id.tv_sequence)
            TextView tv_sequence;

            // 细菌代号
            @BindView(R.id.tv_xjdh)
            TextView tv_xjdh;

            // 细菌名称
            @BindView(R.id.tv_xjmc)
            TextView tv_xjmc;

            // 菌落计数
            @BindView(R.id.tv_jljs)
            TextView tv_jljs;

            // 培养基
            @BindView(R.id.tv_pyj)
            TextView tv_pyj;

            // 培养时间
            @BindView(R.id.tv_pysj)
            TextView tv_pysj;

            // 培养条件
            @BindView(R.id.tv_pytj)
            TextView tv_pytj;

            // 发现方式
            @BindView(R.id.tv_fxfs)
            TextView tv_fxfs;

            public MyAPHolder(View itemView) {
                super(itemView);
                //如果是数据为空，不做注解绑定view，因为是空布局文件，ButterKnife.bind会crash
                if (mIsEmptyData == true) {
                    //do nothing
                } else {
                    ButterKnife.bind(this, itemView);
                }
            }
        }
    }

    //-----RecyclerViewAdapter 药敏结果----------------------------------------------------------------------------------------------

    /**
     * Recyclerview适配器 药敏结果
     */
    class RecyclerViewAdapter_ymjg extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

        private Context mContext;
        private int mParentItemSysjcPostion; //父列表，实验室检查列表的postion
        private final int VIEW_TYPE_EMPTY_VIEW = 99; //空数据时，显示空布局类型 https://www.jianshu.com/p/8d6a8860088b
        private boolean mIsEmptyData = true; //用来表示当前数据是否为空

        public RecyclerViewAdapter_ymjg(Context context, int parentItemSysjcPostion) {
            mContext = context;

            mParentItemSysjcPostion = parentItemSysjcPostion;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;

            if (VIEW_TYPE_EMPTY_VIEW == viewType) {
                //空布局
                view = LayoutInflater.from(mContext).inflate(R.layout.item_mr_detail_recyclerview_empty, parent, false);
            } else {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_mr_detail_jcjy_ymjg, parent, false);
            }

            MyAPHolder holder = new MyAPHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            //如果是数据为空，啥也不干，直接显示空布局R.layout.item_mr_detail_recyclerview_empty就好
            if (mIsEmptyData == true) {
                //do nothing
            } else {
                MyAPHolder holder = (MyAPHolder) viewHolder;
                bindViewHolder(position, holder);
            }
        }

        @Override
        public void onClick(View view) {
            MyAPHolder holder = (MyAPHolder) view.getTag();
            switch (view.getId()) {
                case R.id.item_mr_detail_jcjy_ymjg:  //点击整个item                    I
                    break;
            }
        }

        private void bindViewHolder(int position, MyAPHolder holder) {
            //LogUtils.d("5-----1>position=" + position);
            holder.item_mr_detail_jcjy_ymjg.setOnClickListener(this);
            holder.item_mr_detail_jcjy_ymjg.setTag(holder);

            List<HisDataYmjg> ymjgList = null;
            if (CommonConfig.MR_INDEX_MENZHEN_ID.equals(mIndex)) {
                ymjgList = hisMzRecord.getSysjc().get(mParentItemSysjcPostion).getYmjg();
            } else if (CommonConfig.MR_INDEX_ZHUYUAN_ID.equals(mIndex)) {
                YqjcAndSysjcCombineData entryDate = mYqjcAndSysjcCombineDataMap.get(mYqjcAndSysjcCombineData_keyArray[mJcjyCurrentPositon]);
                ymjgList = entryDate.hisDataSysjcList.get(mParentItemSysjcPostion).getYmjg();
            }

            // 序号
            holder.tv_sequence.setText("" + (position + 1));
            // 细菌代号
            holder.tv_xjdh.setText(ymjgList.get(position).getXjdh());
            // 药敏代码
            holder.tv_ymdm.setText(ymjgList.get(position).getYmdm());
            // 药敏名称
            holder.tv_ymmc.setText(ymjgList.get(position).getYmmc());
            // 检测结果
            holder.tv_jcjg.setText(ymjgList.get(position).getJcjg());
            // 抑菌浓度
            holder.tv_yjnd.setText(ymjgList.get(position).getYjnd());
            // 纸片含药量
            holder.tv_zphyl.setText(ymjgList.get(position).getZphyl());
            // 抑菌环直径
            holder.tv_yjhzj.setText(ymjgList.get(position).getYjhzj());
        }

        @Override
        public int getItemCount() {

            int count = GetItemCountFunc();

            //==========================空数据显示
            if (count == 0) {
                mIsEmptyData = true;
                count = 1; //如果数据为空，那么count设置为1，用来显示空视图
            } else {
                mIsEmptyData = false;
            }

            return count;
        }

        /**
         * 将列表数量的计算，独立成一个函数，是为了让这个函数在外层的实验室检验列表也能调用，
         * 以此来判断是否需要隐藏当前的(检验结果、细菌结果、药敏结果)列表区域，详情请参考调用此函数的地方
         *
         * @return int 列表数量
         */
        public int GetItemCountFunc() {
            int count = 0;

            if (CommonConfig.MR_INDEX_MENZHEN_ID.equals(mIndex)) {
                if (hisMzRecord.getSysjc() != null
                        && hisMzRecord.getSysjc().size() != 0
                        && hisMzRecord.getSysjc().get(mParentItemSysjcPostion) != null
                        && hisMzRecord.getSysjc().get(mParentItemSysjcPostion).getYmjg() != null) {
                    count = hisMzRecord.getSysjc().get(mParentItemSysjcPostion).getYmjg().size();
                }
            } else if (CommonConfig.MR_INDEX_ZHUYUAN_ID.equals(mIndex)) {
                if (mYqjcAndSysjcCombineDataMap == null) {
                    count = 0;
                } else {
                    YqjcAndSysjcCombineData entryDate = mYqjcAndSysjcCombineDataMap.get(mYqjcAndSysjcCombineData_keyArray[mJcjyCurrentPositon]);

                    if (entryDate.hisDataSysjcList == null) {
                        count = 0;
                    } else {
                        if (entryDate.hisDataSysjcList.get(mParentItemSysjcPostion).getYmjg() != null) {
                            count = entryDate.hisDataSysjcList.get(mParentItemSysjcPostion).getYmjg().size();
                        }
                    }
                }
            }

            return count;
        }

        @Override
        public int getItemViewType(int position) {

            if (mIsEmptyData == true) {
                //空布局的类型
                return VIEW_TYPE_EMPTY_VIEW;
            }

            return super.getItemViewType(position);
        }

        class MyAPHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.item_mr_detail_jcjy_ymjg)
            LinearLayout item_mr_detail_jcjy_ymjg;

            // 序号
            @BindView(R.id.tv_sequence)
            TextView tv_sequence;

            // 细菌代号
            @BindView(R.id.tv_xjdh)
            TextView tv_xjdh;

            // 药敏代码
            @BindView(R.id.tv_ymdm)
            TextView tv_ymdm;

            // 药敏名称
            @BindView(R.id.tv_ymmc)
            TextView tv_ymmc;

            // 检测结果
            @BindView(R.id.tv_jcjg)
            TextView tv_jcjg;

            // 抑菌浓度
            @BindView(R.id.tv_yjnd)
            TextView tv_yjnd;

            // 纸片含药量
            @BindView(R.id.tv_zphyl)
            TextView tv_zphyl;

            // 抑菌环直径
            @BindView(R.id.tv_yjhzj)
            TextView tv_yjhzj;

            public MyAPHolder(View itemView) {
                super(itemView);
                //如果是数据为空，不做注解绑定view，因为是空布局文件，ButterKnife.bind会crash
                if (mIsEmptyData == true) {
                    //do nothing
                } else {
                    ButterKnife.bind(this, itemView);
                }
            }
        }
    }

    //-----RecyclerViewAdapter 仪器检查----------------------------------------------------------------------------------------------

    /**
     * Recyclerview适配器 仪器检查
     */
    class RecyclerViewAdapter_yqjc extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

        private Context mContext;
        private final int VIEW_TYPE_EMPTY_VIEW = 99; //空数据时，显示空布局类型 https://www.jianshu.com/p/8d6a8860088b
        private boolean mIsEmptyData = true; //用来表示当前数据是否为空

        public RecyclerViewAdapter_yqjc(Context context) {
            mContext = context;

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = null;

            if (VIEW_TYPE_EMPTY_VIEW == viewType) {
                //空布局
                view = LayoutInflater.from(mContext).inflate(R.layout.item_mr_detail_recyclerview_empty, parent, false);
            } else {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_mr_detail_jcjy_yqjc, parent, false);
            }

            MyAPHolder holder = new MyAPHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            //如果是数据为空，啥也不干，直接显示空布局R.layout.item_mr_detail_recyclerview_empty就好
            if (mIsEmptyData == true) {
                //do nothing
            } else {
                MyAPHolder holder = (MyAPHolder) viewHolder;
                bindViewHolder(position, holder);
            }
        }

        @Override
        public void onClick(View view) {
            MyAPHolder holder = (MyAPHolder) view.getTag();
            switch (view.getId()) {
                case R.id.item_mr_detail_jcjy_yqjc:  //点击整个item                    I
                    break;
            }
        }

        private void bindViewHolder(int position, MyAPHolder holder) {
            //LogUtils.d("5-----1>position=" + position);
            holder.item_mr_detail_jcjy_yqjc.setOnClickListener(this);
            holder.item_mr_detail_jcjy_yqjc.setTag(holder);

            HisDataYqjc yqjc = null;
            if (CommonConfig.MR_INDEX_MENZHEN_ID.equals(mIndex)) {
                yqjc = hisMzRecord.getYqjc().get(position);
            } else if (CommonConfig.MR_INDEX_ZHUYUAN_ID.equals(mIndex)) {
                YqjcAndSysjcCombineData entryDate = mYqjcAndSysjcCombineDataMap.get(mYqjcAndSysjcCombineData_keyArray[mJcjyCurrentPositon]);
                yqjc = entryDate.hisDataYqjcList.get(position);
            }

            // 检查名称
            holder.tv_jcmc.setText(yqjc.getJcmc());
            // 检查类型名称
            holder.tv_jclxmc.setText(yqjc.getJclxmc());
            // 检查部位与方法
            holder.tv_jcbwff.setText(yqjc.getJcbwff());
            // 影响表现或检查所见
            holder.tv_yxbxjcsj.setText(yqjc.getYxbxjcsj());
            // 检查诊断或提示
            holder.tv_jczdts.setText(yqjc.getYxzdts());
            // 备注或建议
            holder.tv_bzhjy.setText(yqjc.getBzhjy());
            // 检查日期
            holder.tv_jcrq.setText(TimeUtils.getTime(yqjc.getJcsj(), TimeUtils.YY_MM_DD_FORMAT_3));
            // 报告日期
            holder.tv_bgrq.setText(TimeUtils.getTime(yqjc.getBgsj(), TimeUtils.YY_MM_DD_FORMAT_3));
        }

        @Override
        public int getItemCount() {
            int count = 0;
            if (CommonConfig.MR_INDEX_MENZHEN_ID.equals(mIndex)) {
                if (hisMzRecord == null || hisMzRecord.getYqjc() == null) {
                    count = 0;
                } else {
                    count = hisMzRecord.getYqjc().size();
                }
            } else if (CommonConfig.MR_INDEX_ZHUYUAN_ID.equals(mIndex)) {
                if (mYqjcAndSysjcCombineDataMap == null) {
                    count = 0;
                } else {
                    if (mYqjcAndSysjcCombineData_keyArray != null) {
                        if (mYqjcAndSysjcCombineData_keyArray.length != 0) {
                            YqjcAndSysjcCombineData entryDate = mYqjcAndSysjcCombineDataMap.get(mYqjcAndSysjcCombineData_keyArray[mJcjyCurrentPositon]);
                            if (entryDate == null) {
                                count = 0;
                            } else {
                                if (entryDate.hisDataYqjcList == null) {
                                    count = 0;
                                } else {
                                    count = entryDate.hisDataYqjcList.size();
                                }
                            }
                        }
                    }
                }
            }

            //==========================空数据显示
            if (count == 0) {
                mIsEmptyData = true;
                count = 1; //如果数据为空，那么count设置为1，用来显示空视图
            } else {
                mIsEmptyData = false;
            }

            return count;
        }

        @Override
        public int getItemViewType(int position) {

            if (mIsEmptyData == true) {
                //空布局的类型
                return VIEW_TYPE_EMPTY_VIEW;
            }

            return super.getItemViewType(position);
        }

        class MyAPHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.item_mr_detail_jcjy_yqjc)
            LinearLayout item_mr_detail_jcjy_yqjc;

            // 检查名称
            @BindView(R.id.tv_jcmc)
            TextView tv_jcmc;

            // 检查类型名称
            @BindView(R.id.tv_jclxmc)
            TextView tv_jclxmc;

            // 检查部位与方法
            @BindView(R.id.tv_jcbwff)
            TextView tv_jcbwff;

            // 影响表现或检查所见
            @BindView(R.id.tv_yxbxjcsj)
            TextView tv_yxbxjcsj;

            // 检查诊断或提示
            @BindView(R.id.tv_jczdts)
            TextView tv_jczdts;

            // 备注或建议
            @BindView(R.id.tv_bzhjy)
            TextView tv_bzhjy;

            // 检查日期
            @BindView(R.id.tv_jcrq)
            TextView tv_jcrq;

            // 报告日期
            @BindView(R.id.tv_bgrq)
            TextView tv_bgrq;


            public MyAPHolder(View itemView) {
                super(itemView);
                //如果是数据为空，不做注解绑定view，因为是空布局文件，ButterKnife.bind会crash
                if (mIsEmptyData == true) {
                    //do nothing
                } else {
                    ButterKnife.bind(this, itemView);
                }
            }
        }
    }

    //-----RecyclerViewAdapter 门诊病历的用药-西药（从处方信息里面取）----------------------------------------------------------------------------------------------

    /**
     * Recyclerview适配器 门诊病历的用药-西药（从处方信息里面取）
     */
    class RecyclerViewAdapter_cfxx_xiyao extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

        private Context mContext;
        private final int VIEW_TYPE_EMPTY_VIEW = 99; //空数据时，显示空布局类型 https://www.jianshu.com/p/8d6a8860088b
        private boolean mIsEmptyData = true; //用来表示当前数据是否为空

        public RecyclerViewAdapter_cfxx_xiyao(Context context) {
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;

            if (VIEW_TYPE_EMPTY_VIEW == viewType) {
                //空布局
                view = LayoutInflater.from(mContext).inflate(R.layout.item_mr_detail_recyclerview_empty, parent, false);
            } else {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_mr_detail_zlfa_cfxx_xiyao, parent, false);
            }

            MyAPHolder holder = new MyAPHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            //如果是数据为空，啥也不干，直接显示空布局R.layout.item_mr_detail_recyclerview_empty就好
            if (mIsEmptyData == true) {
                //do nothing
            } else {
                MyAPHolder holder = (MyAPHolder) viewHolder;
                bindViewHolder(position, holder);
            }
        }

        @Override
        public void onClick(View view) {
            MyAPHolder holder = (MyAPHolder) view.getTag();
            switch (view.getId()) {
                case R.id.item_mr_detail_zlfa_cfxx_xiyao:  //点击整个item                    I
                    break;
            }
        }

        private void bindViewHolder(int position, MyAPHolder holder) {
            //LogUtils.d("5-----1>position=" + position);
            holder.item_mr_detail_zlfa_cfxx_xiyao.setOnClickListener(this);
            holder.item_mr_detail_zlfa_cfxx_xiyao.setTag(holder);

            // 药物名称
            String ywmc = hisMzRecord_cfxx_xiyao_List.get(position).getYwmc();
            holder.tv_xiyao_ywmc.setText(EmptyUtil.isEmptyString(ywmc));
            // 剂型名称
            String yyjxmc = hisMzRecord_cfxx_xiyao_List.get(position).getYyjxmc();
            holder.tv_xiyao_jxmc.setText(EmptyUtil.isEmptyString(yyjxmc));
            //药品规格
            String ypgg = hisMzRecord_cfxx_xiyao_List.get(position).getYpgg();
            holder.tv_xiyao_ypgg.setText(EmptyUtil.isEmptyString(ypgg));
            // 发药数量 + 发药数量单位
            String fysl = hisMzRecord_cfxx_xiyao_List.get(position).getYpsl() + hisMzRecord_cfxx_xiyao_List.get(position).getYpdw();
            holder.tv_xiyao_ypsl_ypdw.setText(EmptyUtil.isEmptyString(fysl));
            // 药品用法
            String ypyf = hisMzRecord_cfxx_xiyao_List.get(position).getYpyf();
            holder.tv_xiyao_ypyf.setText(EmptyUtil.isEmptyString(ypyf));
            // 【每次使用剂量】&【每次使用剂量单位】
            String mcjl = hisMzRecord_cfxx_xiyao_List.get(position).getMcsyjl() + hisMzRecord_cfxx_xiyao_List.get(position).getMcsyjldw();
            holder.tv_xiyao_mcsyjl.setText(EmptyUtil.isEmptyString(mcjl));
            // 【每次使用数量】&【每次使用数量单位】
            String mcsl = hisMzRecord_cfxx_xiyao_List.get(position).getMcsysl() + hisMzRecord_cfxx_xiyao_List.get(position).getMcsysldw();
            holder.tv_xiyao_mcsysl.setText(EmptyUtil.isEmptyString(mcsl));
            // 用药频次
            String yypc = hisMzRecord_cfxx_xiyao_List.get(position).getYypc();
            holder.tv_xiyao_yypc.setText(EmptyUtil.isEmptyString(yypc));
            // 用药天数
            String yyts = hisMzRecord_cfxx_xiyao_List.get(position).getYyts() + "";
            holder.tv_xiyao_yyts.setText(EmptyUtil.isEmptyString(yyts));

        }

        @Override
        public int getItemCount() {
            int count = 0;
            if (hisMzRecord_cfxx_xiyao_List != null) {
                count = hisMzRecord_cfxx_xiyao_List.size();
            }

            //==========================空数据显示
            if (count == 0) {
                mIsEmptyData = true;
                count = 1; //如果数据为空，那么count设置为1，用来显示空视图
            } else {
                mIsEmptyData = false;
            }

            return count;
        }

        @Override
        public int getItemViewType(int position) {

            if (mIsEmptyData == true) {
                //空布局的类型
                return VIEW_TYPE_EMPTY_VIEW;
            }

            return super.getItemViewType(position);
        }

        class MyAPHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.item_mr_detail_zlfa_cfxx_xiyao)
            LinearLayout item_mr_detail_zlfa_cfxx_xiyao;

            // 药物名称
            @BindView(R.id.tv_xiyao_ywmc)
            TextView tv_xiyao_ywmc;
            // 剂型名称
            @BindView(R.id.tv_xiyao_jxmc)
            TextView tv_xiyao_jxmc;
            // 药品规格
            @BindView(R.id.tv_xiyao_ypgg)
            TextView tv_xiyao_ypgg;
            // 发药数量 + 发药数量单位
            @BindView(R.id.tv_xiyao_ypsl_ypdw)
            TextView tv_xiyao_ypsl_ypdw;
            // 药品用法
            @BindView(R.id.tv_xiyao_ypyf)
            TextView tv_xiyao_ypyf;
            // 每次剂量
            @BindView(R.id.tv_xiyao_mcsyjl)
            TextView tv_xiyao_mcsyjl;
            // 每次数量
            @BindView(R.id.tv_xiyao_mcsysl)
            TextView tv_xiyao_mcsysl;
            // 用药频次
            @BindView(R.id.tv_xiyao_yypc)
            TextView tv_xiyao_yypc;
            // 用药天数
            @BindView(R.id.tv_xiyao_yyts)
            TextView tv_xiyao_yyts;

            public MyAPHolder(View itemView) {
                super(itemView);
                //如果是数据为空，不做注解绑定view，因为是空布局文件，ButterKnife.bind会crash
                if (mIsEmptyData == true) {
                    //do nothing
                } else {
                    ButterKnife.bind(this, itemView);
                }
            }
        }
    }


    //-----RecyclerViewAdapter 门诊病历的手术信息----------------------------------------------------------------------------------------------

    /**
     * Recyclerview适配器 门诊病历的手术信息（从公共信息里面取）
     */
    class RecyclerViewAdapter_ssxx_menzhen extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

        private Context mContext;
        private final int VIEW_TYPE_EMPTY_VIEW = 99; //空数据时，显示空布局类型 https://www.jianshu.com/p/8d6a8860088b
        private boolean mIsEmptyData = true; //用来表示当前数据是否为空

        public RecyclerViewAdapter_ssxx_menzhen(Context context) {
            mContext = context;

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;

            if (VIEW_TYPE_EMPTY_VIEW == viewType) {
                //空布局
                view = LayoutInflater.from(mContext).inflate(R.layout.item_mr_detail_recyclerview_empty, parent, false);
            } else {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_mr_detail_zlfa_ssxx, parent, false);
            }

            MyAPHolder holder = new MyAPHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            //如果是数据为空，啥也不干，直接显示空布局R.layout.item_mr_detail_recyclerview_empty就好
            if (mIsEmptyData == true) {
                //do nothing
            } else {
                MyAPHolder holder = (MyAPHolder) viewHolder;
                bindViewHolder(position, holder);
            }
        }

        @Override
        public void onClick(View view) {
            MyAPHolder holder = (MyAPHolder) view.getTag();
            switch (view.getId()) {
                case R.id.item_mr_detail_zlfa_ssxx:  //点击整个item                    I
                    break;
            }

        }

        private void bindViewHolder(int position, MyAPHolder holder) {
            //LogUtils.d("5-----1>position=" + position);
            holder.item_mr_detail_zlfa_ssxx.setOnClickListener(this);
            holder.item_mr_detail_zlfa_ssxx.setTag(holder);

            // 医院手术操作名称
            holder.tv_yyssczmc.setText(hisMzRecord.getSsxx().get(position).getYyssczmc());
            // 手术前诊断
            holder.tv_ssqzd.setText(hisMzRecord.getSsxx().get(position).getYyssqzdmc());
            // 手术后诊断
            holder.tv_sshzd.setText(hisMzRecord.getSsxx().get(position).getYysshzdmc());
            //【手术过程描述】
            holder.tv_ssgcms.setText(hisMzRecord.getSsxx().get(position).getSsgcms());
            //【手术及操作方法】
            holder.tv_ssjczff.setText(hisMzRecord.getSsxx().get(position).getSsjczff());
        }

        @Override
        public int getItemCount() {
            int count = 0;
            if (hisMzRecord != null && hisMzRecord.getSsxx() != null) {
                count = hisMzRecord.getSsxx().size();
            }

            //==========================空数据显示
            if (count == 0) {
                mIsEmptyData = true;
                count = 1; //如果数据为空，那么count设置为1，用来显示空视图
            } else {
                mIsEmptyData = false;
            }

            return count;
        }

        @Override
        public int getItemViewType(int position) {

            if (mIsEmptyData == true) {
                //空布局的类型
                return VIEW_TYPE_EMPTY_VIEW;
            }

            return super.getItemViewType(position);
        }

        class MyAPHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.item_mr_detail_zlfa_ssxx)
            LinearLayout item_mr_detail_zlfa_ssxx;

            // 医院手术操作名称
            @BindView(R.id.tv_yyssczmc)
            TextView tv_yyssczmc;
            // 手术前诊断
            @BindView(R.id.tv_ssqzd)
            TextView tv_ssqzd;
            // 手术后诊断
            @BindView(R.id.tv_sshzd)
            TextView tv_sshzd;
            //【手术过程描述】
            @BindView(R.id.tv_ssgcms)
            TextView tv_ssgcms;
            //【手术及操作方法】
            @BindView(R.id.tv_ssjczff)
            TextView tv_ssjczff;

            public MyAPHolder(View itemView) {
                super(itemView);
                //如果是数据为空，不做注解绑定view，因为是空布局文件，ButterKnife.bind会crash
                if (mIsEmptyData == true) {
                    //do nothing
                } else {
                    ButterKnife.bind(this, itemView);
                }
            }
        }
    }

    //-----RecyclerViewAdapter 住院病历的用药(从医嘱信息里面取)----------------------------------------------------------------------------------------------

    /**
     * Recyclerview适配器 住院病历的用药(从医嘱信息里面取)
     */
    class RecyclerViewAdapter_yzxx extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

        private Context mContext;
        private final int VIEW_TYPE_EMPTY_VIEW = 99; //空数据时，显示空布局类型 https://www.jianshu.com/p/8d6a8860088b
        private boolean mIsEmptyData = true; //用来表示当前数据是否为空

        public RecyclerViewAdapter_yzxx(Context context) {
            mContext = context;

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;

            if (VIEW_TYPE_EMPTY_VIEW == viewType) {
                //空布局
                view = LayoutInflater.from(mContext).inflate(R.layout.item_mr_detail_recyclerview_empty, parent, false);
            } else {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_mr_detail_zlfa_yzxx, parent, false);
            }

            MyAPHolder holder = new MyAPHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            //如果是数据为空，啥也不干，直接显示空布局R.layout.item_mr_detail_recyclerview_empty就好
            if (mIsEmptyData == true) {
                //do nothing
            } else {
                MyAPHolder holder = (MyAPHolder) viewHolder;
                bindViewHolder(position, holder);
            }
        }

        @Override
        public void onClick(View view) {
            MyAPHolder holder = (MyAPHolder) view.getTag();
            switch (view.getId()) {
                case R.id.item_mr_detail_zlfa_yzxx:  //点击整个item                    I
                    break;
            }

        }

        private void bindViewHolder(int position, MyAPHolder holder) {
            //LogUtils.d("5-----1>position=" + position);
            holder.item_mr_detail_zlfa_yzxx.setOnClickListener(this);
            holder.item_mr_detail_zlfa_yzxx.setTag(holder);

            YzxxAndSsxxCombineData entryDate = mYzxxAndSsxxCombineDataMap.get(mYzxxAndSsxxCombineData_keyArray[mZlfaCurrentPositon]);
            HisDataYzxx yzxx = entryDate.hisDataYzxxList.get(position);

            // 药物名称
            holder.tv_ywmc.setText(yzxx.getYzmxmc());
            // 药品规格
            holder.tv_ypgg.setText(yzxx.getYpgg());
            // 发药数量 + 发药数量单位
            holder.tv_ypsl_ypdw.setText(yzxx.getYpsl() + yzxx.getYpdw());
            // 药品用法
            holder.tv_ypyf.setText(yzxx.getYpyf());
            // 【每次使用剂量】&【每次使用剂量单位】
            holder.tv_mcsyjl.setText(yzxx.getMcsyjl() + yzxx.getMcsyjldw());
            // 【每次使用数量】&【每次使用数量单位】
            holder.tv_mcsysl.setText(yzxx.getMcsl() + yzxx.getMcsldw());
            // 用药频度
            holder.tv_yypd.setText(yzxx.getYypd());
            // 用药天数
            holder.tv_yyts.setText((yzxx.getYyts() == null) ? "" : yzxx.getYyts().toString());
        }

        @Override
        public int getItemCount() {
            int count = 0;
            if (mYzxxAndSsxxCombineDataMap == null || mYzxxAndSsxxCombineDataMap.size() == 0) {
                count = 0;
            } else {
                if (mYzxxAndSsxxCombineData_keyArray == null || mYzxxAndSsxxCombineData_keyArray.length == 0) {
                    count = 0;
                } else {
                    YzxxAndSsxxCombineData entryDate = mYzxxAndSsxxCombineDataMap.get(mYzxxAndSsxxCombineData_keyArray[mZlfaCurrentPositon]);
                    if (entryDate == null) {
                        count = 0;
                    } else {
                        if (entryDate.hisDataYzxxList == null) {
                            count = 0;
                        } else {
                            count = entryDate.hisDataYzxxList.size();
                        }
                    }
                }
            }

            //==========================空数据显示
            if (count == 0) {
                mIsEmptyData = true;
                count = 1; //如果数据为空，那么count设置为1，用来显示空视图
            } else {
                mIsEmptyData = false;
            }

            return count;
        }

        @Override
        public int getItemViewType(int position) {

            if (mIsEmptyData == true) {
                //空布局的类型
                return VIEW_TYPE_EMPTY_VIEW;
            }

            return super.getItemViewType(position);
        }

        class MyAPHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.item_mr_detail_zlfa_yzxx)
            LinearLayout item_mr_detail_zlfa_yzxx;

            //------住院病历的用药 （从医嘱信息里面取）-----------------
            // 药物名称
            @BindView(R.id.tv_ywmc)
            TextView tv_ywmc;
            // 药品规格
            @BindView(R.id.tv_ypgg)
            TextView tv_ypgg;
            // 发药数量 + 发药数量单位
            @BindView(R.id.tv_ypsl_ypdw)
            TextView tv_ypsl_ypdw;
            // 药品用法
            @BindView(R.id.tv_ypyf)
            TextView tv_ypyf;
            // 每次剂量
            @BindView(R.id.tv_mcsyjl)
            TextView tv_mcsyjl;
            // 每次数量
            @BindView(R.id.tv_mcsysl)
            TextView tv_mcsysl;
            // 用药天数
            @BindView(R.id.tv_yyts)
            TextView tv_yyts;
            // 用药频度
            @BindView(R.id.tv_yypd)
            TextView tv_yypd;

            public MyAPHolder(View itemView) {
                super(itemView);
                //如果是数据为空，不做注解绑定view，因为是空布局文件，ButterKnife.bind会crash
                if (mIsEmptyData == true) {
                    //do nothing
                } else {
                    ButterKnife.bind(this, itemView);
                }
            }
        }
    }

    //-----RecyclerViewAdapter 住院诊断信息----------------------------------------------------------------------------------------------、
    class RecyclerViewAdapter_zdxx_zhuyuan extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context mContext;
        private final int VIEW_TYPE_EMPTY_VIEW = 99; //空数据时，显示空布局类型
        private boolean mIsEmptyData = true; //用来表示当前数据是否为空

        public RecyclerViewAdapter_zdxx_zhuyuan(Context mContext) {
            this.mContext = mContext;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = null;

            if (VIEW_TYPE_EMPTY_VIEW == viewType) {
                //空布局
                view = LayoutInflater.from(mContext).inflate(R.layout.item_mr_detail_recyclerview_empty, parent, false);
            } else {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_mr_detail_zlfa_zdxx, parent, false);
            }

            MyAPHolder holder = new MyAPHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            if (mIsEmptyData == true) {
                //do nothing
            } else {
                MyAPHolder holder = (MyAPHolder) viewHolder;
                bindViewHolder(position, holder);
            }
        }

        private void bindViewHolder(int position, MyAPHolder holder) {
            holder.item_mr_detail_zdxx.setTag(holder);
            List<HisDataZdxx> dataZdxxes = hisZyRecord.getZdxx();
            HisDataZdxx zdxx = dataZdxxes.get(position);
            // 医院诊断类别代码名称
            holder.tv_zdlbmc.setText(zdxx.getZdlbmc());
            // 诊断编码
            holder.tv_zdbm.setText(zdxx.getZdbm());
            // 医院诊断编码名称
            holder.tv_zdbmmc.setText(zdxx.getZdbmmc());
            //诊断编码类型
            if ("01".equals(zdxx.getBmlx())) {
                holder.tv_bmlx.setText("ICD-10");
            } else {
                holder.tv_bmlx.setText("国标-95");
            }
            //诊断时间
//            holder.tv_zdsj.setText(TimeUtils.formatDate_YY_MM_DD_FORMAT_3(zdxx.getZdsj()));
            holder.tv_zdsj.setText(TimeUtils.getTime(zdxx.getZdsj(), TimeUtils.YY_MM_DD_FORMAT_3));
            //诊断说明
            holder.tv_zdsm.setText(zdxx.getZdsm());
            //主要诊断标志
            if ("1".equals(zdxx.getZyzdbz())) {
                holder.tv_zyzdbz.setText("主要诊断");
            } else {
                holder.tv_zyzdbz.setText("其他诊断");
            }
            //疑似诊断标志
            if ("0".equals(zdxx.getYzdbz())) {
                holder.tv_yzdbz.setText("已确诊");
            } else {
                holder.tv_yzdbz.setText("仍疑似");
            }
            //入院病情
            if ("1".equals(zdxx.getRybq())) {
                holder.tv_rybq.setText("有");
            } else if ("2".equals(zdxx.getRybq())) {
                holder.tv_rybq.setText("临床未确定");
            } else if ("3".equals(zdxx.getRybq())) {
                holder.tv_rybq.setText("情况不明");
            } else {
                holder.tv_rybq.setText("无");
            }
            //出院情况编码
            if ("1".equals(zdxx.getCyqkbm())) {
                holder.tv_cyqkbm.setText("治愈");
            } else if ("2".equals(zdxx.getCyqkbm())) {
                holder.tv_cyqkbm.setText("好转");
            } else if ("3".equals(zdxx.getCyqkbm())) {
                holder.tv_cyqkbm.setText("未愈");
            } else if ("4".equals(zdxx.getCyqkbm())) {
                holder.tv_cyqkbm.setText("死亡");
            } else {
                holder.tv_cyqkbm.setText("其他");
            }
        }


        @Override
        public int getItemCount() {
            int count = 0;
            if (hisZyRecord != null && hisZyRecord.getSsxx() != null) {
                count = hisZyRecord.getZdxx().size();
            }

            //==========================空数据显示
            if (count == 0) {
                mIsEmptyData = true;
                count = 1; //如果数据为空，那么count设置为1，用来显示空视图
            } else {
                mIsEmptyData = false;
            }

            return count;
        }

        @Override
        public int getItemViewType(int position) {

            if (mIsEmptyData == true) {
                //空布局的类型
                return VIEW_TYPE_EMPTY_VIEW;
            }
            return super.getItemViewType(position);
        }


        class MyAPHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.item_mr_detail_zdxx)
            LinearLayout item_mr_detail_zdxx;
            @BindView(R.id.zdlbmc_tv)
            TextView tv_zdlbmc;
            @BindView(R.id.zdbm_tv)
            TextView tv_zdbm;
            @BindView(R.id.zdbmmc_tv)
            TextView tv_zdbmmc;
            @BindView(R.id.bmlx_tv)
            TextView tv_bmlx;
            @BindView(R.id.zdsj_tv)
            TextView tv_zdsj;
            @BindView(R.id.zdsm_tv)
            TextView tv_zdsm;
            @BindView(R.id.zyzdbz_tv)
            TextView tv_zyzdbz;
            @BindView(R.id.yzdbz_tv)
            TextView tv_yzdbz;
            @BindView(R.id.rybq_tv)
            TextView tv_rybq;
            @BindView(R.id.cyqkbm_tv)
            TextView tv_cyqkbm;


            public MyAPHolder(View itemView) {
                super(itemView);
                //如果是数据为空，不做注解绑定view，因为是空布局文件，ButterKnife.bind会crash
                if (mIsEmptyData == true) {
                    //do nothing
                } else {
                    ButterKnife.bind(this, itemView);
                }
            }
        }
    }
    //-----RecyclerViewAdapter 住院病历的手术信息----------------------------------------------------------------------------------------------

    /**
     * Recyclerview适配器 住院病历的手术信息（从公共信息里面取）
     */
    class RecyclerViewAdapter_ssxx_zhuyuan extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

        private Context mContext;
        private final int VIEW_TYPE_EMPTY_VIEW = 99; //空数据时，显示空布局类型 https://www.jianshu.com/p/8d6a8860088b
        private boolean mIsEmptyData = true; //用来表示当前数据是否为空

        public RecyclerViewAdapter_ssxx_zhuyuan(Context context) {
            mContext = context;

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;

            if (VIEW_TYPE_EMPTY_VIEW == viewType) {
                //空布局
                view = LayoutInflater.from(mContext).inflate(R.layout.item_mr_detail_recyclerview_empty, parent, false);
            } else {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_mr_detail_zlfa_ssxx, parent, false);
            }

            MyAPHolder holder = new MyAPHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            //如果是数据为空，啥也不干，直接显示空布局R.layout.item_mr_detail_recyclerview_empty就好
            if (mIsEmptyData == true) {
                //do nothing
            } else {
                MyAPHolder holder = (MyAPHolder) viewHolder;
                bindViewHolder(position, holder);
            }
        }

        @Override
        public void onClick(View view) {
            MyAPHolder holder = (MyAPHolder) view.getTag();
            switch (view.getId()) {
                case R.id.item_mr_detail_zlfa_ssxx:  //点击整个item                    I
                    break;
            }

        }

        private void bindViewHolder(int position, MyAPHolder holder) {
            //LogUtils.d("5-----1>position=" + position);
            holder.item_mr_detail_zlfa_ssxx.setOnClickListener(this);
            holder.item_mr_detail_zlfa_ssxx.setTag(holder);

            YzxxAndSsxxCombineData entryDate = mYzxxAndSsxxCombineDataMap.get(mYzxxAndSsxxCombineData_keyArray[mZlfaCurrentPositon]);
            HisDataSsxx ssxx = entryDate.hisDataSsxxList.get(position);

            // 医院手术操作名称
            holder.tv_yyssczmc.setText(ssxx.getYyssczmc());
            // 手术前诊断
            holder.tv_ssqzd.setText(ssxx.getYyssqzdmc());
            // 手术后诊断
            holder.tv_sshzd.setText(ssxx.getYysshzdmc());
            //【手术过程描述】
            holder.tv_ssgcms.setText(ssxx.getSsgcms());
            //【手术及操作方法】
            holder.tv_ssjczff.setText(ssxx.getSsjczff());
        }

        @Override
        public int getItemCount() {
            int count = 0;
            if (mYzxxAndSsxxCombineDataMap == null || mYzxxAndSsxxCombineDataMap.size() == 0) {
                count = 0;
            } else {
                if (mYzxxAndSsxxCombineData_keyArray == null || mYzxxAndSsxxCombineData_keyArray.length == 0) {
                    count = 0;
                } else {
                    YzxxAndSsxxCombineData entryDate = mYzxxAndSsxxCombineDataMap.get(mYzxxAndSsxxCombineData_keyArray[mZlfaCurrentPositon]);
                    if (entryDate == null) {
                        count = 0;
                    } else {
                        if (entryDate.hisDataSsxxList == null) {
                            count = 0;
                        } else {
                            count = entryDate.hisDataSsxxList.size();
                        }
                    }
                }
            }

            //==========================空数据显示
            if (count == 0) {
                mIsEmptyData = true;
                count = 1; //如果数据为空，那么count设置为1，用来显示空视图
            } else {
                mIsEmptyData = false;
            }

            return count;
        }

        @Override
        public int getItemViewType(int position) {

            if (mIsEmptyData == true) {
                //空布局的类型
                return VIEW_TYPE_EMPTY_VIEW;
            }

            return super.getItemViewType(position);
        }

        class MyAPHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.item_mr_detail_zlfa_ssxx)
            LinearLayout item_mr_detail_zlfa_ssxx;

            // 医院手术操作名称
            @BindView(R.id.tv_yyssczmc)
            TextView tv_yyssczmc;
            // 手术前诊断
            @BindView(R.id.tv_ssqzd)
            TextView tv_ssqzd;
            // 手术后诊断
            @BindView(R.id.tv_sshzd)
            TextView tv_sshzd;
            //【手术过程描述】
            @BindView(R.id.tv_ssgcms)
            TextView tv_ssgcms;
            //【手术及操作方法】
            @BindView(R.id.tv_ssjczff)
            TextView tv_ssjczff;

            public MyAPHolder(View itemView) {
                super(itemView);
                //如果是数据为空，不做注解绑定view，因为是空布局文件，ButterKnife.bind会crash
                if (mIsEmptyData == true) {
                    //do nothing
                } else {
                    ButterKnife.bind(this, itemView);
                }
            }
        }
    }
}
