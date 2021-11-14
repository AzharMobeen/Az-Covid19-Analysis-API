package com.az.covid19.analysis.helper;

import com.az.covid19.analysis.constant.AppConstants;
import com.az.covid19.analysis.dto.CovidReport;
import com.az.covid19.analysis.exception.CustomRuntimeException;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CovidAnalysisServiceHelper {

    public List<CovidReport> getAllNewCasesFromCSV(boolean onlyTodayCases, int fromIndex) {
        CSVParser csvParser = new CSVParserBuilder().withSeparator(',').build();
        List<CovidReport> covidReportList = new ArrayList<>();
        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(AppConstants.CSV_FILE))) {
            CSVReader reader = new CSVReaderBuilder(inputStreamReader).withCSVParser(csvParser).withSkipLines(1).build();
            String[] lineInArray;
            while ((lineInArray = reader.readNext()) != null) {
                CovidReport.CovidReportBuilder covidReport = CovidReport.builder().state(lineInArray[0])
                        .country(lineInArray[1]).latitude(parseStringToDouble(lineInArray[2]))
                        .longitude(parseStringToDouble(lineInArray[3]))
                        .todayCases(parseStringToInt(lineInArray[lineInArray.length-1]));
                if(!onlyTodayCases)
                    covidReport.dateWiseCases(buildDateWiseCases(lineInArray, fromIndex));
                covidReportList.add(covidReport.build());
            }
            log.info("Total number of records {} ", covidReportList.size());
        } catch (IOException | CsvValidationException ex) {
            log.error("Exception occurred while processing CSV file :: ", ex);
        }
        return covidReportList;
    }

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
