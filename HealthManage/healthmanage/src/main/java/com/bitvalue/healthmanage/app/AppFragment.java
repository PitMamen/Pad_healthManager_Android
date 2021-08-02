package com.bitvalue.healthmanage.app;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bitvalue.healthmanage.action.ToastAction;
import com.bitvalue.healthmanage.http.model.HttpData;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.hjq.base.BaseFragment;
import com.hjq.http.listener.OnHttpListener;

import okhttp3.Call;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/10/18
 *    desc   : 业务 Fragment 基类
 */
public abstract class AppFragment<A extends AppActivity> extends BaseFragment<A>
        implements ToastAction, OnHttpListener<Object> {

    private HomeActivity homeActivity;

    public void forward(HomeActivity homeActivity, Fragment fragment, boolean hide) {
        this.homeActivity = homeActivity;
        forward(getId(), fragment, null, hide);
    }

    public void forward(int viewId, Fragment fragment, String name, boolean hide) {
        if (homeActivity.getSupportFragmentManager() == null){
            return;
        }
        FragmentTransaction trans = homeActivity.getSupportFragmentManager().beginTransaction();
        if (hide) {
            trans.hide(this);
            trans.add(viewId, fragment);
        } else {
            trans.replace(viewId, fragment);
        }

        trans.addToBackStack(name);
        trans.commitAllowingStateLoss();
    }

    public void backward() {
        if (homeActivity.getSupportFragmentManager() == null){
            return;
        }
        homeActivity.getSupportFragmentManager().popBackStack();
    }

    /**
     * 当前加载对话框是否在显示中
     */
    public boolean isShowDialog() {
        A activity = getAttachActivity();
        if (activity != null) {
            return activity.isShowDialog();
        }

        return false;
    }

    /**
     * 显示加载对话框
     */
    public void showDialog() {
        A activity = getAttachActivity();
        if (activity != null) {
            activity.showDialog();
        }
    }

    /**
     * 隐藏加载对话框
     */
    public void hideDialog() {
        A activity = getAttachActivity();
        if (activity != null) {
            activity.hideDialog();
        }
    }

    /**
     * {@link OnHttpListener}
     */

    @Override
    public void onStart(Call call) {
        showDialog();
    }

    @Override
    public void onSucceed(Object result) {
        if (result instanceof HttpData) {
            toast(((HttpData<?>) result).getMessage());
        }
    }

    @Override
    public void onFail(Exception e) {
        if (!e.getMessage().isEmpty()){
            toast(e.getMessage());
        }
    }

    @Override
    public void onEnd(Call call) {
        hideDialog();
    }
}