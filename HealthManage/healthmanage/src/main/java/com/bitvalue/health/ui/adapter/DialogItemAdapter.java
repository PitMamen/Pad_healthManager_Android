package com.bitvalue.health.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bitvalue.health.api.requestbean.QuestionResultBean;
import com.bitvalue.healthmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 03/24
 */
public class DialogItemAdapter extends BaseAdapter {
    public List<QuestionResultBean.ListDTO> list;
    LayoutInflater inflater;

    public DialogItemAdapter(Context context, List<QuestionResultBean.ListDTO> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list != null && list.size() > 0 ? list.size() : 0;
    }

    @Override
    public QuestionResultBean.ListDTO getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.dialog_item, null);
//            convertView.setLayoutParams(new ViewGroup.LayoutParams(200,200));
            holder.typeTextview = (TextView) convertView.findViewById(R.id.tv_content_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.typeTextview.setText(getItem(position).name);
        return convertView;
    }


    public static class ViewHolder {
        public TextView typeTextview;
    }
}