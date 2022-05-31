package com.bitvalue.health.contract.healthmanagercontract;

import com.bitvalue.health.api.responsebean.ArticleBean;
import com.bitvalue.health.api.responsebean.CheckNewVersionBean;
import com.bitvalue.health.api.responsebean.SearchArticleResult;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.callback.Callback;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 05/25
 */
public interface AppUpdateContract {

    interface Model extends IModel {
        void getAppDownUrl(String id, Callback callback);

        void checkNewAppVersion(Callback callback);
    }

    interface View extends IView {

        void getAppDownUrlSuccess(String url);

        void getAppDownUrlFaile(String messageFail);


        void checkNewAppVersionSuccess(CheckNewVersionBean newVersionBean);

        void checkNewAppVersionFail(String failMessage);


    }

    interface Presenter {

        void getAppDownUrl(String id);

        void checkNewAppVersion();

    }
}
