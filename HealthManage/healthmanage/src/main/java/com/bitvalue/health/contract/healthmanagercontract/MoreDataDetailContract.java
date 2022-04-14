package com.bitvalue.health.contract.healthmanagercontract;

import com.bitvalue.health.api.requestbean.UserLocalVisitBean;
import com.bitvalue.health.api.responsebean.DataReViewRecordResponse;
import com.bitvalue.health.api.responsebean.TaskDetailBean;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.callback.Callback;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 02/11
 */
public interface MoreDataDetailContract {

    interface Model extends IModel {

        void qryUserLocalVisit(UserLocalVisitBean visitBean, Callback callback);

        void getDataReviewRecord(String tradedID,String userID,Callback callback);

        void saveDataReviewRecord(DataReViewRecordResponse request,Callback callback);
    }

    interface View extends IView {

        void qryUserVisitSuccess(List<TaskDetailBean> iamgeList);
        void qryUserVisitFail(String failMessage);



        void getDataReviewRecordSuccess(List<DataReViewRecordResponse> responseList);
        void getDataReviewRecordFail(String messageFail);


        void saveDataReviewRecordSuucess(DataReViewRecordResponse reViewRecordResponse);
        void saveDataReviewRecordFail(String messageFail);

    }


    interface Presenter{

        //查看患者上传资料
        void qryUserLocalVisit(UserLocalVisitBean visitBean);

        //获取审核记录
        void getDataReviewRecord(String tradedID,String userID);

        //保存审核资料
        void saveDataReviewRecord(DataReViewRecordResponse request);

    }

}
