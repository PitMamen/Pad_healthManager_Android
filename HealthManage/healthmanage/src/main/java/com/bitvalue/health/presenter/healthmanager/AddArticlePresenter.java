package com.bitvalue.health.presenter.healthmanager;

import com.bitvalue.health.api.responsebean.ArticleBean;
import com.bitvalue.health.api.responsebean.SearchArticleResult;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.healthmanagercontract.AddArticleContract;
import com.bitvalue.health.model.healthmanagermodel.AddArticleModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author created by bitvalue
 * @data :
 */
public class AddArticlePresenter extends BasePresenter<AddArticleContract.View, AddArticleContract.Model> implements AddArticleContract.Presenter {
    @Override
    protected AddArticleContract.Model createModule() {
        return new AddArticleModel();
    }

    @Override
    public void getUsefulArticle(int pageSize, int startPage, String deptCode) {
        mModel.getUsefulArticle(pageSize, startPage, deptCode, new CallBackAdapter() {
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                if (isViewAttach()) {
                    getView().getArticleSuccess((List<ArticleBean>) o);
                }
            }

            @Override
            public void onFailedLog(String str, int what) {
                super.onFailedLog(str, what);
                if (isViewAttach()) {
                    getView().getArticleFaile(str);
                }
            }
        });
    }

    @Override
    public void qryArticleByTitle(int pageSize, int start, String title) {
        mModel.qryArticleByTitle(pageSize, start, title, new CallBackAdapter() {
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                if (isViewAttach()) {
                    getView().qryArticleByTitleSuccess((SearchArticleResult) o);
                }
            }


            @Override
            public void onFailedLog(String str, int what) {
                super.onFailedLog(str, what);
                if (isViewAttach()) {
                    getView().getArticleFaile(str);
                }


            }
        });


    }
}
