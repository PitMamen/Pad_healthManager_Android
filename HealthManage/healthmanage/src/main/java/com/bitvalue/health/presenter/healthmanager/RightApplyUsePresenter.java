package com.bitvalue.health.presenter.healthmanager;

import android.util.Log;

import com.bitvalue.health.api.requestbean.DocListBean;
import com.bitvalue.health.api.requestbean.FinshMidRequestBean;
import com.bitvalue.health.api.responsebean.DataReViewRecordResponse;
import com.bitvalue.health.api.responsebean.MyRightBean;
import com.bitvalue.health.api.responsebean.QueryRightsRecordBean;
import com.bitvalue.health.api.responsebean.SaveAnalyseApi;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.healthmanagercontract.RightApplyUseContract;
import com.bitvalue.health.model.healthmanagermodel.RightApplyUseModel;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 02/21
 */
public class RightApplyUsePresenter extends BasePresenter<RightApplyUseContract.View, RightApplyUseContract.Model> implements RightApplyUseContract.Presenter {
    @Override
    protected RightApplyUseContract.Model createModule() {
        return new RightApplyUseModel();
    }

    @Override
    public void getMyRight(int goodsId, String id, String userId) {
        mModel.getMyRight(goodsId, id, userId, new CallBackAdapter() {
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                if (isViewAttach()) {
                    getView().getMyRightSuccess((List<MyRightBean>) o);
                }
            }

            @Override
            public void onFailedLog(String str, int what) {
                super.onFailedLog(str, what);
                if (isViewAttach()) {
                    getView().getMyRightFail(str);
                }

            }
        });
    }

    @Override
    public void queryRightsRecord(int pageNo, int pageSize, int rightsId, String userId) {
        mModel.queryRightsRecord(pageNo, pageSize, rightsId, userId, new CallBackAdapter() {
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                if (isViewAttach()) {
                    getView().queryRightsRecordSuccess((QueryRightsRecordBean) o);
                }
            }

            @Override
            public void onFailedLog(String str, int what) {
                super.onFailedLog(str, what);
                if (isViewAttach()) {
                    getView().queryRightsRecordFail(str);
                }

            }
        });

    }

    @Override
    public void finishMidRequest(FinshMidRequestBean finshMidRequestBean) {
        mModel.finishMidRequest(finshMidRequestBean, new CallBackAdapter() {
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                if (isViewAttach()) {
                    getView().finishMidRequestSuccess((FinshMidRequestBean) o);
                }
            }


            @Override
            public void onFailedLog(String str, int what) {
                super.onFailedLog(str, what);
                if (isViewAttach()) {
                    getView().finishMidRequestFail(str);
                }
            }
        });

    }

    @Override
    public void getDocList(String departId) {
        if (mModel!=null){
            mModel.getDocList(departId,new CallBackAdapter(){
                @Override
                public void onSuccess(Object o, int what) {
                    super.onSuccess(o, what);
                    if (isViewAttach()){
                        getView().getDocListSuccess((List<DocListBean>) o);
                    }
                }


                @Override
                public void onFailedLog(String str, int what) {
                    super.onFailedLog(str, what);
                    if (isViewAttach()){
                        getView().getDocListFail(str);
                    }
                }
            });
        }
    }

    @Override
    public void getDataReviewRecord(String tradedID, String userID) {
        if (mModel != null) {
            mModel.getDataReviewRecord(tradedID, userID, new CallBackAdapter() {
                @Override
                public void onSuccess(Object o, int what) {
                    super.onSuccess(o, what);
                    if (isViewAttach()) {
                        getView().getDataReviewRecordSuccess((List<DataReViewRecordResponse>) o);
                    }
                }

                @Override
                public void onFailedLog(String str, int what) {
                    super.onFailedLog(str, what);
                    if (isViewAttach()) {
                        getView().getDataReviewRecordFail(str);
                    }
                }
            });
        }
    }

}
