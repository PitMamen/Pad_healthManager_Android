package com.bitvalue.health.ui.fragment.healthmanage;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.ApiResult;
import com.bitvalue.health.api.eventbusbean.MainRefreshObj;
import com.bitvalue.health.api.requestbean.MealCreateOrderApi;
import com.bitvalue.health.api.responsebean.DepartmentResponeBean;
import com.bitvalue.health.api.responsebean.DiseaseListBean;
import com.bitvalue.health.api.responsebean.GoodListBean;
import com.bitvalue.health.api.responsebean.LoginBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.api.responsebean.PlanListBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.OnItemClick;
import com.bitvalue.health.callback.ViewCallback;
import com.bitvalue.health.model.planmodel.DiseaseListApi;
import com.bitvalue.health.model.planmodel.GoodListApi;
import com.bitvalue.health.model.planmodel.PlanListApi;
import com.bitvalue.health.model.planmodel.getDepartmentListApi;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.AlreadySelectPatientAdapter;
import com.bitvalue.health.ui.adapter.PlansAdapter;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.SharedPreManager;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.health.util.customview.MPopupWindow;
import com.bitvalue.health.util.customview.TypeGravity;
import com.bitvalue.health.util.customview.UseEquityDialog;
import com.bitvalue.health.util.customview.WrapRecyclerView;
import com.bitvalue.health.util.customview.spinner.EditSpinner;
import com.bitvalue.healthmanage.R;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.http.listener.OnHttpListener;
import com.hjq.toast.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * @author created by bitvalue
 * @data : 01/14
 * 患者报道 右边的 计划列表界面
 */
public class FollowUpPlanFragment extends BaseFragment implements OnHttpListener, OnItemClick, AlreadySelectPatientAdapter.OnDeleteCallBack {
    @BindView(R.id.rl_back)
    RelativeLayout back;

    @BindView(R.id.tv_sortby_time)
    TextView tv_sortby_time;

//    @BindView(R.id.sp_keshi)
//    Spinner spinner_keshi;
//    @BindView(R.id.sp_text)
//    TextView tv_keshi;


    @BindView(R.id.sp_zhuanbing)
    Spinner sp_zhuanbing;
    @BindView(R.id.sp_text_zhuanb)
    TextView tv_zhuabing;


    @BindView(R.id.rl_confrm)
    RelativeLayout rl_confirm;

    @BindView(R.id.hs_selectedlist)
    WrapRecyclerView selectList;

    @BindView(R.id.rl_selectplan)
    RelativeLayout rl_select_planl;
    @BindView(R.id.tv_pat_name)
    TextView tv_select_planname;


    @BindView(R.id.list_plans)
    RecyclerView list_plans;
    @BindView(R.id.rl_default_view)
    RelativeLayout default_view;

    @BindView(R.id.ed_spinner_select)
    EditSpinner spinner2;


    private List<GoodListBean> planListBeans = new ArrayList<>();
    private AlreadySelectPatientAdapter adapter_selectPatient;
    private ArrayAdapter<String> spinnerAdapter;
    private ArrayAdapter<String> spinnerzhuanbingAdapter;
    private PlansAdapter plansAdapter;
    private MPopupWindow mPopupWindow;
    private boolean isSortTime = true;//初始值按时间排序
    private HomeActivity homeActivity;
    private List<NewLeaveBean.RowsDTO> selectPatientList = new ArrayList<>();


    private String[] zhuanbinglist;  //专病 先写死
    private GoodListBean selectPlanBean = null;  //从计划列表中选中的套餐计划
    private List<String> departmentList = new ArrayList<>();
    private Map<String, Integer> map = new HashMap<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_followup_plan_layout;
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        EventBus.getDefault().register(this);
        rl_select_planl.setVisibility(View.GONE);
        list_plans.setLayoutManager(new LinearLayoutManager(homeActivity));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(homeActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        selectList.setLayoutManager(linearLayoutManager);
        spinner2.setRightImageResource(R.mipmap.down_shixin);
        spinner2.setHint("请选科室");
//        spinner2.setTextColor(R.color.text_desc_dark);
        initSpinnerDepartment();
        initSpinnerSpecial();
//        list_plans.addItemDecoration(MUtils.spaceDivider(DensityUtil.dip2px(homeActivity, this.getResources().getDimension(R.dimen.qb_px_4)), false));
        plansAdapter = new PlansAdapter(R.layout.item_plan_list, planListBeans);
        plansAdapter.setOnItemClick(this);
        list_plans.setAdapter(plansAdapter);
        adapter_selectPatient = new AlreadySelectPatientAdapter(R.layout.item_select_patient_layout, selectPatientList);
        adapter_selectPatient.setOnItemDeleteClickLisenler(this);
        selectList.setAdapter(adapter_selectPatient);

//        spinner2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.e("EditSpinner", "item " + position + " click");
//                tv_zhuabing.setText("请选专病");
//                int departmentID = map.get(departmentList.get(position));
//                getDiseaseList(departmentID);
//                getMyPlans(departmentID);
//            }
//        });

    }


    /**
     * 初始化科室 Spinner
     */
    private void initSpinnerDepartment() {

        //将adapter 加入到spinner中
//        spinner_keshi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String selectDepartmentName = departmentList.get(position);
//                tv_keshi.setText(selectDepartmentName.length() >= 8 ? selectDepartmentName.substring(0, 7) : selectDepartmentName);
//                tv_zhuabing.setText("请选专病");
//                int departmentID = map.get(departmentList.get(position));
//                getDiseaseList(departmentID);
//                getMyPlans(departmentID);
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

    }


    private void initSpinnerSpecial() {

        //将adapter 加入到spinner中
        sp_zhuanbing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectZhuanBing = zhuanbinglist[position];
                tv_zhuabing.setText(selectZhuanBing.length() >= 8 ? selectZhuanBing.substring(0, 7) : selectZhuanBing);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        getMyPlans(0);
        getDepartmentList();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 获取套餐列表
     */
    private void getMyPlans(int departmentId) {
        GoodListApi goodListApi = new GoodListApi();
        goodListApi.goodsType = "plan_package";
        goodListApi.departmentId = departmentId;
        EasyHttp.get(this).api(goodListApi).request(new HttpCallback<ApiResult<List<GoodListBean>>>(this) {
            @Override
            public void onSucceed(ApiResult<List<GoodListBean>> result) {
                super.onSucceed(result);
                default_view.setVisibility(result.getData().size() == 0 ? View.VISIBLE : View.GONE);
                if (!EmptyUtil.isEmpty(result)) {
                    planListBeans = result.getData();
                    plansAdapter.updateList(planListBeans);
                    plansAdapter.setNewData(planListBeans);
                }
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
    }


    /**
     * 获取科室接口
     */
    private void getDepartmentList() {
        EasyHttp.get(this).api(new getDepartmentListApi()).request(new HttpCallback<ApiResult<List<DepartmentResponeBean>>>(this) {
            @Override
            public void onSucceed(ApiResult<List<DepartmentResponeBean>> result) {
                super.onSucceed(result);
                if (!EmptyUtil.isEmpty(result)) {
                    if (result.getCode()==0){
                        if (!EmptyUtil.isEmpty(result.getData()) && result.getData().size() > 0) {
                            for (int i = 0; i < result.getData().size(); i++) {
                                departmentList.add(result.getData().get(i).getDepartmentName());
                                map.put(result.getData().get(i).getDepartmentName(), result.getData().get(i).getDepartmentId());
                            }

                            spinner2.setItemData(departmentList);
                            spinner2.setOnItemClickListener((parent, view, position, id) -> {
                                tv_zhuabing.setText("请选专病");
                                int departmentID = map.get(departmentList.get(position));
                                getDiseaseList(departmentID);
                                getMyPlans(departmentID);
                            });


//                            spinnerAdapter = new ArrayAdapter<>(homeActivity, android.R.layout.simple_spinner_item, departmentList);
//                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            spinner_keshi.setAdapter(spinnerAdapter);
                        }
                    }else {
                         ToastUtils.show("获取科室列表失败:"+result.getMessage());
                    }

                }
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
    }


    /**
     * 获取专病接口
     */
    private void getDiseaseList(int departmentID) {
        DiseaseListApi diseaseListApi = new DiseaseListApi();
        diseaseListApi.departmentId = departmentID;
        EasyHttp.get(this).api(diseaseListApi).request(new HttpCallback<ApiResult<List<DiseaseListBean>>>(this) {
            @Override
            public void onSucceed(ApiResult<List<DiseaseListBean>> result) {
                super.onSucceed(result);
                if (!EmptyUtil.isEmpty(result)) {
                    if (!EmptyUtil.isEmpty(result.getData()) && result.getData().size() > 0) {
                        zhuanbinglist = new String[result.getData().size()];
                        for (int i = 0; i < result.getData().size(); i++) {
                            zhuanbinglist[i] = result.getData().get(i).diseaseName;
                        }
                        spinnerzhuanbingAdapter = new ArrayAdapter<>(homeActivity, android.R.layout.simple_spinner_item, zhuanbinglist);
                        //设置下拉列表的风格,simple_spinner_dropdown_item是android系统自带的样式，等会自己定义改动
                        spinnerzhuanbingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp_zhuanbing.setAdapter(spinnerzhuanbingAdapter);
                    }
                }


            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
    }


    /***
     * 分配计划
     * @param packegeID 套餐ID    pakecgeName 套餐名称   UserID 患者ID
     */
    private void mealOrderPackege(String packegeID, String UserID) {
        MealCreateOrderApi orderApi = new MealCreateOrderApi();
        LoginBean loginBean = SharedPreManager.getObject(Constants.KYE_USER_BEAN, LoginBean.class, Application.instance());
        if (loginBean == null) {
            return;
        }
        int docId = loginBean.getAccount().user.userId;
        orderApi.patientId = UserID;
        orderApi.doctorId = String.valueOf(docId);
        orderApi.templateId = String.valueOf(packegeID);
        orderApi.beginTime = TimeUtils.getCurrenTimeYMDHMS() + " 05:00:00";
        EasyHttp.post(this).api(orderApi).request(new HttpCallback<ApiResult>(this) {
            @Override
            public void onSucceed(ApiResult result) {
                super.onSucceed(result);
                hideDialog();
                //分配计划成功后 通知待分配界面 获取最新数据
                if (result.getCode() == 0) {
                    EventBus.getDefault().post(new MainRefreshObj());
                    ToastUtils.show("分配随访计划成功!");
                } else {
                    ToastUtils.show(result.getMessage());
                }
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
                hideDialog();
                ToastUtils.show("分配计划失败");
            }
        });

    }


    @OnClick({R.id.rl_back, R.id.tv_sortby_time, R.id.rl_confrm})
    public void OnClick(View view) {
        switch (view.getId()) {
            //返回
            case R.id.rl_back:
                if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0)
                    homeActivity.getSupportFragmentManager().popBackStack();
                break;
//
////                新增随访
//            case R.id.tv_addplan:
////                startActivity(new Intent(this, EditCreatePlanActivity.class));
//                break;


            //确认分配
            case R.id.rl_confrm:
                if (selectPlanBean == null) {
                    ToastUtils.show("请选择套餐计划!");
                    return;
                }
                if (selectPatientList.size() == 0) {
                    ToastUtils.show("请选择患者!");
                    return;
                }

                for (int i = 0; i < selectPatientList.size(); i++) {
                    mealOrderPackege(String.valueOf(selectPlanBean.getTemplateId()), selectPatientList.get(i).getUserId());
                }
                break;

//                按创建时间排序
            case R.id.tv_sortby_time:
//                showPopChoose(R.layout.pop_sort);
                break;

        }


    }

    private void showPopChoose(int layoutId) {
        mPopupWindow = MPopupWindow.create(homeActivity)
                .setLayoutId(layoutId)
//                .setBackgroundDrawable(new ColorDrawable(Color.GREEN))
                .setAnimationStyle(R.style.AnimDown)
//                .setOutsideTouchable(false)
                .setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        if (mPopupWindow != null) {
                        }
                    }
                })
                .setViewCallBack(viewCallback)
                .setTarget(tv_sortby_time)
                .setGravity(TypeGravity.BOTTOM_CENTER)
                .build();
        mPopupWindow.show();
    }

    /**
     * 弹窗点击排序功能
     */
    private ViewCallback viewCallback = new ViewCallback() {
        @Override
        public void onInitView(View view, int mLayoutId) {
            switch (mLayoutId) {
                case R.layout.pop_sort:
                    TextView tv_sort_time = view.findViewById(R.id.tv_sort_time);
                    TextView tv_sort_num = view.findViewById(R.id.tv_sort_num);
                    tv_sort_num.setTextColor(isSortTime ? homeActivity.getColor(R.color.white) : homeActivity.getColor(R.color.main_blue));
                    tv_sort_time.setTextColor(isSortTime ? homeActivity.getColor(R.color.main_blue) : homeActivity.getColor(R.color.white));
                    tv_sort_time.setOnClickListener(v -> {
                        isSortTime = true;
                        getMyPlans(0);
                        tv_sort_num.setTextColor(homeActivity.getColor(R.color.white));
                        tv_sort_time.setTextColor(homeActivity.getColor(R.color.main_blue));
                        tv_sortby_time.setText("按创建时间排序");
                        mPopupWindow.dismiss();
                    });
                    tv_sort_num.setOnClickListener(v -> {
                        isSortTime = false;
//                        Collections.sort(planListBeans);
                        plansAdapter.setNewData(planListBeans);
                        tv_sort_num.setTextColor(homeActivity.getColor(R.color.main_blue));
                        tv_sort_time.setTextColor(homeActivity.getColor(R.color.white));
                        tv_sortby_time.setText("按使用人数排序");
                        mPopupWindow.dismiss();
                    });
                    break;
            }
        }
    };


    @Override
    public void onSucceed(Object result) {

    }

    @Override
    public void onFail(Exception e) {

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(List<NewLeaveBean.RowsDTO> info) {
        selectPatientList = info;
        adapter_selectPatient.setNewData(selectPatientList);
    }


    @Override
    public void onItemClick(Object object, boolean isCheck) {
        selectPlanBean = (GoodListBean) object;
        if (selectPlanBean != null) {
            if (rl_select_planl.getVisibility() == View.GONE) {
                rl_select_planl.setVisibility(View.VISIBLE);
            }
            tv_select_planname.setText(selectPlanBean.getGoodsName());
        }
    }

    @Override
    public void onItemDelete(NewLeaveBean.RowsDTO item) {
        if (selectPatientList != null && selectPatientList.size() > 0) {
            selectPatientList.remove(item);
            adapter_selectPatient.setNewData(selectPatientList);
        }
    }
}
