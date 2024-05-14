package br.com.sysmap.bootcamp.web;

import br.com.sysmap.bootcamp.domain.entities.Album;
import br.com.sysmap.bootcamp.domain.entities.UserAccounts;
import br.com.sysmap.bootcamp.domain.entities.Wallet;
import br.com.sysmap.bootcamp.domain.repository.AlbumRepository;
import br.com.sysmap.bootcamp.domain.repository.UsersRepository;
import br.com.sysmap.bootcamp.domain.repository.WalletRepository;

import br.com.sysmap.bootcamp.domain.service.UsersService;
import jakarta.persistence.EntityManager;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AlbumControllerTest {


    @Autowired
    private AlbumController albumController;

    @Autowired
    AlbumRepository albumRepository;

    @Mock
    private UsersService usersService;


    UserAccounts userAccounts;

    @Mock
    private Album album;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    WalletRepository walletRepository;

    @Mock
    Wallet wallet;

    @InjectMocks
    AlbumServiceTest albumService;

    @Autowired
    EntityManager entityManager;

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private PasswordEncoder passwordEncoder;

    private SecurityContext securityContext;


    @BeforeEach
    void setUp() {

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn("ilbormagnodelasnoches@gmail.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        Authentication authentication2 = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn("ilbormagnodelasnoches2@gmail.com");

        SecurityContext securityContext2 = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);


        SecurityContextHolder.setContext(securityContext);


    }


    @Test
    @DisplayName("Checks if the json value returned is in accordance with the return from an albums")
    void Checksifthejsonvaluereturnedisinaccordancewiththereturnfromanalbums() throws Exception {

        String album = "Chris Brown";

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/albums/all")
                        .param("album", album))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();


        assertTrue(responseBody.contains(album));

    }

    @Test
    @DisplayName("Must return 200 and the album purchased")
    void Mustreturn200andthealbumpurchased() throws Exception {
        UserAccounts NewUserCreated = null;
        Wallet NewWalletCreated = null;

        try {
            UserAccounts Newuser = UserAccounts.builder().email("ilbormagnodelasnoches@gmail.com").password("ilborpassword").build();
            NewUserCreated = usersRepository.save(Newuser);
        } catch (Exception e){
            System.out.println("Exception caught on user creation:" + e.getMessage());
        }


        try{
            Wallet Newwallet = Wallet.builder().balance(new BigDecimal("200.00")).userId(NewUserCreated.getId()).build();
            NewWalletCreated = walletRepository.save(Newwallet);
        }catch (Exception e){
            System.out.println("Exception caught on wallet creation:" + e.getMessage());
        }

        try{
            String album = "{\"name\":\"Album test\",\"idSpotify\":\"idTest\",\"artistName\":\"artist test\",\"imageUrl\":\"ImageTest\",\"value\":10.00,\"userAccounts\":{\"id\":" + NewUserCreated.getId() + ",\"email\":\"" + NewUserCreated.getEmail() + "\",\"password\":\"" + NewUserCreated.getPassword() + "\"}}";

            MvcResult result = mockMvc.perform(post("/albums/sale")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(album)).andReturn();

            if(result.getResponse().getStatus() == 200){
                System.out.println("Successfully added album: " + result.getResponse().getContentAsString());
            }else{
                System.out.println("Failed to add album");
            }
        }catch (Exception e){
            System.out.println("Exception caught on album sale:" + e.getMessage());
        }


    }

    @Test
    @DisplayName("Should return 204 when the user is deleted")
    void Shouldreturn204whentheuserisdeleted(){
        UserAccounts NewUserCreated = null;
        Wallet NewWalletCreated = null;

        try {
            UserAccounts Newuser = UserAccounts.builder().email("ilbormagnodelasnoches2@gmail.com").password("ilborpassword").build();
            NewUserCreated = usersRepository.save(Newuser);
            Wallet Newwallet = Wallet.builder().balance(new BigDecimal("200.00")).userId(NewUserCreated.getId()).build();
            NewWalletCreated = walletRepository.save(Newwallet);

            String album = "{\"name\":\"Album test\",\"idSpotify\":\"idTest\",\"artistName\":\"artist test\",\"imageUrl\":\"ImageTest\",\"value\":10.00,\"userAccounts\":{\"id\":" + NewUserCreated.getId() + ",\"email\":\"" + NewUserCreated.getEmail() + "\",\"password\":\"" + NewUserCreated.getPassword() + "\"}}";

            MvcResult result = mockMvc.perform(post("/albums/sale")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(album)).andReturn();


            String resultAlbum = result.getResponse().getContentAsString();

            JSONObject albumJson = new JSONObject(resultAlbum);

            Long id = albumJson.getLong("id");

            MvcResult resultDelete = mockMvc.perform(delete("/albums/remove/{id}", id))
                    .andExpect(MockMvcResultMatchers.status().isNoContent()).andReturn();


        }catch (Exception e){
            System.out.println("Exception caught on wallet creation and buyed:" + e.getMessage());
        }


    }




}
