package com.bitvalue.health.presenter.healthmanager;

import android.util.Log;

import com.bitvalue.health.api.requestbean.AllocatedPatientRequest;
import com.bitvalue.health.api.responsebean.ClientsResultBean;
import com.bitvalue.health.api.responsebean.InpatientBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.healthmanagercontract.PatientReportContract;
import com.bitvalue.health.model.healthmanagermodel.PatientReportModel;
import com.bitvalue.health.util.EmptyUtil;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author created by bitvalue
 * @data :
 */
public class PatientReportPresenter extends BasePresenter<PatientReportContract.View, PatientReportContract.Model> implements PatientReportContract.Presenter {
    @Override
    protected PatientReportContract.Model createModule() {
        return new PatientReportModel();
    }


    @Override
    public void qryAllocatedPatienList(AllocatedPatientRequest allocatedPatientRequest) {
        mModel.qryAllocatedPatienList(allocatedPatientRequest,new CallBackAdapter(){
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                if (isViewAttach()) {
                    getView().qryAllocatedPatienSuccess((List<NewLeaveBean.RowsDTO>) o);
                }
            }

            @Override
            public void onFailedLog(String str, int what) {
                super.onFailedLog(str, what);
                if (isViewAttach()) {
                    getView().qryAllocatedPatienFail(str);
                }
            }
        });
    }

    @Override
    public void qryByNameAllocatedPatienList(AllocatedPatientRequest allocatedPatientRequest) {
        mModel.qryAllocatedPatienList(allocatedPatientRequest,new CallBackAdapter(){
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                if (isViewAttach()) {
                    getView().qryByNameAllocatedPatienListSuccess((List<NewLeaveBean.RowsDTO>) o);
                }
            }

            @Override
            public void onFailedLog(String str, int what) {
                super.onFailedLog(str, what);
                if (isViewAttach()) {
                    getView().qryByNameAllocatedPatienListFail(str);
                }
            }
        });
    }

    @Override
    public void qryUnregisterPatienList(AllocatedPatientRequest allocatedPatientRequest) {
        mModel.qryUnregisterPatienList(allocatedPatientRequest,new CallBackAdapter(){
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                if (isViewAttach()) {
                    getView().qryUnregisterSuccess((List<NewLeaveBean.RowsDTO>) o);
                }
            }

            @Override
            public void onFailedLog(String str, int what) {
                super.onFailedLog(str, what);
                if (isViewAttach()) {
                    getView().qryUnregisterFail(str);
                }
            }
        });
    }

    @Override
    public void getInpartientList(String depatmentID) {
          if (!EmptyUtil.isEmpty(depatmentID)){
              mModel.getInpartientList(depatmentID,new CallBackAdapter(){
                  @Override
                  public void onSuccess(Object o, int what) {
                      super.onSuccess(o, what);
                      if (isViewAttach()) {
                          getView().getInpartientListSuccess((List<InpatientBean>) o);
                      }
                  }

                  @Override
                  public void onFailedLog(String str, int what) {
                      super.onFailedLog(str, what);
                      if (isViewAttach()) {
                          getView().getInpartientListFail(str);
                      }
                  }
              });

          }
    }
}
