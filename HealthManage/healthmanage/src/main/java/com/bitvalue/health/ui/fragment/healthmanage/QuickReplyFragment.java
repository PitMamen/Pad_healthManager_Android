package com.bitvalue.health.ui.fragment.healthmanage;

import static com.bitvalue.health.util.Constants.USER_ID;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bitvalue.health.api.requestbean.QuestionResultBean;
import com.bitvalue.health.api.requestbean.QuickReplyRequest;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.OnItemClickCallback;
import com.bitvalue.health.contract.quickreply.QuickReplyContract;
import com.bitvalue.health.presenter.quickreplypresenter.QuickreplyPresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.QuickReplyAdapter;
import com.bitvalue.health.util.ClickUtils;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.customview.QuickReplyDialog;
import com.bitvalue.health.util.customview.WrapRecyclerView;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.modules.message.MessageInfoUtil;
import com.hjq.toast.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author created by bitvalue
 * @data : 01/12
 */
public class QuickReplyFragment extends BaseFragment<QuickreplyPresenter> implements QuickReplyContract.View, OnItemClickCallback {
    @BindView(R.id.layout_back)
    LinearLayout back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.list_quickreply)
    WrapRecyclerView recyclerView;
    @BindView(R.id.tv_custom_reply)
    TextView tv_custom_reply;

    private HomeActivity homeActivity;

    private List<QuickReplyRequest> quickReplyList = new ArrayList<>();
    private QuickReplyAdapter quickReplyAdapter;
    private String docUserId;
    private QuickReplyDialog dialog;
    private QuickReplyRequest quickReplyRequest = new QuickReplyRequest();

    @Override
    protected QuickreplyPresenter createPresenter() {
        return new QuickreplyPresenter();
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
    public void initData() {
        super.initData();
        qryCaseCommonWord();
    }


    /**
     * 获取快捷用语
     */
    private void qryCaseCommonWord() {
        quickReplyRequest.userId = docUserId;
        mPresenter.qryCaseCommonWords(quickReplyRequest);
    }


    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        tv_title.setText(getString(R.string.my_quickreply));
        back.setVisibility(View.VISIBLE);
        if (getArguments() == null) {
            return;
        }
        docUserId = getArguments().getString(USER_ID, "");
        LinearLayoutManager layoutManager = new LinearLayoutManager(homeActivity);
        recyclerView.setLayoutManager(layoutManager);
        quickReplyAdapter = new QuickReplyAdapter(R.layout.item_quickreplay_layout, quickReplyList, homeActivity);
        recyclerView.setAdapter(quickReplyAdapter);
        quickReplyAdapter.setOnClicListner(this);
    }

    @Override
    public void onItemClick(Object object) {
        QuickReplyRequest quickReply = (QuickReplyRequest) object;
        EventBus.getDefault().post(MessageInfoUtil.buildTextMessage(quickReply.content));
        homeActivity.getSupportFragmentManager().popBackStack();
    }

    @OnClick({R.id.layout_back, R.id.tv_custom_reply})
    public void onClickButton(View view) {
        switch (view.getId()) {
            //返回
            case R.id.layout_back:
                if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    homeActivity.getSupportFragmentManager().popBackStack();
                }
                break;

            // 自定义快捷用语
            case R.id.tv_custom_reply:
                if (!ClickUtils.isFastClick()) {
                    if (quickReplyAdapter != null && quickReplyAdapter.getItemCount() >= 20) {
                       ToastUtils.show("超出限制条数!不能创建新的快捷用语");
                        return;
                    }

                    dialog = new QuickReplyDialog(homeActivity);
                    dialog.setOnclickListener(new QuickReplyDialog.OnButtonClickListener() {
                        @Override
                        public void onPositiveClick() {
                            String inputString = dialog.getInputString();
                            if (EmptyUtil.isEmpty(inputString)) {
                                ToastUtils.show("不能为空!");
                                return;
                            }
                            QuickReplyRequest quickReplyRequest = new QuickReplyRequest();
                            quickReplyRequest.userId = docUserId;
                            quickReplyRequest.content = inputString;
                            mPresenter.saveCaseCommonWords(quickReplyRequest);
                        }

                        @Override
                        public void onNegtiveClick() {
                            dialog.dismiss();
                        }
                    }).show();
                }

                break;


            default:
                break;
        }
    }


    /**
     * 查询快捷常用语 成功回调
     *
     * @param listData
     */
    @Override
    public void qryCaseCommonWordsSuccess(List<QuickReplyRequest> listData) {
        getActivity().runOnUiThread(() -> {
            if (listData != null && listData.size() > 0) {
                quickReplyList = listData;
                quickReplyAdapter.setNewData(quickReplyList);
            }
        });
    }


    /**
     * 查询快捷常用语 失败回调
     *
     * @param faileMessage 失败信息
     */
    @Override
    public void qryCaseCommonWordsFail(String faileMessage) {

    }


    /**
     * 新增 修改 快捷用语 成功回调
     *
     * @param quickReplyResult
     */
    @Override
    public void saveCaseCommonWordsSuccess(QuickReplyRequest quickReplyResult) {
        homeActivity.runOnUiThread(() -> {
            ToastUtils.show("操作成功");
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            qryCaseCommonWord(); //添加自定义快捷用语成功后 更新列表 重新请求接口
        });

    }

    /**
     * 新增 修改 快捷用语 失败回调
     *
     * @param failMessage 失败信息
     */
    @Override
    public void saveCaseCommonWordsFail(String failMessage) {

    }
}
