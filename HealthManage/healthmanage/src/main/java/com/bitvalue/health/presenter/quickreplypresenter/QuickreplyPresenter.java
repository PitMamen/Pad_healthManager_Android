package com.bitvalue.health.presenter.quickreplypresenter;

import com.bitvalue.health.api.requestbean.QuestionResultBean;
import com.bitvalue.health.api.requestbean.QuickReplyRequest;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.quickreply.QuickReplyContract;
import com.bitvalue.health.model.quickreplymodel.QuickreplyModel;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 04/28
 */
public class QuickreplyPresenter extends BasePresenter<QuickReplyContract.View, QuickReplyContract.Model> implements QuickReplyContract.Presenter {
    @Override
    protected QuickReplyContract.Model createModule() {
        return new QuickreplyModel();
    }
    @Override
    public void qryCaseCommonWords(QuickReplyRequest replyRequest) {
        if (mModel != null) {
            mModel.qryCaseCommonWords(replyRequest, new CallBackAdapter() {
                @Override
                public void onSuccess(Object o, int what) {
                    super.onSuccess(o, what);
                    if (isViewAttach()) {
                        getView().qryCaseCommonWordsSuccess((List<QuickReplyRequest>) o);
                    }
                }

                @Override
                public void onFailedLog(String str, int what) {
                    super.onFailedLog(str, what);
                    if (isViewAttach()) {
                        getView().qryCaseCommonWordsFail(str);
                    }
                }
            });
        }
    }

    @Override
    public void saveCaseCommonWords(QuickReplyRequest request) {
        if (mModel != null) {
            mModel.saveCaseCommonWords(request, new CallBackAdapter() {
                @Override
                public void onSuccess(Object o, int what) {
                    super.onSuccess(o, what);
                    if (isViewAttach()) {
                        getView().saveCaseCommonWordsSuccess((QuickReplyRequest) o);
                    }
                }

                @Override
                public void onFailedLog(String str, int what) {
                    super.onFailedLog(str, what);
                    if (isViewAttach()) {
                        getView().saveCaseCommonWordsFail(str);
                    }
                }
            });
        }
    }


}
