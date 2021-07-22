package com.bitvalue.healthmanage.ui.contacts.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.ui.contacts.bean.ContactBean;
import com.bitvalue.healthmanage.ui.contacts.bean.ContactsGroupBean;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

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
        holder.onBind(child,group);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 跳转事件
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
            iv_trait_group.setImageResource(R.drawable.arrow_right);
        }

        @Override
        public void collapse() {
//            name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrows_bottom_ic, 0);
            iv_trait_group.setImageResource(R.drawable.arrows_bottom_ic);
        }

        public void setGroupName(String title) {
            name.setText(title);
        }
    }

    public static class ChildContentViewHolder extends ChildViewHolder {
        private TextView name;
        public ChildContentViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_trait);
        }

        public void onBind(ContactBean child, ExpandableGroup group) {
            name.setText(child.getName());
            if (group.getTitle().equals("水果")) {
                name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_contact, 0, 0, 0);
            } else if (group.getTitle().equals("球类")) {
                name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_avatar, 0, 0, 0);
            } else {
                name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_camera, 0, 0, 0);
            }
        }
    }
}
