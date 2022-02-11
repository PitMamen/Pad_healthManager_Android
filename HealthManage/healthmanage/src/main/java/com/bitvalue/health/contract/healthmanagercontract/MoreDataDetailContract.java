package com.bitvalue.health.contract.healthmanagercontract;

import com.bitvalue.health.api.requestbean.UserLocalVisitBean;
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
    }

    interface View extends IView {

        void qryUserVisitSuccess(List<TaskDetailBean> iamgeList);
        void qryUserVisitFail(String failMessage);

    }


    interface Presenter{

        void qryUserLocalVisit(UserLocalVisitBean visitBean);

    }

}
