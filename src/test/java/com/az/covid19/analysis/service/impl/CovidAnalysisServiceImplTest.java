package com.az.covid19.analysis.service.impl;

import com.az.covid19.analysis.dto.AllNewCasesResponse;
import com.az.covid19.analysis.dto.CovidReport;
import com.az.covid19.analysis.exception.CustomRuntimeException;
import com.az.covid19.analysis.helper.CovidAnalysisServiceHelper;
import com.az.covid19.analysis.validator.CovidAnalysisValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@DisplayName("CovidAnalysisService Test Cases")
class CovidAnalysisServiceImplTest {

    @InjectMocks
    CovidAnalysisServiceImpl covidAnalysisService;

    @Mock
    CovidAnalysisServiceHelper covidAnalysisServiceHelper;

    @Mock
    CovidAnalysisValidator covidAnalysisValidator;

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.openMocks(this);
        Mockito.reset(covidAnalysisServiceHelper);
    }

    @Test
    void getTodayAllNewCases() {
        List<CovidReport> covidReportList = TestUtil.buildCovidReportList();
        Mockito.when(covidAnalysisServiceHelper.getAllNewCasesFromCSV(true,0))
                .thenReturn(covidReportList);
        AllNewCasesResponse response = covidAnalysisService.getTodayAllNewCases();
        assertNotNull(response);
        assertTrue(response.getCovidReportList().size() > 0);
    }

    @Test
    void getTodayAllNewCasesDescOrder() {
        List<CovidReport> covidReportList = TestUtil.buildCovidReportList();
        Mockito.when(covidAnalysisServiceHelper.getAllNewCasesFromCSV(true,0))
                .thenReturn(covidReportList);
        AllNewCasesResponse response = covidAnalysisService.getTodayAllNewCasesDescOrder();
        assertNotNull(response);
        assertTrue(response.getCovidReportList().size() > 0);
    }

    @DisplayName("getTodayAllNewCasesInSpecificCountry for failure")
    @Test
    void getTodayAllNewCasesInSpecificCountryFailure() {
        String country = "UAE";
        Mockito.doNothing().when(covidAnalysisValidator).validateCountry(country);
        List<CovidReport> covidReportList = TestUtil.buildCovidReportList();
        Mockito.when(covidAnalysisServiceHelper.getAllNewCasesFromCSV(true,0))
                .thenReturn(covidReportList);
        Assertions.assertThrows(CustomRuntimeException.class,
                () -> covidAnalysisService.getTodayAllNewCasesInSpecificCountry(country), "Data not found");
    }

    @DisplayName("getTodayAllNewCasesInSpecificCountry for Success")
    @Test
    void getTodayAllNewCasesInSpecificCountry() {
        String country = "Pakistan";
        Mockito.doNothing().when(covidAnalysisValidator).validateCountry(country);
        List<CovidReport> covidReportList = TestUtil.buildCovidReportList();
        Mockito.when(covidAnalysisServiceHelper.getAllNewCasesFromCSV(true,0))
                .thenReturn(covidReportList);
        CovidReport response = covidAnalysisService.getTodayAllNewCasesInSpecificCountry(country);
        assertNotNull(response);
        assertEquals(response.getCountry(), country);
    }

    @DisplayName("getAllNewCasesInCountryAndSinceDate for Success")
    @Test
    void getAllNewCasesInCountryAndSinceDate() {
        String country = "Pakistan"; String date = "11-10-21";
        Mockito.doNothing().when(covidAnalysisValidator).validateCountry(country);
        Mockito.doNothing().when(covidAnalysisValidator).validateDate(date);
        Mockito.when(covidAnalysisServiceHelper.convertStringToLocalDate(date))
                .thenReturn(TestUtil.convertStringToLocalDate("11-10-21"));
        Mockito.doNothing().when(covidAnalysisValidator).validateDateWithCurrentDate(LocalDate.now());

        List<CovidReport> covidReportList = TestUtil.buildCovidReportListForAllNewCasesInCountryAndSinceDate();
        Mockito.when(covidAnalysisServiceHelper.getAllNewCasesFromCSV(false,  4)).thenReturn(covidReportList);
        AllNewCasesResponse response = covidAnalysisService.getAllNewCasesInCountryAndSinceDate(country, date);
        assertNotNull(response);
        assertTrue(response.getCovidReportList().size() > 0);
    }

    @DisplayName("getAllNewCasesInCountryAndSinceDate for Failure")
    @Test
    void getAllNewCasesInCountryAndSinceDateFailure() {
        String country = "UAE"; String date = "11-10-21";
        Mockito.doNothing().when(covidAnalysisValidator).validateCountry(country);
        Mockito.doNothing().when(covidAnalysisValidator).validateDate(date);
        Mockito.when(covidAnalysisServiceHelper.convertStringToLocalDate(date)).thenReturn(LocalDate.now());
        Mockito.doNothing().when(covidAnalysisValidator).validateDateWithCurrentDate(LocalDate.now());

        List<CovidReport> covidReportList = TestUtil.buildCovidReportListForAllNewCasesInCountryAndSinceDate();
        Mockito.when(covidAnalysisServiceHelper.getAllNewCasesFromCSV(false,  4)).thenReturn(covidReportList);
        Assertions.assertThrows(CustomRuntimeException.class, () ->
                covidAnalysisService.getAllNewCasesInCountryAndSinceDate(country, date), "Data not found");
    }

    @Test
    void getTodayAllNewCasesForTopNCountries() {
        List<CovidReport> covidReportList = TestUtil.buildCovidReportList();
        Mockito.when(covidAnalysisServiceHelper.getAllNewCasesFromCSV(true,0))
                .thenReturn(covidReportList);

        AllNewCasesResponse response = covidAnalysisService.getTodayAllNewCasesForTopNCountries(2);
        assertNotNull(response);
        assertTrue(response.getCovidReportList().size() > 0);
    }
}