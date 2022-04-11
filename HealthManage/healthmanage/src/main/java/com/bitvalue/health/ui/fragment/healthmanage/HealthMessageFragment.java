package com.bitvalue.health.ui.fragment.healthmanage;


import static com.bitvalue.health.util.Constants.MAX_IMG;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.api.ApiResult;
import com.bitvalue.health.api.requestbean.SaveTotalMsgApi;
import com.bitvalue.health.api.requestbean.UpdateImageApi;
import com.bitvalue.health.api.requestbean.UploadFileApi;
import com.bitvalue.health.api.responsebean.ArticleBean;
import com.bitvalue.health.api.responsebean.AudioUploadResultBean;
import com.bitvalue.health.api.responsebean.ImageModel;
import com.bitvalue.health.api.responsebean.PaperBean;
import com.bitvalue.health.api.responsebean.VideoResultBean;
import com.bitvalue.health.api.eventbusbean.AddVideoObject;
import com.bitvalue.health.api.eventbusbean.GetMissionObj;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.OnItemDelete;
import com.bitvalue.health.net.FileUploadUtils;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.activity.media.ImagePreviewActivity;
import com.bitvalue.health.ui.activity.media.ImageSelectActivity;
import com.bitvalue.health.ui.activity.media.VideoPlayActivity;
import com.bitvalue.health.ui.adapter.AudioAdapter;
import com.bitvalue.health.ui.adapter.PaperQuickAdapter;
import com.bitvalue.health.ui.adapter.VideoQuickAdapter;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.DensityUtil;
import com.bitvalue.health.util.MUtils;
import com.bitvalue.health.util.PermissionUtil;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.base.IUIKitCallBack;
import com.bitvalue.sdk.collab.base.TUIKitListenerManager;
import com.bitvalue.sdk.collab.component.AudioPlayer;
import com.bitvalue.sdk.collab.helper.CustomHealthMessage;
import com.bitvalue.sdk.collab.modules.chat.GroupChatManagerKit;
import com.bitvalue.sdk.collab.modules.group.info.GroupInfo;
import com.bitvalue.sdk.collab.modules.message.MessageInfo;
import com.bitvalue.sdk.collab.modules.message.MessageInfoUtil;
import com.bitvalue.sdk.collab.utils.BackgroundTasks;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.tencent.imsdk.v2.V2TIMConversation;

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

/***
 * 健康管理模块  健康消息界面
 */
public class HealthMessageFragment extends BaseFragment implements BGANinePhotoLayout.Delegate {
//public class NewMsgFragment extends AppFragment implements BGASortableNinePhotoLayout.Delegate {

    @BindView(R.id.img_add_pic)
    ImageView img_add_pic;

    //https://github.com/bingoogolapple/BGAPhotoPicker-Android  第三方依赖
    @BindView(R.id.npl_item_moment_photos)
    BGANinePhotoLayout ninePhotoLayout;

    @BindView(R.id.list_articles)
    RecyclerView list_articles;

    @BindView(R.id.list_videos)
    RecyclerView list_videos;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_add_audio)
    TextView tv_add_audio;

    @BindView(R.id.tv_send_msg)
    TextView tv_send_msg;

    @BindView(R.id.et_text_msg)
    EditText et_text_msg;

    @BindView(R.id.layout_add_audio)
    LinearLayout layout_add_audio;

    @BindView(R.id.layout_add_video)
    LinearLayout layout_add_video;

    @BindView(R.id.layout_add_paper)
    LinearLayout layout_add_paper;

    @BindView(R.id.list_audio)
    RecyclerView list_audio;


    private String msgType;
    private boolean isRecording;
    private List<UploadFileApi> mUploadedAudios = new ArrayList<>();
    private List<UpdateImageApi> mUploadImages = new ArrayList<>();
    private ArrayList<String> videos = new ArrayList<>();
    private List<ArticleBean> articleBeans = new ArrayList<>();
    private List<VideoResultBean.ListDTO> videoBeans = new ArrayList<>();

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
    int[] i = {0};
    int[] j = {0};

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) getActivity();
    }


    /**
     * 初始化控件 并 获取上个界面传过来的数据
     *
     * @param rootView
     */
    @Override
    public void initView(View rootView) {
        EventBus.getDefault().register(this);
        msgType = getArguments().getString(Constants.MSG_TYPE);
        mIds = getArguments().getStringArrayList(Constants.MSG_IDS);
        if (msgType.equals(Constants.MSG_SINGLE)) {
            tv_title.setText(getString(R.string.health_mesage));
        } else {
            tv_title.setText(getString(R.string.health_mass_mes));
        }

        ninePhotoLayout.setDelegate(HealthMessageFragment.this);
        ninePhotoLayout.setData(photos);

        initAudioListView();  //初始化语音列表
        initArticleListView(); //初始化文章列表
        initVideoListView(); //初始化视频列表
    }

    /**
     * 初始化文章列表
     */
    private void initArticleListView() {
        list_articles.setLayoutManager(new LinearLayoutManager(getActivity()));
        list_articles.addItemDecoration(MUtils.spaceDivider(
                DensityUtil.dip2px(getActivity(), getActivity().getResources().getDimension(R.dimen.qb_px_3)), false));
        paperAdapter = new PaperQuickAdapter(R.layout.item_paper, articleBeans);
        paperAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //点击文章 预览 进入 文章预览界面
                homeActivity.switchSecondFragment(Constants.FRAGMENT_ARTICLE_DETAIL, articleBeans.get(position));
            }
        });
        list_articles.setAdapter(paperAdapter);

        paperAdapter.setOnItemDelete(position -> {
            articleBeans.remove(position);
            articles.remove(position);
            paperAdapter.setNewData(articleBeans);
        });
    }


    //初始化视频列表
    private void initVideoListView() {
        list_videos.setLayoutManager(new LinearLayoutManager(getActivity()));
        list_videos.addItemDecoration(MUtils.spaceDivider(
                DensityUtil.dip2px(getActivity(), getActivity().getResources().getDimension(R.dimen.qb_px_3)), false));
        videoAdapter = new VideoQuickAdapter(R.layout.item_paper, videoBeans);
        videoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                new VideoPlayActivity.Builder()
                        .setVideoTitle(videoBeans.get(position).title)
                        .setVideoSource(videoBeans.get(position).vedioUrl)
                        .start(getActivity());
            }
        });
        videoAdapter.setOnItemDelete(position -> {
            videoBeans.remove(position);
            videos.remove(position);
            videoAdapter.setNewData(videoBeans);
        });
        list_videos.setAdapter(videoAdapter);
    }


    /**
     * 初始化语音列表
     */
    private void initAudioListView() {
        list_audio.setLayoutManager(new LinearLayoutManager(getActivity()));
        list_audio.addItemDecoration(MUtils.spaceDivider(
                DensityUtil.dip2px(getActivity(), getActivity().getResources().getDimension(R.dimen.qb_px_1)), false));
        adapter = new AudioAdapter(R.layout.item_audio, mUploadedAudios);

        adapter.setOnItemDelete(new OnItemDelete() {
            @Override
            public void onItemDelete(int position) {
                mUploadedAudios.remove(position);
                adapter.setNewData(mUploadedAudios);
            }
        });

        list_audio.setAdapter(adapter);
    }

    /**
     * 处理订阅消息 科普文章
     *
     * @param articleBean
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ArticleBean articleBean) {
        for (ArticleBean articleBeanOld : articleBeans) {//去重
            if (articleBeanOld.articleId == articleBean.articleId) {
                ToastUtil.toastShortMessage("请勿添加重复的文章");
                return;
            }
        }
        articleBeans.add(articleBean);
        articles.add(articleBean.articleId + "");
        paperAdapter.setNewData(articleBeans);
    }

    /**
     * 处理订阅消息   视频
     *
     * @param listDTO
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(VideoResultBean.ListDTO listDTO) {
        if (listDTO.videoFor.equals(Constants.VIDEO_FOR_MSG)) {
            for (VideoResultBean.ListDTO videoOld : videoBeans) {//去重
                if (videoOld.vedioId == listDTO.vedioId) {
                    ToastUtil.toastShortMessage("请勿添加重复的视频");
                    return;
                }
            }

            videoBeans.add(listDTO);
            videos.add(listDTO.vedioId + "");
            videoAdapter.setNewData(videoBeans);
        }
    }

    /***
     * 语音、视频、图片的权限处理
     */
    public void checkPermission() {
        RxPermissions.getInstance(getActivity())
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
                                tv_add_audio.setTextColor(getActivity().getResources().getColor(R.color.orange));
                                AudioPlayer.getInstance().startRecord(new AudioPlayer.Callback() {
                                    @Override
                                    public void onCompletion(Boolean success) {
                                        tv_add_audio.setText("点击录音");
                                        tv_add_audio.setTextColor(getActivity().getResources().getColor(R.color.main_blue));
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
                            new AlertDialog.Builder(getActivity())
                                    .setMessage("请先进行相关授权，再重启APP！")
                                    .setPositiveButton("进入设置", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
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

    @Override
    public void initData() {
        mImageModels.add(new ImageModel(Constants.IMG_ADD));
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_new_msg;
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

    @OnClick({R.id.layout_back, R.id.layout_add_audio, R.id.layout_add_video, R.id.layout_add_paper, R.id.tv_send_msg, R.id.img_add_pic})
    public void onClick(View view) {
        switch (view.getId()) {
//            点击 录音 事件触发
            case R.id.layout_add_audio:
                checkPermission();
                break;

            //返回
            case R.id.layout_back:
                if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    homeActivity.getSupportFragmentManager().popBackStack();
                }
                break;

            //   点击添加视频  事件触发
            case R.id.layout_add_video:
                AddVideoObject addVideoObject = new AddVideoObject();
                addVideoObject.videoFor = Constants.VIDEO_FOR_MSG;
                homeActivity.switchSecondFragment(Constants.FRAGMENT_ADD_VIDEO, addVideoObject); //跳转至添加视频界面
                break;

//                点击添加图片  事件触发
            case R.id.img_add_pic:
                PermissionUtil.checkPermission(getActivity(), permit -> {
                    int canSelectNun = MAX_IMG - photos.size();
                    if (canSelectNun < 1) {
                        ToastUtil.toastShortMessage("最多选择9张照片");
                        return;
                    }
                    ImageSelectActivity.start(homeActivity, canSelectNun, new ImageSelectActivity.OnPhotoSelectListener() {

                        @Override
                        public void onSelected(List<String> data) {
                            getImages(data);
                        }

                        @Override
                        public void onCancel() {
                        }
                    });
                });

                break;

            //                点击添加文章  事件触发
            case R.id.layout_add_paper:
                homeActivity.switchSecondFragment(Constants.FRAGMENT_ADD_PAPER, new GetMissionObj());  //跳转至添加文章界面
                break;
            case R.id.tv_send_msg:
                checkTotalMsg();
                break;
        }
    }

    /**
     * 只要一项有内容就可以提交
     */
    private void checkTotalMsg() {
        if (!et_text_msg.getText().toString().isEmpty() || (mUploadedAudios.size() > 0) || (videos.size() > 0)
                || (photos.size() > 0) || (articles.size() > 0)) {
            showLoading();
            //可以提交
            if (mUploadedAudios.size() > 0) {//上传语音消息后，判断图片消息再上传图片，再上传总消息
                uploadedAudioMsgs();
            } else if (mUploadedAudios.size() == 0 && photos.size() > 0) {//没有语音且有图片，上传图片后再上传总消息
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
            } else {//
                commitTotalMsg();
            }
        } else {
            ToastUtil.toastShortMessage("请输入至少一种类型的消息");
            return;
        }
    }


    /**
     * 上传语音
     */
    private void uploadedAudioMsgs() {
        FileUploadUtils.INSTANCE.uploadAudio(HealthMessageFragment.this, mUploadedAudios.get(i[0]), new FileUploadUtils.OnAudioUploadCallback() {
            @Override
            public void onSuccess(ApiResult<AudioUploadResultBean> result) {
                hideDialog();
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
                } else if (photos.size() > 0) {
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
                } else {
                    commitTotalMsg();
                }


            }

            @Override
            public void onFail() {
                hideDialog();
                ToastUtil.toastShortMessage("请求失败");
            }
        });
    }


    /**
     * 上传图片
     */
    private void uploadedPicMsgs() {
        FileUploadUtils.INSTANCE.uploadPic(HealthMessageFragment.this, mUploadImages.get(j[0]), new FileUploadUtils.OnAudioUploadCallback() {
            @Override
            public void onSuccess(ApiResult<AudioUploadResultBean> result) {
                hideDialog();
                if (result.getCode() != 0) {
                    ToastUtil.toastLongMessage(result.getMessage());
                    return;
                }

                photosFinal.add(result.getData().fileLinkUrl);

                //组装图片列表数据
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
                hideDialog();
                ToastUtil.toastShortMessage("请求失败");
            }
        });
    }


    /**
     * 消息列表归总 并提交
     */
    private void commitTotalMsg() {
        SaveTotalMsgApi saveTotalMsgApi = new SaveTotalMsgApi();
        saveTotalMsgApi.createTime = TimeUtils.getTime(System.currentTimeMillis(), TimeUtils.YY_MM_DD_FORMAT_4);
        if (photosFinal.size() > 0) {
            saveTotalMsgApi.picList = getProcessString(photosFinal);
        }
        saveTotalMsgApi.remindContent = et_text_msg.getText().toString();
        saveTotalMsgApi.userId = getProcessString(mIds);

        if (audiosFinal.size() > 0) {
            saveTotalMsgApi.voiceList = getProcessString(audiosFinal);
        }

        if (videos.size() > 0) {
            saveTotalMsgApi.videoList = getProcessString(videos);
        }

        if (articles.size() > 0) {
            saveTotalMsgApi.articleList = getProcessString(articles);
        }

        EasyHttp.post(this).api(saveTotalMsgApi).request(new HttpCallback<ApiResult<List<SaveTotalMsgApi>>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(ApiResult<List<SaveTotalMsgApi>> result) {
                super.onSucceed(result);
                hideDialog();
                //增加判空
                if (result == null || result.getData() == null) {
                    return;
                }
                if (result.getCode() == 0) {
                    //step1 组装消息
                    ToastUtil.toastShortMessage("发送成功");
                    CustomHealthMessage message = new CustomHealthMessage();
                    message.title = "健康消息";
                    message.msgDetailId = result.getData().get(0).id + "";
                    message.contentId = result.getData().get(0).contentId;
                    message.userId = mIds;
                    message.content = saveTotalMsgApi.remindContent;
                    //这个属性区分消息类型 HelloChatController中onDraw方法去绘制布局
                    message.setType("CustomHealthMessage");
                    message.setDescription("健康管理消息");

                    //step1 分开群发单发消息
                    if (msgType.equals(Constants.MSG_SINGLE)) {//单发消息
                        EventBus.getDefault().post(message);
                    } else {//群发消息
                        for (int i = 0; i < mIds.size(); i++) {
                            MessageInfo info = MessageInfoUtil.buildCustomMessage(new Gson().toJson(message), message.description, null);
                            GroupChatManagerKit groupChatManagerKit = GroupChatManagerKit.getInstance();

                            GroupInfo groupInfo = new GroupInfo();
                            groupInfo.setId(mIds.get(i));
                            groupInfo.setType(V2TIMConversation.V2TIM_GROUP);
                            groupChatManagerKit.setCurrentChatInfo(groupInfo);
                            TUIKitListenerManager.getInstance().setMessageSender(groupChatManagerKit);
                            groupChatManagerKit.sendMessage(info, false, new IUIKitCallBack() {
                                @Override
                                public void onSuccess(Object data) {
                                    BackgroundTasks.getInstance().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                        }
                                    });
                                }

                                @Override
                                public void onError(String module, int errCode, String errMsg) {
                                    ToastUtil.toastLongMessage(errMsg);
                                }
                            });
                        }
                    }

                    //step3 发送完成后关闭本页面
                    homeActivity.getSupportFragmentManager().popBackStack();
                } else {
                    ToastUtil.toastShortMessage(result.getMessage());
                }
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
                hideDialog();
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

//    @Override   //后期扩展可编辑图片的预留的控件回调
//    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
//        Utils.checkPermission(getAttachActivity(), new Utils.PermissionCallBack() {
//            @Override
//            public void onPermissionResult(boolean permit) {
//                int canSelectNun = MAX_IMG - photos.size();
//                if (canSelectNun < 1) {
//                    ToastUtil.toastShortMessage("最多选择9张照片");
//                    return;
//                }
//                ImageSelectActivity.start(getAttachActivity(), canSelectNun, new ImageSelectActivity.OnPhotoSelectListener() {
//
//                    @Override
//                    public void onSelected(List<String> data) {
//                        getImages(data);
//                    }
//
//                    @Override
//                    public void onCancel() {
//                    }
//                });
//            }
//        });
//    }
//
//    @Override
//    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
//        photos.remove(position);
//        ninePhotoLayout.setData(photos);
//    }
//
//    @Override
//    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
//        ImagePreviewActivity.start(homeActivity, model);
//    }
//
//    @Override
//    public void onNinePhotoItemExchanged(BGASortableNinePhotoLayout sortableNinePhotoLayout, int fromPosition, int toPosition, ArrayList<String> models) {
//        Toast.makeText(homeActivity, "排序发生变化", Toast.LENGTH_SHORT).show();
//    }

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
