package com.bitvalue.health.contract.chattract;

import com.bitvalue.health.api.requestbean.ReportStatusBean;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.callback.Callback;

/**
 * @author created by bitvalue
 * @data : 10/29
 */
public class Chatcontract {

//    "/health-api/medical/doctor/updateAttendanceStatus"


    public interface ChatIvew extends IView{
        void  updateAttendanceStausSuccess();

        void updateFail(String FailMessage);
    }



    public interface ChatModel extends IModel {

    void updateAttendanceStaus(ReportStatusBean bean, Callback callback);

    }


    public interface ChatPersenter{
        void updateAttendanceStatus(ReportStatusBean bean);
    }
}
