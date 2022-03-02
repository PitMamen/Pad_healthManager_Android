package com.bitvalue.health.presenter.docfriendpersenter;

import com.bitvalue.health.api.responsebean.TaskDeatailBean;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.doctorfriendscontract.NeedDealWithContract;
import com.bitvalue.health.model.docfriendmodel.NeedDealWithModel;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 10/28
 */
public class DocFrienPersenter extends BasePresenter<NeedDealWithContract.NeedDealWithView, NeedDealWithContract.NeedDealWithModel> implements NeedDealWithContract.NeedDealWithPersent {
    @Override
    protected NeedDealWithContract.NeedDealWithModel createModule() {
        return new NeedDealWithModel();
    }

    @Override
    public void getMyTaskDetail(int execFlag, int taskType, String docUserId) {
        mModel.getMyTaskDetail(execFlag, taskType, docUserId, new CallBackAdapter() {
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                if (isViewAttach()) {
                    getView().getMyTaskDetailSuccess((List<TaskDeatailBean>) o);
                }
            }


            @Override
            public void onFailedLog(String str, int what) {
                super.onFailedLog(str, what);
                if (isViewAttach()) {
                    getView().getMyTaskDetailFail(str);
                }
            }
        });

    }

    @Override
    public void getMyAlreadyDealTaskDetail(int execFlag, int taskType, String docUserId) {
        mModel.getMyAlreadyDealTaskDetail(execFlag, taskType, docUserId, new CallBackAdapter() {
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                if (isViewAttach()) {
                    getView().getMyAlreadyDealTaskDetailSuccess((List<TaskDeatailBean>) o);
                }
            }


            @Override
            public void onFailedLog(String str, int what) {
                super.onFailedLog(str, what);
                if (isViewAttach()) {
                    getView().getMyAlreadyDealTaskDetailFail(str);
                }
            }
        });
    }
}
