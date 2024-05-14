package br.com.sysmap.bootcamp.web;

import br.com.sysmap.bootcamp.domain.entities.UserAccounts;
import br.com.sysmap.bootcamp.domain.repositories.UserAccountsRepository;
import br.com.sysmap.bootcamp.domain.services.UserAccountsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)

public class UserAccountsControllerTest {
    @Autowired
    private MockMvc mockMvc;


    @Mock
    private UserAccountsService userAccountsService;

    @Mock
    private UserAccountsRepository userAccountsRepository;

    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp(){

        objectMapper = new ObjectMapper();

    }

    @Test
    @DisplayName("Should return users when valid users is saved")
    public void shouldReturnUsersWhenValidUsersIsSaved() throws Exception {
        UserAccounts userAccounts = UserAccounts.builder().name("marcos").email("marcos@gmail.com").password("test").build();
        when(userAccountsService.save(userAccounts)).thenReturn(userAccounts);

        mockMvc.perform(post("/users/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userAccounts)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("marcos"))
                .andExpect(jsonPath("$.email").value("marcos@gmail.com"));

    }

    @Test
    @DisplayName("Should return users when valid users is Updated")
    public void shouldReturnUsersWhenValidUsersIsUpdated() throws Exception {

        UserAccounts userAccounts = UserAccounts.builder().id(100L).name("testUpdate").email("testUpdate@gmail.com").password("test").build();
        when(userAccountsRepository.save(userAccounts)).thenReturn(userAccounts);



        UserAccounts userAccountsToUpdate = UserAccounts.builder().id(100L).name("testUpdateEdited").email("marcos@gmail.com").password("test").build();

        mockMvc.perform(put("/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAccountsToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("testUpdateEdited"));

    }


    @Test
    @DisplayName("Should return user by id ")
    public void shouldReturnUserById() throws Exception {

        UserAccounts userAccounts = UserAccounts.builder().id(100L).name("testUpdate").email("testUpdate@gmail.com").password("test").build();
        when(userAccountsRepository.save(userAccounts)).thenReturn(userAccounts);


        when(userAccountsRepository.findById(300L)).thenReturn(Optional.of(userAccounts));
        mockMvc.perform(get("/users/{id}", 300L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAccounts)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return all users")
    public void shouldReturnAllUsers() throws Exception {


        UserAccounts userAccounts = UserAccounts.builder().build();

        when(userAccountsService.findAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAccounts)))
                .andExpect(status().isOk());



    }


    @Test
    @DisplayName("Authentication Test")
    public void Authentication() throws Exception {


        UserAccounts userAccounts = UserAccounts.builder().name("marcos").email("marscos@gmail.com").password("test").build();
        when(userAccountsRepository.save(userAccounts)).thenReturn(userAccounts);
        //first creating user
        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAccounts)))
                .andExpect(status().isOk());

        //Testing authentication
        mockMvc.perform(post("/users/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAccounts)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString());

    }



}
