package com.unsia.edu.models.common;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonResponse <T> {
    private Integer statusCode;
    private String message;
    private T data;
}
