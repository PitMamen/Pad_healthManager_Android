package com.bitvalue.health.util.customview.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bitvalue.health.api.requestbean.DocListBean;
import com.bitvalue.health.pickdatetime.DatePickDialog;
import com.bitvalue.health.pickdatetime.bean.DateParams;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.healthmanage.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author created by bitvalue
 * @data : 02/18
 */
public class UseEquityDialog extends Dialog {
    private Context mcontext;


    private OnClickBottomListener onClickBottomListener;

    private TextView tv_visit_type;
    private TextView tv_patient_name;
    private TextView tv_department;
    private Spinner sp_listdoc;
    private TextView tv_selected_doc;


    //    private ImageView sp_select_time;
    LinearLayout ll_select_time;
    private LinearLayout ll_continue_time;
    private TextView tv_selected_time;

    private Spinner sp_continuedtime;

    private RelativeLayout rl_comfrm;
    private RelativeLayout rl_cancel;


    private String visitType;
    private String visitName;
    private String departmentname;

    private ArrayAdapter<String> spinnerAdapter;
    private ArrayAdapter<String> Adapter_continueTime;

    private String selectDoc;
    private String selectContinueTime;
    private String selectTakeTime;
    private List<DocListBean> listbean;
    private int DocUserId; //医生Userid
    private String mRoleType = "casemanager";
    private String[] inpatientAreaList = {"请选择医生"};  // 先写死
    private String[] time = {"5分钟", "10分钟", "20分钟", "30分钟", "120分钟"};  //时间 先写死

    public UseEquityDialog(@NonNull Context context) {
        super(context);
        mcontext = context;
    }

    public UseEquityDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected UseEquityDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    public UseEquityDialog setOnclickListener(OnClickBottomListener onclickListener) {
        this.onClickBottomListener = onclickListener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_common_layout);
        initView();
        initSpinner();
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);

        initClicklisenter();

    }


    private void initView() {
        tv_visit_type = findViewById(R.id.tv_visit_type);
        tv_patient_name = findViewById(R.id.tv_patientname);
        tv_department = findViewById(R.id.tv_visit_depatment);
        sp_listdoc = findViewById(R.id.sp_doclist);
        ll_select_time = findViewById(R.id.ll_selecttime);
        tv_selected_time = findViewById(R.id.tv_select_time);
        sp_continuedtime = findViewById(R.id.sp_continuedtime_select);
        rl_comfrm = findViewById(R.id.rl_confrm);
        rl_cancel = findViewById(R.id.rl_cancel);
        ll_continue_time = findViewById(R.id.ll_continue_time);
        selectTakeTime = TimeUtils.getCurrenTime_Second();
        tv_selected_time.setText(TimeUtils.getCurrenTime_Second() + ":00");//这里默认一个当前时间


    }


    private void initSpinner() {
        //将adapter 加入到spinner中
        spinnerAdapter = new ArrayAdapter<>(mcontext, android.R.layout.simple_spinner_dropdown_item, inpatientAreaList);
        //设置下拉列表的风格,simple_spinner_dropdown_item是android系统自带的样式，等会自己定义改动
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  //android.R.layout.simple_spinner_dropdown_item

        sp_listdoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (map != null && map.size() > 0) {
                    DocUserId = map.get(sp_listdoc.getSelectedItem().toString());
                }
                if (inpatientAreaList != null && inpatientAreaList.length > 0) {
                    selectDoc = inpatientAreaList[position];
                }

                sp_listdoc.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_listdoc.setAdapter(spinnerAdapter);
        //将adapter 加入到spinner中
        Adapter_continueTime = new ArrayAdapter<>(mcontext, android.R.layout.simple_spinner_dropdown_item, time);
        //设置下拉列表的风格,simple_spinner_dropdown_item是android系统自带的样式，等会自己定义改动
        Adapter_continueTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  //android.R.layout.simple_spinner_dropdown_item

        sp_continuedtime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectContinueTime = sp_continuedtime.getSelectedItem().toString();
                selectContinueTime = selectContinueTime.substring(0, selectContinueTime.length() - 2);
                sp_continuedtime.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_continuedtime.setAdapter(Adapter_continueTime);
    }


    private void initClicklisenter() {
        rl_comfrm.setOnClickListener(v -> {
            if (onClickBottomListener != null) {
                onClickBottomListener.onPositiveClick();
            }
        });

        rl_cancel.setOnClickListener(v -> {
            if (onClickBottomListener != null) {
                onClickBottomListener.onNegtiveClick();
            }
        });


        ll_select_time.setOnClickListener(v -> {
            showDatePickDialog(DateParams.TYPE_YEAR, DateParams.TYPE_MONTH, DateParams.TYPE_DAY,
                    DateParams.TYPE_HOUR, DateParams.TYPE_MINUTE);
        });

    }


    private void showDatePickDialog(@DateParams.Type int... style) {
        Calendar todayCal = Calendar.getInstance();
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        endCal.add(Calendar.YEAR, 6);

        new DatePickDialog.Builder()
                .setTypes(style)
                .setCurrentDate(todayCal.getTime())
                .setStartDate(startCal.getTime())
                .setEndDate(endCal.getTime())
                .setOnSureListener(date -> {
                    String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
                    selectTakeTime = time;
                    tv_selected_time.setText(time);
                })
                .show(mcontext);
    }


    @Override
    public void show() {
        super.show();
        refreshView();
    }


    private void refreshView() {
        tv_visit_type.setText(!EmptyUtil.isEmpty(visitType) ? visitType : "");
        tv_patient_name.setText(!EmptyUtil.isEmpty(visitName) ? visitName : "");
        tv_department.setText(!EmptyUtil.isEmpty(departmentname) ? departmentname : "");
    }


    public UseEquityDialog setVisitType(String type) {
        this.visitType = type;
        return this;
    }

    public UseEquityDialog setVisitName(String paitentname) {
        this.visitName = paitentname;
        return this;
    }

    public UseEquityDialog setDepartment(String department) {
        this.departmentname = department;
        return this;
    }


    public UseEquityDialog setRoleType(String roleType) {
        this.mRoleType = roleType;
        return this;
    }


    public String getSelectDoc() {
        return selectDoc;
    }

    public String getSelectContinueTime() {
        return selectContinueTime;
    }


    public int getDocUserId() {
        return DocUserId;
    }


    public String getSelectTakeTime() {
        return selectTakeTime;
    }

    private Map<String, Integer> map = new HashMap<>();

    public void setDocList(List<DocListBean> list) {
        this.listbean = list;
        if (listbean != null && listbean.size() > 0) {
            inpatientAreaList = new String[listbean.size()];
            for (int i = 0; i < list.size(); i++) {
                inpatientAreaList[i] = listbean.get(i).getUserName();
                map.put(listbean.get(i).getUserName(), listbean.get(i).getUserId());
            }
        }
    }


    public interface OnClickBottomListener {
        /**
         * 点击确定按钮事件
         */
        void onPositiveClick();

        /**
         * 点击取消按钮事件
         */
        void onNegtiveClick();
    }


}
