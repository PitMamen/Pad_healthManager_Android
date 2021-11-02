package com.bitvalue.health.presenter.cloudclinicpersenter;

import android.util.Log;

import com.bitvalue.health.api.responsebean.VideoClientsResultBean;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.cloudcliniccontract.CloudClinicContract;
import com.bitvalue.health.model.cloudclinicmodel.CloudClinicModel;
import com.bitvalue.health.util.EmptyUtil;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMMessage;

import java.util.ArrayList;

/**
 * @author created by bitvalue
 * @data : 10/28
 */
public class CloudClinicPersenter extends BasePresenter<CloudClinicContract.CloudClinicView,CloudClinicContract.CloudClinicModel> implements  CloudClinicContract.CloudClinicPersent {
    @Override
    protected CloudClinicContract.CloudClinicModel createModule() {
        return new CloudClinicModel() {
        };
    }

    @Override
    public void getConversationList(long nextSeq, int count) {
        mModel.getConversationList(nextSeq, count, new CallBackAdapter() {
            @Override
            public void loginV2TIMSuccess(V2TIMConversationResult v2TIMConversationResult) {
                super.loginV2TIMSuccess(v2TIMConversationResult);
                Log.e(TAG, "loginV2TIMSuccess:CloudClinicPersenter" );
                if (isViewAttach() && null != v2TIMConversationResult)
                    getView().getConversationSuccess(v2TIMConversationResult);
            }


            @Override
            public void loginV2TIMFail(int code, String desc) {
                super.loginV2TIMFail(code, desc);
                if (isViewAttach())
                    getView().getConversationfail(code, desc);
            }
        });
    }

    @Override
    public void listennerIMNewMessage() {
        Log.e(TAG, "listennerIMNewMessage: "+mModel );
        mModel.listenerIMNewMessage(new CallBackAdapter() {
            @Override
            public void IMgetNewMessage(V2TIMMessage v2TIMMessage) {
                super.IMgetNewMessage(v2TIMMessage);
                if (isViewAttach())
                    if (!EmptyUtil.isEmpty(v2TIMMessage)) {
                        getView().onNewMessage(v2TIMMessage);
                    }
            }
        });
    }

    @Override
    public void qryMedicalPatients(String attendanceStatus) {
      mModel.qryMedicalPatients(attendanceStatus,new CallBackAdapter(){
          @Override
          public void onSuccess(Object o, int what) {
              super.onSuccess(o, what);
              if (isViewAttach()){
                  ArrayList<VideoClientsResultBean> videoClientsResultBeans = (ArrayList<VideoClientsResultBean>) o;
                  if (!EmptyUtil.isEmpty(videoClientsResultBeans))
                  getView().qryMedicalPatientsSuccess(videoClientsResultBeans);
                  else
                      getView().qryMedicalPatientsNotData();
              }
          }

          @Override
          public void onFailedLog(String str, int what) {
              super.onFailedLog(str, what);
              if (isViewAttach())
                  getView().qryMedicalPatientsFail(str);

          }


          @Override
          public void onError(String erromessge, int what) {
              super.onError(erromessge, what);
              if (isViewAttach())
              getView().qryMedicalPatientsFail(erromessge);
          }
      });
    }
}
