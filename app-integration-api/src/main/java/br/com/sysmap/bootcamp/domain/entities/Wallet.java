package br.com.sysmap.bootcamp.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "WALLET")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "user_id")
    private Long userId;


}
