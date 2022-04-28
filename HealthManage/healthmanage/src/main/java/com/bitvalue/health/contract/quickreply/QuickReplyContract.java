package com.bitvalue.health.contract.quickreply;

import com.bitvalue.health.api.requestbean.QuestionResultBean;
import com.bitvalue.health.api.requestbean.QuickReplyRequest;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.callback.Callback;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 04/28
 */
public interface QuickReplyContract {

    interface Model extends IModel {
        void qryCaseCommonWords(QuickReplyRequest request, Callback callback);

        void saveCaseCommonWords(QuickReplyRequest request,Callback callback);
    }

    interface View extends IView {
        void qryCaseCommonWordsSuccess(List<QuickReplyRequest> questionResultBean);

        void qryCaseCommonWordsFail(String faileMessage);


        void saveCaseCommonWordsSuccess(QuickReplyRequest quickReplyResult);
        void saveCaseCommonWordsFail(String failMessage);
    }

    interface Presenter {

        void qryCaseCommonWords(QuickReplyRequest replyRequest);


        void saveCaseCommonWords(QuickReplyRequest request);

    }

}
