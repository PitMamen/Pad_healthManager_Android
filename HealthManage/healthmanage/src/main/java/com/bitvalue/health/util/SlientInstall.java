package com.bitvalue.health.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * @author created by bitvalue
 * @data : 05/24
 */
public class SlientInstall {

    public static boolean installed(String apkPath) {
/**
 * 执行具体的静默安装逻辑，需要手机ROOT。

 安装成功返回true，安装失败返回false。

 */

        boolean result = false;

        DataOutputStream dataOutputStream = null;

        BufferedReader errorStream = null;

        try {
// 申请su权限

            Process process = Runtime.getRuntime().exec("su");

            dataOutputStream = new DataOutputStream(process.getOutputStream());

// 执行pm install命令

            String command = "pm install -r " + apkPath + "\n";

            dataOutputStream.write(command.getBytes(Charset.forName("utf-8")));

            dataOutputStream.flush();

            dataOutputStream.writeBytes("exit\n");

            dataOutputStream.flush();

            process.waitFor();

            errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String msg = "";

            String line;

// 读取命令的执行结果

            while ((line = errorStream.readLine()) != null) {

                msg += line;

            }

//        Log.d("TAG","install msg is "+ msg);

// 如果执行结果中包含Failure字样就认为是安装失败，否则就认为安装成功

            if (!msg.contains("Failure")) {

                result = true;

            }

        } catch (Exception e) {

            Log.e("TAG", e.getMessage(), e);

        } finally {

            try {

                if (dataOutputStream != null) {

                    dataOutputStream.close();

                }

                if (errorStream != null) {

                    errorStream.close();

                }

            } catch (IOException e) {

                Log.e("TAG", e.getMessage(), e);

            }

        }

        return result;
    }


}
