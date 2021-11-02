package com.bitvalue.health.base.presenter;


import com.bitvalue.health.base.view.IView;

/**
 * @author pxk on 2021/10/24
 */
public interface IPresenter<T extends IView> {
    /**
     * 绑定view
     *
     * @param view view
     */
    void attachView(T view);

    /**
     * 分离view
     */
    void detachView();

    /**
     * 判断view是否已经销毁
     *
     * @return true 未销毁
     */
    boolean isViewAttach();

}
