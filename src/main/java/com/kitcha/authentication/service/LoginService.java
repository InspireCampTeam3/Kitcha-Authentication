package com.kitcha.authentication.service;

import com.kitcha.authentication.Utils.JwtUtils;
import com.kitcha.authentication.client.InterestFeignClient;
import com.kitcha.authentication.dto.CustomUserDetails;
import com.kitcha.authentication.dto.LoginDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class LoginService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final InterestFeignClient interestFeignClient;

    public List<String> authenticate(LoginDto dto) {
        // SecurityConfiguration's authenticationManager Bean 사용
        // 인증 시도
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

        // 인증 성공 후 사용자 정보 로드 및 토큰 생성
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        // InterestFeignClient를 통해 관심사(interest) 값을 받아옴
        String userId = Optional.ofNullable(customUserDetails.getUsername()).orElse("");
        String interestValue = "";
        log.info("Send Msg to interest");
        try {
            ResponseEntity<Map<String, String>> interestResponse = interestFeignClient.interest(userId);
            log.info("Interest response success");
            interestValue = interestResponse.getBody() != null ? interestResponse.getBody().get("interest") : "";
        } catch (Exception e) {
            log.info("Interest response error");
            interestValue = "";
        }

        return List.of(jwtUtils.generateToken(customUserDetails),
                Optional.ofNullable(customUserDetails.getRole()).orElse(""),
                interestValue);
    }
}
