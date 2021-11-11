package com.az.covid19.analysis.controller;

import com.az.covid19.analysis.constant.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppConstants.API_COVID_ANALYSIS_URI)
@SecurityRequirement(name = "az-covid19-analysis")
public class CovidAnalysisController {

    @GetMapping("/test")
    public String test() {
        return "Welcome to covid analysis API";
    }

}
