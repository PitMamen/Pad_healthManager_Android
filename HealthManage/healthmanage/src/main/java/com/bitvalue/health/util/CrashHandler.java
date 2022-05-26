package com.bitvalue.health.util;


import static com.bitvalue.health.util.Constants.LOG_ALLEVENT;
import static com.bitvalue.health.util.Constants.LOG_ERROR;
import static com.bitvalue.health.util.Constants.LOG_FAIL;
import static com.bitvalue.health.util.Constants.LOG_FATAL;
import static com.bitvalue.health.util.Constants.LOG_LOG;
import static com.bitvalue.health.util.Constants.LOG_OPENRECOD;
import static com.bitvalue.health.util.Constants.LOG_RECORD;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.bitvalue.sdk.collab.utils.FileUtil;
import com.blankj.utilcode.util.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author created by bitvalue
 * @data : 05/25
 * 日志写入工具类
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

  public static final String TAG = "CrashHandler";
  private Context mContext;

  //CrashHandler实例
  private static CrashHandler INSTANCE;

  static {
    INSTANCE = new CrashHandler();
  }


  //用于格式化日期,作为日志文件名的一部分
  private DateFormat formatter;

  private ExecutorService executorService;

  /**
   * 保证只有一个CrashHandler实例
   */
  @SuppressLint("SimpleDateFormat")
  private CrashHandler() {
    executorService = Executors.newFixedThreadPool(1);
    formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
  }

  /**
   * 获取CrashHandler实例 ,单例模式
   */
  public static CrashHandler getInstance() {
    return INSTANCE;
  }

  public void init(Context context) {
    //获取系统默认的UncaughtException处理器
    //系统默认的UncaughtException处理类
    Thread.setDefaultUncaughtExceptionHandler(this);
    mContext = context;
  }

  /**
   * 当UncaughtException发生时会转入该函数来处理
   * 主线程死掉重启APP
   */
  @RequiresApi(api = Build.VERSION_CODES.M)
  @Override
  public void uncaughtException(Thread thread, final Throwable ex) {
//        StringBuilder builder = new StringBuilder(" Thread_name : ");
//        builder.append(thread.getName()).append(" id : ").append(thread.getId()).append(" detail ").append(ex.getMessage()).append("  ").append(ex.fillInStackTrace());
//        handleException(builder.toString(), LOG_FATAL);
//        if (thread.getName().equalsIgnoreCase("main")) {
//            Util.RestartAPP(mContext);
//        }
  }


  @RequiresApi(api = Build.VERSION_CODES.M)
  private void printThread() {
    Map<Thread, StackTraceElement[]> stacks = Thread.getAllStackTraces();
    Set<Thread> set = stacks.keySet();
    saveLogFile(" \n" + "线程信息--------", LOG_ERROR);
    for (Thread key : set) {
      StackTraceElement[] stackTraceElements = stacks.get(key);
      saveLogFile("\n " + "线程信息name--  " + key.getName() + "---id---" + key.getId(), LOG_ERROR);
      if (stackTraceElements != null) {
        saveLogFile("\n" + "线程信息 message--  ", LOG_ERROR);
        for (StackTraceElement st : stackTraceElements) {
          saveLogFile(st.toString(), LOG_ERROR);
        }
      }
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.M)
  public void handleException(String ex, int type) {
    if (ex == null) {
      return;
    }
    if (type != LOG_ALLEVENT) {
      final String content = formatter.format(new Date()) + "  " + Thread.currentThread().getName() + "   " + ex + " \n";
      executorService.submit(() -> saveLogFile(content, type));
    } else {
      Log.d("handleException", "------6--------1----" + ex);
      executorService.submit(() -> saveLogFile(ex + " \n\n", type));
    }
  }


  @RequiresApi(api = Build.VERSION_CODES.M)
  public void handleException(String ex) {
    if (ex == null) {
      return;
    }
    final String content = formatter.format(new Date()) + "  " + Thread.currentThread().getName() + "   " + ex + " \n";
    executorService.submit(() -> saveLogFile(content + " \n\n", LOG_LOG));
  }


  @RequiresApi(api = Build.VERSION_CODES.M)
  private void saveLogFile(String ex, int type) {
    String fileName;
    switch (type) {
      case LOG_ERROR:
        fileName = "Error.txt";
        break;
      case LOG_LOG:
        fileName = "Log.txt";
        break;
      case LOG_RECORD:
        fileName = "Record.txt";
        break;
      case LOG_FAIL:
        fileName = "Fail.txt";
        break;
      case LOG_OPENRECOD:
        fileName = "openRecord.txt";
        break;
      case LOG_FATAL:
        fileName = "Error.txt";
        printThread();
        break;
      case LOG_ALLEVENT:
        fileName = "allevent.txt";
        break;
      default:
        fileName = "Log.txt";
        break;
    }
    try {
      if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
        String path = FileUtil.initPath_log_save();
        if (path != null) {
          File dir = new File(Environment.getExternalStorageDirectory(), "documents/" + fileName.split(".txt")[0] + "/" +TimeUtils.getCurrenTime_Second());
          if (!dir.exists()) dir.mkdirs();
          FileOutputStream fos = new FileOutputStream(dir.getPath() + "/" + fileName, true);
          byte[] bytes = ex.getBytes();
          fos.write(bytes);
          fos.close();
        }
      }
    } catch (Exception e) {
      Log.e(TAG, "an error occured while writing file...", e);
    }
  }

}



