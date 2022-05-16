package com.bitvalue.health.contract.healthmanagercontract;

import com.bitvalue.health.api.requestbean.CallRequest;
import com.bitvalue.health.api.requestbean.FinshMidRequestBean;
import com.bitvalue.health.api.requestbean.QuickReplyRequest;
import com.bitvalue.health.api.requestbean.SaveRightsUseBean;
import com.bitvalue.health.api.requestbean.UpdateRightsRequestTimeRequestBean;
import com.bitvalue.health.api.responsebean.CallResultBean;
import com.bitvalue.health.api.responsebean.DataReViewRecordResponse;
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


        void sendsummary_resultSuucess(DataReViewRecordResponse reViewRecordResponse);

        void sendsummary_resultFail(String messageFail);


        void getSummaryListSuucess(List<DataReViewRecordResponse> reViewRecordResponse);

        void getSummaryListFail(String messageFail);


        void saveCaseCommonWordsSuccess(String quickReplyResult);

        void saveCaseCommonWordsFail(String failMessage);

        void callSuccess(CallResultBean resultBean);

        void callFail(String failMessage);

        void queryRightsRecordSuccess(QueryRightsRecordBean queryRightsRecordBean);

        void queryRightsRecordFail(String faiMessage);

        void updateRightsRequestTimeSuccess(boolean isSuccess);

        void updateRightsRequestTimeFail(String failMessage);

        void qryRightsUserLogSuccess(List<DataReViewRecordResponse> listData);

        void qryRightsUserLogFail(String failMessage);
    }

    interface Model extends IModel {
        void saveRightsUseRecord(SaveRightsUseBean bean, Callback callback);


        void sendsummary_result(DataReViewRecordResponse request, Callback callback);


        void getsummary_resultList(String userId, Callback callback);


        void saveCaseCommonWords(QuickReplyRequest request, Callback callback);

        void callPhone(CallRequest callRequest, Callback callback);

        void queryRightsRecord(int pageNo, int pageSize, int rightsId, String userId, String id, Callback callback);

        void updateRightsRequestTime(UpdateRightsRequestTimeRequestBean requestTimeRequestBean, Callback callback);

        void qryRightsUserLog(String tradedId,String userId,Callback callback);
    }

    interface Presenter {
        //保存权益使用记录
        void saveRightsUseRecord(SaveRightsUseBean bean);


        //保存问诊小结
        void sendsummary_result(DataReViewRecordResponse request);


        //获取问诊小结
        void getsummary_resultList(String userId);


        //保存快捷用语
        void saveCaseCommonWords(QuickReplyRequest request);


        //拨打电话
        void callPhone(CallRequest callRequest);

        //查询权益使用记录
        void queryRightsRecord(int pageNo, int pageSize, int rightsId, String userId, String id);//权益使用记录


        //更新权益状态 医生确认接诊后调用
        void updateRightsRequestTime(UpdateRightsRequestTimeRequestBean requestTimeRequestBean);


        void qryRightsUserLog(String tradedId,String userId);

    }


}
