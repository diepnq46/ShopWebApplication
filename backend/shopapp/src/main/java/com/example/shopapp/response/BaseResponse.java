package com.example.shopapp.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BaseResponse {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
