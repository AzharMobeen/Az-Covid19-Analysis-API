package com.az.covid19.analysis.service.impl;

import com.az.covid19.analysis.constant.AppConstants;
import com.az.covid19.analysis.dto.CovidReport;
import com.az.covid19.analysis.dto.AllNewCasesResponse;
import com.az.covid19.analysis.exception.CustomRuntimeException;
import com.az.covid19.analysis.helper.CovidAnalysisServiceHelper;
import com.az.covid19.analysis.service.CovidAnalysisService;
import com.az.covid19.analysis.validator.CovidAnalysisValidator;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class CovidAnalysisServiceImpl implements CovidAnalysisService {

    private final CovidAnalysisServiceHelper covidAnalysisServiceHelper;
    private final CovidAnalysisValidator covidAnalysisValidator;

    @Override
    public AllNewCasesResponse getTodayAllNewCases() {
        List<CovidReport> covidReportList = getAllNewCasesFromCSV(true, 0);
        return AllNewCasesResponse.builder().covidReportList(covidReportList).build();
    }

    @Override
    public AllNewCasesResponse getTodayAllNewCasesDescOrder() {
        List<CovidReport> covidReportList = getAllNewCasesFromCSV(true, 0);
        covidReportList = covidReportList.parallelStream()
                .sorted(Comparator.comparing(CovidReport::getTodayCases).reversed())
                .collect(Collectors.toList());
        return AllNewCasesResponse.builder().covidReportList(covidReportList).build();
    }

    @Override
    public CovidReport getTodayAllNewCasesInSpecificCountry(String country) {
        covidAnalysisValidator.validateCountry(country);
        List<CovidReport> covidReportList = getAllNewCasesFromCSV(true, 0);
        Optional<CovidReport> covidReportOptional = covidReportList.parallelStream()
                .filter(covidReport -> covidReport.getCountry().equalsIgnoreCase(country))
                .findFirst();
        if(covidReportOptional.isPresent())
            return covidReportOptional.get();
        throw new CustomRuntimeException("Data not found", "Data not found for required country", HttpStatus.NOT_FOUND);
    }

    @Override
    public AllNewCasesResponse getAllNewCasesInCountryAndSinceDate(String country, String date) {
        covidAnalysisValidator.validateCountry(country);
        covidAnalysisValidator.validateDate(date);
        LocalDate sinceDate = covidAnalysisServiceHelper.convertStringToLocalDate(date);
        covidAnalysisValidator.validateDateWithCurrentDate(sinceDate);
        long days =  ChronoUnit.DAYS.between(sinceDate, LocalDate.now());
        List<CovidReport> covidReportList = getAllNewCasesFromCSV(false, (int) days);
        covidReportList = covidReportList.parallelStream()
                .filter(covidReport -> covidReport.getCountry().equalsIgnoreCase(country))
                .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(covidReportList))
            throw new CustomRuntimeException("Data not found", "Data not found for required country",
                    HttpStatus.NOT_FOUND);
        return AllNewCasesResponse.builder().covidReportList(covidReportList).build();
    }

    @Override
    public AllNewCasesResponse getTodayAllNewCasesForTopNCountries(int topN) {
        List<CovidReport> covidReportList = getAllNewCasesFromCSV(true, 0);
        covidReportList = covidReportList.parallelStream()
                .sorted(Comparator.comparing(CovidReport::getTodayCases).reversed()).limit(topN)
                .collect(Collectors.toList());
        return AllNewCasesResponse.builder().covidReportList(covidReportList).build();
    }

    private List<CovidReport> getAllNewCasesFromCSV(boolean onlyTodayCases, int fromIndex) {
        CSVParser csvParser = new CSVParserBuilder().withSeparator(',').build();
        List<CovidReport> covidReportList = new ArrayList<>();
        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(AppConstants.CSV_FILE))) {
            CSVReader reader = new CSVReaderBuilder(inputStreamReader).withCSVParser(csvParser).withSkipLines(1).build();
            String[] lineInArray;
            while ((lineInArray = reader.readNext()) != null) {
                CovidReport.CovidReportBuilder covidReport = CovidReport.builder().state(lineInArray[0])
                    .country(lineInArray[1]).latitude(covidAnalysisServiceHelper.parseStringToDouble(lineInArray[2]))
                    .longitude(covidAnalysisServiceHelper.parseStringToDouble(lineInArray[3]))
                    .todayCases(covidAnalysisServiceHelper.parseStringToInt(lineInArray[lineInArray.length-1]));
                if(!onlyTodayCases)
                    covidReport.dateWiseCases(covidAnalysisServiceHelper.buildDateWiseCases(lineInArray, fromIndex));
                covidReportList.add(covidReport.build());
            }
            log.info("Total number of records {} ", covidReportList.size());
        } catch (IOException | CsvValidationException ex) {
            log.error("Exception occurred while processing CSV file :: ", ex);
        }
        return covidReportList;
    }
}