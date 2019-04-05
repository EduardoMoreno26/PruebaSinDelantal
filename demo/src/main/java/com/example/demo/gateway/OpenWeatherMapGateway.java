package com.example.demo.gateway;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.example.demo.Utils.Constants.*;

@Component
public class OpenWeatherMapGateway {

    @Value("${openWeather.key}")
    private String apiKey;

    @Value("${openWeather.url}")
    private String url;

    private final RestTemplate restTemplate;

    @Autowired
    public OpenWeatherMapGateway(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public Double getWeatherByCity(final String location) {

        ResponseEntity<String> responseEntity =
                this.restTemplate.exchange(addParametersToTheURLAndGetIt(location), HttpMethod.GET, headersOnlyAccept(), String.class);

        Map<String, Object> map = jsonToMap(responseEntity.getBody());

        Map<String, Object> mainMap = jsonToMap(map.get(MAIN).toString());

        return (Double) mainMap.get(TEMP);

    }

    public Double getWeatherByCoordinates (final String latitude , final String length){

        ResponseEntity<String> responseEntity =
                this.restTemplate.exchange(addParametersToTheURLAndGetIt(latitude,length), HttpMethod.GET, headersOnlyAccept(), String.class);

        Map<String, Object> map = jsonToMap(responseEntity.getBody());

        Map<String, Object> mainMap = jsonToMap(map.get(MAIN).toString());

        return (Double) mainMap.get(TEMP);


    }

    private URI addParametersToTheURLAndGetIt(final String latitude, final String length) {

        return UriComponentsBuilder
                .fromHttpUrl(url)
                .queryParam(PARAM_LAT, latitude)
                .queryParam(PARAM_LON, length)
                .queryParam(APP_ID_KEY, apiKey)
                .queryParam(UNITS, CELSIUS)
                .build().encode().toUri();
    }

    private URI addParametersToTheURLAndGetIt(final String location) {

        return UriComponentsBuilder
                .fromHttpUrl(url)
                .queryParam(PARAM_CITY, location)
                .queryParam(APP_ID_KEY, apiKey)
                .queryParam(UNITS, CELSIUS)
                .build().encode().toUri();
    }

    private static HttpEntity headersOnlyAccept() {
        final HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        return new HttpEntity(headers);
    }

    private Map<String, Object> jsonToMap(final String response) {

        final Type type = new TypeToken<HashMap<String, Object>>() {
        }.getType();

        return new Gson().fromJson(response, type);
    }

}
