package com.bitvalue.health.presenter.healthmanager;

import com.bitvalue.health.api.responsebean.TaskDetailBean;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.healthmanagercontract.HealthUploadDataContract;
import com.bitvalue.health.model.healthmanagermodel.HealthUploadDataModel;

/**
 * @author created by bitvalue
 * @data :
 */
public class HealthUploadDataPresenter extends BasePresenter<HealthUploadDataContract.View,HealthUploadDataContract.Model> implements HealthUploadDataContract.Presenter {
    @Override
    protected HealthUploadDataContract.Model createModule() {
        return new HealthUploadDataModel();
    }

    @Override
    public void queryHealthPlanContent(String contentId, String planType, String userId) {
   mModel.queryHealthPlanContent(contentId,planType,userId,new CallBackAdapter(){
       @Override
       public void onSuccess(Object o, int what) {
           super.onSuccess(o, what);
           if (isViewAttach()){
               getView().querySuccess((TaskDetailBean) o);
           }
       }

       @Override
       public void onFailedLog(String str, int what) {
           super.onFailedLog(str, what);

           if (isViewAttach()){
               getView().queryFail(str);
           }
       }
   });

    }
}
