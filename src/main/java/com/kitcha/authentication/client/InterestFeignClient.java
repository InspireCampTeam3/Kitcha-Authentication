package com.kitcha.authentication.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "INTEREST")
public interface InterestFeignClient {
    @GetMapping("/interest")
    ResponseEntity<Map<String, String>> interest(@RequestParam("userId") String userId);
}
