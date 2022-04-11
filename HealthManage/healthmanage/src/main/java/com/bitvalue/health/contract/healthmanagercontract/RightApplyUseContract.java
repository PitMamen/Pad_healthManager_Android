package com.bitvalue.health.contract.healthmanagercontract;

import com.bitvalue.health.api.requestbean.DocListBean;
import com.bitvalue.health.api.requestbean.FinshMidRequestBean;
import com.bitvalue.health.api.responsebean.MyRightBean;
import com.bitvalue.health.api.responsebean.QueryRightsRecordBean;
import com.bitvalue.health.api.responsebean.SaveAnalyseApi;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.callback.Callback;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 02/21
 */
public interface RightApplyUseContract {

    interface View extends IView {
        void getMyRightSuccess(List<MyRightBean> myRightBeanList);

        void getMyRightFail(String faiMessage);


        void queryRightsRecordSuccess(QueryRightsRecordBean queryRightsRecordBean);
        void queryRightsRecordFail(String faiMessage);

        void finishMidRequestSuccess(FinshMidRequestBean finshMidRequestBean);
        void finishMidRequestFail(String messageFail);

        void getDocListSuccess(List<DocListBean> docListBeans);
        void getDocListFail(String  faileMessage);

    }

    interface Model extends IModel {
        void getMyRight(int goodsId, String id, String userId, Callback callback);


        void queryRightsRecord(int pageNo,int pageSize,int rightsId,String userId,Callback callback);

        void finishMidRequest(FinshMidRequestBean finshMidRequestBean,Callback callback);

        void getDocList(String departId,Callback callback);
    }

    interface Presenter {

        void getMyRight(int goodsId, String id, String userId);  //  商品ID  权益ID  用户ID
        void queryRightsRecord(int pageNo,int pageSize,int rightsId,String userId);

        void finishMidRequest(FinshMidRequestBean finshMidRequestBean);

        void getDocList(String departId);

    }


}
