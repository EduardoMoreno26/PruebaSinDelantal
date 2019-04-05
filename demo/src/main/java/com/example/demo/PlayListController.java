package com.example.demo;

import com.example.demo.Utils.Validator;
import com.example.demo.enums.Categories;
import com.example.demo.gateway.OpenWeatherMapGateway;
import com.example.demo.gateway.SpotifyGateway;
import com.wrapper.spotify.SpotifyApi;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.xml.ws.Response;
import java.util.List;

@Controller
public class PlayListController {

    @Autowired
    private OpenWeatherMapGateway openweathermapGateway;
    @Autowired
    private SpotifyGateway spotifyGateway;

    @GetMapping(value = "/v1/playlists/cities/{city_name}", produces = {"application/json" })
    public ResponseEntity<List<String>>  getTracksByCityName(@PathVariable("city_name") final String cityName, @RequestParam(value = "country", required = false) @SafeHtml String country) {

        System.out.println(cityName);
        System.out.println(country);

        final String location = Validator.validateCityAndCountryCode(cityName, country);

        final Double weatherTemp = openweathermapGateway.getWeatherByCity(location);

        final SpotifyApi credentialsSpotify = spotifyGateway.getCredentialsSpotify();

        final String categoryPlayLists = spotifyGateway.getCategoryPlayLists(credentialsSpotify, evaluateTemperature(weatherTemp));

        return ResponseEntity.ok(spotifyGateway.getPlayListsTracks(credentialsSpotify, categoryPlayLists));

    }

    @GetMapping(value = "/v1/playlists/coordinates/", produces = {"application/json" })
    public ResponseEntity<List<String>> getTracksByCoordinates(@RequestParam(value = "latitude", required = false) @SafeHtml String latitude, @RequestParam(value = "length", required = false) @SafeHtml String length) {

        System.out.println(latitude);
        System.out.println(length);

        Validator.validateCoordinates(latitude,length);

        final Double weatherTemp = openweathermapGateway.getWeatherByCoordinates(latitude,length);

        final SpotifyApi credentialsSpotify = spotifyGateway.getCredentialsSpotify();

        final String categoryPlayLists = spotifyGateway.getCategoryPlayLists(credentialsSpotify, evaluateTemperature(weatherTemp));

        return ResponseEntity.ok(spotifyGateway.getPlayListsTracks(credentialsSpotify, categoryPlayLists));

    }

    private Categories evaluateTemperature(Double temperature) {

        if (temperature > 30) {
            return Categories.PARTY;
        } else if (temperature > 15 && temperature < 30) {
            return Categories.POP;
        } else if (temperature > 10 && temperature < 14) {
            return Categories.ROCK;
        } else if (temperature < 14) {
            return Categories.CLASSICAL;
        }
        return Categories.MEXICO;
    }

}
