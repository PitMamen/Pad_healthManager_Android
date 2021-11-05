package com.bitvalue.health.presenter.settingpresenter;

import com.bitvalue.health.api.requestbean.QuestionResultBean;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.settingcontract.AddQuestionContract;
import com.bitvalue.health.model.settingmodel.AddQuestionModel;

/**
 * @author created by bitvalue
 * @data :
 */
public class AddQuestionPresenter extends BasePresenter<AddQuestionContract.View, AddQuestionContract.Model> implements AddQuestionContract.Presenter {
    @Override
    protected AddQuestionModel createModule() {
        return new AddQuestionModel();
    }

    @Override
    public void qryQuestByKeyWord(int pageSize, int start, String keyWord) {
        mModel.qryQuestByKeyWord(pageSize, start, keyWord, new CallBackAdapter() {
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                if (isViewAttach()) {
                    getView().qryQuestByKeyWordSuccess((QuestionResultBean) o);
                }

            }

            @Override
            public void onFailedLog(String str, int what) {
                super.onFailedLog(str, what);
                if (isViewAttach()) {
                    getView().qryQuestByKeyWordFail(str);
                }


            }
        });
    }
}
