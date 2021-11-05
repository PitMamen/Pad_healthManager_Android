package com.bitvalue.health.util.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitvalue.healthmanage.R;

/**
 * Created by Hanqing on 2015/12/26.
 */
public class IndicatorView extends LinearLayout {

    TextView mTitle;

    public IndicatorView(Context context) {
        super(context);
        init();
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView);
        String text = a.getString(R.styleable.IndicatorView_textxxx);
        mTitle.setText(text);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.indicator_title_bar, this);
        mTitle = (TextView) view.findViewById(R.id.tv_title);
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }
}
