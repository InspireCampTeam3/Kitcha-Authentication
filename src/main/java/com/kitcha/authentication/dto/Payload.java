package com.kitcha.authentication.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payload {
    private Long user_id;
    private String nickname;
    private String email;
    private String password;
    private String role;
}
