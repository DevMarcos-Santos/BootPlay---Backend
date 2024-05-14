package br.com.sysmap.bootcamp.web;

import br.com.sysmap.bootcamp.domain.entities.Wallet;
import br.com.sysmap.bootcamp.domain.services.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@CrossOrigin(originPatterns = "*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/wallet")
@Tag(name = "Wallet", description = "Wallet API")
@Order(1)
public class WalletController {

    private final WalletService walletService;

    @Operation(summary = "Credit value in wallet")
    @PostMapping("/credit/{value}")
    public ResponseEntity<String> credit(@PathVariable BigDecimal value){
        this.walletService.credit(value);

        return ResponseEntity.ok("Credit successful");
    }

    @Operation(summary = "My Wallet")
    @GetMapping("")
    public Optional<Wallet> wallet(){
        return this.walletService.myWallet();


    }
}
