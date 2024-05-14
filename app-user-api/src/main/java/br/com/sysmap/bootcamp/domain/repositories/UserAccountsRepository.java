package br.com.sysmap.bootcamp.domain.repositories;

import br.com.sysmap.bootcamp.domain.entities.UserAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountsRepository extends JpaRepository<UserAccounts, Long> {

    Optional<UserAccounts> findByEmail(String email);
}
