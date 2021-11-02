package com.bitvalue.health.util.recyclerView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private int mDividerHeight = 1;//分割线高度，默认为1px
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    boolean showTop = true;

    /**
     * 默认分割线：高度为2px，颜色为灰色
     *
     * @param context
     */
    public DividerItemDecoration(Context context) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    /**
     * 自定义分割线
     *
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     */
    public DividerItemDecoration(int dividerHeight, int dividerColor) {
        mDividerHeight = dividerHeight;
        mDivider = new ColorDrawable(dividerColor);
    }

    public DividerItemDecoration(int dividerHeight, int dividerColor, boolean showTop) {
        this(dividerHeight, dividerColor);
        this.showTop = showTop;
    }


    //获取分割线尺寸
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            //竖直方向的
            if (((GridLayoutManager) layoutManager).getOrientation() == LinearLayoutManager.VERTICAL) {
                outRect.left = 0;
                outRect.right = 0;
                if (showTop && parent.getChildAdapterPosition(view) < ((GridLayoutManager) layoutManager).getSpanCount())
                    outRect.top = mDividerHeight;
                outRect.bottom = mDividerHeight;
            } else {
                //最后一项需要right
                if (parent.getChildAdapterPosition(view) % ((GridLayoutManager) layoutManager).getSpanCount() == 1) {
                    outRect.right = mDividerHeight;
                }
                outRect.top = mDividerHeight;
                outRect.left = mDividerHeight;
                outRect.bottom = mDividerHeight;
            }
        } else if (layoutManager instanceof LinearLayoutManager) {
            //竖直方向的
            if (((LinearLayoutManager) layoutManager).getOrientation() == LinearLayoutManager.VERTICAL) {
                outRect.left = 0;
                outRect.right = 0;
                if (showTop && parent.getChildAdapterPosition(view) == 0)
                    outRect.top = mDividerHeight;
                outRect.bottom = mDividerHeight;
            } else {
                //最后一项需要right
                if (parent.getChildAdapterPosition(view) == layoutManager.getItemCount() - 1) {
                    outRect.right = mDividerHeight;
                }
                outRect.top = mDividerHeight;
                outRect.left = mDividerHeight;
                outRect.bottom = mDividerHeight;
            }
        }

    }

    //绘制分割线
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        //没有子view或者没有没有颜色直接return
        if (mDivider == null || layoutManager.getChildCount() == 0) {
            return;
        }
        int left;
        int right;
        int top;
        int bottom;
        final int childCount = parent.getChildCount();
        if (layoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                //计算下边的
                left = layoutManager.getLeftDecorationWidth(child);
                right = parent.getWidth() - layoutManager.getLeftDecorationWidth(child);
                top = child.getBottom() + params.bottomMargin;
                bottom = top + mDividerHeight;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
                if (showTop && i == 0) {
                    left = layoutManager.getLeftDecorationWidth(child);
                    right = parent.getWidth() - layoutManager.getLeftDecorationWidth(child);
                    top = child.getTop() - params.topMargin - mDividerHeight;
                    bottom = child.getTop();
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }
            }
        } else {
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                //计算右边的
                left = child.getRight() + params.rightMargin;
                right = left + mDividerHeight;
                top = layoutManager.getTopDecorationHeight(child);
                bottom = parent.getHeight() - layoutManager.getTopDecorationHeight(child);
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

}