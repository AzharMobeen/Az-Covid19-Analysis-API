package com.az.covid19.analysis.validator;

import com.az.covid19.analysis.exception.CustomRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

@Component
public class CovidAnalysisValidator {

    public void validateCountry(String country) {
        if(!StringUtils.hasText(country))
            throw new CustomRuntimeException("Invalid Country", "Provided country is not valid",
                    HttpStatus.BAD_REQUEST);
    }

    public void validateDate(String date) {
        if(!StringUtils.hasText(date) )
            throw new CustomRuntimeException("Invalid Date", "Provided date is not valid",
                    HttpStatus.BAD_REQUEST);

    }

    public void validateDateWithCurrentDate(LocalDate sinceDate) {
        if(sinceDate.isAfter(LocalDate.now()))
            throw new CustomRuntimeException("Invalid Since Date", "Provided date should be old date",
                    HttpStatus.BAD_REQUEST);
    }
}
