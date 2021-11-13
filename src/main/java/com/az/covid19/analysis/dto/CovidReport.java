package com.az.covid19.analysis.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CovidReport {

    private String country;
    private String state;
    private double latitude;
    private double longitude;
    private Integer todayCases;
    private Map<LocalDate, Integer> dateWiseCases;
}
