package com.bitvalue.health.ui.adapter;

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

import com.bitvalue.health.Application;
import com.bitvalue.health.api.responsebean.ClientsResultBean;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.healthmanage.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        @BindView(R.id.tv_trait_group)
        TextView name;
        @BindView(R.id.tv_group_no)
        TextView tv_group_no;
        @BindView(R.id.tv_new_count)
        TextView tv_new_count;
        @BindView(R.id.iv_trait_group)
        ImageView iv_trait_group;
        @BindView(R.id.layout_pot)
        LinearLayout layout_pot;

        public GroupContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        public void onBind(int flatPosition, ExpandableGroup group) {
            ClientsResultBean clientsResultBean = (ClientsResultBean) group;
            tv_group_no.setText(clientsResultBean.num + "");
            if (clientsResultBean.newMsgNum > 0) {
                layout_pot.setVisibility(View.VISIBLE);
                tv_new_count.setText(clientsResultBean.newMsgNum + "");
                if (clientsResultBean.newMsgNum > 99) {
                    tv_new_count.setText(clientsResultBean.newMsgNum + "+");
                } else {
                    tv_new_count.setText(clientsResultBean.newMsgNum + "");
                }
            } else {
                layout_pot.setVisibility(View.GONE);
            }
        }



        @Override
        public void expand() {
            iv_trait_group.setImageResource(R.drawable.icon_left);
        }

        @Override
        public void collapse() {
            iv_trait_group.setImageResource(R.drawable.icon_down);
        }

        public void setGroupName(String title) {
            name.setText(title);
        }
    }

    public static class ChildContentViewHolder extends ChildViewHolder {

        @BindView(R.id.tv_name)
        TextView name;
        @BindView(R.id.tv_sex)
        TextView tv_sex;
        @BindView(R.id.tv_age)
        TextView tv_age;
        @BindView(R.id.tv_date)
        TextView tv_date;
        @BindView(R.id.tv_project_name)
        TextView tv_project_name;
        @BindView(R.id.img_head)
        ImageView img_head;
        @BindView(R.id.img_boll)
        ImageView img_boll;
        @BindView(R.id.cb_choose)
        CheckBox cb_choose;
        @BindView(R.id.layout_item)
        ConstraintLayout layout_item;

        public ChildContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void onBind(ClientsResultBean.UserInfoDTO child, ExpandableGroup group, int childIndex) {
            name.setText(child.userName);
            tv_sex.setText(child.userSex);
            tv_age.setText(child.userAge + "岁");
            tv_date.setText(TimeUtils.getTime(child.beginTime, TimeUtils.YY_MM_DD_FORMAT_3));
            tv_project_name.setText(child.goodsName);

            cb_choose.setVisibility(child.isShowCheck ? View.VISIBLE : View.GONE);

            img_boll.setVisibility(child.hasNew ? View.VISIBLE : View.GONE);

            cb_choose.setChecked(child.isChecked);

            cb_choose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (null != onChildCheckListener) {
                        onChildCheckListener.onChildCheck(isChecked, childIndex, child);
                    }
                }
            });

            layout_item.setBackgroundColor(child.isClicked ? Application.instance().getResources().getColor(R.color.bg_gray_light) : Application.instance().getResources().getColor(R.color.white));

            if (null != child.headUrl && !child.headUrl.isEmpty()) {
                Glide.with(img_head)
//                        .load("http://img.duoziwang.com/2021/03/1623076080632524.jpg")
                        .load(child.headUrl)
                        .transform(new RoundedCorners((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                20, Application.instance().getResources().getDisplayMetrics())))
                        .into(img_head);
            } else {
                if (!EmptyUtil.isEmpty(child.userSex))
                img_head.setImageDrawable(child.userSex.equals("男") ? Application.instance().getResources().getDrawable(R.drawable.head_male) : Application.instance().getResources().getDrawable(R.drawable.head_female));
            }

        }
    }

    public interface OnChildItemClickListener {
        void onChildItemClick(ClientsResultBean.UserInfoDTO child, ExpandableGroup group, int childIndex, int flatPosition);
    }

    public interface OnChildCheckListener {
        void onChildCheck(boolean isCheck, int childIndex, ClientsResultBean.UserInfoDTO child);
    }
}
