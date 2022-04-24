package com.bitvalue.health.ui.fragment.healthmanage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.ApiResult;
import com.bitvalue.health.api.requestbean.GetArticleByIdApi;
import com.bitvalue.health.api.requestbean.QueryPlanDetailApi;
import com.bitvalue.health.api.responsebean.ArticleBean;
import com.bitvalue.health.api.responsebean.ArticleTypePlanResponseBean;
import com.bitvalue.health.api.responsebean.QuestResopnseBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.component.picture.imageEngine.impl.GlideEngine;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.blankj.utilcode.util.ImageUtils;
import com.bumptech.glide.Glide;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.DeflaterInputStream;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.http.HTTP;

/***
 * 文章预览界面  调用WeBView
 */
public class ArticleDetailFragment extends BaseFragment {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_keshi)
    TextView tv_keshi;
    @BindView(R.id.tv_bingzhong)
    TextView tv_bingzhong;
    @BindView(R.id.tv_zuozhe)
    TextView tv_author;
    @BindView(R.id.tv_time_create)
    TextView tv_creatTime;
    @BindView(R.id.tv_content_one)
    TextView tv_content1;

    @BindView(R.id.layout_back)
    LinearLayout back_layout;

    private HomeActivity homeActivity;
    private SecondHandler handler;

    @OnClick({R.id.layout_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    homeActivity.getSupportFragmentManager().popBackStack();
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
        Object object = getArguments().getSerializable(Constants.ARTICLE_DETAIL);
        handler = new SecondHandler(this);
        //如果传过来是 文章 详情实体 则 直接通过文章ID 查询详情
        if (object instanceof ArticleBean) {
            ArticleBean articleBean = (ArticleBean) object;
            getArticleDetail(articleBean.articleId);

            //如果是通过随访计划中 点击文章条目传过来的实体 则需要先获取到计划的详情 通过计划的文章详情拿到 文章的ID  再通过此文章ID 查询文章详情
        } else if (object instanceof QueryPlanDetailApi) {
            QueryPlanDetailApi queryPlanDetailApi = (QueryPlanDetailApi) object;
            getArticleByPlanDetail(queryPlanDetailApi);
        }


    }


    private void getArticleByPlanDetail(QueryPlanDetailApi request) {
        EasyHttp.get(this).api(request).request(new HttpCallback<ApiResult<ArticleTypePlanResponseBean>>((this)) {
            @Override
            public void onSucceed(ApiResult<ArticleTypePlanResponseBean> result) {
                super.onSucceed(result);
                if (!EmptyUtil.isEmpty(result)) {
                    if (result.getCode() == 0) {
                        if (result.getData() != null) {
                            int articleId = result.getData().articleId;
                            getArticleDetail(articleId);
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


    private void getArticleDetail(int id) {
        GetArticleByIdApi getArticleByIdApi = new GetArticleByIdApi();
        getArticleByIdApi.id = id;
        EasyHttp.get(this).api(getArticleByIdApi).request(new HttpCallback<ApiResult<ArticleBean>>(this) {
            @Override
            public void onSucceed(ApiResult<ArticleBean> result) {
                super.onSucceed(result);
                if (result.getCode() == 0) {
                    if (!EmptyUtil.isEmpty(result.getData())) {
                        ArticleBean articleBean = result.getData();
                        tv_title.setText(articleBean.title);
                        String title = articleBean.title;
                        String htmlText1 = articleBean.content;
                        String htmlText2 = articleBean.categoryName;
                        String htmlText3 = articleBean.categoryName;
                        tv_title.setText(title);
                        tv_keshi.setText("所属科室:" + htmlText2);
                        tv_bingzhong.setText("所属科室:" + htmlText3);
                        tv_author.setText("作者:" + articleBean.author);
                        tv_creatTime.setText("创建时间:" + articleBean.createTime);
                        setActivityContent(htmlText1, handler);
                    }
                }

            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
    }


    private void setActivityContent(final String activityContent, Handler handler) {
        new Thread(() -> {
            Html.ImageGetter imageGetter = source -> {
                Drawable drawable;
                drawable = getImageNetwork(source);
                if (drawable != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                } else if (drawable == null) {
                    return null;
                }
                return drawable;
            };
            CharSequence charSequence = Html.fromHtml(activityContent.trim(), imageGetter, null);
            Message ms = Message.obtain();
            ms.what = 1;
            ms.obj = charSequence;
            handler.sendMessage(ms);
        }).start();
    }


    public Drawable getImageNetwork(String imageUrl) {
        URL myFileUrl = null;
        Drawable drawable = null;
        try {
            myFileUrl = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            // 在这一步最好先将图片进行压缩，避免消耗内存过多
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            drawable = new BitmapDrawable(bitmap);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return drawable;
    }


    @Override
    public void initData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null)
            handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_article_detail_layout;
    }


    private static class SecondHandler extends Handler {

        private WeakReference<ArticleDetailFragment> mWeakReference;

        public SecondHandler(ArticleDetailFragment secondActivity) {
            mWeakReference = new WeakReference<>(secondActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            ArticleDetailFragment host = mWeakReference.get(); // 判断所在的 Activity 的引用是否被回收了
            if (host != null) {
                switch (msg.what) {
                    case 1:
                        CharSequence charSequence = (CharSequence) msg.obj;
                        if (charSequence != null) {
                            host.tv_content1.setText(charSequence);
                            host.tv_content1.setMovementMethod(LinkMovementMethod.getInstance());
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }


}
