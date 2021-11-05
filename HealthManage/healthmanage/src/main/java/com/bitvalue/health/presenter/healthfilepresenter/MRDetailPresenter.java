package com.bitvalue.health.presenter.healthfilepresenter;

import android.content.Context;

import com.bitvalue.health.Application;
import com.bitvalue.health.contract.healthfilescontract.MRDetailContract;
import com.bitvalue.health.model.healthfilesmodel.MRDetailRequestApi;


public class MRDetailPresenter implements MRDetailContract.Presenter {
    private MRDetailContract.View view;
//    private RetrofitClient mClient;
    private Context mContext;

    public MRDetailPresenter(MRDetailContract.View view) {
        this.view = view;
        mContext = Application.instance();
//        mClient = RetrofitClient.getInstance(CommonConfig.GetLoginBaseUrl());
    }

    @Override
    public void start() {

    }

    @Override
    public void getMRDetail(MRDetailRequestApi mrDetailRequestApi) {
        //TODO 获取数据流程要改
//        LogUtils.e("=病例详情查看接口reqestBody===="+ GsonUtils.getGson().toJson(reqestBody));
//        view.showLoadDialog();
//        mClient.toService(MRApi.class)
//                .queryMRForSearch(reqestBody)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new DisposableObserver<MRResponse<MRDetailResponseBean>>() {
//                    @Override
//                    public void onNext(MRResponse<MRDetailResponseBean> response) {
//                        Log.d( "wl","病例详情查看接口出参==="+GsonUtils.getGson().toJson(response));
//                        if (response.getReturnCode() == 0) {
//                            String sourceEntityJsonStr = null;
//                            String medicalMainStr=null;
//                            try {
//                                if(response.getData().getResultList()!=null&&response.getData().getResultList().size()>0){
//                                    String result = response.getData().getResultList().get(0);
//                                  //  String mrModelStr = Sha1withRSAUtil.decode_4_pfx_p12(result); //只有一个病历，取第一个即可
//                                    String mrModelStr = Sha1withRSAUtil.decrypt(result);
//                                    // 取出 medicalRecords 病历记录数组，传给病历列表和病历详情去解析
//                                    JSONObject mrModeJsonObject = new JSONObject(mrModelStr);
//                                    JSONObject medicalRecordJsonObject = mrModeJsonObject.getJSONObject("medicalRecord");
//                                    //以字符串的方式获取每个记录，用于后面的对象（门诊或者住院）的转换
//                                    sourceEntityJsonStr = medicalRecordJsonObject.getString("source");
//                                    medicalMainStr = medicalRecordJsonObject.getString("medicalMain");
//                                }
//                            } catch (Exception e) {
//                                view.showTips("病历数据解析失败，请检查证书文件是否正确");
//                                e.printStackTrace();
//                            }
//                            view.refreshMRDetail(sourceEntityJsonStr,medicalMainStr);
//                        } else if (response.getReturnCode() == 210001) {
//                            view.showTips("用户没有查询权限，私有云未授权或不在授权期内");
//                        } else if("User not logged in.".equals(response.getMessage())){
//                            view.showTips("登录失效，请重新登录我的账户APP");
//                        }else {
//                            view.showTips(response.getMessage());
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e("wl","接口请求错误 ："+e.getMessage());
//                        view.dismissLoadDialog();
//                        if("timeout".equals(e.getMessage())){
//                            view.showTips("网络超时！！");
//                        }else {
//                            view.showTips(e.getMessage());
//                        }
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        view.dismissLoadDialog();
//                    }
//                });
    }
}
