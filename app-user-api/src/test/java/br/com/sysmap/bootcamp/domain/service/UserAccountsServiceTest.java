package br.com.sysmap.bootcamp.domain.service;

import  br.com.sysmap.bootcamp.domain.entities.UserAccounts;
import br.com.sysmap.bootcamp.domain.repositories.UserAccountsRepository;
import br.com.sysmap.bootcamp.domain.repositories.WalletRepository;
import br.com.sysmap.bootcamp.domain.services.UserAccountsService;

import br.com.sysmap.bootcamp.dto.UserAccountsDto;
import jakarta.validation.constraints.NotNull;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserAccountsServiceTest {

    @Autowired
    private UserAccountsService userAccountsService;

    @MockBean
    private UserAccountsRepository userAccountsRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private WalletRepository walletRepository;



    @Test
    @DisplayName("should Return Users When Valid Users Is Saved")
     void shouldReturnUsersWhenValidUserIsSaved() {

        UserAccountsRepository userAccountsRepository = mock(UserAccountsRepository.class);

        UserAccountsService userAccountsService = new UserAccountsService(userAccountsRepository, passwordEncoder, walletRepository);

        UserAccounts userSave = UserAccounts.builder().name("Marcos").email("marcos@gmail.com").password("1234").build();

        when(userAccountsRepository.save(any(UserAccounts.class))).thenReturn(userSave);

        UserAccounts savedUser = userAccountsService.save(userSave);

        assertEquals(userSave, savedUser);

    }

    @Test
    @DisplayName("Should return user by id")
    public void shouldReturnUserById() throws Exception {
        Long userId = 1L;

        UserAccounts userAccounts = UserAccounts.builder().id(userId).build();

        when(userAccountsService.findById(userId)).thenReturn(Optional.of(userAccounts));

        Optional<UserAccounts> result = userAccountsService.findById(userId);


        assertEquals(userAccounts, result.get());

    }

    @Test
    @DisplayName("If the id does not exist, return null")
    public void shouldReturnNullForNonexistentId() throws Exception {

        Long userId = 1L;

        UserAccounts userAccounts = UserAccounts.builder().id(userId).build();

        when(userAccountsService.findById(userId)).thenReturn(null);

        Optional<UserAccounts> result = userAccountsService.findById(userId);

        //When there is no user with the id, it will return null
        assertNull(result);

    }



    @Test
    @DisplayName("Should return all users")
    public void shouldReturnAllUsers() throws Exception {

        List<UserAccounts> userList = Arrays.asList(
                UserAccounts.builder().id(1L).name("test1").email("test1@gmail.com").password("test").build(),
                UserAccounts.builder().id(2L).name("test2").email("test2@gmail.com").password("test").build(),
                UserAccounts.builder().id(3L).name("test3").email("test3@gmail.com").password("teste").build()
        );

        when(userAccountsService.findAll()).thenReturn(userList);

        List<UserAccounts> result = userAccountsService.findAll();

        assertEquals(userList, result);


    }
}
