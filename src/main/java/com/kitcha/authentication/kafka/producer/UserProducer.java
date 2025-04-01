package com.kitcha.authentication.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kitcha.authentication.dto.Field;
import com.kitcha.authentication.dto.KafkaUserDto;
import com.kitcha.authentication.dto.Payload;
import com.kitcha.authentication.dto.Schema;
import com.kitcha.authentication.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class UserProducer {
    private KafkaTemplate<String, String> kafkaTemplate;

    List<Field> fields = Arrays.asList(
            new Field("int64", true, "user_id"),
            new Field("string", true, "email"),
            new Field("string", true, "nickname"),
            new Field("string", true, "password"),
            new Field("string", true, "role")
    );

    Schema schema = Schema.builder()
            .type("struct")
            .fields(fields)
            .optional(false)
            .name("file")
            .build();

    @Autowired
    public UserProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public UserEntity send(String topic, UserEntity user) {
        Payload payload = Payload.builder()
                .nickname(user.getNickname())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .build();

        KafkaUserDto kafkaUserDto = new KafkaUserDto(schema, payload);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(kafkaUserDto);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        kafkaTemplate.send(topic, jsonInString);
        log.info("User sent to topic: " + topic);

        return user;
    }
}
