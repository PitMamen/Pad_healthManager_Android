package com.bitvalue.health.contract.settingcontract;

import com.bitvalue.health.api.requestbean.QuestionResultBean;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.callback.Callback;

/**
 * @author created by bitvalue
 * @data :
 */
public interface AddQuestionContract {
    interface Model extends IModel {
        void qryQuestByKeyWord(int pageSize, int start, String keyWord, Callback callback);
    }

    interface View extends IView {
        void qryQuestByKeyWordSuccess(QuestionResultBean questionResultBean);
        void qryQuestByKeyWordFail(String faileMessage);
    }

    interface Presenter {

        void qryQuestByKeyWord(int pageSize,int start,String keyWord);
    }
}
