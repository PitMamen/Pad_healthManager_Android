package com.bitvalue.health.contract.healthmanagercontract;

import com.bitvalue.health.api.responsebean.ArticleBean;
import com.bitvalue.health.api.responsebean.SearchArticleResult;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.callback.Callback;

import java.util.ArrayList;
import java.util.List;

/**
 * @author created by bitvalue
 * @data :
 */
public interface AddArticleContract {
    interface Model extends IModel {
        void getUsefulArticle(int pageSize,int startPage,String deptCode, Callback callback);  //获取文章
        void qryArticleByTitle(int pageSize,int start,String title,Callback callback);
    }

    interface View extends IView {
        void getArticleSuccess(List<ArticleBean> articleBeanArrayList);

        void qryArticleByTitleSuccess(SearchArticleResult searchArticleResult);

        void getArticleFaile(String messageFail);

    }

    interface Presenter {

        void getUsefulArticle(int pageSize,int startPage,String deptCode);  //获取所属科室下的文章
        void qryArticleByTitle(int pageSize,int start,String title); //根据查询标题获取文章
    }
}
