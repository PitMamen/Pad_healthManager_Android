package com.bitvalue.health.contract.healthmanagercontract;

import com.bitvalue.health.api.requestbean.FinshMidRequestBean;
import com.bitvalue.health.api.requestbean.SaveRightsUseBean;
import com.bitvalue.health.api.responsebean.MyRightBean;
import com.bitvalue.health.api.responsebean.QueryRightsRecordBean;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.callback.Callback;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 02/22
 */
public interface InterestsUseApplyByDocContract {


    interface View extends IView {
        void saveRightsUseRecordSuccess(SaveRightsUseBean bean);

        void saveRightsUseRecordFail(String failMessage);


    }

    interface Model extends IModel {
        void saveRightsUseRecord(SaveRightsUseBean bean, Callback callback);
    }

    interface Presenter {
        void saveRightsUseRecord(SaveRightsUseBean bean);
    }


}
