package com.az.covid19.analysis.constant;

public class AppConstants {
    public static final String AUTHORIZATION = "Authorization";
    public static final String H2_CONSOLE_URI = "/h2-console/**";

    public static final String CSV_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    public static final String CSV_FILE = "requiredFile.csv";

    public static final String API_VERSION = "1.0";
    public static final String API_DESCRIPTION = "Johns Hopkins University Center for Systems Science and Engineering (JHU CSSE) publishes data about Coronavirus COVID-19 impact on a daily basis." +
            " In this API we try to give endpoints for analysis";
    public static final String API_TITLE = "Az Covid19 Analysis API";
    public static final String SECURITY_SCHEME_NAME = "az-covid19-analysis";
    public static final String SECURITY_SCHEME = "Bearer";

    public static final String REPORT_START_DATE = "1-22-20";

    public static final String URI_VERSION  =   "/api/v1";
    // For User Controller URIs
    public static final String URI_FOR_USERS = URI_VERSION + "/users";
    public static final String URI_FOR_USERS_AUTHENTICATE = "/authenticate";
    // For Covid Analysis Controller URIs
    public static final String URI_FOR_COVID_ANALYSIS = URI_VERSION + "/covid/cases";
    public static final String URI_FOR_TODAY_ALL_NEW_CASES = "/today";
    public static final String URI_FOR_TODAY_ALL_NEW_CASES_DESC = "/today/desc";
    public static final String URI_FOR_TODAY_ALL_NEW_CASES_IN_COUNTRY = "/today/{country}";
    public static final String URI_FOR_TODAY_ALL_NEW_CASES_IN_TOP_N_COUNTRIES = "/today/countries/{topN}";
    public static final String URI_FOR_ALL_NEW_CASES_IN_COUNTRY_SINCE_DATE = "/{country}/{date}";
}
