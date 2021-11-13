package com.az.covid19.analysis.helper;

import com.az.covid19.analysis.constant.AppConstants;
import com.az.covid19.analysis.exception.CustomRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CovidAnalysisServiceHelper {

    public Map<LocalDate, Integer> buildDateWiseCases(String[] lineInArray, int from) {
        Map<LocalDate, Integer> dateWiseCases = new HashMap<>();
        LocalDate startDate = convertStringToLocalDate(AppConstants.REPORT_START_DATE);
        int lastIndex = lineInArray.length - 1;
        int startIndex = lastIndex-from;
        for(int index = startIndex; index <= lastIndex; index++) {
            dateWiseCases.put(startDate, parseStringToInt(lineInArray[index]));
            startDate = startDate.plusDays(1);
        }
        return dateWiseCases;
    }

    // LocalDate always return yyyy-mm-dd format
    public LocalDate convertStringToLocalDate(String date) {
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("M-dd-yy");
        try {
            return LocalDate.parse(date, parser);
        } catch (Exception e) {
            log.error("Invalid date format :: ", e);
            throw new CustomRuntimeException("Invalid date", e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    public double parseStringToDouble(String s) {
        return StringUtils.hasText(s) ? Double.parseDouble(s): 0;
    }

    public int parseStringToInt(String s) {
        return StringUtils.hasText(s) ? Integer.parseInt(s) : 0;
    }
}
