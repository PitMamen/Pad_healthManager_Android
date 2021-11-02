package com.bitvalue.health.contract.healthmanagercontract;

import com.bitvalue.health.api.responsebean.SaveAnalyseApi;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.callback.Callback;

/**
 * @author created by bitvalue
 * @data :
 */
public interface HealthAnalyseDisplayContract {
    interface Model extends IModel {
        void getUserEevaluate(int id, Callback callback);
        void commitAnalse(SaveAnalyseApi requestBean, Callback callback);

    }

    interface View extends IView {
        void getUserEevaluateSuccess(SaveAnalyseApi EevaluateBean);
        void getUserEevaluateFail(String messageFail);

        void commitSuccess(SaveAnalyseApi resultBean);
        void commitFail(String  failmessage);

    }

    interface Presenter {

        void getUserEevaluate(int id);

        void commitAnalse(SaveAnalyseApi commitBody);
    }
}
