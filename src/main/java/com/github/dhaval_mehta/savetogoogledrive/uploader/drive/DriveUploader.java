package com.github.dhaval_mehta.savetogoogledrive.uploader.drive;

import com.github.dhaval_mehta.savetogoogledrive.model.DownloadFileInfo;
import com.github.dhaval_mehta.savetogoogledrive.model.UploadInformation;
import com.github.dhaval_mehta.savetogoogledrive.model.UploadStatus;
import com.github.dhaval_mehta.savetogoogledrive.model.User;
import com.github.dhaval_mehta.savetogoogledrive.uploader.Uploader;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import javax.validation.constraints.NotNull;
import java.io.IOException;

import static com.github.dhaval_mehta.savetogoogledrive.utility.HttpUtilities.USER_AGENT;

abstract class DriveUploader implements Uploader {
    private static final String CREATE_FILE_URL = "https://www.googleapis.com/upload/drive/v3/files?uploadType=resumable";

    final int chunkSize;
    protected User user;
    DownloadFileInfo downloadFileInfo;
    UploadInformation uploadInformation;
    private String createdFileUrl;

    DriveUploader(DownloadFileInfo downloadFileInfo, User user) {
        this();
        this.user = user;
        this.downloadFileInfo = downloadFileInfo;
        uploadInformation.setFileName(downloadFileInfo.getFileName());
        uploadInformation.setUrl(downloadFileInfo.getUploadUrl().toString());
        uploadInformation.setTotalSize(downloadFileInfo.getContentLength());

        try {
            obtainUploadUrl();
        } catch (Exception e) {
            uploadInformation.setUploadStatus(UploadStatus.failed);
            uploadInformation.setErrorMessage(e.getMessage());
        }
    }

    private DriveUploader() {
        chunkSize = 20* 1024 * 1024; // 1 MB
        uploadInformation = new UploadInformation();
        uploadInformation.setUploadStatus(UploadStatus.waiting);

    }

    /**
     * It will upload bytes in range [start,end] to Google drive.
     *
     * @param start starting byte of range
     * @param end   ending byte of range
     */
    void uploadPartially(@NotNull byte[] buffer, long start, long end) throws IOException {
        String contentRange = "bytes " + start + "-" + end + "/" + downloadFileInfo.getContentLength();
        Request.Post(createdFileUrl)
                .addHeader("User-Agent", USER_AGENT)
                .addHeader("Content-Range", contentRange)
                .bodyByteArray(buffer)
                .execute()
                .discardContent();
    }

    private void obtainUploadUrl() throws IOException {
        user.refreshTokenIfNecessary();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", downloadFileInfo.getFileName());
        HttpResponse response = Request.Post(CREATE_FILE_URL)
                .addHeader("User-Agent", USER_AGENT)
                .addHeader("Authorization", user.getToken().getTokenType() + " " + user.getToken().getAccessToken())
                .addHeader("X-Upload-Content-Type", downloadFileInfo.getContentType())
                .addHeader("X-Upload-Content-Length", String.valueOf(downloadFileInfo.getContentLength()))
                .bodyString(jsonObject.toString(), ContentType.APPLICATION_JSON)
                .execute()
                .returnResponse();
        createdFileUrl = response.getFirstHeader("Location").getValue();
    }

    @Override
    public UploadInformation getUploadInformation() {
        return uploadInformation;
    }
}
