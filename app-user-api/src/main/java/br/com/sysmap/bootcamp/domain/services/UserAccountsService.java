package br.com.sysmap.bootcamp.domain.services;

import br.com.sysmap.bootcamp.domain.entities.UserAccounts;
import br.com.sysmap.bootcamp.domain.entities.Wallet;
import br.com.sysmap.bootcamp.domain.repositories.UserAccountsRepository;
import br.com.sysmap.bootcamp.domain.repositories.WalletRepository;
import br.com.sysmap.bootcamp.dto.AuthDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserAccountsService implements UserDetailsService {

    private final UserAccountsRepository usersRepository;

    private final PasswordEncoder passwordEncoder;
    private final WalletRepository walletRepository;

    //POST
    @Transactional(propagation = Propagation.REQUIRED)
    public UserAccounts save(UserAccounts user){
        Optional<UserAccounts> usersOptional = this.usersRepository.findByEmail(user.getEmail());
        if(usersOptional.isPresent()){
            throw new RuntimeException("User already exists");
        }

        if(StringUtils.isEmpty(user.getName()) || StringUtils.isEmpty(user.getEmail()) || StringUtils.isEmpty(user.getPassword())){
            throw new RuntimeException("Username or email or password is incorrect");
        }

        user = user.toBuilder().password(this.passwordEncoder.encode(user.getPassword())).build();

        log.info("Saving user: {}" , user);

        UserAccounts savedUser = this.usersRepository.save(user);

        Wallet wallet = new Wallet();
        wallet.setUserAccounts(savedUser);
        BigDecimal initialBalance = new BigDecimal(100);
        wallet.setBalance(initialBalance);
        wallet.setPoints(0L);
        wallet.setLastUpdate(LocalDateTime.now());
        walletRepository.save(wallet);



        return savedUser;



    }

    //GET ALL
    public List<UserAccounts> findAll(){
        log.info("Finding all users");
        return this.usersRepository.findAll();
    }

    //GET By ID
   public Optional<UserAccounts> findById(Long id){
        log.info("Finding user by id: {}", id);
        return this.usersRepository.findById(id);
   }

   //PUT

   public UserAccounts put(UserAccounts user){

            log.info("Updating user: {}" , user);
            return this.usersRepository.save(user);


   }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserAccounts> userAccountsOptional = this.usersRepository.findByEmail(username);

        return userAccountsOptional.map(users -> new User(users.getEmail(), users.getPassword(), new ArrayList<GrantedAuthority>()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found" + username));
    }

    public UserAccounts findByEmail(String email){
        return this.usersRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public AuthDto auth(AuthDto authDto){
        UserAccounts userAccounts = this.findByEmail(authDto.getEmail());

       if (!this.passwordEncoder.matches(authDto.getPassword(), userAccounts.getPassword())){
           throw new RuntimeException("Invalid password");
       }

       StringBuilder password = new StringBuilder().append(userAccounts.getEmail()).append(":").append(userAccounts.getPassword());


        return AuthDto.builder().email(userAccounts.getEmail()).token(
                Base64.getEncoder().withoutPadding().encodeToString(password.toString().getBytes())
        ).id(userAccounts.getId()).build();
    }
}
