package com.bitvalue.health.ui.fragment.healthmanage;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.modules.message.MessageInfoUtil;
import com.hjq.toast.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author created by bitvalue
 * @data : 01/12
 */
public class AddRemindFragment extends BaseFragment {

    @BindView(R.id.rl_title)
    RelativeLayout back;
    @BindView(R.id.tv_send)
    TextView tv_send;
    @BindView(R.id.ll_careof_remind)
    LinearLayout careof_remind;
    @BindView(R.id.ll_medication_remind)
    LinearLayout medication_remind;
    @BindView(R.id.ll_followup_remind)
    LinearLayout followup_remind;
    @BindView(R.id.et_input_msg)
    EditText ed_inputmessage;
    @BindView(R.id.tv_careof_text)
    TextView tv_careof_text;
    @BindView(R.id.tv_medication_text)
    TextView tv_medication_text;
    @BindView(R.id.tv_followup_text)
    TextView tv_followup_text;

    @BindView(R.id.iv_triangle_care)
    ImageView iv_triangle_care;
    @BindView(R.id.iv_triangle_medication)
    ImageView iv_triangle_medi;
    @BindView(R.id.iv_triangle_followup)
    ImageView iv_triangle_foll;

    private HomeActivity homeActivity;
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_sendremind_layout;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }

    @OnClick({R.id.rl_title,R.id.tv_send,R.id.ll_careof_remind,R.id.ll_medication_remind,R.id.ll_followup_remind})
    public void onClick(View view) {
        switch (view.getId()) {
            //返回
            case R.id.rl_title:
                homeActivity.getSupportFragmentManager().popBackStack();
                break;
            //发送
            case R.id.tv_send:
                if (EmptyUtil.isEmpty(ed_inputmessage.getText().toString().trim())){
                    ToastUtils.show("不能发送空消息!");
                    return;
                }
                EventBus.getDefault().post(MessageInfoUtil.buildTextMessage(ed_inputmessage.getText().toString().trim()));
                homeActivity.getSupportFragmentManager().popBackStack();
                break;
//                关怀提醒
            case R.id.ll_careof_remind:
                chanageBackground(1);

                break;
//                用药提醒
            case R.id.ll_medication_remind:
                chanageBackground(2);

                break;

//                复诊提醒
            case R.id.ll_followup_remind:
                chanageBackground(3);

                break;


        }
    }

    private void chanageBackground(int type) {
        if (type == 1) {
            iv_triangle_care.setVisibility(View.VISIBLE);
            iv_triangle_medi.setVisibility(View.GONE);
            iv_triangle_foll.setVisibility(View.GONE);
            careof_remind.setBackground(homeActivity.getDrawable(R.drawable.shape_remind_layout));
            medication_remind.setBackground(homeActivity.getDrawable(R.drawable.shape_remind_unpress));
            followup_remind.setBackground(homeActivity.getDrawable(R.drawable.shape_remind_unpress));
            tv_careof_text.setTextColor(getResources().getColor(R.color.white));
            tv_medication_text.setTextColor(getResources().getColor(R.color.black_one));
            tv_followup_text.setTextColor(getResources().getColor(R.color.black_one));
        } else if (type == 2) {
            iv_triangle_medi.setVisibility(View.VISIBLE);
            iv_triangle_care.setVisibility(View.GONE);
            iv_triangle_foll.setVisibility(View.GONE);
            medication_remind.setBackground(homeActivity.getDrawable(R.drawable.shape_remind_layout));
            careof_remind.setBackground(homeActivity.getDrawable(R.drawable.shape_remind_unpress));
            followup_remind.setBackground(homeActivity.getDrawable(R.drawable.shape_remind_unpress));
            tv_medication_text.setTextColor(getResources().getColor(R.color.white));
            tv_careof_text.setTextColor(getResources().getColor(R.color.black_one));
            tv_followup_text.setTextColor(getResources().getColor(R.color.black_one));
        } else if (type == 3) {
            iv_triangle_medi.setVisibility(View.GONE);
            iv_triangle_care.setVisibility(View.GONE);
            iv_triangle_foll.setVisibility(View.VISIBLE);
            followup_remind.setBackground(homeActivity.getDrawable(R.drawable.shape_remind_layout));
            medication_remind.setBackground(homeActivity.getDrawable(R.drawable.shape_remind_unpress));
            careof_remind.setBackground(homeActivity.getDrawable(R.drawable.shape_remind_unpress));

            tv_followup_text.setTextColor(getResources().getColor(R.color.white));
            tv_careof_text.setTextColor(getResources().getColor(R.color.black_one));
            tv_medication_text.setTextColor(getResources().getColor(R.color.black_one));
        }

    }
}
