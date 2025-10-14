package com.box.common.network;

import android.annotation.SuppressLint;

import com.box.common.utils.subpackage.Base64;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class Des {
    public static final String signKey = "Df1&#%$WT9sGc%^urZO0!XkjglAv!Vel";
    public static final String signKeyXdqy = "Df1&#%$WT9sAv!Vel";
    public static final String key = "qn%49E&E";
    public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";

    public static String encode(String data) {
        try {
            return encode(data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @SuppressLint("TrulyRandom")
    public static String encode(byte[] data) throws Exception {
        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
            byte[] bytes = cipher.doFinal(data);
            return Base64.encode(bytes);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }


    public static byte[] decode(byte[] data) throws Exception {
        try {
//            SecureRandom sr = new SecureRandom();
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }


    public static String decodeValue(String data) {
        byte[] datas;
        String value = null;
        try {
            if (System.getProperty("os.name") != null && (System.getProperty("os.name").equalsIgnoreCase("sunos") || System.getProperty("os.name").equalsIgnoreCase("linux"))) {
                datas = decode(Base64.decode(data));
            } else {
                datas = decode(Base64.decode(data));
            }

            value = new String(datas);
        } catch (Exception e) {
            value = "";
        }
        return value;
    }

    public static void main(String[] args) throws Exception {

        //System.out.println("ming:api=login&username=crealing;mi:" + Des.encode("api=login&username=crealing"));
    }
}
