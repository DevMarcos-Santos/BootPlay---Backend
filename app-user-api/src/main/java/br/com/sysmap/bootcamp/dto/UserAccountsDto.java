package br.com.sysmap.bootcamp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAccountsDto {
    private Long id;
    private String name;
    private String email;
    private String password;
}
