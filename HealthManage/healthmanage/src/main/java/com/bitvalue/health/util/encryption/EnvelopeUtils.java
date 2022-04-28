package com.bitvalue.health.util.encryption;

import android.media.MediaCodec;
import android.os.Build;

import androidx.annotation.RequiresApi;

/**
 * @author created by pxk
 * @data : 04/28
 */
public class EnvelopeUtils {
  @RequiresApi(api = Build.VERSION_CODES.O)
  public static String decrypt(String encryptedRecord, String wrappedDEK, String encodedPrivateKey) {
    try {
      byte[] dek = RSAUtils.decrypt(wrappedDEK, encodedPrivateKey);
      byte[] aesKey = new byte[32];
      byte[] aesIV = new byte[16];
      if (dek.length != aesKey.length + aesIV.length) {
        throw new Exception();
      }
      System.arraycopy(dek, 0, aesKey, 0, aesKey.length);
      System.arraycopy(dek, aesKey.length, aesIV, 0, aesIV.length);
      return new String(AESUtils.decrypt(encryptedRecord, aesKey, aesIV));
    } catch (MediaCodec.CryptoException e) {
      throw e;
    } catch (Exception exc) {
      throw new MediaCodec.CryptoException(200, exc.getMessage());
    }
  }


}
