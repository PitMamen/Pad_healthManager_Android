package com.bitvalue.health.util;

import static com.tencent.liteav.demo.beauty.utils.ResourceUtils.getResources;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

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
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return resource_list != null && resource_list.size() > 0 ? resource_list.size() : 0;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                PhotoView view = new PhotoView(mContext);
                view.enable();
                view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Picasso.with(Application.instance()).load(resource_list.get(index_position)).error(R.drawable.image_error_bg).into(view);
                index_position = position;

                Log.e("TAG", "index_position: " + index_position + "  currentPosition: " + position);

                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
    }



    public void onClickPosition(int position) {
        this.index_position = position;
    }


}
