package br.com.sysmap.bootcamp.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "WALLET")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "points")
    private Long points;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserAccounts userAccounts;

}
