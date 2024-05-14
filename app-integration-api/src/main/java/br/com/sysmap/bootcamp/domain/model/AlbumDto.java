package br.com.sysmap.bootcamp.domain.model;

import lombok.Data;
import se.michaelthelin.spotify.enums.AlbumType;
import se.michaelthelin.spotify.enums.ModelObjectType;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.ExternalUrl;
import se.michaelthelin.spotify.model_objects.specification.Image;

import java.math.BigDecimal;


@Data
public class AlbumDto {

    private AlbumType albumType;
    private ArtistSimplified[] artists;
    private ExternalUrl externalUrls;
    private String id;
    private String name;
    private Image[] images;
    private String releaseDate;
    private ModelObjectType modelObjectType;
    private BigDecimal albumValue;
}
