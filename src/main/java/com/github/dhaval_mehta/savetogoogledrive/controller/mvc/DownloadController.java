package com.github.dhaval_mehta.savetogoogledrive.controller.mvc;

import com.github.dhaval_mehta.savetogoogledrive.downloader.DownloadManager;
import com.github.dhaval_mehta.savetogoogledrive.downloader.DownloadTask;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;

@Controller
@RequestMapping("/download")
public class DownloadController {

    private static final SecureRandom random = new SecureRandom();

    private static synchronized String generateRandomString() {
        return new BigInteger(130, random).toString(32) + new BigInteger(String.valueOf(System.currentTimeMillis())).toString(32);
    }

    @GetMapping("/status")
    public ResponseEntity getAllStatus() {
        HashMap<String, Long> statusMap = new HashMap<>();
        DownloadManager.getInstance()
                .getAllTask()
                .forEach(task -> statusMap.put(task.toString(), task.getMaxSpeed()));
        return ResponseEntity.ok(statusMap);
    }

    @GetMapping("/submit")
    public ResponseEntity submitDownloadTask(@RequestParam("url") String url) throws IOException {

        String random = generateRandomString();
        File d = new File(System.getenv("TEMP_STROAGE"), random);

        DownloadManager.getInstance().addTask(
                new DownloadTask(d, random, url));

        return ResponseEntity.accepted().build();
    }
}
