package com.bitvalue.health.util.encryption;

import android.media.MediaCodec;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author created by pxk
 * @data :
 */
public class AESUtils {

  /***
   * 文档中的 实例代码
   * @param encodedCipherText
   * @param key
   * @param iv
   * @return
   */

  @RequiresApi(api = Build.VERSION_CODES.O)
  public static byte[] decrypt(String encodedCipherText, byte[] key, byte[] iv) {
    byte[] cipherBytes = Base64.getDecoder().decode(encodedCipherText);
    return decrypt(cipherBytes, key, iv);
  }
  public static byte[] decrypt(byte[] cipherBytes, byte[] key, byte[] iv) {
    try {
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
      IvParameterSpec ivSpec = new IvParameterSpec(iv);
      cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), ivSpec);
      return cipher.doFinal(cipherBytes);
    } catch (Exception e) {
      throw new MediaCodec.CryptoException(200, e.getMessage());
    }
  }
}
