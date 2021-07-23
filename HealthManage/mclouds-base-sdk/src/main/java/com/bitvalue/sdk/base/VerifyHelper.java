package com.bitvalue.sdk.base;

import android.content.Context;
import android.util.Base64;

import com.bitvalue.sdk.base.util.net.CheckCallBack;
import com.bitvalue.sdk.base.util.net.CheckResultBean;
import com.bitvalue.sdk.base.util.net.RequestUtil;
import com.bitvalue.sdk.base.util.sp.SharedPreManager;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;

import okhttp3.Call;

public enum VerifyHelper {
    INSTANCE;
    /**
     * 指定加密算法为RSA
     */
    private static final String ALGORITHM = "RSA";
    /**
     * 密钥长度，用来初始化
     */
    private static final int KEYSIZE = 2048;
    private static final String KEY_PUBLIC = "key_public";
    private static final String KEY_PRIVATE = "key_private";

    //    http://localhost:8066/userInfo/verifyPublicKey?userId=6&publicKey=gfdgdfgdgg
    private static String certificateUrl = "http://localhost:8066/userInfo/verifyPublicKey";

    // 生成公私钥字符串
    public static String getPublicKey(Context context) {

        /** RSA算法要求有一个可信任的随机数源 */
        SecureRandom secureRandom = new SecureRandom();

        /** 为RSA算法创建一个KeyPairGenerator对象 */
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);

            /** 利用上面的随机数据源初始化这个KeyPairGenerator对象 */
            keyPairGenerator.initialize(KEYSIZE, secureRandom);

            /** 生成密匙对 */
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            /** 得到公钥 */
            Key publicKey = keyPair.getPublic();

            /** 得到私钥 */
            Key privateKey = keyPair.getPrivate();

            byte[] publicKeyBytes = publicKey.getEncoded();
            byte[] privateKeyBytes = privateKey.getEncoded();

            String publicKeyBase64 = Base64.encodeToString(publicKeyBytes, Base64.DEFAULT);
            String privateKeyBase64 = Base64.encodeToString(privateKeyBytes, Base64.DEFAULT);
            SharedPreManager.putString(KEY_PUBLIC, publicKeyBase64, context);
            SharedPreManager.putString(KEY_PRIVATE, privateKeyBase64, context);

            return publicKeyBase64;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void checkUserId(Context context, String userId, CheckCallBack checkCallBack) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("userId", userId);
        String pubKey = SharedPreManager.getString(KEY_PUBLIC, context);
        if (null == pubKey || pubKey.isEmpty()) {
            checkCallBack.onGetResult(false);
            return;
        }
        hashMap.put("publicKey", pubKey);

        RequestUtil.certificateUser(certificateUrl, hashMap, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                checkCallBack.onGetResult(false);
            }

            @Override
            public void onResponse(String response, int id) {
                CheckResultBean checkResultBean = new Gson().fromJson(response, CheckResultBean.class);
                if (checkResultBean.code == 0) {
                    checkCallBack.onGetResult(true);
                } else {
                    checkCallBack.onGetResult(false);
                }
            }
        });
    }
}
