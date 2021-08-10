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
import com.bitvalue.healthmanage.http.response.ClientsResultBean;
import com.bitvalue.healthmanage.util.TimeUtils;
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
    private OnChildItemClickListener onChildItemClickListener;

    public RecyclerAdapter(Activity activity, List<? extends ExpandableGroup> groups) {
        super(groups);
        this.activity = activity;
    }

    public void setOnChildItemClickListener(OnChildItemClickListener onChildItemClickListener) {
        this.onChildItemClickListener = onChildItemClickListener;
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
//        final ClientsResultBean.UserInfoDTO child = ((ClientsResultBean.UserInfoDTO) group).getItems().get(childIndex);
        List<ClientsResultBean.UserInfoDTO> userInfo = ((ClientsResultBean) group).userInfo;
        ClientsResultBean.UserInfoDTO child = userInfo.get(childIndex);
        holder.onBind(child, group);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onChildItemClickListener) {
                    onChildItemClickListener.onChildItemClick(child, group, childIndex, flatPosition);
                }
            }
        });
    }

    @Override
    public void onBindGroupViewHolder(GroupContentViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.onBind(flatPosition, group);
        holder.setGroupName(group.getTitle());
    }

    public static class GroupContentViewHolder extends GroupViewHolder {

        public void onBind(int flatPosition, ExpandableGroup group) {
            ClientsResultBean clientsResultBean = (ClientsResultBean) group;
            tv_group_no.setText(clientsResultBean.num + "");
        }

        private TextView name, tv_group_no;
        private ImageView iv_trait_group;

        public GroupContentViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_trait_group);
            tv_group_no = (TextView) itemView.findViewById(R.id.tv_group_no);
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
        private TextView name,tv_sex,tv_age,tv_date,tv_project_name;
        private ImageView img_head;

        public ChildContentViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            img_head = itemView.findViewById(R.id.img_head);
            tv_sex = itemView.findViewById(R.id.tv_sex);
            tv_age = itemView.findViewById(R.id.tv_age);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_project_name = itemView.findViewById(R.id.tv_project_name);
        }

        public void onBind(ClientsResultBean.UserInfoDTO child, ExpandableGroup group) {
            name.setText(child.userName);
            tv_sex.setText(child.userSex);
            tv_age.setText(child.userAge + "");
            tv_date.setText(TimeUtils.getTime(child.beginTime,TimeUtils.YY_MM_DD_FORMAT_3));
            tv_project_name.setText(child.goodsName);

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

    public interface OnChildItemClickListener {

        void onChildItemClick(ClientsResultBean.UserInfoDTO child, ExpandableGroup group, int childIndex, int flatPosition);

    }
}
