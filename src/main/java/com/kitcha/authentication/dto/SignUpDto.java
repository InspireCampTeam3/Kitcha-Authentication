package com.kitcha.authentication.dto;

import lombok.Data;

@Data
public class SignUpDto {
    private String nickname;
    private String email;
    private String password;
}
