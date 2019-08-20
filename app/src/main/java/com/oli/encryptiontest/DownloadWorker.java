package com.oli.encryptiontest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import static android.content.Context.NOTIFICATION_SERVICE;

public class DownloadWorker extends Worker {

    private final String TAG = "nexa_" + this.getClass().getSimpleName();

    private String my_key = "PwwRzW2Y0lrRddH7";
    private String my_spec_key = "IZMfBAQMOpacEyZZ";

    private NotificationCompat.Builder builder;
    private NotificationManager manager;

    public DownloadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        String url = getInputData().getString("url");

        if (downloadFile(url)) {
            if (builder != null) {
                builder.setContentText("Download finished");
                builder.setProgress(0, 0, false);
                manager.notify(1, builder.build());
            }
            return Result.success();
        } else {
            if (builder != null) {
                builder.setContentText("Download failed");
                builder.setProgress(0, 0, false);
                manager.notify(1, builder.build());
            }

            return Result.failure();
        }
    }

    private void showNotification(int id) {

        String channelid = "001";
        manager = (NotificationManager)
                getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelid, "channel1",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("File download channel");


            manager.createNotificationChannel(channel);
        }

        builder = new NotificationCompat
                .Builder(getApplicationContext(), channelid)
                .setContentTitle("Downloading")
                .setContentText("Downloading")
                .setProgress(100, 50, true)
                .setSmallIcon(R.mipmap.ic_launcher);

        manager.notify(1, builder.build());

    }

    private boolean downloadFile(String downloadUrl) {

        showNotification(1);

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
            return false;
        }
    }
}
