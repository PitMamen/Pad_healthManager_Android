package com.bitvalue.health.ui.activity.media;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.base.BaseAdapter;
import com.bitvalue.health.ui.activity.AppActivity;
import com.bitvalue.health.ui.adapter.ImageSelectAdapter;
import com.bitvalue.health.util.GridSpaceDecoration;
import com.bitvalue.health.util.IntentKey;
import com.bitvalue.health.util.ThreadPoolManager;
import com.bitvalue.health.util.action.StatusAction;
import com.bitvalue.health.util.action.StatusLayout;
import com.bitvalue.health.util.aop.Permissions;
import com.bitvalue.health.util.aop.SingleClick;
import com.bitvalue.health.util.customview.dialog.AlbumDialog;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * author : Android pxk
 * time   : 2021/11/01
 * desc   : 选择图片界面
 */
public final class ImageSelectActivity extends AppActivity
        implements StatusAction, Runnable,
        BaseAdapter.OnItemClickListener,
        BaseAdapter.OnItemLongClickListener,
        BaseAdapter.OnChildClickListener {



    /**
     * 最大选中
     */
    private int mMaxSelect = 1;
    /**
     * 选中列表
     */
    private final ArrayList<String> mSelectImage = new ArrayList<>();

    /**
     * 全部图片
     */
    private final ArrayList<String> mAllImage = new ArrayList<>();
    /**
     * 图片专辑
     */
    private final HashMap<String, List<String>> mAllAlbum = new HashMap<>();

    /**
     * 专辑选择对话框
     */
    private AlbumDialog.Builder mAlbumDialog;


    @BindView(R.id.hl_image_select_hint)
    StatusLayout mStatusLayout;

    @BindView(R.id.rv_image_select_list)
     RecyclerView mRecyclerView;

    @BindView(R.id.fab_image_select_floating)
     FloatingActionButton mFloatingView;

    private ImageSelectAdapter mAdapter;


    public static void start(com.bitvalue.health.base.BaseActivity activity, OnPhotoSelectListener listener) {
        start(activity, 1, listener);
    }

    @Permissions({Permission.MANAGE_EXTERNAL_STORAGE})
    public static void start(com.bitvalue.health.base.BaseActivity activity, int maxSelect, OnPhotoSelectListener listener) {
        if (maxSelect < 1) {
            // 最少要选择一个图片
            throw new IllegalArgumentException("At least one picture must be selected");
        }
        Intent intent = new Intent(activity, ImageSelectActivity.class);
        intent.putExtra(IntentKey.AMOUNT, maxSelect);
        activity.startActivityForResult(intent, (resultCode, data) -> {

            if (listener == null || data == null) {
                return;
            }

            ArrayList<String> list = data.getStringArrayListExtra(IntentKey.IMAGE);
            if (list == null || list.isEmpty()) {
                listener.onCancel();
                return;
            }

            Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                if (!new File(iterator.next()).isFile()) {
                    iterator.remove();
                }
            }

            if (resultCode == RESULT_OK && !list.isEmpty()) {
                listener.onSelected(list);
                return;
            }
            listener.onCancel();
        });
    }



    @Override
    protected int getLayoutId() {
        return R.layout.image_select_activity;
    }

    @Override
    protected void initView() {
        checkPermission();  //权限申请

        setOnClickListener(mFloatingView);

        mAdapter = new ImageSelectAdapter(this, mSelectImage);
        mAdapter.setOnChildClickListener(R.id.fl_image_select_check, this);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        // 禁用动画效果
        mRecyclerView.setItemAnimator(null);
        // 添加分割线
        mRecyclerView.addItemDecoration(new GridSpaceDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics())));
    }

    public void checkPermission() {
        RxPermissions.getInstance(this)
                .request(Manifest.permission.CAMERA
                        , Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                        , Manifest.permission.READ_PHONE_STATE
                        , Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.RECORD_AUDIO)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean flag) {
                        if (!flag) {
                            ToastUtil.toastShortMessage("请授权以获取图片");
                        }
                    }
                });

    }

    @Override
    protected void initData() {
        // 获取最大的选择数
        mMaxSelect = getInt(IntentKey.AMOUNT, mMaxSelect);

        // 显示加载进度条
        showLoading();
        // 加载图片列表
        ThreadPoolManager.getInstance().execute(this);
    }

    @Override
    public StatusLayout getStatusLayout() {
        return mStatusLayout;
    }

    @SingleClick
    @Override
    public void onRightClick(View view) {
        if (mAllImage.isEmpty()) {
            return;
        }

        ArrayList<AlbumDialog.AlbumInfo> data = new ArrayList<>(mAllAlbum.size() + 1);

        int count = 0;
        Set<String> keys = mAllAlbum.keySet();
        for (String key : keys) {
            List<String> list = mAllAlbum.get(key);
            if (list != null && !list.isEmpty()) {
                count += list.size();
                data.add(new AlbumDialog.AlbumInfo(list.get(0), key, String.format(getString(R.string.image_select_total), list.size()), mAdapter.getData() == list));
            }
        }
        data.add(0, new AlbumDialog.AlbumInfo(mAllImage.get(0), getString(R.string.image_select_all), String.format(getString(R.string.image_select_total), count), mAdapter.getData() == mAllImage));

        if (mAlbumDialog == null) {
            mAlbumDialog = new AlbumDialog.Builder(this)
                    .setListener((dialog, position, bean) -> {

                        setRightTitle(bean.getName());
                        // 滚动回第一个位置
                        mRecyclerView.scrollToPosition(0);
                        if (position == 0) {
                            mAdapter.setData(mAllImage);
                        } else {
                            mAdapter.setData(mAllAlbum.get(bean.getName()));
                        }
                        // 执行列表动画
                        mRecyclerView.setLayoutAnimation(AnimationUtils.
                                loadLayoutAnimation(getActivity(), R.anim.from_right_layout));
                        mRecyclerView.scheduleLayoutAnimation();
                    });
        }
        mAlbumDialog.setData(data)
                .show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Iterator<String> iterator = mSelectImage.iterator();
        // 遍历判断选择了的图片是否被删除了
        while (iterator.hasNext()) {
            String path = iterator.next();
            File file = new File(path);
            if (file.isFile()) {
                continue;
            }

            iterator.remove();
            mAllImage.remove(path);

            File parentFile = file.getParentFile();
            if (parentFile != null) {
                List<String> data = mAllAlbum.get(parentFile.getName());
                if (data != null) {
                    data.remove(path);
                }
                mAdapter.notifyDataSetChanged();

                if (mSelectImage.isEmpty()) {
                    mFloatingView.setImageResource(R.drawable.camera_ic);
                } else {
                    mFloatingView.setImageResource(R.drawable.succeed_ic);
                }
            }
        }
    }

    @SingleClick
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_image_select_floating) {
            if (mSelectImage.isEmpty()) {
                // 点击拍照
                CameraActivity.start(this, file -> {

                    // 当前选中图片的数量必须小于最大选中数
                    if (mSelectImage.size() < mMaxSelect) {
                        mSelectImage.add(file.getPath());
                    }

                    // 这里需要延迟刷新，否则可能会找不到拍照的图片
                    postDelayed(() -> {
                        // 重新加载图片列表
                        ThreadPoolManager.getInstance().execute(ImageSelectActivity.this);
                    }, 1000);
                });
                return;
            }

            // 完成选择
            setResult(RESULT_OK, new Intent().putStringArrayListExtra(IntentKey.IMAGE, mSelectImage));
            finish();
        }
    }

    /**
     * {@link BaseAdapter.OnItemClickListener}
     *
     * @param recyclerView RecyclerView对象
     * @param itemView     被点击的条目对象
     * @param position     被点击的条目位置
     */
    @Override
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
        ImagePreviewActivity.start(getActivity(), mAdapter.getData(), position);
    }

    /**
     * {@link BaseAdapter.OnItemLongClickListener}
     *
     * @param recyclerView RecyclerView对象
     * @param itemView     被点击的条目对象
     * @param position     被点击的条目位置
     */
    @Override
    public boolean onItemLongClick(RecyclerView recyclerView, View itemView, int position) {
        if (mSelectImage.size() < mMaxSelect) {
            // 长按的时候模拟选中
            return itemView.findViewById(R.id.fl_image_select_check).performClick();
        }
        return false;
    }

    /**
     * {@link BaseAdapter.OnChildClickListener}
     *
     * @param recyclerView RecyclerView对象
     * @param childView    被点击的条目子 View Id
     * @param position     被点击的条目位置
     */
    @Override
    public void onChildClick(RecyclerView recyclerView, View childView, int position) {
        if (childView.getId() == R.id.fl_image_select_check) {

            String path = mAdapter.getItem(position);
            File file = new File(path);
            if (!file.isFile()) {
                mAdapter.removeItem(position);
                toast(R.string.image_select_error);
                return;
            }

            if (mSelectImage.contains(path)) {
                mSelectImage.remove(path);

                if (mSelectImage.isEmpty()) {
                    mFloatingView.hide();
                    postDelayed(() -> {
                        mFloatingView.setImageResource(R.drawable.camera_ic);
                        mFloatingView.show();
                    }, 200);
                }

                mAdapter.notifyItemChanged(position);
                return;
            }

            if (mMaxSelect == 1 && mSelectImage.size() == 1) {

                List<String> data = mAdapter.getData();
                if (data != null) {
                    int index = data.indexOf(mSelectImage.remove(0));
                    if (index != -1) {
                        mAdapter.notifyItemChanged(index);
                    }
                }
                mSelectImage.add(path);

            } else if (mSelectImage.size() < mMaxSelect) {

                mSelectImage.add(path);

                if (mSelectImage.size() == 1) {
                    mFloatingView.hide();
                    postDelayed(() -> {
                        mFloatingView.setImageResource(R.drawable.succeed_ic);
                        mFloatingView.show();
                    }, 200);
                }
            } else {
                toast(String.format(getString(R.string.image_select_max_hint), mMaxSelect));
            }
            mAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void run() {
        mAllAlbum.clear();
        mAllImage.clear();

        final Uri contentUri = MediaStore.Files.getContentUri("external");
        final String sortOrder = MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC";
        final String selection = "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)" + " AND " + MediaStore.MediaColumns.SIZE + ">0";

        ContentResolver contentResolver = getContentResolver();
        String[] projections = new String[]{MediaStore.Files.FileColumns._ID, MediaStore.MediaColumns.DATA,
                MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.DATE_MODIFIED,
                MediaStore.MediaColumns.MIME_TYPE, MediaStore.MediaColumns.WIDTH,
                MediaStore.MediaColumns.HEIGHT, MediaStore.MediaColumns.SIZE};

        Cursor cursor = null;
        if (XXPermissions.isGrantedPermission(this, Permission.MANAGE_EXTERNAL_STORAGE)) {
            cursor = contentResolver.query(contentUri, projections, selection, new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)}, sortOrder);
        }
        if (cursor != null && cursor.moveToFirst()) {

            int pathIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
            int mimeTypeIndex = cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE);
            int sizeIndex = cursor.getColumnIndex(MediaStore.MediaColumns.SIZE);

            do {
                long size = cursor.getLong(sizeIndex);
                // 图片大小不得小于 1 KB
                if (size < 1024) {
                    continue;
                }

                String type = cursor.getString(mimeTypeIndex);
                String path = cursor.getString(pathIndex);
                if (TextUtils.isEmpty(path) || TextUtils.isEmpty(type)) {
                    continue;
                }

                File file = new File(path);
                if (!file.exists() || !file.isFile()) {
                    continue;
                }

                File parentFile = file.getParentFile();
                if (parentFile != null) {
                    // 获取目录名作为专辑名称
                    String albumName = parentFile.getName();
                    List<String> data = mAllAlbum.get(albumName);
                    if (data == null) {
                        data = new ArrayList<>();
                        mAllAlbum.put(albumName, data);
                    }
                    data.add(path);
                    mAllImage.add(path);
                }

            } while (cursor.moveToNext());

            cursor.close();
        }

        postDelayed(() -> {
            // 滚动回第一个位置
            mRecyclerView.scrollToPosition(0);
            // 设置新的列表数据
            mAdapter.setData(mAllImage);

            if (mSelectImage.isEmpty()) {
                mFloatingView.setImageResource(R.drawable.camera_ic);
            } else {
                mFloatingView.setImageResource(R.drawable.succeed_ic);
            }

            // 执行列表动画
            mRecyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.fall_down_layout));
            mRecyclerView.scheduleLayoutAnimation();

            if (mAllImage.isEmpty()) {
                // 显示空布局
                showEmpty();
                // 设置右标题
                setRightTitle(null);
            } else {
                // 显示加载完成
                showComplete();
                // 设置右标题
                setRightTitle(R.string.image_select_all);
            }
        }, 500);
    }

    /**
     * 图片选择监听
     */
    public interface OnPhotoSelectListener {

        /**
         * 选择回调
         *
         * @param data 图片列表
         */
        void onSelected(List<String> data);

        /**
         * 取消回调
         */
        default void onCancel() {
        }
    }
}