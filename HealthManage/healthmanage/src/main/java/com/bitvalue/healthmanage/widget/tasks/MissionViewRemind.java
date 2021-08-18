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
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.http.model.HttpData;
import com.bitvalue.healthmanage.http.myhttp.FileUploadUtils;
import com.bitvalue.healthmanage.http.request.UploadFileApi;
import com.bitvalue.healthmanage.http.response.AudioUploadResultBean;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.ui.adapter.AudioAdapter;
import com.bitvalue.healthmanage.ui.fragment.NewMsgFragment;
import com.bitvalue.healthmanage.util.DensityUtil;
import com.bitvalue.healthmanage.util.MUtils;
import com.bitvalue.healthmanage.widget.tasks.bean.SavePlanApi;
import com.bitvalue.sdk.collab.component.AudioPlayer;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kotlin.jvm.Throws;
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

    private HomeActivity homeActivity;
    private MissionViewCallBack missionViewCallBack;
    private List<UploadFileApi> mUploadedAudios = new ArrayList<>();
    private AudioAdapter adapter;
    private boolean isRecording;
    private ArrayList<String> audiosFinal = new ArrayList<>();
    private SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO templateTaskContentDTO = new SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO();

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
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UploadFileApi uploadFileApi = mUploadedAudios.get(position);
                ToastUtil.toastLongMessage("开始播放");
                AudioPlayer.getInstance().startPlay(uploadFileApi.path, new AudioPlayer.Callback() {
                    //                AudioPlayer.getInstance().startPlay(uploadFileApi.fileLinkUrl, new AudioPlayer.Callback() {
                    @Override
                    public void onCompletion(Boolean success) {
                        ToastUtil.toastLongMessage("播放完成");
                    }
                });
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
            public void onSuccess(HttpData<AudioUploadResultBean> result) {

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
        templateTaskContentDTO.contentDetail = new SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO.ContentDetailDTO();
        templateTaskContentDTO.contentDetail.remindName = "健康提醒";
        //填入的数据
//        if (et_text_msg.getText().toString().isEmpty()) {
//            ToastUtil.toastShortMessage("请输入提醒信息");
//            //提示信息然后抛出异常，相当于判断数据是否ok
//            try {
//                throw new Exception("are you ok?");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        templateTaskContentDTO.contentDetail.remindContent = et_text_msg.getText().toString();
        if (audiosFinal.size() > 0) {
            templateTaskContentDTO.contentDetail.voiceList = getProcessString(audiosFinal);
        }
        return templateTaskContentDTO;
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

    public interface MissionViewCallBack {
        void onDeleteMission();

        void onGetMissionData();
    }

}
