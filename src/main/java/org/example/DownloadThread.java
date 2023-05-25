package org.example;

import org.example.config.AppUtil;
import org.example.constants.Constants;
import org.example.models.FileInfo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DownloadThread extends Thread {
    private final FileInfo file;
    DownloadManager manager;
    public DownloadThread(FileInfo file, DownloadManager manager) {
        this.file = file;
        this.manager = manager;
    }
    @Override
    public void run() {
        this.file.setStatus(Constants.DOWNLOADING);
        this.manager.updateUI(this.file);
        // download logic
        try {
            URL fileUrl = new URL(file.getUrl());
            HttpURLConnection connection = (HttpURLConnection) fileUrl.openConnection();
            long fileSize = connection.getContentLengthLong();
            if (fileSize > 0) {
                file.setSize(AppUtil.formatFileSize((double) fileSize));
                InputStream inputStream = connection.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(this.file.getPath());
//                Files.copy(new URL(this.file.getUrl()).openStream(), Paths.get(this.file.getPath()));
                byte[] buffer = new byte[4096];
                int bytesRead;
                long downloadedBytes = 0;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    downloadedBytes += bytesRead;
                    double progress = (double) downloadedBytes / fileSize;
                    file.setProgress(progress);
                }
                this.file.setStatus(Constants.DONE);
            } else {
                System.out.println("Unable to determine the size");
            }
        } catch (Exception e) {
            this.file.setStatus(Constants.FAILED);
            System.out.println("Downloading Failed");
            e.printStackTrace();
        }
        this.manager.updateUI(this.file);
    }
}
