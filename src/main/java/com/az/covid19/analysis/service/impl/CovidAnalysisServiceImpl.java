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
        List<CovidReport> covidReportList = covidAnalysisServiceHelper.getAllNewCasesFromCSV(true, 0);
        return AllNewCasesResponse.builder().covidReportList(covidReportList).build();
    }

    @Override
    public AllNewCasesResponse getTodayAllNewCasesDescOrder() {
        List<CovidReport> covidReportList = covidAnalysisServiceHelper.getAllNewCasesFromCSV(true, 0);
        covidReportList = covidReportList.parallelStream()
                .sorted(Comparator.comparing(CovidReport::getTodayCases).reversed())
                .collect(Collectors.toList());
        return AllNewCasesResponse.builder().covidReportList(covidReportList).build();
    }

    @Override
    public CovidReport getTodayAllNewCasesInSpecificCountry(String country) {
        covidAnalysisValidator.validateCountry(country);
        List<CovidReport> covidReportList = covidAnalysisServiceHelper.getAllNewCasesFromCSV(true, 0);
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
        List<CovidReport> covidReportList = covidAnalysisServiceHelper.getAllNewCasesFromCSV(false, (int) days);
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
        List<CovidReport> covidReportList = covidAnalysisServiceHelper.getAllNewCasesFromCSV(true, 0);
        covidReportList = covidReportList.parallelStream()
                .sorted(Comparator.comparing(CovidReport::getTodayCases).reversed()).limit(topN)
                .collect(Collectors.toList());
        return AllNewCasesResponse.builder().covidReportList(covidReportList).build();
    }


}