package com.zxzx74147.devlib.utils;

import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by zhengxin on 2018/3/16.
 */

public class RsaUtil {



    public static PrivateKey getPrivateKey(InputStream inputStream){
        return null;
    }


    /**
     * 从文件中输入流中加载公钥
     * @param in 公钥输入流
     * @throws Exception 加载公钥时产生的异常
     */
    public static RSAPublicKey loadPublicKey(InputStream in) throws Exception{
        try {
            BufferedReader br= new BufferedReader(new InputStreamReader(in));
            String readLine= null;
            StringBuilder sb= new StringBuilder();
            while((readLine= br.readLine())!=null){
                if(readLine.charAt(0)=='-'){
                    continue;
                }else{
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            return loadPublicKey(sb.toString());
        } catch (IOException e) {
            throw new Exception("公钥数据流读取错误");
        } catch (NullPointerException e) {
            throw new Exception("公钥输入流为空");
        }
    }

    /**
     * 从字符串中加载公钥
     * @param publicKeyStr 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    public static RSAPublicKey loadPublicKey(String publicKeyStr) throws Exception{
        try {
            byte[] buffer  = Base64.decode(publicKeyStr,Base64.DEFAULT);
//            BASE64Decoder base64Decoder= new BASE64Decoder();
//            byte[] buffer= base64Decoder.decodeBuffer(publicKeyStr);
            KeyFactory keyFactory= KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec= new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    /**
     * 从文件中加载私钥
     * @param in 私钥文件名
     * @return 是否成功
     * @throws Exception
     */
    public static RSAPrivateKey loadPrivateKey(InputStream in) throws Exception{
        try {
            BufferedReader br= new BufferedReader(new InputStreamReader(in));
            String readLine= null;
            StringBuilder sb= new StringBuilder();
            while((readLine= br.readLine())!=null){
                if(readLine.charAt(0)=='-'){
                    continue;
                }else{
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            return loadPrivateKey(sb.toString());
        } catch (IOException e) {
            throw new Exception("私钥数据读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥输入流为空");
        }
    }

    public static RSAPrivateKey loadPrivateKey(String privateKeyStr) throws Exception{
        try {
            byte[] buffer  = Base64.decode(privateKeyStr,Base64.DEFAULT);
//            byte[] buffer= base64Decoder.decodeBuffer(privateKeyStr);
            PKCS8EncodedKeySpec keySpec= new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory= KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }


    /**
     * 加密过程
     * @param publicKey 公钥
     * @param plainTextData 明文数据
     * @return
     * @throws Exception 加密过程中的异常信息
     *
     *
     */

    public static String encrypt(RSAPublicKey publicKey, String plainTextData)
             {
        Cipher cipher = null;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA");
            // cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] output = cipher.doFinal(plainTextData.getBytes("UTF-8"));
            return Base64.encodeToString(output,Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
//            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
//            throw new Exception("加密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
//            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            e.printStackTrace();
//            throw new Exception("明文数据已损坏");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
                 return null;
    }

//    public static String decrypt(RSAPrivateKey privateKey, byte[] cipherData)
//    {
//        Cipher cipher = null;
//        try {
//            // 使用默认RSA
//            cipher = Cipher.getInstance("RSA");
//            // cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
//            cipher.init(Cipher.DECRYPT_MODE, privateKey);
//
//            //模长
//            int key_len = privateKey.getModulus().bitLength() / 8;
//            byte[] bytes = cipherData;
//            byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
//            System.err.println(bcd.length);
//            //如果密文长度大于模长则要分组解密
//            String ming = "";
//            byte[][] arrays = splitArray(bcd, key_len);
//            for(byte[] arr : arrays){
//                ming += new String(cipher.doFinal(arr));
//            }
//            return ming;
//
////            byte[] output = cipher.doFinal(cipherData);
////            return Base64.encodeToString(output,Base64.DEFAULT);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
////            throw new Exception("无此解密算法");
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//            return null;
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
////            throw new Exception("解密私钥非法,请检查");
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
////            throw new Exception("密文长度非法");
//        } catch (BadPaddingException e) {
//            e.printStackTrace();
////            throw new Exception("密文数据已损坏");
//        }
//        return null;
//    }
//
//    /**
//     *拆分数组
//     */
//    public static byte[][] splitArray(byte[] data,int len){
//        int x = data.length / len;
//        int y = data.length % len;
//        int z = 0;
//        if(y!=0){
//            z = 1;
//        }
//        byte[][] arrays = new byte[x+z][];
//        byte[] arr;
//        for(int i=0; i<x+z; i++){
//            arr = new byte[len];
//            if(i==x+z-1 && y!=0){
//                System.arraycopy(data, i*len, arr, 0, y);
//            }else{
//                System.arraycopy(data, i*len, arr, 0, len);
//            }
//            arrays[i] = arr;
//        }
//        return arrays;
//    }
//
//
//    public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
//        byte[] bcd = new byte[asc_len / 2];
//        int j = 0;
//        for (int i = 0; i < (asc_len + 1) / 2; i++) {
//            bcd[i] = asc_to_bcd(ascii[j++]);
//            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
//        }
//        return bcd;
//    }
//    public static byte asc_to_bcd(byte asc) {
//        byte bcd;
//
//        if ((asc >= '0') && (asc <= '9'))
//            bcd = (byte) (asc - '0');
//        else if ((asc >= 'A') && (asc <= 'F'))
//            bcd = (byte) (asc - 'A' + 10);
//        else if ((asc >= 'a') && (asc <= 'f'))
//            bcd = (byte) (asc - 'a' + 10);
//        else
//            bcd = (byte) (asc - 48);
//        return bcd;
//    }

    public static String decrypt(RSAPrivateKey privateKey, String cipherData)
            {
        Cipher cipher = null;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA");
            // cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] chiperDecodeData = Base64.decode(cipherData,Base64.DEFAULT);
            byte[] output = cipher.doFinal(chiperDecodeData);
            return new String(output);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
//            throw new Exception("无此解密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
//            throw new Exception("解密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
//            throw new Exception("密文长度非法");
        } catch (BadPaddingException e) {
            e.printStackTrace();
//            throw new Exception("密文数据已损坏");
        }
        return null;
    }
//    public byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception{
//        if(publicKey== null){
//            throw new Exception("加密公钥为空, 请设置");
//        }
//        Cipher cipher= null;
//        try {
//            cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
//            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//            byte[] output= cipher.doFinal(plainTextData);
//            return output;
//        } catch (NoSuchAlgorithmException e) {
//            throw new Exception("无此加密算法");
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//            return null;
//        }catch (InvalidKeyException e) {
//            throw new Exception("加密公钥非法,请检查");
//        } catch (IllegalBlockSizeException e) {
//            throw new Exception("明文长度非法");
//        } catch (BadPaddingException e) {
//            throw new Exception("明文数据已损坏");
//        }
//    }
}
