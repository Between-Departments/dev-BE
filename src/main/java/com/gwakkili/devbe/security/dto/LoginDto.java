package com.gwakkili.devbe.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginDto {

    private String mail;

    private String password;
}
