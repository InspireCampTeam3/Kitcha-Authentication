package com.kitcha.authentication.controller;

import com.kitcha.authentication.dto.LoginDto;
import com.kitcha.authentication.dto.SignUpDto;
import com.kitcha.authentication.service.LoginService;
import com.kitcha.authentication.service.SignUpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonMap;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final LoginService loginService;
    private final SignUpService signUpService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginMember(@Valid @RequestBody LoginDto dto) {
        List<String> information = loginService.authenticate(dto);
        String jwtToken = information.get(0);
        String role = information.get(1);
        String interest = information.get(2);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);

        return ResponseEntity.ok().headers(headers).body(
                Map.of("message", "로그인 성공", "role", role, "interest", interest));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Map<String, String>> signUpMember(@Valid @RequestBody SignUpDto dto) {
        signUpService.signUpMember(dto);

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail(dto.getEmail());
        loginDto.setPassword(dto.getPassword());

        List<String> information = loginService.authenticate(loginDto);
        String jwtToken = information.get(0);
        String role = information.get(1);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);

        return ResponseEntity.ok().headers(headers).body(
                Map.of("message", "회원가입 성공", "role", role));
    }

    @GetMapping("/email-check")
    public ResponseEntity<Map<String, String>> emailCheck(@RequestParam String email) {
        if (signUpService.emailExists(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(singletonMap("message", "이미 가입된 Email 입니다."));
        } else {
            return ResponseEntity.ok(singletonMap("message", "가입 가능한 Email 입니다."));
        }
    }

    @GetMapping("/nickname-check")
    public ResponseEntity<Map<String, String>> nicknameCheck(@RequestParam String nickname) {
        if (signUpService.nicknameExists(nickname)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(singletonMap("message", "이미 가입된 닉네임 입니다."));
        } else {
            return ResponseEntity.ok(singletonMap("message", "가입 가능한 닉네임 입니다."));
        }
    }
}
