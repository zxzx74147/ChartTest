package com.zxzx74147.devlib.utils;

import android.util.Base64;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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
    private static int MAX_ENCRYPT_BLOCK = 117;
    public static String encrypt(RSAPublicKey publicKey, String plainTextData)
             {
        Cipher cipher = null;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            byte[] input = plainTextData.getBytes("UTF-8");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int inputLen = input.length;
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(input, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(input, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }

            // cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());


            byte[] output = out.toByteArray();//cipher.doFinal(plainTextData.getBytes("UTF-8"));
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

    private static int MAX_DECRYPT_BLOCK = 128;
    public static byte[] decryptByPrivateKey(byte[] encryptedData, PrivateKey privateK)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

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
