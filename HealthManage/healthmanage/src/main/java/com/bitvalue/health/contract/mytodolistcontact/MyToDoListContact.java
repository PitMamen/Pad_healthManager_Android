package com.bitvalue.health.contract.mytodolistcontact;

import com.bitvalue.health.api.requestbean.AllocatedPatientRequest;
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

        void qryPatientListFail(String messageFail);

        void qryPatientByNameSuccess(List<NewLeaveBean.RowsDTO> itinfoDetailDTOList);
        void qryPatientByNameFail(String failmessage);

    }


    public interface MyToDoListModel extends IModel {

        //获取所有患者列表
        void qryPatientList(AllocatedPatientRequest requestNewLeaveBean, Callback callback);
        //根据名字搜索患者
        void qryPatientByName(AllocatedPatientRequest requestNewLeaveBean,Callback callback);

    }


    public interface MyToDoListPersenter {

//        需要获取 所有患者列表

        void qryPatientList(AllocatedPatientRequest requestNewLeaveBean);
        //根据关键字 查询患者
        void qryPatientByName(AllocatedPatientRequest requestNewLeaveBean);

    }
}
