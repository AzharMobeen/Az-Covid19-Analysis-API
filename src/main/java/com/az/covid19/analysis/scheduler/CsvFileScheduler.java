package com.az.covid19.analysis.scheduler;

import com.az.covid19.analysis.constant.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

@Slf4j
@Component
public class CsvFileScheduler {

    // 21600000 milliseconds = 6 hr | 1000 milliseconds = 1 seconds
    @Scheduled(fixedDelay = 21600000, initialDelay = 1000)
    public void processCsvFile(){
        log.info("CsvFileScheduler scheduler started");
        try {
            URL url = new URL(AppConstants.CSV_URL);
            URLConnection urlConnection = url.openConnection();
            saveCSVFile(urlConnection.getInputStream());
        } catch (Exception exception) {
            log.error("Exception occurred while processing CSV file", exception);
        }
        log.info("CsvFileScheduler scheduler successfully completed!");
    }

    public static void saveCSVFile(InputStream inputStream) {
        try (OutputStream outputStream = new FileOutputStream(AppConstants.CSV_FILE)) {
            IOUtils.copy(inputStream,outputStream);
        } catch (IOException ioException) {
            log.error("Exception occurred while storing CSV file", ioException);
        }
    }
}
