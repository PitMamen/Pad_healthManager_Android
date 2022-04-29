package com.bitvalue.health.ui.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bitvalue.health.api.requestbean.QuickReplyRequest;
import com.bitvalue.health.callback.OnItemClickCallback;
import com.bitvalue.healthmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author created by bitvalue
 * @data : 10/28
 */
public class QuickReplyAdapter extends BaseQuickAdapter<QuickReplyRequest, BaseViewHolder> {
    private OnItemClickCallback itemClick;
    private List<QuickReplyRequest> currentData;

    private Context mContext;

    public void setOnClicListner(OnItemClickCallback callback) {
        this.itemClick = callback;
    }


    public QuickReplyAdapter(int layoutResId, @Nullable List<QuickReplyRequest> data, Context context) {
        super(layoutResId, data);
        if (null == data) {
            currentData = new ArrayList<>();
        } else {
            currentData = data;
        }
//        mContext = context;
//        initPopupWindow();
    }


    private TextView copyTv, deleteTv;
    private PopupWindow popupWindow;

    /**
     * 初始化Popupwindow
     *
     * @param
     */
    private void initPopupWindow() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popwind_item_layout, null);
        popupWindow = new PopupWindow(view, 200, 100);//使用弹出窗口的形式显示复制，删除按钮
        copyTv = (TextView) view.findViewById(R.id.pop_copy_tv);
        deleteTv = (TextView) view.findViewById(R.id.pop_delete_tv);
    }

    /**
     * Popupwindow显示
     *
     * @param v
     */
    @SuppressWarnings("deprecation")
    private void showPop(View v) {
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());// 设置此项可点击Popupwindow外区域消失，注释则不消失

        // 设置出现位置
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY,
                location[0] + v.getWidth() / 2 - popupWindow.getWidth() / 2,
                location[1] - popupWindow.getHeight());
    }


    @Override
    protected void convert(BaseViewHolder holder, QuickReplyRequest replyBean) {
        if (null == replyBean) {
            return;
        }
        holder.setText(R.id.tv_quick_reply, replyBean.content);


//        holder.itemView.setOnLongClickListener(v -> {
//            showPop(v);
//            return true;
//        });

        holder.itemView.setOnClickListener(v -> {
            if (null != itemClick) {
                itemClick.onItemClick(replyBean);
            }
        });

    }
}
