package br.com.sysmap.bootcamp.web;

import br.com.sysmap.bootcamp.domain.entities.UserAccounts;
import br.com.sysmap.bootcamp.domain.entities.Wallet;
import br.com.sysmap.bootcamp.domain.services.UserAccountsService;
import br.com.sysmap.bootcamp.domain.services.WalletService;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class WalletControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private AuthenticationManager  authenticationManager;

    @Mock
    private WalletService walletService;

    @Mock
    private UserAccountsService userAccountsService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp(){

        objectMapper = new ObjectMapper();

    }



    @Test
    public void testCreditWallet() throws Exception {

        //First creating the user, as we are using the memory bank

        UserAccounts userAccounts = UserAccounts.builder().name("admin").email("admin").password("admin").build();
        Mockito.when(userAccountsService.save(userAccounts)).thenReturn(userAccounts);

        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAccounts)))
                .andExpect(status().isOk());

        Authentication auth = new UsernamePasswordAuthenticationToken("admin", "admin" ,  AuthorityUtils.createAuthorityList("ROLE_ADMIN"));

        SecurityContextHolder.getContext().setAuthentication(auth);

        BigDecimal creditValue = new BigDecimal(100);

        Mockito.doNothing().when(walletService).credit(creditValue);

        mockMvc.perform(post("/wallet/credit/{value}", creditValue))
                .andExpect(status().isOk())
                .andExpect(content().string("Credit successful"));

    }

    @Test
    @DisplayName("Should return a wallet")
    public void shouldReturnWallet() throws Exception {

        //First creating the user, as we are using the memory bank

        UserAccounts userAccounts = UserAccounts.builder().name("test").email("test").password("test").build();
        Mockito.when(userAccountsService.save(userAccounts)).thenReturn(userAccounts);

        Authentication auth = new UsernamePasswordAuthenticationToken("test", "test" ,  AuthorityUtils.createAuthorityList("ROLE_TEST"));

        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAccounts)))
                .andExpect(status().isOk());



        //Now we will simulate the wallet search

        Mockito.when(walletService.myWallet()).thenReturn(Optional.of(new Wallet()));

        mockMvc.perform(get("/wallet")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(100))
                .andExpect(jsonPath("$.points").value(0));


    }


}
