package com.oli.encryptiontest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class FileEncrypter {

    private static final int READ_WRITE_BLOCK_BUFFER = 1024;
    private static final String ALGO_FILE_ENCRYPTOR = "AES/CBC/PKCS5Padding";
    private static final String ALGO_SECRET_KEY = "AES";

    public static void encryptFile(String keyStr, String specStr,
                                   InputStream in, OutputStream out) throws IOException {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(specStr.getBytes("UTF-8"));
            SecretKeySpec keySpec = new SecretKeySpec(keyStr.getBytes("UTF-8"), ALGO_SECRET_KEY);

            Cipher c = Cipher.getInstance(ALGO_FILE_ENCRYPTOR);
            c.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);

            out = new CipherOutputStream(out, c);

            int count = 0;

            byte[] buffer = new byte[READ_WRITE_BLOCK_BUFFER];

            while ((count = in.read(buffer)) != -1) {
                out.write(buffer, 0, count);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    public static void decryptFile(String keyStr, String specStr,
                                   InputStream in, OutputStream out) throws IOException {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(specStr.getBytes("UTF-8"));
            SecretKeySpec keySpec = new SecretKeySpec(keyStr.getBytes("UTF-8"), ALGO_SECRET_KEY);

            Cipher c = Cipher.getInstance(ALGO_FILE_ENCRYPTOR);
            c.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);

            out = new CipherOutputStream(out, c);

            int count = 0;

            byte[] buffer = new byte[READ_WRITE_BLOCK_BUFFER];

            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }
}
