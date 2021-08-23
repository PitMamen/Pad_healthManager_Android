package com.bitvalue.healthmanage.ui.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.http.model.HttpData;
import com.bitvalue.healthmanage.http.myhttp.FileUploadUtils;
import com.bitvalue.healthmanage.http.request.GetCustomMsgApi;
import com.bitvalue.healthmanage.http.request.SaveTotalMsgApi;
import com.bitvalue.healthmanage.http.request.UpdateImageApi;
import com.bitvalue.healthmanage.http.request.UploadFileApi;
import com.bitvalue.healthmanage.http.response.ArticleBean;
import com.bitvalue.healthmanage.http.response.AudioUploadResultBean;
import com.bitvalue.healthmanage.http.response.ImageModel;
import com.bitvalue.healthmanage.http.response.PaperBean;
import com.bitvalue.healthmanage.http.response.VideoResultBean;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.ui.adapter.AudioAdapter;
import com.bitvalue.healthmanage.ui.adapter.PaperQuickAdapter;
import com.bitvalue.healthmanage.ui.adapter.VideoQuickAdapter;
import com.bitvalue.healthmanage.ui.media.ImagePreviewActivity;
import com.bitvalue.healthmanage.ui.media.ImageSelectActivity;
import com.bitvalue.healthmanage.util.DensityUtil;
import com.bitvalue.healthmanage.util.MUtils;
import com.bitvalue.healthmanage.util.Utils;
import com.bitvalue.sdk.collab.component.AudioPlayer;
import com.bitvalue.sdk.collab.helper.CustomHealthMessage;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import okhttp3.Call;
import rx.functions.Action1;

import static com.bitvalue.healthmanage.Constants.MAX_IMG;

public class NewMsgFragmentDisplay extends AppFragment implements BGANinePhotoLayout.Delegate {

    @BindView(R.id.img_add_pic)
    ImageView img_add_pic;

    //https://github.com/bingoogolapple/BGAPhotoPicker-Android  第三方依赖
    @BindView(R.id.npl_item_moment_photos)
    BGANinePhotoLayout ninePhotoLayout;

    @BindView(R.id.list_articles)
    RecyclerView list_articles;

    @BindView(R.id.list_videos)
    RecyclerView list_videos;

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
    private boolean isRecording;
    private List<UploadFileApi> mUploadedAudios = new ArrayList<>();
    private List<PaperBean> mPapers = new ArrayList<>();
    private List<UpdateImageApi> mUploadImages = new ArrayList<>();
    private ArrayList<String> videos = new ArrayList<>();
    private List<ArticleBean> articleBeans = new ArrayList<>();
    private RecyclerView list_audio;
    private AudioAdapter adapter;
    private PaperQuickAdapter paperAdapter;
    private VideoQuickAdapter videoAdapter;
    private HomeActivity homeActivity;
    private List<ImageModel> mImageModels = new ArrayList<>();
    private ArrayList<String> photos = new ArrayList<>();
    private ArrayList<String> photosFinal = new ArrayList<>();
    private ArrayList<String> audiosFinal = new ArrayList<>();
    private ArrayList<String> articles = new ArrayList<>();
    private BGANinePhotoLayout mCurrentClickNpl;
    private ArrayList<String> mIds;
    private String msgCustomId;
    private List<VideoResultBean.ListDTO> videoBeans = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_new_msg_dis;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        homeActivity = (HomeActivity) getActivity();
        msgType = getArguments().getString(Constants.MSG_TYPE);
        msgCustomId = getArguments().getString(Constants.MSG_CUSTOM_ID);
        mIds = getArguments().getStringArrayList(Constants.MSG_IDS);
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

        ninePhotoLayout.setDelegate(NewMsgFragmentDisplay.this);
        ninePhotoLayout.setData(photos);

        initAudioListView();
        initArticleListView();
        initVideoListView();

        getMsgDetail();
    }

    private void initVideoListView() {
        list_videos.setLayoutManager(new LinearLayoutManager(getAttachActivity()));
        list_videos.addItemDecoration(MUtils.spaceDivider(
                DensityUtil.dip2px(getAttachActivity(), getAttachActivity().getResources().getDimension(R.dimen.qb_px_3)), false));
        videoAdapter = new VideoQuickAdapter(R.layout.item_paper, videoBeans);
        videoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        list_videos.setAdapter(videoAdapter);
    }

    private void getMsgDetail() {//    /health/doctor/getUserRemind
        GetCustomMsgApi getCustomMsgApi = new GetCustomMsgApi();
        getCustomMsgApi.id = msgCustomId;
        EasyHttp.get(this).api(getCustomMsgApi).request(new HttpCallback<HttpData<SaveTotalMsgApi>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(HttpData<SaveTotalMsgApi> result) {
                super.onSucceed(result);
                //TODO 展示获取到的信息
                if (result.getCode() == 0) {
                    SaveTotalMsgApi saveTotalMsgApi = result.getData();
                    et_text_msg.setText(saveTotalMsgApi.remindContent);

                    processPics(saveTotalMsgApi.picList);
                    processAudios(saveTotalMsgApi.voiceList);
                    processArticles(saveTotalMsgApi);
                    processVideos(saveTotalMsgApi);

                } else {
                    ToastUtil.toastShortMessage(result.getMessage());
                }
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
    }

    private void processVideos(SaveTotalMsgApi saveTotalMsgApi) {
        if (null == saveTotalMsgApi.vedioInfo || saveTotalMsgApi.vedioInfo.size() == 0) {
            return;
        }

        videoBeans = saveTotalMsgApi.vedioInfo;
        videoAdapter.setNewData(videoBeans);
    }

    private void processPics(String picList) {
        if (null == picList || picList.isEmpty()) {
            return;
        }
        String[] split = picList.split(",");
        for (int i = 0; i < split.length; i++) {
            photos.add(split[i]);
        }
        ninePhotoLayout.setData(photos);
    }

    private void processAudios(String voiceList) {
        if (null == voiceList || voiceList.isEmpty()) {
            return;
        }
        String[] split = voiceList.split(",");
        for (int i = 0; i < split.length; i++) {
            UploadFileApi uploadFileApi = new UploadFileApi();
            uploadFileApi.fileLinkUrl = split[i];
            mUploadedAudios.add(uploadFileApi);
        }
        adapter.setNewData(mUploadedAudios);
    }

    private void processArticles(SaveTotalMsgApi saveTotalMsgApi) {
        if (null == saveTotalMsgApi.articleInfo || saveTotalMsgApi.articleInfo.size() == 0) {
            return;
        }

        articleBeans = saveTotalMsgApi.articleInfo;
        paperAdapter.setNewData(articleBeans);
    }

    private void initArticleListView() {
        list_articles.setLayoutManager(new LinearLayoutManager(getAttachActivity()));
        list_articles.addItemDecoration(MUtils.spaceDivider(
                DensityUtil.dip2px(getAttachActivity(), getAttachActivity().getResources().getDimension(R.dimen.qb_px_3)), false));
        paperAdapter = new PaperQuickAdapter(R.layout.item_paper, articleBeans);
        paperAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });
        list_articles.setAdapter(paperAdapter);
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
                String playPath = "";
                if (null == uploadFileApi.path || uploadFileApi.path.isEmpty()) {
                    playPath = uploadFileApi.fileLinkUrl;
                } else {
                    playPath = uploadFileApi.path;
                }
                AudioPlayer.getInstance().startPlay(playPath, new AudioPlayer.Callback() {
                    @Override
                    public void onCompletion(Boolean success) {
                        ToastUtil.toastLongMessage("播放完成");
                    }
                });
            }
        });
        list_audio.setAdapter(adapter);
    }

    /**
     * 处理订阅消息
     *
     * @param articleBean
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ArticleBean articleBean) {
        articleBeans.add(articleBean);
//        paperAdapter.notifyDataSetChanged();//TODO 刷新数据
        articles.add(articleBean.articleId + "");
        paperAdapter.setNewData(articleBeans);
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
                                        tv_add_audio.setText("点击录音");
                                        tv_add_audio.setTextColor(getAttachActivity().getResources().getColor(R.color.main_blue));
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

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    //    @SingleClick
    @OnClick({R.id.img_back, R.id.layout_add_audio, R.id.layout_add_video, R.id.layout_add_paper, R.id.tv_send_msg, R.id.img_add_pic})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_add_audio:
                checkPermission();
                break;
            case R.id.layout_add_video:
                homeActivity.switchSecondFragment(Constants.FRAGMENT_ADD_VIDEO, "");
                break;

            //可能需要检查权限，用带回调的封装的Utils.checkPermission
            case R.id.img_add_pic:
                Utils.checkPermission(getAttachActivity(), new Utils.PermissionCallBack() {
                    @Override
                    public void onPermissionResult(boolean permit) {
                        int canSelectNun = MAX_IMG - photos.size();
                        if (canSelectNun < 1) {
                            ToastUtil.toastShortMessage("最多选择9张照片");
                            return;
                        }
                        ImageSelectActivity.start(getAttachActivity(), canSelectNun, new ImageSelectActivity.OnPhotoSelectListener() {

                            @Override
                            public void onSelected(List<String> data) {
                                getImages(data);
                            }

                            @Override
                            public void onCancel() {
                            }
                        });
                    }
                });

                break;
            case R.id.layout_add_paper:
                homeActivity.switchSecondFragment(Constants.FRAGMENT_ADD_PAPER, "");
                break;
            case R.id.tv_send_msg:
                checkTotalMsg();

                break;
            case R.id.img_back:
                if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    homeActivity.getSupportFragmentManager().popBackStack();
                }
                break;
        }
    }

    private void checkTotalMsg() {
        if (et_text_msg.getText().toString().isEmpty()) {
            ToastUtil.toastShortMessage("请输入文本消息");
            return;
        }
        if (mUploadedAudios.size() == 0) {
            ToastUtil.toastShortMessage("请录制语音消息");
            return;
        }
//        if (videos.size() == 0){
//            ToastUtil.toastShortMessage("请添加视频消息");
//            return;
//        }

        if (photos.size() == 0) {
            ToastUtil.toastShortMessage("请添加图片消息");
            return;
        }

//        if (articles.size() == 0){
//            ToastUtil.toastShortMessage("请添加文章");
//            return;
//        }

        uploadedAudioMsgs();
    }

    int[] i = {0};

    private void uploadedAudioMsgs() {
        FileUploadUtils.INSTANCE.uploadAudio(NewMsgFragmentDisplay.this, mUploadedAudios.get(i[0]), new FileUploadUtils.OnAudioUploadCallback() {
            @Override
            public void onSuccess(HttpData<AudioUploadResultBean> result) {

                if (result.getCode() != 0) {
                    ToastUtil.toastLongMessage(result.getMessage());
                    return;
                }

                //要上传语音数据
                audiosFinal.add(result.getData().fileLinkUrl);

                //组装录音列表数据
                mUploadedAudios.get(i[0]).fileLinkUrl = result.getData().fileLinkUrl;
                mUploadedAudios.get(i[0]).id = result.getData().id;
                i[0]++;
                if (i[0] < mUploadedAudios.size()) {
                    uploadedAudioMsgs();
                } else {
                    //组装图片数据
                    for (int i = 0; i < photos.size(); i++) {
                        UpdateImageApi updateImageApi = new UpdateImageApi();
                        File file = new File(photos.get(i));
                        if (!file.isDirectory() && file.exists()) {
                            updateImageApi.file = file;
                            mUploadImages.add(updateImageApi);
                        }
                    }

                    //先置空
                    photosFinal.clear();
                    j = new int[]{0};

                    uploadedPicMsgs();
                }


            }

            @Override
            public void onFail() {
                ToastUtil.toastShortMessage("请求失败");
            }
        });
    }

    int[] j = {0};

    private void uploadedPicMsgs() {
        FileUploadUtils.INSTANCE.uploadPic(NewMsgFragmentDisplay.this, mUploadImages.get(j[0]), new FileUploadUtils.OnAudioUploadCallback() {
            @Override
            public void onSuccess(HttpData<AudioUploadResultBean> result) {

                if (result.getCode() != 0) {
                    ToastUtil.toastLongMessage(result.getMessage());
                    return;
                }

                photosFinal.add(result.getData().fileLinkUrl);

                //组装录音列表数据
                mUploadImages.get(j[0]).fileLinkUrl = result.getData().fileLinkUrl;
                mUploadImages.get(j[0]).id = result.getData().id;
                j[0]++;
                if (j[0] < mUploadImages.size()) {
                    uploadedPicMsgs();
                } else {
                    commitTotalMsg();
                }
            }

            @Override
            public void onFail() {
                ToastUtil.toastShortMessage("请求失败");
            }
        });
    }

    private void commitTotalMsg() {
        SaveTotalMsgApi saveTotalMsgApi = new SaveTotalMsgApi();
        saveTotalMsgApi.createTime = System.currentTimeMillis() + "";
        saveTotalMsgApi.picList = getProcessString(photosFinal);
        saveTotalMsgApi.remindContent = et_text_msg.getText().toString();
        saveTotalMsgApi.userId = getProcessString(mIds);
        saveTotalMsgApi.voiceList = getProcessString(audiosFinal);
        saveTotalMsgApi.articleList = getProcessString(articles);
        EasyHttp.post(this).api(saveTotalMsgApi).request(new HttpCallback<HttpData<List<SaveTotalMsgApi>>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(HttpData<List<SaveTotalMsgApi>> result) {
                super.onSucceed(result);
                if (result.getCode() == 0) {
                    ToastUtil.toastShortMessage("发送成功");
                    CustomHealthMessage message = new CustomHealthMessage();
                    message.title = "健康消息";
                    message.msgDetailId = result.getData().get(0).id + "";
                    message.content = saveTotalMsgApi.remindContent;
                    //这个属性区分消息类型 HelloChatController中onDraw方法去绘制布局
                    message.setType("CustomHealthMessage");
                    message.setDescription("健康管理消息");
                    EventBus.getDefault().post(message);
                    homeActivity.getSupportFragmentManager().popBackStack();
                } else {
                    ToastUtil.toastShortMessage(result.getMessage());
                }
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
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