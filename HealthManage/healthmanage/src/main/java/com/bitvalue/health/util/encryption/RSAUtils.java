package com.bitvalue.health.util.encryption;

import android.media.MediaCodec;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author created by bitvalue
 * @data : 04/28
 */
public class RSAUtils {
  @RequiresApi(api = Build.VERSION_CODES.O)
  public static byte[] decrypt(String encodedCipherText, String encodedPrivateKey) throws MediaCodec.CryptoException {
    byte[] cipherBytes = Base64.getDecoder().decode(encodedCipherText);
    byte[] privateKey = Base64.getDecoder().decode(encodedPrivateKey);
    return decrypt(cipherBytes, privateKey);
  }
  public static byte[] decrypt(byte[] cipherBytes, byte[] privateKey) throws MediaCodec.CryptoException {
    if (privateKey == null) {
      throw new MediaCodec.CryptoException(200,"Can not decrypt using RSA without private key.");
    }
    try {
      Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
      PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKey);
      KeyFactory kf = KeyFactory.getInstance("RSA");
      cipher.init(Cipher.DECRYPT_MODE, kf.generatePrivate(spec));
      return cipher.doFinal(cipherBytes);
    } catch (Exception e) {
      throw new MediaCodec.CryptoException(200, e.getMessage());
    }
  }



}
