package com.bitvalue.sdk.collab.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.bitvalue.sdk.collab.R;
import com.bitvalue.sdk.collab.utils.ScreenUtil;


public class UnreadCountTextView extends AppCompatTextView {

    private int mNormalSize = ScreenUtil.getPxByDp(13.4f);

    private Paint roundRectPaint;
    private RectF roundRectF;
    private int height;
    private int roundWidth;

    private Paint textPaint;
    private float textSize;
    private String text;
    private PaintFlagsDrawFilter paintFlagsDrawFilter;

    public UnreadCountTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UnreadCountTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        roundRectPaint = new Paint();
        roundRectPaint.setStyle(Paint.Style.FILL);
        roundRectPaint.setAntiAlias(true);
        roundRectPaint.setColor(Color.RED);
        roundRectF = new RectF();
        roundRectPaint.setAntiAlias(true);
        //设置字体为粗体
        textPaint = new Paint(Paint.LINEAR_TEXT_FLAG);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        textPaint.setTextAlign(Paint.Align.CENTER);
        //实现抗锯齿
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取控件高度
        height = MeasureSpec.getSize(heightMeasureSpec);
        //定义字体大小，自测了一下，与高度相差6dp的字体大小看着还是挺舒服的
        textSize = height - ScreenUtil.getPxByDp(4.4f);
        textPaint.setTextSize(textSize);
        //获取文本宽度
        int textWidth = (int) textPaint.measureText(text);
        //区分画圆的是圆形还是圆角矩形
        if (text.length() > 1) {
            roundWidth = textWidth + height - textWidth / text.length();
        } else {
            roundWidth = height;
        }
        //重新测量控件
        setMeasuredDimension(roundWidth, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画矩形
        canvas.setDrawFilter(paintFlagsDrawFilter);
        roundRectF.set(0, 0, roundWidth*0.8f, height*0.8f);
        canvas.drawRoundRect(roundRectF, height / 2, height / 2, roundRectPaint);
        //画字
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        int baseLintY = (int) (roundRectF.centerY() - top / 2 - bottom / 2);
        canvas.drawText(text, roundRectF.centerX(), baseLintY, textPaint);
    }

    public void setText(String text) {
        this.text = text;
        //重走onMeasure
        requestLayout();
    }


//    private Paint mPaint;
//
//    public UnreadCountTextView(Context context) {
//        super(context);
//        init();
//    }
//
//    public UnreadCountTextView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init();
//    }
//
//    public UnreadCountTextView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init();
//    }
//
//    private void init() {
//        mPaint = new Paint();
//        mPaint.setColor(getResources().getColor(R.color.read_dot_bg));
//        mPaint.setAntiAlias(true);
//        setTextColor(Color.WHITE);
//        setTextSize(TypedValue.COMPLEX_UNIT_SP, 13.6f);
//    }
//
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        if (getText().length() == 0) {
//            // 没有字符，就在本View中心画一个小圆点
//            int l = (getMeasuredWidth() - ScreenUtil.getPxByDp(10)) / 2;
//            int t = l;
//            int r = getMeasuredWidth() - l;
//            int b = r;
//            canvas.drawOval(new RectF(l, t, r, b), mPaint);
//        } else if (getText().length() == 1) {
//            canvas.drawOval(new RectF(0, 0, mNormalSize, mNormalSize), mPaint);
//        } else if (getText().length() > 1) {
//            canvas.drawRoundRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()), getMeasuredHeight() / 2, getMeasuredHeight() / 2, mPaint);
//        }
//        super.onDraw(canvas);
//
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int width = mNormalSize;
//        int height = mNormalSize;
//        if (getText().length() > 1) {
//            width = mNormalSize + ScreenUtil.getPxByDp((getText().length() - 1) * 10);
//        }
//        setMeasuredDimension(width, height);
//    }
}
