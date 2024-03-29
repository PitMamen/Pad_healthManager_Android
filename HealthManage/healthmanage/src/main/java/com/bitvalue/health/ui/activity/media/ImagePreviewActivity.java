package com.bitvalue.health.ui.activity.media;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bitvalue.health.base.BaseActivity;
import com.bitvalue.health.base.BaseAdapter;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.ui.adapter.ImagePreviewAdapter;
import com.bitvalue.health.ui.adapter.RecyclerPagerAdapter;
import com.bitvalue.health.util.IntentKey;
import com.bitvalue.healthmanage.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 *    点击某张图片之后放大 查看大图的界面
 */
public final class ImagePreviewActivity extends BaseActivity
        implements ViewPager.OnPageChangeListener,
        BaseAdapter.OnItemClickListener {

    public static void start(Context context, String url) {
        ArrayList<String> images = new ArrayList<>(1);
        images.add(url);
        start(context, images);
    }

    public static void start(Context context, List<String> urls) {
        start(context, urls, 0);
    }

    public static void start(Context context, List<String> urls, int index) {
        if (urls == null || urls.isEmpty()) {
            return;
        }
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        if (urls.size() > 2500) {
            // 请注意：如果传输的数据量过大，会抛出此异常，并且这种异常是不能被捕获的
            // 所以当图片数量过多的时候，我们应当只显示一张，这种一般是手机图片过多导致的
            // 经过测试，传入 3121 张图片集合的时候会抛出此异常，所以保险值应当是 2500
            // android.os.TransactionTooLargeException: data parcel size 521984 bytes
            urls = Collections.singletonList(urls.get(index));
        }

        if (urls instanceof ArrayList) {
            intent.putExtra(IntentKey.IMAGE, (ArrayList<String>) urls);
        } else {
            intent.putExtra(IntentKey.IMAGE, new ArrayList<>(urls));
        }
        intent.putExtra(IntentKey.INDEX, index);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    private ViewPager mViewPager;
    private ImagePreviewAdapter mAdapter;

    /** 圆圈指示器 */
    private CircleIndicator mCircleIndicatorView;
    /** 文本指示器 */
    private TextView mTextIndicatorView;

    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.vp_image_preview_pager);
        mCircleIndicatorView = findViewById(R.id.ci_image_preview_indicator);
        mTextIndicatorView = findViewById(R.id.tv_image_preview_indicator);
    }

    @Override
    protected void initData() {
        ArrayList<String> images = getStringArrayList(IntentKey.IMAGE);
        if (images == null || images.isEmpty()) {
            finish();
            return;
        }
        mAdapter = new ImagePreviewAdapter(this);
        mAdapter.setData(images);
        mAdapter.setOnItemClickListener(this);
        mViewPager.setAdapter(new RecyclerPagerAdapter(mAdapter));
        if (images.size() != 1) {
            if (images.size() < 10) {
                // 如果是 10 张以内的图片，那么就显示圆圈指示器
                mCircleIndicatorView.setVisibility(View.VISIBLE);
                mCircleIndicatorView.setViewPager(mViewPager);
            } else {
                // 如果超过 10 张图片，那么就显示文字指示器
                mTextIndicatorView.setVisibility(View.VISIBLE);
                mViewPager.addOnPageChangeListener(this);
            }

            int index = getInt(IntentKey.INDEX);
            if (index < images.size()) {
                mViewPager.setCurrentItem(index);
                onPageSelected(index);
            }
        }
    }



    /**
     * {@link ViewPager.OnPageChangeListener}
     */

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @SuppressLint("SetTextI18n")
    @Override
    public void onPageSelected(int position) {
        mTextIndicatorView.setText((position + 1) + "/" + mAdapter.getItemCount());
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPager.removeOnPageChangeListener(this);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.image_preview_activity;
    }

    /**
     * {@link BaseAdapter.OnItemClickListener}
     * @param recyclerView      RecyclerView 对象
     * @param itemView          被点击的条目对象
     * @param position          被点击的条目位置
     */
    @Override
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
        // 单击图片退出当前的 Activity
        if (!isFinishing()) {
            finish();
        }
    }
}