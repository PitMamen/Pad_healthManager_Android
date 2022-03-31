package com.bitvalue.health.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bitvalue.health.api.responsebean.AnswerResultBean;
import com.bitvalue.healthmanage.R;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 03/26
 */
public class DialogItemAnswerAdapter extends BaseAdapter {
    List<AnswerResultBean.RecordsDTO> recordsDTOList;
    LayoutInflater inflater;

    public DialogItemAnswerAdapter(Context context, List<AnswerResultBean.RecordsDTO> list) {
        recordsDTOList = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return recordsDTOList != null && recordsDTOList.size() > 0 ? recordsDTOList.size() : 0;
    }

    @Override
    public AnswerResultBean.RecordsDTO getItem(int position) {
        return recordsDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DialogItemAnswerAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new DialogItemAnswerAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.dialog_item, null);
            holder.typeTextview = (TextView) convertView.findViewById(R.id.tv_content_name);
            holder.tv_bestnewModify = (TextView) convertView.findViewById(R.id.tv_bestnewmodify);
            convertView.setTag(holder);
        } else {
            holder = (DialogItemAnswerAdapter.ViewHolder) convertView.getTag();
        }
        holder.typeTextview.setText(getItem(position).getName());
        holder.tv_bestnewModify.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
        return convertView;
    }

    public static class ViewHolder {
        public TextView typeTextview;
        public TextView tv_bestnewModify;
    }
}
