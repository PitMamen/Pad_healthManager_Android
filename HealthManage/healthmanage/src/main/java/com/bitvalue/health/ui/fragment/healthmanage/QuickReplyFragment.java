package com.bitvalue.health.ui.fragment.healthmanage;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bitvalue.health.api.responsebean.QuickReplyBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.OnItemClickCallback;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.QuickReplyAdapter;
import com.bitvalue.health.util.customview.WrapRecyclerView;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.modules.message.MessageInfoUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author created by bitvalue
 * @data : 01/12
 */
public class QuickReplyFragment extends BaseFragment implements OnItemClickCallback {
    @BindView(R.id.rl_title)
    RelativeLayout back;
    @BindView(R.id.list_quickreply)
    WrapRecyclerView recyclerView;
    private HomeActivity homeActivity;

    private List<QuickReplyBean> quickReplyList = new ArrayList<>();
    private QuickReplyAdapter quickReplyAdapter;


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.quick_reply_layout;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        back.setOnClickListener(v -> {
            if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                homeActivity.getSupportFragmentManager().popBackStack();
            }
        });

        quickReplyList.add(new QuickReplyBean("你好,很高兴为您服务?"));
        quickReplyList.add(new QuickReplyBean("是否有预约挂号?"));
        quickReplyList.add(new QuickReplyBean("请问您哪里不舒服?有些什么病症"));
        quickReplyList.add(new QuickReplyBean("请问有没有过敏症状?"));
        quickReplyList.add(new QuickReplyBean("请问有没有遗传疾病?"));
        quickReplyList.add(new QuickReplyBean("请问有没有药物过敏?"));
        quickReplyList.add(new QuickReplyBean("请问是从什么时候开始出现该症状的?"));
        quickReplyList.add(new QuickReplyBean("建议到院外科就诊"));
        quickReplyList.add(new QuickReplyBean("请问您哪里不舒服?"));
        LinearLayoutManager layoutManager = new LinearLayoutManager(homeActivity);
        recyclerView.setLayoutManager(layoutManager);
        quickReplyAdapter = new QuickReplyAdapter(R.layout.item_quickreplay_layout, quickReplyList);
        recyclerView.setAdapter(quickReplyAdapter);
        quickReplyAdapter.setOnClicListner(this);
    }

    @Override
    public void onItemClick(Object object) {
        String quickReply = (String) object;
        EventBus.getDefault().post(MessageInfoUtil.buildTextMessage(quickReply));
        homeActivity.getSupportFragmentManager().popBackStack();
    }
}
