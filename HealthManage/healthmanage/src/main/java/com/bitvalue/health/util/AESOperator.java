package com.bitvalue.health.util;


import android.util.Base64;
import android.util.Log;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by Administrator on 2016/1/28.
 */
public class AESOperator {
    private String sKey = "smkldospdosldaaa";//key，可自行修改
    private String ivParameter = "0392039203920300";//偏移量,可自行修改
    private static AESOperator instance = null;

    private AESOperator() {

    }

    public static AESOperator getInstance() {
        if (instance == null)
            instance = new AESOperator();
        return instance;
    }

    public  String encrypt(String encData, String secretKey, String vector) throws Exception {

        if (secretKey == null) {
            return null;
        }
        if (secretKey.length() != 16) {
            return null;
        }
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = secretKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(vector.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(encData.getBytes("utf-8"));
        //String encrypt = new String(Base64.encode(encrypted));
        String encrypt = new String(Base64.encode(encrypted,encrypted.length));
        return encrypt;
        //return new BASE64Encoder().encode(encrypted);// 此处使用BASE64做转码。
    }

    //PIN码解密
    public String decrypt(String sSrc, String key, String ivs) throws Exception {
        try {
            //如果使用android本身的base64库，参数要改成UTF-8
            byte[] raw = stringCover(key).getBytes("UTF-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivs.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            // 用bc库解密
          //  byte[] encrypted1 = Base64.decode(sSrc.getBytes());

            //用android自带的解密
            byte[] encrypted1 = Base64.decode(sSrc.getBytes(),Base64.DEFAULT);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString;
        } catch (Exception ex) {
            Log.d("hujun","ex---->"+ex);
            return null;
        }
    }

    //对传入参数进行补位为16位
    public String stringCover(String st){
        for(int i=st.length();i<16;i++){
            st=st+"0";
        }
        Log.i("kazengtong", "stringTrue----->" + st);
        return st;
    }

    public static String encodeBytes(byte[] bytes) {
        StringBuffer strBuf = new StringBuffer();

        for (int i = 0; i < bytes.length; i++) {
            strBuf.append((char) (((bytes[i] >> 4) & 0xF) + ((int) 'a')));
            strBuf.append((char) (((bytes[i]) & 0xF) + ((int) 'a')));
        }

        return strBuf.toString();
    }


}
