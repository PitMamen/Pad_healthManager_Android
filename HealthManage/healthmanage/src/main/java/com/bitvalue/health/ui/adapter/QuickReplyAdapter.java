package com.bitvalue.health.ui.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bitvalue.health.api.requestbean.QuickReplyRequest;
import com.bitvalue.health.callback.OnItemClickCallback;
import com.bitvalue.health.util.InputMethodUtils;
import com.bitvalue.healthmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author created by bitvalue
 * @data : 10/28
 */
public class QuickReplyAdapter extends BaseQuickAdapter<QuickReplyRequest, BaseViewHolder> {
    private OnItemClickCallback itemClick;
    private List<QuickReplyRequest> currentData;
    private OnPopwindClickLisener lisener;
    private Context mContext;


    public void setOnClicListner(OnItemClickCallback callback) {
        this.itemClick = callback;
    }

    public void setOnPopwindClickLisener(OnPopwindClickLisener onPopwindClickLisener) {
        this.lisener = onPopwindClickLisener;
    }


    public QuickReplyAdapter(int layoutResId, @Nullable List<QuickReplyRequest> data, Context context) {
        super(layoutResId, data);
        if (null == data) {
            currentData = new ArrayList<>();
        } else {
            currentData = data;
        }
        mContext = context;
        initPopupWindow();
    }


    @Override
    public void setNewData(@Nullable List<QuickReplyRequest> data) {
        currentData = data;
        super.setNewData(data);
    }

    private TextView editTv, deleteTv;
    private PopupWindow popupWindow;

    /**
     * 初始化Popupwindow
     *
     * @param
     */
    private void initPopupWindow() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popwind_item_layout, null);
        popupWindow = new PopupWindow(view, 200, 100);//使用弹出窗口的形式显示复制，删除按钮
        editTv = (TextView) view.findViewById(R.id.pop_edit_tv);
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
                (int) (location[0] + v.getWidth() / 2 - popupWindow.getWidth() / 0.5f),
                location[1] - popupWindow.getHeight());
    }


    @Override
    protected void convert(BaseViewHolder holder, QuickReplyRequest replyBean) {
        if (null == replyBean) {
            return;
        }
        holder.setText(R.id.tv_quick_reply, replyBean.content);
        EditText editText = holder.getView(R.id.tv_quick_reply);

        holder.itemView.setOnLongClickListener(v -> {
            showPop(v);
            editTv.setOnClickListener(v13 -> {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                editText.setFocusable(true);
                editText.requestFocus();
                editText.setFocusableInTouchMode(true);
                editText.setSelection(editText.getText().length());//光标显示在文字后面
                InputMethodUtils.showSoftInput(editText);
                //编辑
                editText.setOnEditorActionListener((v12, actionId, keyEvent) -> {
                    if (null != keyEvent && KeyEvent.KEYCODE_ENTER == keyEvent.getKeyCode()) {
                        switch (keyEvent.getAction()) {
                            case KeyEvent.ACTION_UP:
                                if (lisener != null) {
                                    replyBean.content = editText.getText().toString();
                                    lisener.onNewEditItem(replyBean);
                                }
                                editText.setFocusable(false);
                                InputMethodUtils.hideSoftInput(mContext);
                                return true;
                            default:
                                return true;
                        }
                    }
                    return false;
                });
            });

            //删除
            deleteTv.setOnClickListener(v1 -> {
                if (currentData != null && currentData.size() > 0) {
                    currentData.remove(replyBean);
                    if (lisener != null) {
                        lisener.onDeleteItem(replyBean);
                    }
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                    notifyDataSetChanged();
                }
            });
            return true;
        });

        holder.itemView.setOnClickListener(v -> {
            if (null != itemClick) {
                itemClick.onItemClick(replyBean);
            }
        });

    }


    public interface OnPopwindClickLisener {
        void onDeleteItem(QuickReplyRequest position);

        void onNewEditItem(QuickReplyRequest position);
    }


}
