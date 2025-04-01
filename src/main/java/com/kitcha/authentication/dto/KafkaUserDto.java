package com.kitcha.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KafkaUserDto {
    private Schema schema;
    private Payload payload;
}
