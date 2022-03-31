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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.responsebean.ArticleBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.util.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.hjq.toast.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 文章预览界面  调用WeBView
 */
public class ArticleDetailFragment extends BaseFragment {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.layout_back)
    LinearLayout back_layout;

    private HomeActivity homeActivity;
    private ArticleBean articleBean;
    private WebView webView;
    private ProgressBar progressBar;
    private String url;
    private boolean is_loading_login_over;

    @OnClick({R.id.layout_back})
    public void onClick(View view) {
        switch (view.getId()) {
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
        back_layout.setVisibility(View.VISIBLE);
        articleBean = (ArticleBean) getArguments().getSerializable(Constants.ARTICLE_DETAIL);
        if (articleBean == null && articleBean.previewUrl.isEmpty()) {
            ToastUtil.toastShortMessage("文章数据错误");
            return;
        }
        tv_title.setText(articleBean.title);
        url = articleBean.previewUrl;
        initWebView(rootView);
    }

    private void initWebView(View view) {
        progressBar = view.findViewById(R.id.progressBar1);
        webView = view.findViewById(R.id.webView);
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
//        webView.loadDataWithBaseURL(null, articleBean.content, "text/html", "UTF-8", null);

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
                if (!title.isEmpty()) {
//                    tv_title.setText(title);
                }
            }
        });
    }


    @Override
    public void initData() {

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
