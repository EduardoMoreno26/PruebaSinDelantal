package com.example.demo.gateway;

import com.example.demo.enums.Categories;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.*;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import com.wrapper.spotify.requests.data.browse.GetCategorysPlaylistsRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistsTracksRequest;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.example.demo.Utils.Constants.LIMIT;
import static com.example.demo.Utils.Constants.OFF;
import static com.neovisionaries.i18n.CountryCode.MX;

@Component
public class SpotifyGateway {

    @Value("${spotify.username}")
    private String clientId;

    @Value("${spotify.password}")
    private String password;

    @SneakyThrows
    public SpotifyApi getCredentialsSpotify() {

        final SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(password)
                .build();

        final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();

        final ClientCredentials clientCredentials = clientCredentialsRequest.execute();

        spotifyApi.setAccessToken(clientCredentials.getAccessToken());

        return spotifyApi;
    }

    @SneakyThrows
    public String getCategoryPlayLists(final SpotifyApi spotifyApi, final Categories categories) {

        final GetCategorysPlaylistsRequest categoryPlayListsRequest = spotifyApi.getCategorysPlaylists(categories.getCategoryId())
                .country(MX)
                .limit(LIMIT)
                .offset(OFF)
                .build();

        final Paging<PlaylistSimplified> responsePlayList = categoryPlayListsRequest.execute();

        List<String> collect = Arrays.stream(responsePlayList.getItems())
                .map(PlaylistSimplified::getId)
                .collect(Collectors.toList());

        final Random r = new Random();

        return collect.stream().skip(r.nextInt(collect.size() - 1)).findFirst().get();

    }

    @SneakyThrows
    public List<String> getPlayListsTracks(final SpotifyApi spotifyApi, final String idPlayList) {

        final GetPlaylistsTracksRequest playListsTracksRequest = spotifyApi.getPlaylistsTracks(idPlayList)
                .market(MX)
                .limit(LIMIT)
                .offset(OFF)
                .build();

        final Paging<PlaylistTrack> reposePlaylistTrack = playListsTracksRequest.execute();

        return createResponsePlayList(reposePlaylistTrack);

    }


    private List<String> createResponsePlayList(final Paging<PlaylistTrack> playlistTrackPaging) {

        final List<String> playList = new ArrayList<>();

        final List<Track> collect = Arrays.stream(playlistTrackPaging.getItems())
                .map(PlaylistTrack::getTrack)
                .collect(Collectors.toList());

        for (Track track : collect) {

            List<ArtistSimplified> artist = Arrays.asList(track.getArtists());

            playList.add(artist.stream()
                    .findFirst()
                    .map(ArtistSimplified::getName)
                    .get() + " -- " + track.getName());
        }

        return playList;
    }

}
