package br.com.sysmap.bootcamp.domain.mapper;

import br.com.sysmap.bootcamp.domain.model.AlbumDto;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;

import java.util.List;

@Named("AlbumMapper")
@Mapper
public interface AlbumMapper {

    AlbumMapper album_mapper = Mappers.getMapper(AlbumMapper.class);

    List<AlbumDto> toModel(AlbumSimplified[] albumSimplifiedList);


}
