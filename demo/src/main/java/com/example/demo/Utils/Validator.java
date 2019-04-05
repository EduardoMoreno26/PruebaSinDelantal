package com.example.demo.Utils;

import org.springframework.util.StringUtils;

public class Validator {


    public static String validateCityAndCountryCode(final String cityName, final String countryCode) {

        if (!StringUtils.isEmpty(cityName) && !StringUtils.isEmpty(countryCode)) {
            return cityName + "," + countryCode;
        }
        System.out.println("ERROR the values is Empty");

        throw new RuntimeException("ERROR the values is Empty");
    }

    public static void validateCoordinates(final String latitude, final String length) {

        if (!StringUtils.isEmpty(latitude) && !StringUtils.isEmpty(length)) {

            System.out.println("Success values");

        } else {
            throw new RuntimeException("ERROR the values is Empty");
        }

    }


}
