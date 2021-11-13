package com.az.covid19.analysis.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class AllNewCasesResponse {
    private List<CovidReport> covidReportList;
}
