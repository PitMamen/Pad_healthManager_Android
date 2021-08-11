package com.bitvalue.healthmanage.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.bitvalue.healthmanage.BuildConfig;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.widget.recyclerView.DividerItemDecoration;
import com.bumptech.glide.Glide;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;


/**
 * some utils
 * Created by Administrator on 2017/5/2.
 */

public class MUtils {

    /**
     * RV默认灰色分隔线
     */
    public static HorizontalDividerItemDecoration defDivider(Context mContext) {
        return new HorizontalDividerItemDecoration.Builder(mContext).size(1).colorResId(R.color.divider).build();
    }

    public static HorizontalDividerItemDecoration defDivider(Context mContext, int size) {
        return new HorizontalDividerItemDecoration.Builder(mContext).size(size)
                .colorResId(R.color.divider).build();
    }

    /**
     * RV透明间距分割线
     */
    public static DividerItemDecoration spaceDivider(Context mContext, int pxSize) {
        return spaceDivider(pxSize, false);
    }

    public static DividerItemDecoration spaceDivider(int pxSize, boolean showTop) {
        return new DividerItemDecoration(pxSize, Color.TRANSPARENT, showTop);
    }


//    private static WeakReference<BufferDialog> pd;

    private static int dialogCount = 0;
    private static final Object lock = new Object();

    // 请求网络提示
//    public static synchronized void startnet(Context context, String str) {
//        synchronized (lock) {
//            dialogCount++;
//            if (pd == null || pd.get() == null) {
//                pd = new WeakReference<>(new BufferDialog(context, str));
//                pd.get().setCanceledOnTouchOutside(false);
//                pd.get().setOnKeyListener(new DialogInterface.OnKeyListener() {
//                    @Override
//                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//                            pd.get().dismiss();
//
//                        }
//                        return false;
//                    }
//                });
//            } else {
//                pd.get().setTitle(str);
//            }
//            if (!pd.get().isShowing())
//                pd.get().show();
//        }
//    }

//    // 请求网络提示
//    public static synchronized void startnet(Activity activity, String str) {
//        synchronized (lock) {
//            dialogCount++;
//            if (pd == null || pd.get() == null) {
//                pd = new WeakReference<>(new BufferDialog(activity, str));
//                pd.get().setCanceledOnTouchOutside(false);
//                pd.get().setOnKeyListener(new DialogInterface.OnKeyListener() {
//                    @Override
//                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//                            pd.get().dismiss();
//
//                        }
//                        return false;
//                    }
//                });
//            } else {
//                pd.get().setTitle(str);
//            }
//            if (!pd.get().isShowing() && !activity.isFinishing()) {
//                pd.get().show();
//            }
//        }
//    }


//    // 请求网络提示
//    public static void startnet(Context context) {
//        startnet(context, context.getString(R.string.dialog_text));
//    }
//
//    // 请求网络提示
//    public static void startnet(Activity activity) {
//        startnet(activity, activity.getString(R.string.dialog_text));
//    }

    private static final String TAG = "MUtils";

    // 结束
//    public static synchronized void endnet() {
//        synchronized (lock) {
//            if (pd != null) {
//                if (pd.get() != null && dialogCount <= 1) {
//                    pd.get().dismiss();
//                    pd.clear();
//                    pd = null;
//                }
//            }
//            dialogCount--;
//            if (dialogCount < 0)
//                dialogCount = 0;
//        }
//    }

    public static void loadImage2Round(Context context, String url, int p, int err, ImageView tager) {
        Glide.with(context).load(url).dontTransform().dontAnimate()
                .placeholder(p).error(err).into(tager);
    }

    public static void loadImage2Round(Context context, String url, int def, ImageView tager) {
        loadImage2Round(context, url, def, def, tager);
    }

//    public static void loadImage(Context context, String url, int def, ImageView tager) {
//        loadImage(context, url, def, tager, false, false);
//    }
//
//    public static void loadImage(Context context, File file, int def, ImageView tager) {
//        loadImage(context, "file://" + file.getPath(), def, tager);
//    }
//
//    public static void loadImage(Context mContext, String head_image, int default_student_icon, ImageView buyIcon, boolean dontAnim) {
//        loadImage(mContext, head_image, default_student_icon, buyIcon, dontAnim, false);
//    }
//
//    /**
//     * 加载图片
//     */
//    private static void loadImage(Context context, String url, int def, ImageView tager,
//                                  boolean dontAnim, boolean asBitmap) {
//        DrawableTypeRequest<String> glide = Glide.with(context).load(url);
//        if (asBitmap) glide.asBitmap();
//        if (def != -1) glide.placeholder(def).error(def);
//        if (dontAnim) glide.dontAnimate();
//        glide.into(tager);
//    }

    /**
     * 解决小米手机上获取图片路径为null的情况
     *
     * @param intent
     * @return
     */
    public static Uri getUri(Intent intent, Context context) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.ImageColumns._ID},
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }

}
