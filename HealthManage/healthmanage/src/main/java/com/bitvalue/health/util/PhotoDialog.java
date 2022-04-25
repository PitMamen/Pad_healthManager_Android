package com.bitvalue.health.util;


import static com.bitvalue.health.util.Constants.TARGET_HEIGHT;
import static com.bitvalue.health.util.Constants.TARGET_WIDTH;
import static com.tencent.liteav.demo.beauty.utils.ResourceUtils.getResources;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bitvalue.health.Application;
import com.bitvalue.health.util.photopreview.PhotoView;
import com.bitvalue.healthmanage.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.Url;

/**
 * @author created by bitvalue
 * @data : 04/22
 */
public class PhotoDialog extends Dialog {

    private ViewPager viewPager;
    private Context mContext;
    private List<String> resource_list;
    private int index_position;
    private PagerAdapter pagerAdapter;


    public PhotoDialog(@NonNull Context context, List<String> stringList) {
        super(context);
        mContext = context;
        if (stringList == null) {
            resource_list = new ArrayList<>();
        } else {
            resource_list = stringList;
        }
    }

    public PhotoDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected PhotoDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_layout);
        initView();
        initAdapter();
    }

    private void initView() {
        viewPager = findViewById(R.id.pager);
        viewPager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));
    }


    private void initAdapter() {
        pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return resource_list != null && resource_list.size() > 0 ? resource_list.size() : 0;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container,  int position) {
                PhotoView photoView = new PhotoView(mContext);
                photoView.setImageResource(R.drawable.image_error_bg);
                photoView.enable();
                photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                Log.e("TAG", "ViewPagerPosition: " + position + " indePosition : " + index_position);
                Picasso.with(mContext).load(index_position >= 0 ? resource_list.get(index_position).trim() : resource_list.get(position).trim()).error(R.drawable.image_error_bg).resize(TARGET_WIDTH,TARGET_HEIGHT).onlyScaleDown().into(photoView);
                container.addView(photoView);
                index_position = -1;
//                position = index_position;

//                Observable.just(0).subscribeOn(Schedulers.io()).subscribe(r -> {
//                    Bitmap bitmap = Picasso.with(mContext).load(index_position >= 0 ? resource_list.get(index_position).trim() : resource_list.get(position).trim()).error(R.drawable.image_error_bg).get();
//                    if (bitmap != null) {
//                        Application.instance().getHomeActivity().runOnUiThread(() -> {
//                            index_position = -1;
//                            photoView.setImageBitmap(bitmap);
//                            container.addView(photoView);
//                        });
//                    }
//                });
                return photoView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        };

        viewPager.setAdapter(pagerAdapter);
    }

    public synchronized void updateData(List<String> data) {
        resource_list = data;
        if (pagerAdapter != null) {
            pagerAdapter.notifyDataSetChanged();
        }
    }


    public void onClickPosition(int position) {
        this.index_position = position;
        Log.e("TAG", "onClickPosition: " + position);
    }


}
