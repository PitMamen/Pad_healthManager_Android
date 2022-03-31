package com.bitvalue.health.presenter.visitplanpersenter;

import com.bitvalue.health.api.requestbean.AllocatedPatientRequest;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.visitplancontact.VisitPlanContact;
import com.bitvalue.health.model.visitplanmodel.VisitPlanModel;

import java.util.List;

/**
 * @author created by bitvalue
 * @data :
 */
public class VisitPlanPersenter extends BasePresenter<VisitPlanContact.VisitPlanView, VisitPlanContact.VisitPlanModel> implements VisitPlanContact.VisitPlanPersenter {
    @Override
    protected VisitPlanContact.VisitPlanModel createModule() {
        return new VisitPlanModel();
    }

    @Override
    public void qryPatientList(AllocatedPatientRequest requestNewLeaveBean) {
        mModel.qryPatientList(requestNewLeaveBean, new CallBackAdapter() {
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                if (isViewAttach()) {
                    getView().qryPatientListSuccess((List<NewLeaveBean.RowsDTO>) o);
                }
            }

            @Override
            public void onFailedLog(String str, int what) {
                super.onFailedLog(str, what);
                if (isViewAttach()) {
                    getView().qryPatientListFail(str);
                }
            }
        });
    }

    @Override
    public void qryPatientByName(AllocatedPatientRequest requestNewLeaveBean) {
        mModel.qryPatientByName(requestNewLeaveBean, new CallBackAdapter() {
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

}
