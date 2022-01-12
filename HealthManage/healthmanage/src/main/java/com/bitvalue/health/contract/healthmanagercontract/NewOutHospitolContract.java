package com.bitvalue.health.contract.healthmanagercontract;

import com.bitvalue.health.api.requestbean.RequestNewLeaveBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.callback.Callback;

import java.util.List;

/**
 * @author created by bitvalue
 * @data :
 */
public interface NewOutHospitolContract {
    public interface Model extends IModel {
        void getAllLeaveHospitolPatients(RequestNewLeaveBean requestNewLeaveBean, Callback callback);

        void qryPatientByName(RequestNewLeaveBean requestNewLeaveBean,Callback callback);
    }

    public interface View extends IView {

        void getAllLeaveHospitolPatientsSuccess(List<NewLeaveBean.RowsDTO> infoDetailDTOList);

        void getAllLeaveHospitolPatientsFail(String failMessage);


        //查询
        void qryPatientByNameSuccess(List<NewLeaveBean.RowsDTO> itinfoDetailDTOList);
        void qryPatientByNameFail(String failmessage);


    }

    public interface Presenter {

        void getAllLeaveHospitolPatients(RequestNewLeaveBean requestNewLeaveBean);


        void qryPatientByName(RequestNewLeaveBean requestNewLeaveBean);
    }
}
