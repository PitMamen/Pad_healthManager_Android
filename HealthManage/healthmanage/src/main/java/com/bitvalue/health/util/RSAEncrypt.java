package com.bitvalue.health.util;

import android.os.Build;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

/**
 * @author created by bitvalue
 * @data : 04/26
 */
public class RSAEncrypt {
    public static final String TAG = RSAEncrypt.class.getSimpleName();
    String point = "xxx";
    private static final int KEYSIZE = 1024;

    /**
     * 随机生成密钥对
     *
     * @param
     * @throws NoSuchAlgorithmException
     */
    public static Map<String, String> genKeyPair() throws NoSuchAlgorithmException {
//    Log.d("TAG", "genKeyPair: "+"{}|开始生成公私钥",point);
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(KEYSIZE, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encode(publicKey.getEncoded(), Base64.DEFAULT));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encode(privateKey.getEncoded(), Base64.DEFAULT));
        // 将公钥和私钥保存到Map
        Map<String, String> map = new HashMap<>();
        map.put("publicKey", publicKeyString);
        map.put("privateKey", privateKeyString);
        return map;
    }

    /**
     * RSA公钥加密
     *
     * @param data      加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String publicKeyEncrypt(String data, String publicKey) throws Exception {
//    log.info("{}|RSA公钥加密前的数据|str:{}|publicKey:{}",point,str,publicKey);
        //base64编码的公钥
        byte[] decoded = Base64.decode(publicKey.getBytes(), Base64.DEFAULT);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));

        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeToString(cipher.doFinal(data.getBytes("UTF-8")), Base64.DEFAULT);
//    log.info("{}|公钥加密后的数据|outStr:{}",point,outStr);
        return outStr;
    }

    /**
     * RSA公钥解密
     *
     * @param str
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String publicKeyDecrypt(String str, String publicKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decode(str.getBytes("UTF-8"), Base64.DEFAULT);
        //base64编码的私钥
        byte[] decoded = Base64.decode(publicKey, Base64.DEFAULT);
        PublicKey pubKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        Log.e("TAG", "7777777777777777: ");
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }


    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @param
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    public static String privateKeyDecrypt(String str, String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decode(str.getBytes("UTF-8"), Base64.DEFAULT);
        //base64编码的私钥
        byte[] decoded = Base64.decode(privateKey, Base64.DEFAULT);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        Log.e("TAG", "88888888888888888: ");
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }


    /**
     * RSA私钥加密
     *
     * @param str
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String privateKeyEncrypt(String str, String privateKey) throws Exception {
//    log.info("{}|RSA私钥加密前的数据|str:{}|publicKey:{}",point,str,privateKey);
        //base64编码的公钥
        byte[] decoded = Base64.decode(privateKey, Base64.DEFAULT);
        PrivateKey priKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, priKey);
        String outStr = Base64.encodeToString(cipher.doFinal(str.getBytes()), Base64.DEFAULT);
//    log.info("{}|RSA私钥加密后的数据|outStr:{}",point,outStr);
        return outStr;
    }


    //测试
    public static void testExample() {
        try {
            Map<String, String> map_key = RSAEncrypt.genKeyPair();
            String message = "我是测试";
            Log.d(TAG, "前端请求的原数据\n" + message);
            String messageEn = RSAEncrypt.publicKeyEncrypt(message, map_key.get("publicKey"));
            Log.d(TAG, "前端请求的加密:\n" + messageEn);
            String messageDe = RSAEncrypt.privateKeyDecrypt(messageEn, map_key.get("privateKey"));
            Log.d(TAG, "后端解密出来的数据:\n" + messageDe);


            //前端数据展示处理
            String s = RSAEncrypt.privateKeyEncrypt(messageDe, map_key.get("privateKey"));
            Log.d(TAG, "后端返回的加密数据\n" + s);
            //私钥加密，公钥解密
            String s1 = RSAEncrypt.publicKeyDecrypt(s, map_key.get("publicKey"));
            Log.d(TAG, "前端解密出来显示的数据\n" + s1);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "生成密钥对异常---" + e.getMessage());
        }
    }


}
