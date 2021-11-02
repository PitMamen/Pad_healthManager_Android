package com.bitvalue.health.util.recyclerView;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.util.DensityUtil;


public class GridDividerItemDecoration extends RecyclerView.ItemDecoration {
    Context context;
    public GridDividerItemDecoration(Context context) {
      this.context=context;
    }

    @Override
    public void getItemOffsets(Rect outRect, View child, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, child, parent, state);

        int pos = parent.getChildAdapterPosition(child);
        int column = (pos) % 3+1;// 计算这个child 处于第几列

        outRect.top = 0;
        outRect.bottom = DensityUtil.dip2px(context, 31);
        //注意这里一定要先乘 后除  先除数因为小于1然后强转int后会为0
        outRect.left = (column-1) * DensityUtil.dip2px(context, 42) / 3; //左侧为(当前条目数-1)/总条目数*divider宽度
        outRect.right = (3-column)* DensityUtil.dip2px(context, 42) /3 ;//右侧为(总条目数-当前条目数)/总条目数*divider宽度
    }

}