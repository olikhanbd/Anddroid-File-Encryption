package com.oli.encryptiontest;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    public static final String ENC_FILE_NAME = "sampleenc.pdf";
    public static final String DEC_FILE_NAME = "sampledec.pdf";
    private static final String TAG = "MainActivity";
    String my_key = "PwwRzW2Y0lrRddH7";
    String my_spec_key = "IZMfBAQMOpacEyZZ";

    Button btnEncrypt, btnDecrypt, btnRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String dir = Environment.getExternalStorageDirectory() + "/encryptiontest";
        final String filename = "sample.pdf";

        btnEncrypt = findViewById(R.id.btn_encrypt);
        btnDecrypt = findViewById(R.id.btn_decrypt);
        btnRead = findViewById(R.id.btn_read);

        saveFile();

        btnEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File file = new File(dir, "sample.pdf");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                InputStream is = new ByteArrayInputStream(stream.toByteArray());

                //create file
                File encryptedFile = new File(dir, ENC_FILE_NAME);

                try {
                    FileInputStream is = new FileInputStream(file);
                    FileEncrypter.encryptFile(my_key, my_spec_key, is, new FileOutputStream(encryptedFile));
                    Toast.makeText(MainActivity.this, "Encrypted", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btnDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File outputDecFile = new File(dir, DEC_FILE_NAME);
                File encFile = new File(dir, ENC_FILE_NAME);

                try {
                    FileEncrypter.decryptFile(my_key, my_spec_key,
                            new FileInputStream(encFile), new FileOutputStream(outputDecFile));
                    Toast.makeText(MainActivity.this, "Decrypted", Toast.LENGTH_SHORT).show();

                    //to delete decrypted file
                    //outputDecFile.delete();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void saveFile() {
        File folder = new File(Environment.getExternalStorageDirectory() + "/encryptiontest");

        if (!folder.exists()) {
            folder.mkdir();
        }

        File f = new File(folder, "sample.pdf");
        if (!f.exists()) try {

            InputStream is = getAssets().open("sample.pdf");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            FileOutputStream fos = new FileOutputStream(f);
            fos.write(buffer);
            fos.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Log.d(TAG, "getFile: " + f.getPath());
    }
}
