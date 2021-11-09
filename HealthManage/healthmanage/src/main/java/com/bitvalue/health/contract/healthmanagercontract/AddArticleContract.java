package com.bitvalue.health.contract.healthmanagercontract;

import com.bitvalue.health.api.responsebean.ArticleBean;
import com.bitvalue.health.api.responsebean.SearchArticleResult;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.callback.Callback;

import java.util.ArrayList;

/**
 * @author created by bitvalue
 * @data :
 */
public interface AddArticleContract {
    interface Model extends IModel {
        void getUsefulArticle(int count, Callback callback);  //获取文章
        void qryArticleByTitle(int pageSize,int start,String title,Callback callback);
    }

    interface View extends IView {
        void getArticleSuccess(ArrayList<ArticleBean> articleBeanArrayList);

        void qryArticleByTitleSuccess(SearchArticleResult searchArticleResult);

        void getArticleFaile(String messageFail);

    }

    interface Presenter {

        void getUsefulArticle(int count);  //获取文章
        void qryArticleByTitle(int pageSize,int start,String title); //根据查询标题获取文章
    }
}
