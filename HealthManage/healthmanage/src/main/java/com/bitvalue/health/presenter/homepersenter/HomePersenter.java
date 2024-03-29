package com.bitvalue.health.presenter.homepersenter;

import com.bitvalue.health.api.responsebean.CheckNewVersionBean;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.homecontract.HomeContract;
import com.bitvalue.health.model.homemodel.HomeModel;

/**
 * @author created by bitvalue
 * @data : 10/27
 */
public class HomePersenter extends BasePresenter<HomeContract.TUIKitView,HomeContract.TUIKitModel> implements HomeContract.TUIKitPersenter {
    @Override
    protected HomeContract.TUIKitModel createModule() {
        return new HomeModel();
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
                if (isViewAttach())
                    getView().LoginFail(module,code,desc);
            }
        });
    }


    @Override
    public void checkNewAppVersion() {
        if (mModel != null) {
            mModel.checkNewAppVersion(new CallBackAdapter() {
                @Override
                public void onSuccess(Object o, int what) {
                    super.onSuccess(o, what);
                    if (isViewAttach()) {
                        getView().checkNewAppVersionSuccess((CheckNewVersionBean) o);
                    }
                }

                @Override
                public void onFailedLog(String str, int what) {
                    super.onFailedLog(str, what);
                    if (isViewAttach()) {
                        getView().checkNewAppVersionFail(str);
                    }
                }
            });
        }
    }
}
