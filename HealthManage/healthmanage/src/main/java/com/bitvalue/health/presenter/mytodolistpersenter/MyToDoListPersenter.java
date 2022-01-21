package com.bitvalue.health.presenter.mytodolistpersenter;

import com.bitvalue.health.api.requestbean.RequestNewLeaveBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.mytodolistcontact.MyToDoListContact;
import com.bitvalue.health.model.mytodolist.MyToDoListModel;

import java.util.List;

/**
 * @author created by bitvalue
 * @data :
 */
public class MyToDoListPersenter extends BasePresenter<MyToDoListContact.MyToDoListView,MyToDoListContact.MyToDoListModel> implements MyToDoListContact.MyToDoListPersenter {
    @Override
    protected MyToDoListContact.MyToDoListModel createModule() {
        return new MyToDoListModel();
    }

    @Override
    public void qryPatientList(RequestNewLeaveBean requestNewLeaveBean) {
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
    public void qryWaitOotList(RequestNewLeaveBean requestNewLeaveBean) {

    }
}
