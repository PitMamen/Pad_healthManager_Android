package com.bitvalue.health.presenter.homepersenter;

import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.imcontract.TencentIMContract;
import com.bitvalue.health.model.homemodel.TencentIMModel;

/**
 * @author created by bitvalue
 * @data : 11:54
 */
public class TencentIMPersenter extends BasePresenter<TencentIMContract.TUIKitView,TencentIMContract.TUIKitModel> implements TencentIMContract.TUIKitPersenter {
    @Override
    protected TencentIMContract.TUIKitModel createModule() {
        return new TencentIMModel();
    }

    @Override
    public void IMLogin(String userid, String usersig) {
    mModel.IMLogin(userid,usersig,new CallBackAdapter(){
        @Override
        public void IMloginSuccess(Object o) {
            super.IMloginSuccess(o);
            if (isViewAttach())
                getView().LoginSuccess(o);
        }


        @Override
        public void IMloginFail(String module, int code, String desc) {
            super.IMloginFail(module, code, desc);
            getView().LoginFail(module,code,desc);
        }
    });

    }
}
