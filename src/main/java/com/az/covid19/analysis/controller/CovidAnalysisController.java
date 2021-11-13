package com.az.covid19.analysis.controller;

import com.az.covid19.analysis.constant.AppConstants;
import com.az.covid19.analysis.dto.CovidReport;
import com.az.covid19.analysis.dto.AllNewCasesResponse;
import com.az.covid19.analysis.exception.ErrorMessage;
import com.az.covid19.analysis.service.CovidAnalysisService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.URI_FOR_COVID_ANALYSIS)
@SecurityRequirement(name = AppConstants.SECURITY_SCHEME_NAME)
public class CovidAnalysisController {

    private final CovidAnalysisService covidAnalysisService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success", content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AllNewCasesResponse.class))}),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = {@Content(
                    schema = @Schema(implementation = ErrorMessage.class)
            )}),
            @ApiResponse(responseCode = "406", description = "NOT ACCEPTABLE", content = {@Content(
                    schema = @Schema(implementation = ErrorMessage.class)
            )})
        }
    )
    @GetMapping(AppConstants.URI_FOR_TODAY_ALL_NEW_CASES)
    public ResponseEntity<AllNewCasesResponse> getTodayAllNewCases() {
        AllNewCasesResponse response = covidAnalysisService.getTodayAllNewCases();
        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success", content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AllNewCasesResponse.class))}),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = {@Content(
                    schema = @Schema(implementation = ErrorMessage.class)
            )}),
            @ApiResponse(responseCode = "406", description = "NOT ACCEPTABLE", content = {@Content(
                    schema = @Schema(implementation = ErrorMessage.class)
            )})
        }
    )
    @GetMapping(AppConstants.URI_FOR_TODAY_ALL_NEW_CASES_DESC)
    public ResponseEntity<AllNewCasesResponse> getTodayAllNewCasesDescOrder() {
        return ResponseEntity.ok(covidAnalysisService.getTodayAllNewCasesDescOrder());
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success", content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CovidReport.class))}),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = {@Content(
                    schema = @Schema(implementation = ErrorMessage.class)
            )}),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = {@Content(
                    schema = @Schema(implementation = ErrorMessage.class)
            )}),
            @ApiResponse(responseCode = "406", description = "NOT ACCEPTABLE", content = {@Content(
                    schema = @Schema(implementation = ErrorMessage.class)
            )})
        }
    )
    @GetMapping(AppConstants.URI_FOR_TODAY_ALL_NEW_CASES_IN_COUNTRY)
    public ResponseEntity<CovidReport> getTodayAllNewCasesInCountry(@PathVariable(required = true) String country) {
        return ResponseEntity.ok(covidAnalysisService.getTodayAllNewCasesInSpecificCountry(country));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success", content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AllNewCasesResponse.class))}),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = {@Content(
                    schema = @Schema(implementation = ErrorMessage.class)
            )}),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = {@Content(
                    schema = @Schema(implementation = ErrorMessage.class)
            )}),
            @ApiResponse(responseCode = "406", description = "NOT ACCEPTABLE", content = {@Content(
                    schema = @Schema(implementation = ErrorMessage.class)
            )})
        }
    )
    @GetMapping(AppConstants.URI_FOR_ALL_NEW_CASES_IN_COUNTRY_SINCE_DATE)
    public ResponseEntity<AllNewCasesResponse> getAllNewCasesInCountryAndSinceDate(
            @PathVariable(required = true) String country, @PathVariable(required = true) String date) {
        return ResponseEntity.ok(covidAnalysisService.getAllNewCasesInCountryAndSinceDate(country,date));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success", content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AllNewCasesResponse.class))}),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = {@Content(
                    schema = @Schema(implementation = ErrorMessage.class)
            )}),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = {@Content(
                    schema = @Schema(implementation = ErrorMessage.class)
            )}),
            @ApiResponse(responseCode = "406", description = "NOT ACCEPTABLE", content = {@Content(
                    schema = @Schema(implementation = ErrorMessage.class)
            )})
        }
    )
    @GetMapping(AppConstants.URI_FOR_TODAY_ALL_NEW_CASES_IN_TOP_N_COUNTRIES)
    public ResponseEntity<AllNewCasesResponse> getTodayAllNewCasesForTopNCountries(
            @PathVariable(required = true) int topN) {
        return ResponseEntity.ok(covidAnalysisService.getTodayAllNewCasesForTopNCountries(topN));
    }
}
