package com.oli.encryptiontest;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class EncryptedFileDownloader extends AsyncTask<String, Integer, Boolean> {

    private final String TAG = "nexa_" + this.getClass().getSimpleName();

    private String my_key = "PwwRzW2Y0lrRddH7";
    private String my_spec_key = "IZMfBAQMOpacEyZZ";

    private DownloadTaskListener mListener;

    public EncryptedFileDownloader(DownloadTaskListener mListener) {
        this.mListener = mListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mListener.onDownloadStarted();
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        String downloadUrl = strings[0];

        try {
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf('/') + 1);
            String folder = Environment.getExternalStorageDirectory() + "/samples";

            File folderFile = new File(folder);

            if (!folderFile.exists()) {
                folderFile.mkdir();
            }

            URL url = new URL(downloadUrl);
            URLConnection connection = url.openConnection();
            connection.connect();

            int fileLength = connection.getContentLength();

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            OutputStream output = new FileOutputStream(folder + File.separator + fileName);

            FileEncrypter.encryptFile(my_key, my_spec_key, input, output);

            return true;

        } catch (Exception e) {
            Log.d(TAG, "doInBackground: " + e.toString());
        }

        return false;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        mListener.onDownloadProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        mListener.onDownloadFinish(aBoolean);
    }

    public interface DownloadTaskListener {

        void onDownloadStarted();

        void onDownloadFinish(boolean isSuccess);

        void onDownloadProgress(float progress);
    }
}
