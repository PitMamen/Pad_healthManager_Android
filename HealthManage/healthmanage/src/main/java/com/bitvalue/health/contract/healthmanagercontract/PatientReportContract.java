package com.bitvalue.health.contract.healthmanagercontract;

import com.bitvalue.health.api.requestbean.AllocatedPatientRequest;
import com.bitvalue.health.api.responsebean.ClientsResultBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.callback.Callback;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author created by bitvalue
 * @data :
 */
public interface PatientReportContract {


    interface View extends IView {

        void qryAllocatedPatienSuccess(List<NewLeaveBean.RowsDTO> infoDetailDTOList);
        void qryAllocatedPatienFail(String failMessage);


        void qryUnregisterSuccess(List<NewLeaveBean.RowsDTO> infoDetailDTOList);
        void qryUnregisterFail(String messageFail);


        void qryByNameAllocatedPatienListSuccess(List<NewLeaveBean.RowsDTO> infoDetailDTOList);
        void qryByNameAllocatedPatienListFail(String failMessage);


    }


    interface Model extends IModel {
        void qryAllocatedPatienList(AllocatedPatientRequest allocatedPatientRequest,Callback callback);
        void qryByNameAllocatedPatienList(AllocatedPatientRequest allocatedPatientRequest,Callback callback);
        void qryUnregisterPatienList(AllocatedPatientRequest allocatedPatientRequest,Callback callback);
    }


    interface Presenter {

        void qryAllocatedPatienList(AllocatedPatientRequest allocatedPatientRequest);
        void qryByNameAllocatedPatienList(AllocatedPatientRequest allocatedPatientRequest);
        void qryUnregisterPatienList(AllocatedPatientRequest allocatedPatientRequest);

    }
}
