package com.bitvalue.health.presenter.healthmanager;

import com.bitvalue.health.api.requestbean.RequestNewLeaveBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.healthmanagercontract.NewOutHospitolContract;
import com.bitvalue.health.model.healthmanagermodel.NewOutHospitolModel;

import java.util.List;

/**
 * @author created by bitvalue
 * @data :
 */
public class NewOutHospitolPresenter extends BasePresenter<NewOutHospitolContract.View,NewOutHospitolContract.Model> implements NewOutHospitolContract.Presenter {

    @Override
    public void getAllLeaveHospitolPatients(RequestNewLeaveBean requestNewLeaveBean) {
        mModel.getAllLeaveHospitolPatients(requestNewLeaveBean, new CallBackAdapter() {
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                if (isViewAttach()) {
                    getView().getAllLeaveHospitolPatientsSuccess((List<NewLeaveBean.RowsDTO>) o);
                }
            }

            @Override
            public void onFailedLog(String str, int what) {
                super.onFailedLog(str, what);
                if (isViewAttach()) {
                    getView().getAllLeaveHospitolPatientsFail(str);
                }
            }
        });
    }



    /**
     * 搜索患者
     * @param
     */
    @Override
    public void qryPatientByName(RequestNewLeaveBean bean) {
        mModel.qryPatientByName(bean,new CallBackAdapter(){
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                if (isViewAttach()) {
                    getView().qryPatientByNameSuccess((List<NewLeaveBean.RowsDTO>) o);
                }
            }

            @Override
            public void onFailedLog(String str, int what) {
                super.onFailedLog(str, what);
                if (isViewAttach()) {
                    getView().qryPatientByNameFail(str);
                }
            }
        });
    }






    @Override
    protected NewOutHospitolContract.Model createModule() {
        return new NewOutHospitolModel();
    }
}
