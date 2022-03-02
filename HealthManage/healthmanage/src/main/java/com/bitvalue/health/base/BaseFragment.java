package com.bitvalue.health.base;

import static com.hjq.http.EasyUtils.postDelayed;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import com.bitvalue.health.api.ApiResult;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.util.BaseDialog;
import com.bitvalue.health.callback.KeyboardAction;
import com.bitvalue.health.util.WaitDialog;
import com.hjq.http.listener.OnHttpListener;

import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * user：lqm
 * desc：BaseFragment
 */

public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements IView, OnHttpListener<Object>, KeyboardAction {
    protected String TAG = this.getClass().getSimpleName();
    protected T mPresenter;
    protected Activity activity;
    private BaseDialog mDialog;
    private int mDialogTotal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        //判断是否使用MVP模式
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);//因为之后所有的子类都要实现对应的View接口
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(provideContentViewId(), container, false);
        ButterKnife.bind(this, rootView);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }

    public void init() {

    }

    public void initView(View rootView) {
    }

    public void initData() {

    }

    public void initListener() {

    }

    /**
     * 当前加载对话框是否在显示中
     */
    public boolean isShowDialog() {
        return mDialog != null && mDialog.isShowing();
    }

    @Override
    public void showLoading() {
        mDialogTotal++;
        postDelayed(() -> {
            if (mDialogTotal <= 0 || isDetached() || isHidden()) {
                return;
            }

            if (mDialog == null) {
                mDialog = new WaitDialog.Builder(activity)
                        .setCancelable(false)
                        .create();
            }
            if (!mDialog.isShowing()) {
                mDialog.show();
            }
        }, 300);
    }

    /**
     * 隐藏加载对话框
     */
    public void hideDialog() {
        if (mDialogTotal > 0) {
            mDialogTotal--;
        }

        if (mDialogTotal == 0 && mDialog != null && mDialog.isShowing() && !isHidden()) {
            mDialog.dismiss();
        }
    }


    @Override
    public void dismissLoading() {

    }


    @Override
    public void onFail(Throwable ex, String code, String msg) {

        dismissLoading();
    }


    @Override
    public void onNetError() {
        dismissLoading();
    }


    @Override
    public void onStart(Call call) {
        showLoading();
    }

    @Override
    public void onSucceed(Object result) {
        if (result instanceof ApiResult) {
        }
    }

    @Override
    public void onFail(Exception e) {
    }

    @Override
    public void onEnd(Call call) {
        hideDialog();
    }


    //用于创建Presenter和判断是否使用MVP模式(由子类实现)
    protected abstract T createPresenter();

    //得到当前界面的布局文件id(由子类实现)
    protected abstract int provideContentViewId();
}
