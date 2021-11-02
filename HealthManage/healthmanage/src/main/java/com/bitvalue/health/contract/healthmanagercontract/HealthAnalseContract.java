package com.bitvalue.health.contract.healthmanagercontract;

import com.bitvalue.health.api.responsebean.SaveAnalyseApi;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.callback.Callback;

/**
 * @author created by bitvalue
 * @data :
 */
public interface HealthAnalseContract {
    interface View extends IView {
        void commitSuccess(SaveAnalyseApi resultBean);
        void commitFail(String faiMessage);

    }

    interface Model extends IModel {
        void commitAnalse(SaveAnalyseApi requestBean, Callback callback);
    }



    interface Presenter {
        void commitAnalse(SaveAnalyseApi commitBody);
    }
}
