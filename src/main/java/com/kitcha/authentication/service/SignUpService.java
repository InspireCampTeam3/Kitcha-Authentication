package com.kitcha.authentication.service;

import com.kitcha.authentication.dto.SignUpDto;
import com.kitcha.authentication.entity.UserEntity;
import com.kitcha.authentication.exception.DuplicateException;
import com.kitcha.authentication.kafka.producer.UserProducer;
import com.kitcha.authentication.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SignUpService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserProducer userProducer;

    public void signUpMember(SignUpDto dto) throws RuntimeException {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateException("이미 가입된 Email 입니다.");
        }

        if (userRepository.existsByNickname(dto.getNickname())) {
            throw new DuplicateException("이미 가입된 닉네임 입니다.");
        }

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(dto, userEntity);
        userEntity.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        userEntity.setRole("USER");
//        userRepository.save(userEntity);
        userProducer.send("user", userEntity);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean nicknameExists(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}