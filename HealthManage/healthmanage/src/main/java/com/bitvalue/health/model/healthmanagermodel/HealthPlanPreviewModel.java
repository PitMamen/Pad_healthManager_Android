package com.bitvalue.health.model.healthmanagermodel;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bitvalue.health.api.ApiResult;
import com.bitvalue.health.api.responsebean.HealthPlanTaskListBean;
import com.bitvalue.health.api.responsebean.TaskInfoDTO;
import com.bitvalue.health.base.model.BaseModel;
import com.bitvalue.health.callback.Callback;
import com.bitvalue.health.contract.healthmanagercontract.HealthPlanDetailContract;
import com.bitvalue.health.contract.healthmanagercontract.HealthPlanPreviewContract;
import com.bitvalue.health.util.EmptyUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;

/**
 * @author created by bitvalue
 * @data :
 */
public class HealthPlanPreviewModel extends BaseModel implements HealthPlanPreviewContract.Model {
    @Override
    public void queryHealthPlanContent(String contentId, String planType, String userid, Callback callback) {
        mApi.queryHealthPlanContent(contentId, planType, userid)
                .subscribeOn(Schedulers.io())//子线程发射
                .observeOn(AndroidSchedulers.mainThread())//主线程接收
                .subscribe(result -> {
            if (!EmptyUtil.isEmpty(result)) {
                if (result.getCode() == 0) {
                    if (!EmptyUtil.isEmpty(result.getData())) {
                        callback.onSuccess(result.getData(), 1000);
                    }
                } else {
                    callback.onFailedLog(result.getMessage(), 1001);
                }
            }
        }, error -> {
            callback.onFailedLog(error.getMessage(), 1001);
        });
    }

    @SuppressLint("CheckResult")
    @Override
    public void queryhealtPlan(int planID, Callback callback) {
        mApi.queryHealthPlanTaskList(planID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//主线程接收
                .subscribe(result -> {
                    Log.e(TAG, "queryhealtPlan: "+result );
            if (!EmptyUtil.isEmpty(result)) {
                if (result.getCode() == 0) {
                    if (!EmptyUtil.isEmpty(result.getData())) {
                        List<HealthPlanTaskListBean> list=result.getData();
                        List<HealthPlanTaskListBean> formartList=new ArrayList<>();
                            for (int i = 0; i < list.size(); i++) {
                                HealthPlanTaskListBean bean=list.get(i);

                            HealthPlanTaskListBean formartBean=new HealthPlanTaskListBean();
                            formartBean.setTask_describe("第"+(i+1)+"次随访任务");

                                formartBean.setPlan_id(bean.getPlan_id());
                            formartBean.setTask_id(bean.getTask_id());
                            formartBean.setExec_time(bean.getExec_time());
                            formartBean.setExec_flag(bean.getExec_flag());
                            List<TaskInfoDTO> formartTaskInfo=new ArrayList<>();

                            for (TaskInfoDTO task:bean.getTaskInfo()) {
                                boolean added=false;
                                for (TaskInfoDTO formartTask:formartTaskInfo) {

                                    if (task.getPlanType().equals(formartTask.getPlanType())){
                                        //相同就组合在一起
                                        formartTask.setPlanDescribe(formartTask.getPlanDescribe()+"、"+task.getPlanDescribe());
                                        added=true;
                                        break;
                                    }
                                }
                                if (!added){
                                    formartTaskInfo.add(task);
                                }

                            }
                            formartBean.setTaskInfo(formartTaskInfo);
                            formartList.add(formartBean);
                        }

                        callback.onSuccess(formartList, 1000);
                    }
                } else {
                    callback.onFailedLog(result.getMessage(), 1001);
                }
            }
        }, error -> {
            callback.onFailedLog(error.getMessage(), 1001);
        });

    }
}
