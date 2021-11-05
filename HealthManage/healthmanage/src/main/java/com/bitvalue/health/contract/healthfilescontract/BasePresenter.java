package com.bitvalue.health.contract.healthfilescontract;

import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.base.view.IView;

/**
 * Created by LvJianXing on 2017/7/4 19:10:57.
 */

public interface BasePresenter<V extends IView, M extends IModel> {

    void start();

}
