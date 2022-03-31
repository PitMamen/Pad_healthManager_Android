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
    @SuppressLint("CheckResult")
    @Override
    public void queryTaskDetail(int planID, int taskId, String userId, Callback callback) {
        mApi.queryTaskDetail(planID, taskId, userId).subscribeOn(Schedulers.io())
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
    public void queryhealtPlan(String planID, Callback callback) {
        mApi.queryHealthPlanTaskList(planID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//主线程接收
                .subscribe(result -> {
                    if (!EmptyUtil.isEmpty(result)) {
                        if (result.getCode() == 0) {
                            if (!EmptyUtil.isEmpty(result.getData())) {
                                List<HealthPlanTaskListBean> list = result.getData();
                                List<HealthPlanTaskListBean> formartList = new ArrayList<>();
                                for (int i = 0; i < list.size(); i++) {
                                    HealthPlanTaskListBean bean = list.get(i);

                                    HealthPlanTaskListBean formartBean = new HealthPlanTaskListBean();
                                    formartBean.setTask_describe("第" + (i + 1) + "次随访任务");

                                    formartBean.setUser_id(bean.getUser_id());
                                    formartBean.setPlan_id(bean.getPlan_id());
                                    formartBean.setPlan_name(bean.getPlan_name());
                                    formartBean.setTask_id(bean.getTask_id());
                                    formartBean.setExec_time(bean.getExec_time());

                                    List<TaskInfoDTO> formartTaskInfo = new ArrayList<>();

                                    boolean allTaskFinished = true;
                                    for (TaskInfoDTO task : bean.getTaskInfo()) {
                                        if (bean.getExec_flag() == 0) {
                                            //只要有一个没完成 整体任务就是进行中
                                            allTaskFinished = false;
                                        }
                                        boolean added = false;
                                        for (TaskInfoDTO formartTask : formartTaskInfo) {

                                            if (task.getPlanType().equals(formartTask.getPlanType())) {
                                                //相同就组合在一起
                                                formartTask.setFormartPlanDescribe(formartTask.getFormartPlanDescribe() + "、" + task.getPlanDescribe());
                                                added = true;
                                                break;
                                            }
                                        }
                                        if (!added) {
                                            task.setFormartPlanDescribe(task.getPlanDescribe());
                                            formartTaskInfo.add(task);
                                        }

                                    }

                                    //状态标识 0等待开启 1进行中 2已完成
                                    long l = System.currentTimeMillis() - bean.getExec_time();
                                    if (l < 0) {
                                        //等待开启
                                        formartBean.setExec_flag(0);
                                    } else {
                                        formartBean.setExec_flag(allTaskFinished ? 2 : 1);
                                    }


                                    formartBean.setFormartTaskInfo(formartTaskInfo);
                                    formartBean.setTaskInfo(bean.getTaskInfo());
                                    formartList.add(formartBean);
                                }

                                callback.onSuccess(formartList, 1000);
                            }else {
                                callback.onSuccess(result.getData(), 1000);

                            }
                        } else {
                            callback.onFailedLog("请求失败", 1001);
                        }
                    }
                }, error -> {
                    callback.onFailedLog("请求出错!", 1001);
                });

    }
}
