package com.bitvalue.health.util;

import android.annotation.SuppressLint;
import android.media.MediaCodec;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @ClassName: com.example.androiddemo.AESUtil
 * @Description: AES加密解密工具类
 */
public class AESUtil {

    private final static String HEX = "0123456789ABCDEF";
    private final static int JELLY_BEAN_4_2 = 17;
    private static final String IV_PARAMETERSPEC = "12345678";



    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encryptDES(String source, String key){
        //强加密随机数生成器
        SecureRandom random = new SecureRandom();
        try {
            //创建密钥规则
            DESKeySpec keySpec = new DESKeySpec(key.getBytes());
            //创建密钥工厂
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            //按照密钥规则生成密钥
            SecretKey secretKey =  keyFactory.generateSecret(keySpec);
            //加密对象
            Cipher cipher = Cipher.getInstance("DES");
            //初始化加密对象需要的属性
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, random);
            //开始加密
            byte[] result = cipher.doFinal(source.getBytes());
            //Base64加密
            return  Base64.getEncoder().encodeToString(result) ;
        } catch (Exception e) {
//            e.printStackTrace();
            Log.e("TAG", "encryptDES1: "+e.getMessage() );
            return null;
        }
    }




    /**
     * 解密
     * @param cryptograph 密文
     * @param key         约定的密钥
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decryptDES(String cryptograph, String key){
        //强加密随机生成器
        SecureRandom random  =  new SecureRandom();
        try {
            //定义私钥规则
            DESKeySpec keySpec = new DESKeySpec(key.getBytes());
            //定义密钥工厂
            SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
            //按照密钥规则生成密钥
            SecretKey secretkey = factory.generateSecret(keySpec);
            //创建加密对象
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, secretkey, random);
            //Base64对
            byte[] result = Base64.getDecoder().decode(cryptograph.getBytes());
            return new String(cipher.doFinal(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }









    /**
     * 加密
     *
     * @param key 密钥
     * @param src 加密文本
     * @return
     * @throws Exception
     */
    public static String encrypt(String key, String src) throws Exception {
        byte[] rawKey = getRawKey(key.getBytes());
        byte[] result = encrypt(rawKey, src.getBytes());
        return toHex(result);
    }


    /**
     * 解密
     *
     * @param key       密钥
     * @param encrypted 待揭秘文本
     * @return
     * @throws Exception
     */
    public static String decrypt(String key, String encrypted) throws Exception {
        byte[] rawKey = getRawKey(key.getBytes());
        byte[] enc = toByte(encrypted);
        byte[] result = decrypt(rawKey, enc);
        return new String(result);
    }

    /**
     * 获取256位的加密密钥
     *
     * @param seed
     * @return
     * @throws Exception
     */
    @SuppressLint("TrulyRandom")
    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("DES");
        SecureRandom sr = null;
        // 在4.2以上版本中，SecureRandom获取方式发生了改变
        if (android.os.Build.VERSION.SDK_INT >= JELLY_BEAN_4_2) {
            sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        } else {
            sr = SecureRandom.getInstance("SHA1PRNG");
        }
        sr.setSeed(seed);
        // 256 bits or 128 bits,192bits
        kgen.init(256, sr);
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }

    /**
     * 真正的加密过程
     *
     * @param key
     * @param src
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] key, byte[] src) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "DES");
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(src);
        return encrypted;
    }

    /**
     * 真正的解密过程
     *
     * @param key
     * @param encrypted
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] key, byte[] encrypted)
            throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }

    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }


}

