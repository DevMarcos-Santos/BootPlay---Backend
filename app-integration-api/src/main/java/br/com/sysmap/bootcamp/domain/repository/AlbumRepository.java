package br.com.sysmap.bootcamp.domain.repository;

import br.com.sysmap.bootcamp.domain.entities.Album;
import br.com.sysmap.bootcamp.domain.entities.UserAccounts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    List<Album> findAllByUserAccounts(UserAccounts userAccounts);
    Optional<Album> deleteAlbumById(UserAccounts userAccounts);
    boolean existsByUserAccountsAndIdSpotify(UserAccounts userAccounts, String idSpotify);
}
