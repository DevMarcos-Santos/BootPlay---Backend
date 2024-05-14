package br.com.sysmap.bootcamp.domain.services;

import br.com.sysmap.bootcamp.domain.entities.UserAccounts;
import br.com.sysmap.bootcamp.domain.entities.Wallet;
import br.com.sysmap.bootcamp.domain.repositories.WalletRepository;
import br.com.sysmap.bootcamp.dto.WalletDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class WalletService implements Serializable {
    private final UserAccountsService userAccountsService;
    private final WalletRepository walletRepository;
    private  Map<DayOfWeek, Integer> pointsperday;


    public void credit(BigDecimal creditAmount){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        UserAccounts userAccounts = userAccountsService.findByEmail(userEmail);
        Wallet wallet = walletRepository.findByUserAccounts(userAccounts)
                .orElseThrow(() -> new NoSuchElementException("Wallet is not found for user"));

        if(creditAmount.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("The credit amount cannot be negative.");
        }

        BigDecimal currentBalance = wallet.getBalance();
        BigDecimal newBalance  = currentBalance.add(creditAmount);
        wallet.setBalance(newBalance);
        wallet.setLastUpdate(LocalDateTime.now());

        walletRepository.save(wallet);



    }


    public Optional<Wallet> myWallet(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserAccounts userAccounts = userAccountsService.findByEmail(userEmail);

         Optional<Wallet> wallet = walletRepository.findByUserAccounts(userAccounts);


         return wallet;

    }





    public void serviceDay(){
        this.pointsperday = new HashMap<>();
        this.pointsperday.put(DayOfWeek.SUNDAY, 25);
        this.pointsperday.put(DayOfWeek.MONDAY, 7);
        this.pointsperday.put(DayOfWeek.TUESDAY, 6);
        this.pointsperday.put(DayOfWeek.WEDNESDAY, 2);
        this.pointsperday.put(DayOfWeek.THURSDAY, 10);
        this.pointsperday.put(DayOfWeek.FRIDAY, 15);
        this.pointsperday.put(DayOfWeek.SATURDAY, 20);

    }

    public void debit(WalletDto walletDto){


        UserAccounts userAccounts = userAccountsService.findByEmail(walletDto.getEmail());
        Wallet wallet = walletRepository.findByUserAccounts(userAccounts).orElseThrow(() -> new NoSuchElementException("Wallet is not found for email: " + walletDto.getEmail()));


        wallet.setBalance(wallet.getBalance().subtract(walletDto.getValue()));



        this.serviceDay();

        DayOfWeek dayOfWeek = LocalDateTime.now().getDayOfWeek();

        Integer pointsperday = this.pointsperday.getOrDefault(dayOfWeek, 0);

        wallet.setPoints(wallet.getPoints() + pointsperday);


        walletRepository.save(wallet);
    }
}
