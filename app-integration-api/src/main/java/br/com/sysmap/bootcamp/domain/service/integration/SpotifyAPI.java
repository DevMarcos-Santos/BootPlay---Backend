package br.com.sysmap.bootcamp.domain.service.integration;

import br.com.sysmap.bootcamp.domain.model.AlbumDto;
import br.com.sysmap.bootcamp.domain.mapper.AlbumMapper;
import com.neovisionaries.i18n.CountryCode;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Service
public class SpotifyAPI {

    private SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId("96b2342e09f94783983503cfe34ea124")
            .setClientSecret("1a56d012077d4c8482a65e6167e60b79")
            .build();


    public List<AlbumDto> getAlbums(@RequestParam("Album") String Album) throws IOException, ParseException, SpotifyWebApiException {

        spotifyApi.setAccessToken(getToken());

        Random random = new Random();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.ENGLISH));


        return AlbumMapper.album_mapper.toModel(

                spotifyApi.searchAlbums(Album)
                        .market(CountryCode.BR)
                        .limit(25)
                        .build()
                        .execute()
                        .getItems())
                        .stream()
                        .peek(album -> {
                            double RandomValue = random.nextDouble() * 100;
                            String FormatedValue = decimalFormat.format(RandomValue);
                            album.setAlbumValue(new BigDecimal(FormatedValue));
                        }).toList();






    }

    public String getToken() throws IOException, ParseException, SpotifyWebApiException {
        ClientCredentialsRequest credential = spotifyApi.clientCredentials().build();
        return credential.execute().getAccessToken();
    }

}
