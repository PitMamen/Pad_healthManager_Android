package com.bitvalue.health.contract.mytodolistcontact;

import com.bitvalue.health.api.requestbean.RequestNewLeaveBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.callback.Callback;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 01/10
 */
public interface MyToDoListContact {
    public interface MyToDoListView extends IView {

        // 查询所有患者 新接口
        void qryPatientListSuccess(List<NewLeaveBean.RowsDTO> infoDetailDTOList);
        void qryWitoutListSuccess(List<NewLeaveBean.RowsDTO> infoDetailDTOList);

        void qryPatientListFail(String messageFail);
        void qryWaitoutListFail(String messageFail);

    }


    public interface MyToDoListModel extends IModel {

        //获取所有患者列表
        void qryPatientList(RequestNewLeaveBean requestNewLeaveBean, Callback callback);
        void qryWaitoutList(RequestNewLeaveBean requestNewLeaveBean, Callback callback);

    }


    public interface MyToDoListPersenter {

//        需要获取 所有患者列表

        void qryPatientList(RequestNewLeaveBean requestNewLeaveBean);
        void qryWaitOotList(RequestNewLeaveBean requestNewLeaveBean);


    }
}
