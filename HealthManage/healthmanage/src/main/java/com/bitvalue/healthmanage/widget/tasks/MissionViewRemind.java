package com.bitvalue.healthmanage.widget.tasks;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.http.model.ApiResult;
import com.bitvalue.healthmanage.http.FileUploadUtils;
import com.bitvalue.healthmanage.http.api.UploadFileApi;
import com.bitvalue.healthmanage.http.bean.AudioUploadResultBean;
import com.bitvalue.healthmanage.http.bean.PlanDetailResult;
import com.bitvalue.healthmanage.ui.activity.main.HomeActivity;
import com.bitvalue.healthmanage.ui.adapter.AudioAdapter;
import com.bitvalue.healthmanage.callback.OnItemDeleteCallback;
import com.bitvalue.healthmanage.util.DensityUtil;
import com.bitvalue.healthmanage.util.MUtils;
import com.bitvalue.healthmanage.util.DataUtil;
import com.bitvalue.healthmanage.widget.tasks.bean.SavePlanApi;
import com.bitvalue.sdk.collab.component.AudioPlayer;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.hjq.toast.ToastUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

public class MissionViewRemind extends LinearLayout implements DataInterface {

    @BindView(R.id.list_items)
    RecyclerView list_audio;

    @BindView(R.id.img_type)
    ImageView img_type;

    @BindView(R.id.tv_mission_name)
    TextView tv_mission_name;

    @BindView(R.id.et_text_msg)
    EditText et_text_msg;

    @BindView(R.id.tv_add_item)
    TextView tv_add_item;

    @BindView(R.id.layout_add_item)
    LinearLayout layout_add_item;

    private int TaskNo;
    private int MissionNo;

    public int getTaskNo() {
        return TaskNo;
    }

    public void setTaskNo(int taskNo) {
        TaskNo = taskNo;
    }

    public int getMissionNo() {
        return MissionNo;
    }

    public void setMissionNo(int missionNo) {
        MissionNo = missionNo;
    }

    private HomeActivity homeActivity;
    private MissionViewCallBack missionViewCallBack;
    private List<UploadFileApi> mUploadedAudios = new ArrayList<>();
    private AudioAdapter adapter;
    private boolean isRecording;
    private ArrayList<String> audiosFinal = new ArrayList<>();
    private SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO templateTaskContentDTO = new SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO();
    private boolean isModify;

    public MissionViewRemind(Context context) {
        super(context);
        initView(context);
    }

    public MissionViewRemind(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MissionViewRemind(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public MissionViewRemind(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        homeActivity = (HomeActivity) context;
        View.inflate(context, R.layout.layout_misson_remind, this);
        ButterKnife.bind(this);
//        layout_after_end.getLayoutParams().height = Constants.screenHeight / 3;
        initAudioListView();
    }

    private void initAudioListView() {
        list_audio.setLayoutManager(new LinearLayoutManager(homeActivity));
        list_audio.addItemDecoration(MUtils.spaceDivider(DensityUtil.dip2px(homeActivity, homeActivity.getResources().getDimension(R.dimen.qb_px_3)), false));
        adapter = new AudioAdapter(R.layout.item_audio, mUploadedAudios);
        adapter.setOnItemDelete(new OnItemDeleteCallback() {
            @Override
            public void onItemDelete(int position) {
                mUploadedAudios.remove(position);
                audiosFinal.remove(position);
                adapter.setNewData(mUploadedAudios);
            }
        });
        list_audio.setAdapter(adapter);
    }

    @OnClick({R.id.layout_add_item, R.id.img_type})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_add_item:
                if (mUploadedAudios.size() == 1) {
                    ToastUtil.toastShortMessage("仅限添加一条语音");
                    return;
                }
                checkPermission();
                break;
            case R.id.img_type:
                if (null != missionViewCallBack) {
                    missionViewCallBack.onDeleteMission();
                }
                break;
        }
    }

    public void checkPermission() {
        RxPermissions.getInstance(homeActivity)
                .request(Manifest.permission.CAMERA
                        , Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                        , Manifest.permission.READ_PHONE_STATE
                        , Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.RECORD_AUDIO)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean flag) {
                        if (flag) {
                            if (!isRecording) {
                                //开始录音
                                isRecording = true;
                                tv_add_item.setText("录音中，点击结束");
                                tv_add_item.setTextColor(homeActivity.getResources().getColor(R.color.orange));
                                AudioPlayer.getInstance().startRecord(new AudioPlayer.Callback() {
                                    @Override
                                    public void onCompletion(Boolean success) {
                                        tv_add_item.setText("点击录音");
                                        tv_add_item.setTextColor(homeActivity.getResources().getColor(R.color.main_blue));
                                        isRecording = false;

                                        int duration = AudioPlayer.getInstance().getDuration();
                                        String path = AudioPlayer.getInstance().getPath();
                                        //组装录音列表数据
                                        UploadFileApi uploadFileApi = new UploadFileApi();
                                        uploadFileApi.path = path;
                                        File file = new File(path);
                                        if (!file.isDirectory() && file.exists()) {
                                            uploadFileApi.file = file;
                                        }
                                        uploadFileApi.duration = duration;
                                        mUploadedAudios.add(uploadFileApi);
                                        adapter.setNewData(mUploadedAudios);
                                        uploadedAudioMsg(uploadFileApi);
                                    }
                                });
                            } else {
                                AudioPlayer.getInstance().stopRecord();
                            }
                        } else {
                            new AlertDialog.Builder(homeActivity)
                                    .setMessage("请先进行相关授权，再重启APP！")
                                    .setPositiveButton("进入设置", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", homeActivity.getPackageName(), null);
                                            intent.setData(uri);
                                            homeActivity.startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                            SplashScreenActivity.this.finish();
                                        }
                                    })
                                    .setCancelable(false)
                                    .show();
                        }
                    }
                });

    }

    private void uploadedAudioMsg(UploadFileApi uploadFileApi) {
        FileUploadUtils.INSTANCE.uploadAudio(homeActivity, uploadFileApi, new FileUploadUtils.OnAudioUploadCallback() {
            @Override
            public void onSuccess(ApiResult<AudioUploadResultBean> result) {

                if (result.getCode() != 0) {
                    ToastUtil.toastLongMessage(result.getMessage());
                    return;
                }

                //要上传语音数据
                audiosFinal.add(result.getData().fileLinkUrl);
            }

            @Override
            public void onFail() {

            }
        });
    }

    @Override
    public SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO getData() {
        //写死的类型数据
        templateTaskContentDTO.taskType = "Remind";
        if (!isModify) {
            templateTaskContentDTO.contentDetail = new SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO.ContentDetailDTO();
        }
        templateTaskContentDTO.contentDetail.remindName = "健康提醒";
        //填入的数据
        templateTaskContentDTO.contentDetail.remindContent = et_text_msg.getText().toString();
        if (audiosFinal.size() > 0) {
            templateTaskContentDTO.contentDetail.voiceList = getProcessString(audiosFinal);
        } else {
            templateTaskContentDTO.contentDetail.voiceList = "";
        }
        return templateTaskContentDTO;
    }

    @Override
    public PlanDetailResult.UserPlanDetailsDTO getAssembleData() {
        if (et_text_msg.getText().toString().isEmpty() && mUploadedAudios.size() == 0) {
            return null;
        }
        PlanDetailResult.UserPlanDetailsDTO userPlanDetailsDTO = new PlanDetailResult.UserPlanDetailsDTO();
        userPlanDetailsDTO.planType = "Remind";
        userPlanDetailsDTO.planDescribe = et_text_msg.getText().toString();
        userPlanDetailsDTO.remindContent = et_text_msg.getText().toString();
        userPlanDetailsDTO.execFlag = 0;
        if (audiosFinal.size() > 0) {
            userPlanDetailsDTO.voiceList = getProcessString(audiosFinal);
        }
        return userPlanDetailsDTO;
    }

    private String getProcessString(ArrayList<String> photosFinal) {
        String total = "";
        for (int i = 0; i < photosFinal.size(); i++) {
            if (i != photosFinal.size() - 1) {
                total = total + photosFinal.get(i) + ",";
            } else {
                total = total + photosFinal.get(i);
            }
        }
        return total;
    }

    public void setMissionViewCallBack(MissionViewCallBack missionViewCallBack) {
        this.missionViewCallBack = missionViewCallBack;
    }

    public boolean isDataReady() {
        boolean isReady = false;
        if (et_text_msg.getText().toString().length() >= 6 || mUploadedAudios.size() > 0) {
            isReady = true;
        } else {
            if (mUploadedAudios.size() == 0 && !et_text_msg.getText().toString().isEmpty() && et_text_msg.getText().toString().length() < 6) {
                ToastUtils.show("请输入提醒内容超过5个字");
            } else {
                ToastUtils.show("请至少输入一种提醒消息");
            }
        }
        return isReady;
    }

    /**
     * 这里处理一下后台返回null的问题
     *
     * @param templateTaskContentDTO 输入数据
     */
    public void setMissionData(SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO templateTaskContentDTO) {
        this.templateTaskContentDTO = DataUtil.getNotNullData(templateTaskContentDTO);
//        this.templateTaskContentDTO.taskType = "";

        et_text_msg.setText(templateTaskContentDTO.contentDetail.remindContent);

        processAudios(templateTaskContentDTO.contentDetail.voiceList);
        isModify = true;
    }

    private void processAudios(String voiceList) {
        if (null == voiceList || voiceList.isEmpty()) {
            return;
        }
        String[] split = voiceList.split(",");
        for (int i = 0; i < split.length; i++) {
            UploadFileApi uploadFileApi = new UploadFileApi();
            uploadFileApi.fileLinkUrl = split[i];

//            split[i] = split[i].replace("218.77.104.74:8008", "192.168.1.122");
            uploadFileApi.path = split[i];
            mUploadedAudios.add(uploadFileApi);
            audiosFinal.add(split[i]);
        }
        adapter.setNewData(mUploadedAudios);
    }

    public interface MissionViewCallBack {
        void onDeleteMission();

        void onGetMissionData();
    }

}
