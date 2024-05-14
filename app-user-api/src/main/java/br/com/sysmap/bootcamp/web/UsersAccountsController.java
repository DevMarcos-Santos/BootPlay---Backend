package br.com.sysmap.bootcamp.web;

import br.com.sysmap.bootcamp.domain.entities.UserAccounts;
import br.com.sysmap.bootcamp.domain.services.UserAccountsService;
import br.com.sysmap.bootcamp.dto.AuthDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;


@CrossOrigin(originPatterns = "*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Users API")
@Order(2)

public class UsersAccountsController {

    private final UserAccountsService userAccountsService;

    @Operation(summary = "Save user")
    @PostMapping("/create")
    public ResponseEntity<UserAccounts> save (@RequestBody UserAccounts userAccounts){
        return ResponseEntity.ok(this.userAccountsService.save(userAccounts));
    }

    @Operation(summary = "Auth user")
    @PostMapping("/auth")
    public ResponseEntity<AuthDto> auth (@RequestBody AuthDto user){
        return ResponseEntity.ok(this.userAccountsService.auth(user));
    }

    @Operation(summary = "List users")
    @GetMapping("")
    public ResponseEntity<List<UserAccounts>> findAll(){
        return ResponseEntity.ok(  this.userAccountsService.findAll());
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<UserAccounts>> findById(@PathVariable Long id){
        return ResponseEntity.ok(this.userAccountsService.findById(id));
    }

    @Operation(summary = "Update user")
    @PutMapping("/update")
    public ResponseEntity<UserAccounts> update (@RequestBody UserAccounts userAccounts){
        return ResponseEntity.ok(this.userAccountsService.put(userAccounts));
    }
}
