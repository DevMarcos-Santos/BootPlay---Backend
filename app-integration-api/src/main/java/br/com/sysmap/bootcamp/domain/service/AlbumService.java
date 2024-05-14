package br.com.sysmap.bootcamp.domain.service;

import br.com.sysmap.bootcamp.domain.entities.Album;
import br.com.sysmap.bootcamp.domain.entities.UserAccounts;
import br.com.sysmap.bootcamp.domain.model.AlbumDto;
import br.com.sysmap.bootcamp.domain.repository.AlbumRepository;
import br.com.sysmap.bootcamp.domain.service.integration.SpotifyAPI;
import br.com.sysmap.bootcamp.dto.WalletDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.exceptions.detailed.NotFoundException;
import se.michaelthelin.spotify.exceptions.detailed.UnauthorizedException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class AlbumService {

    private final Queue queue;
    private final RabbitTemplate template;
    private final SpotifyAPI spotifyAPI;
    private final AlbumRepository albumRepository;
    private final UsersService usersService;
    private final WalletService walletService;



    public List<AlbumDto> getAlbums(String album) throws IOException, ParseException, SpotifyWebApiException {
        return  this.spotifyAPI.getAlbums(album);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Album saveAlbum(Album album){

            String idSpotify = album.getIdSpotify();
            UserAccounts user = getUser();


            if(albumRepository.existsByUserAccountsAndIdSpotify(user, idSpotify)){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "This album already exists in the collection.");
            }else{

            BigDecimal walletValue = walletService.getBalanceUser(user.getId());
            BigDecimal albumValue = album.getValue();

            if(walletValue.compareTo(albumValue) < 0){
                throw new RuntimeException("Insufficient funds") ;
            }

                album.setUserAccounts(getUser());
                Album albumSaved = albumRepository.save(album);
                WalletDto walletDto = new WalletDto(albumSaved.getUserAccounts().getEmail(), albumSaved.getValue());
                this.template.convertAndSend(queue.getName(), walletDto);



                return albumSaved;
            }





    }

    private UserAccounts getUser() {
        String username = SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal().toString();
        return usersService.findByEmail(username);
    }

    public List<Album> myAlbuns(){
        UserAccounts userAccounts = getUser();
        return albumRepository.findAllByUserAccounts(userAccounts);

    }

    public void deleteAlbum(Long albumId) throws UnauthorizedException, NotFoundException {
        UserAccounts userAccounts = getUser();
        Optional<Album> optionalAlbum = albumRepository.findById(albumId);

        if(optionalAlbum.isPresent()){
            Album album = optionalAlbum.get();
            if(album.getUserAccounts().equals(userAccounts)) {
                albumRepository.delete(album);
            }else {
                throw new UnauthorizedException("This album does not belong to the authenticated user.");
            }
        }else {
            throw new NotFoundException("Album not found with ID: " + albumId);
        }

    }

}
