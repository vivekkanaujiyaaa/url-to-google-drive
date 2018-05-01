package com.github.dhaval_mehta.savetogoogledrive.uploader.drive;

import com.github.dhaval_mehta.savetogoogledrive.model.DownloadFileInfo;
import com.github.dhaval_mehta.savetogoogledrive.model.UploadStatus;
import com.github.dhaval_mehta.savetogoogledrive.model.User;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.github.dhaval_mehta.savetogoogledrive.utility.HttpUtilities.USER_AGENT;

public class ResumableDriveUploader extends DriveUploader {

    private static final Logger LOGGER = Logger.getLogger(ResumableDriveUploader.class.getName());

    ResumableDriveUploader(DownloadFileInfo downloadFileInfo, User user) {
        super(downloadFileInfo, user);
    }

    private byte[] downloadPartially(long start, long end) throws IOException {
        return Request.Get(downloadFileInfo.getUploadUrl().toString())
                .addHeader("Range", "bytes=" + start + "-" + end)
                .addHeader("User-Agent", USER_AGENT)
                .execute()
                .returnContent()
                .asBytes();
    }

    /**
     * This function will upload file to Google drive using resumable upload method.
     */
    @Override
    public void upload() {
        try {
            uploadInformation.setUploadStatus(UploadStatus.uploading);

            while (uploadInformation.getUploadedSize() < downloadFileInfo.getContentLength()) {
                long start = uploadInformation.getUploadedSize();
                long end = uploadInformation.getUploadedSize() + chunkSize - 1;

                if (end >= downloadFileInfo.getContentLength())
                    end = downloadFileInfo.getContentLength() - 1;

                long startTime = System.currentTimeMillis();
                byte[] buffer = downloadPartially(start, end);
                uploadPartially(buffer, start, end);
                long endTime = System.currentTimeMillis();

                uploadInformation.setUploadedSize(end + 1);
                double dataFetched = end - start + 1;
                double timeElapsed = (endTime - startTime) / 1000.0;
                uploadInformation.setSpeed(dataFetched / timeElapsed);
            }

            uploadInformation.setUploadStatus(UploadStatus.completed);

        } catch (Throwable e) {
            handleError(e);
        }
    }

    private void handleError(Throwable e) {
        uploadInformation.setUploadStatus(UploadStatus.failed);
        uploadInformation.setErrorMessage(e.getMessage());
        LOGGER.log(Level.SEVERE, null, e);
    }
}
