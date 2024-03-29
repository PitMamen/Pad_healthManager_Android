package com.bitvalue.health.ui.fragment.healthmanage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.http.SslError;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.ApiResult;
import com.bitvalue.health.api.requestbean.QueryPlanDetailApi;
import com.bitvalue.health.api.requestbean.QuestionResultBean;
import com.bitvalue.health.api.responsebean.QuestResopnseBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.fragment.chat.ChatFragment;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.DataUtil;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.healthmanage.R;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;

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

    @BindView(R.id.layout_back)
    LinearLayout back_layout;


    private HomeActivity homeActivity;
    private QuestionResultBean.ListDTO questionBean;
    private QueryPlanDetailApi questbean;
    private String url;
    private boolean is_loading_login_over;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }

    @Override
    public void initView(View rootView) {
        back_layout.setVisibility(View.VISIBLE);

        Object object = getArguments().getSerializable(Constants.QUESTION_DETAIL);
        if (object instanceof QuestionResultBean.ListDTO) {
            questionBean = (QuestionResultBean.ListDTO) object;
        } else if (object instanceof QueryPlanDetailApi) {
            questbean = (QueryPlanDetailApi) object;
        }
        initDataRecevie();
    }


    private void initDataRecevie() {
        //传过来是个URL 直接显示
        if (questionBean != null && !EmptyUtil.isEmpty(questionBean.questUrl)) {
            url = questionBean.questUrl;
            if (url.contains("?userId=")) {
                tv_right_btn.setVisibility(View.VISIBLE);
            } else {
                url = url + "?showsubmitbtn=hide";
            }
            Log.e(TAG, "问卷URL: " + url);
            initWebView(url);
            //传过来是个实体 需要请求问卷详情
        } else if (questbean != null) {
            //请求 问卷详情
            EasyHttp.get(this).api(questbean).request(new HttpCallback<ApiResult<QuestResopnseBean>>((this)) {
                @Override
                public void onSucceed(ApiResult<QuestResopnseBean> result) {
                    super.onSucceed(result);
                    if (!EmptyUtil.isEmpty(result)) {
                        if (result.getCode() == 0) {
                            if (result.getData() != null) {
                                String url = result.getData().questUrl + "?userId=" + questbean.userId;  // /r需加Userid
                                Log.e(TAG, "问卷URL: " + url);
                                initWebView(url);
                            }
                        }
                    }
                }

                @Override
                public void onFail(Exception e) {
                    super.onFail(e);
                }
            });
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView(String url) {

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

            @SuppressLint("WebViewClientOnReceivedSslError")
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                super.onReceivedSslError(view, handler, error);
                handler.proceed();
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
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
                }
                break;
        }
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
