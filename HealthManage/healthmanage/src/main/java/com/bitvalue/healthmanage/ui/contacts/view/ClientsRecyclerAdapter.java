package com.bitvalue.healthmanage.ui.contacts.view;

import android.app.Activity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppApplication;
import com.bitvalue.healthmanage.http.glide.GlideApp;
import com.bitvalue.healthmanage.http.response.ClientsResultBean;
import com.bitvalue.healthmanage.util.TimeUtils;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

/**
 * 二级折叠列表，借鉴于
 * https://cloud.tencent.com/developer/article/1154841
 */
public class ClientsRecyclerAdapter extends ExpandableRecyclerViewAdapter<ClientsRecyclerAdapter.GroupContentViewHolder, ClientsRecyclerAdapter.ChildContentViewHolder> {

    private Activity activity;
    private OnChildItemClickListener onChildItemClickListener;
    private static OnChildCheckListener onChildCheckListener;

    public ClientsRecyclerAdapter(Activity activity, List<? extends ExpandableGroup> groups) {
        super(groups);
        this.activity = activity;
    }

    public void setOnChildCheckListener(OnChildCheckListener onChildCheckListener) {
        this.onChildCheckListener = onChildCheckListener;
    }

    public interface OnChildCheckListener {
        void onChildCheck(boolean isCheck, int childIndex, ClientsResultBean.UserInfoDTO child);
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
        holder.onBind(child, group, childIndex);
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
            if (clientsResultBean.newMsgNum > 0) {
                layout_pot.setVisibility(View.VISIBLE);
                tv_new_count.setText(clientsResultBean.newMsgNum + "");
            } else {
                layout_pot.setVisibility(View.GONE);
            }
        }

        private TextView name, tv_group_no, tv_new_count;
        private ImageView iv_trait_group;
        private LinearLayout layout_pot;

        public GroupContentViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_trait_group);
            tv_group_no = itemView.findViewById(R.id.tv_group_no);
            iv_trait_group = itemView.findViewById(R.id.iv_trait_group);

            layout_pot = itemView.findViewById(R.id.layout_pot);
            tv_new_count = itemView.findViewById(R.id.tv_new_count);
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
        private TextView name, tv_sex, tv_age, tv_date, tv_project_name;
        private ImageView img_head, img_boll;
        private CheckBox cb_choose;
        private ConstraintLayout layout_item;

        public ChildContentViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            img_head = itemView.findViewById(R.id.img_head);
            tv_sex = itemView.findViewById(R.id.tv_sex);
            tv_age = itemView.findViewById(R.id.tv_age);
            tv_date = itemView.findViewById(R.id.tv_date);
            cb_choose = itemView.findViewById(R.id.cb_choose);
            tv_project_name = itemView.findViewById(R.id.tv_project_name);
            layout_item = itemView.findViewById(R.id.layout_item);

            img_boll = itemView.findViewById(R.id.img_boll);
        }

        public void onBind(ClientsResultBean.UserInfoDTO child, ExpandableGroup group, int childIndex) {
            name.setText(child.userName);
            tv_sex.setText(child.userSex);
            tv_age.setText(child.userAge + "岁");
            tv_date.setText(TimeUtils.getTime(child.beginTime, TimeUtils.YY_MM_DD_FORMAT_3));
            tv_project_name.setText(child.goodsName);

            if (child.isShowCheck) {
                cb_choose.setVisibility(View.VISIBLE);
            } else {
                cb_choose.setVisibility(View.GONE);
            }

            if (child.hasNew) {
                img_boll.setVisibility(View.VISIBLE);
            } else {
                img_boll.setVisibility(View.GONE);
            }

            cb_choose.setChecked(child.isChecked);

            cb_choose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (null != onChildCheckListener) {
                        onChildCheckListener.onChildCheck(isChecked, childIndex, child);
                    }
                }
            });

            if (child.isClicked) {
                layout_item.setBackgroundColor(AppApplication.instance().getResources().getColor(R.color.bg_gray_light));
            } else {
                layout_item.setBackgroundColor(AppApplication.instance().getResources().getColor(R.color.white));
            }

            if (null != child.headUrl && !child.headUrl.isEmpty()) {
                GlideApp.with(img_head)
//                        .load("http://img.duoziwang.com/2021/03/1623076080632524.jpg")
                        .load(child.headUrl)
                        .transform(new RoundedCorners((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                20, AppApplication.instance().getResources().getDisplayMetrics())))
                        .into(img_head);
            } else {
                if (child.userSex.equals("男")) {
                    img_head.setImageDrawable(AppApplication.instance().getResources().getDrawable(R.drawable.head_male));
                } else {
                    img_head.setImageDrawable(AppApplication.instance().getResources().getDrawable(R.drawable.head_female));
                }
            }


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
