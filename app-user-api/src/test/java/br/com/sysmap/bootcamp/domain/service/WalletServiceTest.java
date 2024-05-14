package br.com.sysmap.bootcamp.domain.service;

import br.com.sysmap.bootcamp.domain.entities.UserAccounts;
import br.com.sysmap.bootcamp.domain.entities.Wallet;
import br.com.sysmap.bootcamp.domain.repositories.UserAccountsRepository;
import br.com.sysmap.bootcamp.domain.repositories.WalletRepository;
import br.com.sysmap.bootcamp.domain.services.WalletService;
import br.com.sysmap.bootcamp.dto.WalletDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)

public class WalletServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockBean
    private WalletService walletService;

    @MockBean
    WalletRepository walletRepository;

    @MockBean
    private WalletDto walletDto;

    @MockBean
    private UserAccountsRepository userAccountsRepository;

    @MockBean
    private Wallet wallet;


    @BeforeEach
    void setup(){


    }


    @Test
    @DisplayName("My Wallet")
    void MyWallet() throws Exception {

        when(walletService.myWallet()).thenReturn(Optional.of(new Wallet()));

        Optional<Wallet> walletOptional = walletService.myWallet();

        assertNotNull(walletOptional);
        assertTrue(walletOptional.isPresent());

    }

    @Test
    @DisplayName("Wallet Credit")
    void WalletCredit() throws Exception {

        Authentication auth = new UsernamePasswordAuthenticationToken("test", "test" ,  AuthorityUtils.createAuthorityList("ROLE_TEST"));

        SecurityContextHolder.getContext().setAuthentication(auth);

        BigDecimal value = new BigDecimal(100);

        Mockito.doNothing().when(walletService).credit(value);

        walletService.credit(value);

        Mockito.verify(walletService, Mockito.times(1)).credit(value);
    }

    @Test
    @DisplayName("Wallet Debit")
    void WalletDebit() throws Exception {
        UserAccounts Newuser = null;
        Wallet Newwalet = null;


        try {
            Newuser = UserAccounts.builder().email("ilbormagnodelasnoches@gmail.com").password("ilborpassword").build();
            Mockito.when(userAccountsRepository.save(Newuser)).thenReturn(Newuser);
        } catch (Exception e){
            System.out.println("Exception caught on user creation:" + e.getMessage());
        }


        try{
            Newwalet = new Wallet();
            Newwalet.setBalance(new BigDecimal("200.00"));
            Newwalet.setUserAccounts(Newuser);
            Newwalet.setLastUpdate(LocalDateTime.now());

            Mockito.when(walletRepository.save(Newwalet)).thenReturn(Newwalet);
        }catch (Exception e){
            System.out.println("Exception caught on wallet creation:" + e.getMessage());
        }


        try{
            Authentication auth = new UsernamePasswordAuthenticationToken("ilbormagnodelasnoches@gmail.com", "ilborpassword" ,  AuthorityUtils.createAuthorityList("ROLE_TEST"));

            SecurityContextHolder.getContext().setAuthentication(auth);

            BigDecimal myWallet = Newwalet.getBalance();

            walletDto.setEmail(Newuser.getEmail());
            walletDto.setValue(new BigDecimal(25));

            Mockito.doNothing().when(walletService).debit(walletDto);
        }catch (Exception e){
            System.out.println("Exception caught on debit creation:" + e.getMessage());
        }
    }

    @Test
    @DisplayName("Test Points")
    void Points() throws Exception {
        Authentication auth = new UsernamePasswordAuthenticationToken("test", "test" ,  AuthorityUtils.createAuthorityList("ROLE_TEST"));

        SecurityContextHolder.getContext().setAuthentication(auth);

        Mockito.doNothing().when(walletService).serviceDay();

        walletService.serviceDay();

        Mockito.verify(walletService, Mockito.times(1)).serviceDay();
    }

}





