package com.unsia.edu.models.common;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonResponse <T> {
    private Object errors;
    private T data;
}
