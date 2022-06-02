package com.bitvalue.health.service;

import static com.bitvalue.health.util.Constants.APK_LOCAL_PATH;
import static com.bitvalue.health.util.Constants.APK_URL;
import static com.bitvalue.health.util.Constants.LOG_FAIL;
import static com.bitvalue.health.util.Constants.LOG_RECORD;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.Process;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import com.bitvalue.health.ui.activity.AppUpdateDialog;
import com.bitvalue.health.ui.activity.SplashActivity;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.CrashHandler;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.SharedPreManager;
import com.bitvalue.health.util.SlientInstall;
import com.bitvalue.health.util.VersionUtils;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.component.photoview.Util;
import com.bitvalue.sdk.collab.utils.FileUtil;
import com.hjq.toast.ToastUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * @author created by bitvalue
 * @data : 05/23
 * 后台下载apk  服务
 */
public class DownApkService extends Service {
    private static final String TAG = DownApkService.class.getSimpleName();
    private final int NotificationID = 0x10000;
    private NotificationManager mNotificationManager = null;
    private NotificationCompat.Builder builder;

    // 文件保存路径(如果有SD卡就保存SD卡,如果没有SD卡就保存到手机包名下的路径)
    private String APK_dir = "";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        initApkDir();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 接收Intent传来的参数:
        // 文件下载路径
        String apk_url = intent.getStringExtra(APK_URL);
        if (apk_url.endsWith("apk")) {
            DownFile(apk_url, APK_dir + "HealthManage.apk");
        } else {
            ToastUtils.show("更新失败,目标文件非apk文件");
            stopSelf();
            CrashHandler.getInstance().handleException("更新失败,目标文件非apk文件", LOG_FAIL);
        }


        return super.onStartCommand(intent, flags, startId);
    }

    private void initApkDir() {
        /**
         * 创建路径的时候一定要用[/],不能使用[\],但是创建文件夹加文件的时候可以使用[\].
         * [/]符号是Linux系统路径分隔符,而[\]是windows系统路径分隔符 Android内核是Linux.
         */
        if (isHasSdcard())// 判断是否插入SD卡
        {
            APK_dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/apk/"; // 保存到SD卡路径下
            Log.e(TAG, "有sd卡 ");
        } else {
            APK_dir = getApplicationContext().getFilesDir().getAbsolutePath() + "/VersionChecker/"; // 保存到app的包名路径下
            Log.e(TAG, "无 sd 卡");
        }
        Log.e(TAG, "文件将下载至: " + APK_dir);
        CrashHandler.getInstance().handleException("文件将下载至:" + APK_dir + " 位置", LOG_RECORD);
        File destDir = new File(APK_dir);
        if (!destDir.exists()) {// 判断文件夹是否存在
            destDir.mkdirs();
        }
    }

    /**
     * @Description 判断是否插入SD卡
     */
    private boolean isHasSdcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * @param file_url    下载链接
     * @param target_name 保存路径
     */
    private void DownFile(String file_url, String target_name) {
        RequestParams params = new RequestParams(file_url);
        params.setSaveFilePath(target_name);  //设置下载后的文件保存的位置
        params.setAutoResume(true);  //设置是否在下载是自动断点续传
        params.setAutoRename(true);  //设置是否根据头信息自动命名文件
        x.http().get(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(File result) {
                SharedPreManager.putString(APK_LOCAL_PATH, result.getPath());
//                Intent installIntent = new Intent(Intent.ACTION_VIEW);
//                PendingIntent mPendingIntent = PendingIntent.getActivity(DownApkService.this, 0, installIntent, 0);
//                builder.setContentIntent(mPendingIntent);

                installApk(DownApkService.this, result.getPath());
                stopSelf();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "文件下载失败");
                mNotificationManager.cancel(NotificationID);
                CrashHandler.getInstance().handleException("下载失败:" + ex.getMessage(), Constants.LOG_ERROR);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e(TAG, "文件下载结束，停止下载器");
            }

            @Override
            public void onFinished() {
                Log.e(TAG, "文件下载完成");
                CrashHandler.getInstance().handleException("下载成功,即将安装应用", Constants.LOG_LOG);
                builder.setContentText("下载完成");
                mNotificationManager.notify(NotificationID, builder.build());
                mNotificationManager.cancel(NotificationID);
            }

            @Override
            public void onWaiting() {
                Log.e(TAG, "文件下载处于等待状态");
            }

            @Override
            public void onStarted() {
                ToastUtils.show("开始下载更新文件...");
                CrashHandler.getInstance().handleException("开始下载文件", Constants.LOG_LOG);
                String id = "my_channel_01";
                String name = "应用更新";
                mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                // 针对Android 8.0版本对于消息栏的限制，需要加入channel渠道这一概念
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {  //Android 8.0以上
                    NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
                    mNotificationManager.createNotificationChannel(mChannel);
                    builder = new NotificationCompat.Builder(getApplicationContext());
                    builder.setSmallIcon(R.mipmap.app_icon);
                    builder.setTicker("正在下载新版本");
                    builder.setContentTitle(getApplicationName());
                    builder.setContentText("正在下载,请稍后...");
                    builder.setNumber(0);
                    builder.setChannelId(id);
                    builder.setAutoCancel(true);
                } else {    //Android 8.0以下
                    builder = new NotificationCompat.Builder(getApplicationContext());
                    builder.setSmallIcon(R.mipmap.app_icon);
                    builder.setTicker("正在下载新版本");
                    builder.setContentTitle(getApplicationName());
                    builder.setContentText("正在下载,请稍后...");
                    builder.setNumber(0);
                    builder.setAutoCancel(true);
                }

                mNotificationManager.notify(NotificationID, builder.build());
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                Log.e(TAG, "文件下载中");
                int x = Long.valueOf(current).intValue();
                int totalS = Long.valueOf(total).intValue();
                builder.setProgress(totalS, x, false);
                builder.setContentInfo(getPercent(x, totalS));
                mNotificationManager.notify(NotificationID, builder.build());
                //当前进度和文件总大小
                Log.i("DownAPKService", "current：" + current + "，total：" + total);
            }
        });


    }

    /**
     * @param x     当前值
     * @param total 总值
     *              [url=home.php?mod=space&uid=7300]@return[/url] 当前百分比
     * @Description 返回百分之值
     */
    private String getPercent(int x, int total) {
        String result = "";// 接受百分比的值
        double x_double = x * 1.0;
        double tempresult = x_double / total;
        // 百分比格式，后面不足2位的用0补齐 ##.00%
        DecimalFormat df1 = new DecimalFormat("0.00%");
        result = df1.format(tempresult);
        return result;
    }

    /**
     * @return
     * @Description 获取当前应用的名称
     */
    private String getApplicationName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }

    //安装应用
    private void installApk(Context context, String fileSavePath) {
        File file = new File(Uri.parse(fileSavePath).getPath());
        String filePath = file.getAbsolutePath();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            data = FileProvider.getUriForFile(context, VersionUtils.getPackgeAppId(this) + ".fileprovider", new File(filePath));
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(file);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        startActivity(intent);

//        File file = new File(APK_dir, "HealthManage_v1.2.1_release_0516.apk");
//        File file = new File(fileSavePath);
//        String[] command = {"chmod", "777", file.toString()};
//        ProcessBuilder builder = new ProcessBuilder(command);
//        try {
//            builder.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        // 由于没有在Activity环境下启动Activity,设置下面的标签
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//android 7.0
//            Uri apkUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
//            //添加这一句表示对目标应用临时授权该Uri所代表的文件
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
//        } else {
//            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//        }
//        context.startActivity(intent);
    }


}
