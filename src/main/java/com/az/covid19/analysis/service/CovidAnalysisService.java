package com.az.covid19.analysis.service;


import com.az.covid19.analysis.dto.CovidReport;
import com.az.covid19.analysis.dto.AllNewCasesResponse;

public interface CovidAnalysisService {

    AllNewCasesResponse getTodayAllNewCases();
    AllNewCasesResponse getTodayAllNewCasesDescOrder();
    CovidReport getTodayAllNewCasesInSpecificCountry(String country);
    AllNewCasesResponse getAllNewCasesInCountryAndSinceDate(String country, String date);
    AllNewCasesResponse getTodayAllNewCasesForTopNCountries(int topN);
}
