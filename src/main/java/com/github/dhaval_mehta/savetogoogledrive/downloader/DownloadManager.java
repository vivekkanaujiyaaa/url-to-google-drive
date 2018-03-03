package com.github.dhaval_mehta.savetogoogledrive.downloader;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DownloadManager {

    private static final int NO_OF_THREADS = 8;
    private static final SecureRandom random = new SecureRandom();

    private static volatile DownloadManager downloadManager;
    private ExecutorService executorService;

    private Map<String, DownloadTask> idToTaskMap;

    private DownloadManager() {
        executorService = Executors.newFixedThreadPool(NO_OF_THREADS);
        idToTaskMap = new HashMap<>();
    }

    public static DownloadManager getInstance() {

        if(downloadManager == null)
            downloadManager = new DownloadManager();

        return downloadManager;
    }

    public Future addTask(DownloadTask downloadTask) {
        idToTaskMap.put(downloadTask.toString(), downloadTask);
        return executorService.submit(downloadTask);
    }

    public List<DownloadTask> getAllTask() {
        List<DownloadTask> downloadTaskList = new ArrayList<>();
        idToTaskMap.keySet().forEach(k -> downloadTaskList.add(idToTaskMap.get(k)));
        return downloadTaskList;
    }

    private static synchronized String generateRandomString() {
        return new BigInteger(130, random).toString(32) + new BigInteger(String.valueOf(System.currentTimeMillis())).toString(32);
    }
}