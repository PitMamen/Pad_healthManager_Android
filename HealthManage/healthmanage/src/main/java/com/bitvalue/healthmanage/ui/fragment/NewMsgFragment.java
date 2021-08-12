package com.bitvalue.healthmanage.ui.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.http.model.HttpData;
import com.bitvalue.healthmanage.http.request.UploadFileApi;
import com.bitvalue.healthmanage.http.response.AudioUploadResultBean;
import com.bitvalue.healthmanage.http.response.ImageModel;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.ui.adapter.AudioAdapter;
import com.bitvalue.healthmanage.ui.adapter.NineImageAdapter;
import com.bitvalue.healthmanage.ui.media.ImagePreviewActivity;
import com.bitvalue.healthmanage.ui.media.ImageSelectActivity;
import com.bitvalue.healthmanage.util.DensityUtil;
import com.bitvalue.healthmanage.util.MUtils;
import com.bitvalue.healthmanage.util.UiUtil;
import com.bitvalue.sdk.collab.component.AudioPlayer;
import com.bitvalue.sdk.collab.modules.chat.layout.input.InputLayout;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yqritc.recyclerviewflexibledivider.VerticalDividerItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import okhttp3.Call;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import rx.functions.Action1;

public class NewMsgFragment extends AppFragment implements BGANinePhotoLayout.Delegate {

    @BindView(R.id.img_add_pic)
    ImageView img_add_pic;

    //https://github.com/bingoogolapple/BGAPhotoPicker-Android  第三方依赖
    @BindView(R.id.npl_item_moment_photos)
    BGANinePhotoLayout ninePhotoLayout;

    //    @BindView(R.id.list_photos)
//    RecyclerView list_photos;
    private static final int PRC_PHOTO_PREVIEW = 1;

    private static final int RC_ADD_MOMENT = 1;

    protected static final int CAPTURE = 1;
    protected static final int AUDIO_RECORD = 2;
    protected static final int VIDEO_RECORD = 3;
    protected static final int SEND_PHOTO = 4;
    protected static final int SEND_FILE = 5;
    private String msgType;
    private TextView tv_title, tv_send_msg, tv_add_audio;
    private EditText et_text_msg;
    private LinearLayout layout_add_audio, layout_add_video, layout_add_paper;

    protected View mRecordingGroup;
    protected ImageView mRecordingIcon;
    protected TextView mRecordingTips;
    private AnimationDrawable mVolumeAnim;

    private InputLayout.ChatInputHandler mChatInputHandler;
    private boolean isRecording;
    private List<UploadFileApi> mUploadedAudios = new ArrayList<>();
    private RecyclerView list_audio;
    private AudioAdapter adapter;
    private HomeActivity homeActivity;
    private ArrayList<ImageModel> mImgModelsLive = new ArrayList<ImageModel>();
    private NineImageAdapter nineImageAdapter;
    private List<ImageModel> mImageModels = new ArrayList<>();
    private ArrayList<String> photos = new ArrayList<>();
    private BGANinePhotoLayout mCurrentClickNpl;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_new_msg;
    }

    @Override
    protected void initView() {
        homeActivity = (HomeActivity) getActivity();
        msgType = getArguments().getString(Constants.MSG_TYPE);
        tv_title = getView().findViewById(R.id.tv_title);
        tv_add_audio = getView().findViewById(R.id.tv_add_audio);
        tv_send_msg = getView().findViewById(R.id.tv_send_msg);
        et_text_msg = getView().findViewById(R.id.et_text_msg);
        layout_add_audio = getView().findViewById(R.id.layout_add_audio);
        layout_add_video = getView().findViewById(R.id.layout_add_video);
        layout_add_paper = getView().findViewById(R.id.layout_add_paper);
        mRecordingGroup = getView().findViewById(R.id.voice_recording_view);
        mRecordingIcon = getView().findViewById(R.id.recording_icon);
        mRecordingTips = getView().findViewById(R.id.recording_tips);
        list_audio = getView().findViewById(R.id.list_audio);
        if (msgType.equals(Constants.MSG_SINGLE)) {
            tv_title.setText("健康消息");
        } else {
            tv_title.setText("群发消息");
        }

        ninePhotoLayout.setDelegate(NewMsgFragment.this);
        ninePhotoLayout.setData(photos);

        initAudioListView();
    }

    private void initAudioListView() {
        list_audio.setLayoutManager(new LinearLayoutManager(getAttachActivity()));
        list_audio.addItemDecoration(MUtils.spaceDivider(
                DensityUtil.dip2px(getAttachActivity(), getAttachActivity().getResources().getDimension(R.dimen.qb_px_3)), false));
        adapter = new AudioAdapter(R.layout.item_audio, mUploadedAudios);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UploadFileApi uploadFileApi = mUploadedAudios.get(position);
                ToastUtil.toastLongMessage("开始播放");
                AudioPlayer.getInstance().startPlay(uploadFileApi.fileLinkUrl, new AudioPlayer.Callback() {
                    @Override
                    public void onCompletion(Boolean success) {
                        ToastUtil.toastLongMessage("播放完成");
                    }
                });
            }
        });
        list_audio.setAdapter(adapter);
    }

    public void checkPermission() {
        RxPermissions.getInstance(getAttachActivity())
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
                                tv_add_audio.setText("录音中，点击结束");
                                tv_add_audio.setTextColor(getAttachActivity().getResources().getColor(R.color.orange));
                                AudioPlayer.getInstance().startRecord(new AudioPlayer.Callback() {
                                    @Override
                                    public void onCompletion(Boolean success) {
                                        uploadAudio(success);
                                    }
                                });
                            } else {
                                AudioPlayer.getInstance().stopRecord();
                            }
                        } else {
                            new AlertDialog.Builder(getAttachActivity())
                                    .setMessage("请先进行相关授权，再重启APP！")
                                    .setPositiveButton("进入设置", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", getAttachActivity().getPackageName(), null);
                                            intent.setData(uri);
                                            startActivity(intent);
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

    /**
     * 动画代码
     * mRecordingGroup.setVisibility(View.VISIBLE);
     * mRecordingIcon.setImageResource(R.drawable.recording_volume);
     * mVolumeAnim = (AnimationDrawable) mRecordingIcon.getDrawable();
     * mVolumeAnim.start();
     * mRecordingTips.setTextColor(Color.WHITE);
     * mRecordingTips.setText(TUIKit.getAppContext().getString(R.string.down_cancle_send));
     */

    private void uploadAudio(boolean success) {
        int duration = AudioPlayer.getInstance().getDuration();
        String path = AudioPlayer.getInstance().getPath();
        UploadFileApi uploadFileApi = new UploadFileApi();
        File file = new File(path);
        if (file.isDirectory() || !file.exists()) {
            return;
        }
        uploadFileApi.file = file;
        EasyHttp.post(this).api(uploadFileApi).request(new HttpCallback<HttpData<AudioUploadResultBean>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(HttpData<AudioUploadResultBean> result) {
                super.onSucceed(result);
                tv_add_audio.setText("点击录音");
                tv_add_audio.setTextColor(getAttachActivity().getResources().getColor(R.color.main_blue));
                isRecording = false;
                if (result.getCode() != 0) {
                    ToastUtil.toastLongMessage(result.getMessage());
                    return;
                }

                //组装录音列表数据
                uploadFileApi.path = path;
                uploadFileApi.duration = duration;
                uploadFileApi.fileLinkUrl = result.getData().fileLinkUrl;
                uploadFileApi.id = result.getData().id;
                mUploadedAudios.add(uploadFileApi);
                adapter.setNewData(mUploadedAudios);
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
                tv_add_audio.setText("点击录音");
                tv_add_audio.setTextColor(getAttachActivity().getResources().getColor(R.color.main_blue));
                isRecording = false;
            }
        });
    }

    @Override
    protected void initData() {
        mImageModels.add(new ImageModel(Constants.IMG_ADD));
    }

    @Override
    public void onResume() {
        super.onResume();
        int size = mImageModels.size();
        ImageModel imageModel = mImageModels.get(0);
    }

    //    @SingleClick
    @OnClick({R.id.layout_add_audio, R.id.layout_add_video, R.id.layout_add_paper, R.id.tv_send_msg, R.id.img_add_pic})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_add_audio:
                checkPermission();
                break;
            case R.id.layout_add_video:
                homeActivity.switchSecondFragment(Constants.FRAGMENT_ADD_VIDEO, "");
                break;
            case R.id.img_add_pic:
                ImageSelectActivity.start(getAttachActivity(), 9, new ImageSelectActivity.OnPhotoSelectListener() {

                    @Override
                    public void onSelected(List<String> data) {
                        toast("选择了" + data.toString());

                        getImages(data);
                    }

                    @Override
                    public void onCancel() {
                        toast("取消了");
                    }
                });
                break;
            case R.id.layout_add_paper:
                homeActivity.switchSecondFragment(Constants.FRAGMENT_ADD_PAPER, "");
                break;
            case R.id.tv_send_msg:
                break;
        }
    }

    private void getImages(List<String> data) {
        if (null == data || data.size() == 0) {
            return;
        }

        photos.addAll(data);
        ninePhotoLayout.setData(photos);
    }

    @Override
    public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
        mCurrentClickNpl = ninePhotoLayout;
        ImagePreviewActivity.start(homeActivity, model);
    }

    @Override
    public void onClickExpand(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
        ninePhotoLayout.setIsExpand(true);
        ninePhotoLayout.flushItems();
    }


}
