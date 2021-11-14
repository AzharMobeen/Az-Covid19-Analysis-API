package com.az.covid19.analysis.service.impl;

import com.az.covid19.analysis.dto.CovidReport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TestUtil {

    public static LocalDate convertStringToLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yy");
        return LocalDate.parse(date, formatter);
    }

    public static Map<LocalDate, Integer> buildDateWiseCases() {
        Map<LocalDate, Integer> dateWiseCases = new HashMap<>();
        dateWiseCases.put(convertStringToLocalDate("11-10-21"), 456);
        dateWiseCases.put(convertStringToLocalDate("11-11-21"), 400);
        dateWiseCases.put(convertStringToLocalDate("11-12-21"), 404);
        dateWiseCases.put(convertStringToLocalDate("11-13-21"), 500);
        return dateWiseCases;
    }

    public static List<CovidReport> buildCovidReportListForAllNewCasesInCountryAndSinceDate() {
        return Collections.singletonList(CovidReport.builder().state("--")
                .country("Pakistan").todayCases(4234)
                .dateWiseCases(buildDateWiseCases()).build());
    }

    public static List<CovidReport> buildCovidReportList() {
        return Arrays.asList(CovidReport.builder().state("Punjab").country("Pakistan").todayCases(1234).build(),
                CovidReport.builder().state("US").country("US").todayCases(121234).build(),
                CovidReport.builder().state("India").country("India").todayCases(12234).build(),
                CovidReport.builder().state("UK").country("UK").todayCases(134).build(),
                CovidReport.builder().state("Turkey").country("Turkey").todayCases(1345).build(),
                CovidReport.builder().state("Poland").country("Poland").todayCases(1345).build()
                );
    }
}
