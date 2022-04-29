package com.bitvalue.health.util;

import static com.blankj.utilcode.util.PhoneUtils.call;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bitvalue.health.Application;
import com.hjq.toast.ToastUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.List;


public class PermissionUtil {
    private static final String TAG = PermissionUtil.class.getSimpleName();

    public static final int REQ_PERMISSION_CODE = 0x100;

    public static final int REQUEST_CALL_PERMISSION = 10111; //拨号请求码

    //权限检查
    public static boolean checkPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(Application.instance(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(Application.instance(), Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(Application.instance(), Manifest.permission.RECORD_AUDIO)) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }

            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(Application.instance(), Manifest.permission.READ_SMS)) {
                permissions.add(Manifest.permission.READ_SMS);
            }

            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(Application.instance(), Manifest.permission.READ_PHONE_STATE)) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(Application.instance(), Manifest.permission.READ_PHONE_NUMBERS)) {
                permissions.add(Manifest.permission.READ_PHONE_NUMBERS);
            }

            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(Application.instance(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (permissions.size() != 0) {
                String[] permissionsArray = permissions.toArray(new String[1]);
                ActivityCompat.requestPermissions(activity,
                        permissionsArray,
                        REQ_PERMISSION_CODE);
                return false;
            }
        }

        return true;
    }

    public static void checkPermission(Context context, PermissionCallBack permissionCallBack) {
        RxPermissions.getInstance(context)
                .request(Manifest.permission.CAMERA
                        , Manifest.permission.READ_PHONE_STATE
                        , Manifest.permission.READ_SMS
                        , Manifest.permission.CALL_PHONE
                        , Manifest.permission.READ_PHONE_NUMBERS
                        , Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.RECORD_AUDIO).subscribe(r -> {
            permissionCallBack.onPermissionResult(r);
        });
    }



    /**
     * 判断是否有某项权限
     *
     * @param string_permission 权限
     * @param request_code      请求码
     * @return
     */
    public boolean checkReadPermission(Context context, String string_permission, int request_code) {
        boolean flag = false;
        if (ContextCompat.checkSelfPermission(context, string_permission) == PackageManager.PERMISSION_GRANTED) {//已有权限
            flag = true;
        } else {//申请权限
            ActivityCompat.requestPermissions(Application.instance().getHomeActivity(), new String[]{string_permission}, request_code);
        }
        return flag;
    }

    /**
     * 检查权限后的回调
     * <p>
     * //     * @param requestCode  请求码
     * //     * @param permissions  权限
     * //     * @param grantResults 结果
     */
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_CALL_PERMISSION: //拨打电话
//                if (permissions.length != 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {//失败
//                    ToastUtils.show("请允许拨号权限后再试");
//                } else {//成功
////                    call("tel:" + "10086");
//                }
//                break;
//        }
//    }


    public interface PermissionCallBack {
        void onPermissionResult(boolean permit);
    }
}
