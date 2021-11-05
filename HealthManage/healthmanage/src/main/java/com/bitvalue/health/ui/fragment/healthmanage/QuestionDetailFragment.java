package com.bitvalue.health.ui.fragment.healthmanage;

import android.content.Context;
import android.net.http.SslError;
import android.os.Build;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.requestbean.QuestionResultBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.fragment.chat.ChatFragment;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.DataUtil;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 健康问卷 详情界面  调用WebView
 */
public class QuestionDetailFragment extends BaseFragment {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_right_btn)
    TextView tv_right_btn;

    @BindView(R.id.webView)
    WebView webView;

    @BindView(R.id.progressBar1)
    ProgressBar progressBar;

    private HomeActivity homeActivity;
    private QuestionResultBean.ListDTO questionBean;
    private String url;
    private boolean is_loading_login_over;

    @OnClick({R.id.layout_back, R.id.tv_right_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_right_btn:
                ChatFragment.NewMsgData msgData = new ChatFragment.NewMsgData();
                msgData.msgType = Constants.MSG_SINGLE;
                msgData.userIds = new ArrayList<>();
                Map<String, List<String>> queryParams = DataUtil.getQueryParams(url);
                List<String> userIds = new ArrayList<>();
                if (null != queryParams) {
                    userIds = queryParams.get("userId");
                }
                if (null != userIds && userIds.size() > 0) {
                    msgData.userIds.add(userIds.get(0));
                }
                homeActivity.switchSecondFragment(Constants.FRAGMENT_HEALTH_ANALYSE, msgData);  //健康评估  暂时隐藏此控件
                //TODO 可能要处理发消息的逻辑
                break;
            case R.id.layout_back:
                if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    homeActivity.getSupportFragmentManager().popBackStack();
//                    http://192.168.1.122/s/8a755f7c24ad49c9a2be6e6f79c3ee60   //内网地址
//                    http://218.77.104.74:8008/s/8a755f7c24ad49c9a2be6e6f79c3ee60"
                }
                break;
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }

    @Override
    public void initView(View rootView) {

        questionBean = (QuestionResultBean.ListDTO) getArguments().getSerializable(Constants.QUESTION_DETAIL);
        //TODO 外网切内网
        if (null == questionBean.questUrl) {
            ToastUtil.toastShortMessage("问卷数据错误");
            return;
        }
//        url = questionBean.questUrl.replace("218.77.104.74:8008", "192.168.1.122");
        url = questionBean.questUrl;
        initWebView();

        if (url.contains("?userId=")) {
            tv_right_btn.setVisibility(View.VISIBLE);
        }
    }

    private void initWebView() {

        // 设置web模式
        webView.clearCache(true);
        webView.clearHistory();
        webView.clearFormData();
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//		webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setSupportZoom(true);// 设定支持缩放
        webView.getSettings().setUseWideViewPort(true);// 设定支持viewport
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setDisplayZoomControls(false);// 设置隐藏缩放按钮
        webView.getSettings().setDomStorageEnabled(true);

        /**
         * 让webview支持h5 localStorage 本地存储
         */
        webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = Application.instance().getCacheDir().getAbsolutePath();
        webView.getSettings().setAppCachePath(appCachePath);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);//TODO 可能要修改

            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                super.onReceivedSslError(view, handler, error);
                handler.proceed();
            }
        });

        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
            }

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
                // 判断当进度条为100时将其隐藏
                if (progressBar.getProgress() == 100) {
                    progressBar.setVisibility(View.GONE);
                    is_loading_login_over = true;
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (null == tv_title || null == title) {
                    return;
                }
                tv_title.setText(title);
            }
        });
    }


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_question_detail;
    }
}
