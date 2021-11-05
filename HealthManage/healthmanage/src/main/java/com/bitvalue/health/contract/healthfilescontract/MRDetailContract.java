package com.bitvalue.health.contract.healthfilescontract;


import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.model.healthfilesmodel.MRDetailRequestApi;

public class MRDetailContract {
    public interface View extends BaseView<Presenter> {
        /**
         * @param sourceEntityJsonStr 病历详情的json字符串，解密后的明文
         */
        void refreshMRDetail(String sourceEntityJsonStr,String medicalMainStr);

        void showLoadDialog();
        void dismissLoadDialog();
        void showTips(String msg);
    }


    public interface Presenter extends BasePresenter<IView, IModel> {
        void getMRDetail(MRDetailRequestApi mrDetailRequestApi);
    }
}
