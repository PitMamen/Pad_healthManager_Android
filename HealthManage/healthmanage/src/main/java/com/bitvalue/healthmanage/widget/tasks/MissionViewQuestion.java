package com.bitvalue.healthmanage.widget.tasks;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MissionViewQuestion extends LinearLayout {

    @BindView(R.id.list_items)
    RecyclerView list_items;

    @BindView(R.id.img_type)
    ImageView img_type;

    @BindView(R.id.tv_mission_name)
    TextView tv_mission_name;

    @BindView(R.id.layout_add_item)
    LinearLayout layout_add_item;

    private HomeActivity homeActivity;
    private MissionViewCallBack missionViewCallBack;

    public MissionViewQuestion(Context context) {
        super(context);
        initView(context);
    }

    public MissionViewQuestion(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MissionViewQuestion(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public MissionViewQuestion(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        homeActivity = (HomeActivity) context;
        View.inflate(context, R.layout.layout_misson_question, this);
        ButterKnife.bind(this);
//        layout_after_end.getLayoutParams().height = Constants.screenHeight / 3;

    }

    @OnClick({R.id.layout_add_item,R.id.img_type})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.layout_add_item:
                break;
            case R.id.img_type:
                if (null != missionViewCallBack){
                    missionViewCallBack.onDeleteMission();
                }
                break;
        }
    }

    public void setMissionViewCallBack(MissionViewCallBack missionViewCallBack){
        this.missionViewCallBack = missionViewCallBack;
    }

    public interface MissionViewCallBack {
        void onDeleteMission();

        void onGetMissionData();
    }

}
