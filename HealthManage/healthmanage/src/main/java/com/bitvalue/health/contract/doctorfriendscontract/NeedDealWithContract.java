package com.bitvalue.health.contract.doctorfriendscontract;

import com.bitvalue.health.api.responsebean.TaskDeatailBean;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.callback.Callback;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMMessage;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 10/28
 */
public class NeedDealWithContract {


    public interface NeedDealWithView extends IView {
        void getMyTaskDetailSuccess(List<TaskDeatailBean> taskDeatailBeanList);
        void getMyTaskDetailFail(String failMessage);

        void getMyAlreadyDealTaskDetailSuccess(List<TaskDeatailBean> taskDeatailBeanList);
        void getMyAlreadyDealTaskDetailFail(String failMessage);
    }


    public interface NeedDealWithModel extends IModel {
        void getMyTaskDetail(int execFlag, int taskType, String docUserId,Callback callback);
        void getMyAlreadyDealTaskDetail(int execFlag, int taskType, String docUserId,Callback callback);
    }


    public interface NeedDealWithPersent {
        void getMyTaskDetail(int execFlag, int taskType, String docUserId);
        void getMyAlreadyDealTaskDetail(int execFlag, int taskType, String docUserId);

    }
}
