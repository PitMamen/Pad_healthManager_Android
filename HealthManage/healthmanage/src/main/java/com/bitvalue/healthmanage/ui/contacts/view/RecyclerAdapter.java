package com.bitvalue.healthmanage.ui.contacts.view;

import android.app.Activity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppApplication;
import com.bitvalue.healthmanage.http.glide.GlideApp;
import com.bitvalue.healthmanage.ui.contacts.bean.ContactBean;
import com.bitvalue.healthmanage.ui.contacts.bean.ContactsGroupBean;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.hjq.toast.ToastUtils;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

/**
 * 二级折叠列表，借鉴于
 * https://cloud.tencent.com/developer/article/1154841
 */
public class RecyclerAdapter extends ExpandableRecyclerViewAdapter<RecyclerAdapter.GroupContentViewHolder, RecyclerAdapter.ChildContentViewHolder> {

    private Activity activity;

    public RecyclerAdapter(Activity activity, List<? extends ExpandableGroup> groups) {
        super(groups);
        this.activity = activity;
    }

    @Override
    public GroupContentViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_trait_group, parent, false);
        return new GroupContentViewHolder(view);
    }

    @Override
    public ChildContentViewHolder onCreateChildViewHolder(ViewGroup parent, final int viewType) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_trait, parent, false);
        return new ChildContentViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(ChildContentViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final ContactBean child = ((ContactsGroupBean) group).getItems().get(childIndex);
        holder.onBind(child, group);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 跳转事件
                ToastUtils.show("点击了" + child.getName());
//                ToastUtil.toastLongMessage("点击了" + child.getName());
            }
        });
    }

    @Override
    public void onBindGroupViewHolder(GroupContentViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setGroupName(group.getTitle());
    }

    public static class GroupContentViewHolder extends GroupViewHolder {

        private TextView name;
        private ImageView iv_trait_group;

        public GroupContentViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_trait_group);
            iv_trait_group = (ImageView) itemView.findViewById(R.id.iv_trait_group);
        }

        @Override
        public void expand() {
//            name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right, 0);
            iv_trait_group.setImageResource(R.drawable.icon_left);
        }

        @Override
        public void collapse() {
//            name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrows_bottom_ic, 0);
            iv_trait_group.setImageResource(R.drawable.icon_down);
        }

        public void setGroupName(String title) {
            name.setText(title);
        }
    }

    public static class ChildContentViewHolder extends ChildViewHolder {
        private TextView name;
        private ImageView img_head;

        public ChildContentViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            img_head = itemView.findViewById(R.id.img_head);
        }

        public void onBind(ContactBean child, ExpandableGroup group) {
            name.setText(child.getName());

            GlideApp.with(img_head)
                    .load("http://img.duoziwang.com/2021/03/1623076080632524.jpg")
                    .transform(new RoundedCorners((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            20, AppApplication.instance().getResources().getDisplayMetrics())))
                    .into(img_head);


//            if (group.getTitle().equals("水果")) {
//                name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_contact, 0, 0, 0);
//            } else if (group.getTitle().equals("球类")) {
//                name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_avatar, 0, 0, 0);
//            } else {
//                name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_camera, 0, 0, 0);
//            }

        }
    }
}