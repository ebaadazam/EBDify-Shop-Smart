package com.ecommerce.ebdify.models.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

// This is a standard for all the API responses in the application
public class APIResponse {
    private boolean status;
    private String message;

}
