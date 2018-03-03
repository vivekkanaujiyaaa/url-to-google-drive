package com.github.dhaval_mehta.savetogoogledrive.downloader;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class DownloadTask implements Runnable {

    private final String URL;
    private final File DOWNLOAD_FILE;
    private final int BUFFER_SIZE = 8192;
    private final Timer timer = new Timer();
    private long contentLength = 0;
    private long downloadedSize = 0;
    private DownloadStatus status;
    private SpeedMonitor speedMonitor;

    public DownloadTask(File downloadDirectory, String fileName, String url) throws IOException {
        status = DownloadStatus.IN_QUEUE;
        this.URL = url;
        downloadDirectory.mkdirs();
        DOWNLOAD_FILE = new File(downloadDirectory, fileName);
        DOWNLOAD_FILE.createNewFile();
        speedMonitor = new SpeedMonitor(this);
        timer.scheduleAtFixedRate(speedMonitor, 0, 1);
    }

    @Override
    public void run() {

        status = DownloadStatus.DOWNLOADING;

        try {
            InputStream responseStream = sendDownloadRequest();
            saveResponse(responseStream);
            status = DownloadStatus.COMPLETED;
            timer.cancel();
        } catch (IOException e) {
            status = DownloadStatus.FAILED;
            e.printStackTrace();
        }

    }

    private void saveResponse(InputStream responseStream) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(DOWNLOAD_FILE);
        byte[] buffer = new byte[BUFFER_SIZE];

        while (downloadedSize < contentLength) {

            if (Thread.currentThread().isInterrupted()) {
                handleInterrupt();
                return;
            }

            int len = responseStream.read(buffer);

            if (len == -1) {
                System.err.println("Downloading is not completed yet but " +
                        "input stream return -1 while reading.");

                throw new RuntimeException("Downloading is not completed yet but " +
                        "input stream return -1 while reading.");
            }

            outputStream.write(buffer, 0, len);
            downloadedSize += len;
        }

    }

    private InputStream sendDownloadRequest() throws IOException {
        HttpResponse response = Request.Post(URL)
                .execute()
                .returnResponse();

        contentLength = Long.parseLong(
                response.getFirstHeader("content-length").getValue());

        return response.getEntity().getContent();
    }

    private DownloadStatus getStatus() {
        return status;
    }

    private void handleInterrupt() {
        status = DownloadStatus.FAILED;
    }

    public long getDownloadedSize() {
        return downloadedSize;
    }


    public long getMaxSpeed() {
        return speedMonitor.getMaxSpeed();
    }

    public long getSpeed() {
        return speedMonitor.getSpeed();
    }

    public double getAverageSpeed() {
        return speedMonitor.getAverageSpeed();
    }

    public enum DownloadStatus {
        IN_QUEUE, DOWNLOADING, COMPLETED, FAILED
    }

    private static class SpeedMonitor extends TimerTask {

        private DownloadTask task;
        private long lastSecondSize = 0;
        private long currentSecondSize = 0;
        private long speed;
        private long maxSpeed;
        private double averageSpeed;
        private int counter;

        private SpeedMonitor(DownloadTask task) {
            this.task = task;
        }

        @Override
        public void run() {

            if (task.status.equals(DownloadStatus.COMPLETED) && (currentSecondSize - lastSecondSize) == 0)
                return;

            counter++;
            currentSecondSize = task.getDownloadedSize();
            speed = currentSecondSize - lastSecondSize;
            lastSecondSize = currentSecondSize;
            if (speed > maxSpeed)
                maxSpeed = speed;
            averageSpeed = currentSecondSize / (double) counter;
        }

        long getMaxSpeed() {
            return maxSpeed;
        }

        long getSpeed() {
            return speed;
        }

        double getAverageSpeed() {
            return averageSpeed;
        }
    }
}