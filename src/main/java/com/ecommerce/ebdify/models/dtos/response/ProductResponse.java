package com.ecommerce.ebdify.models.dtos.response;

import com.ecommerce.ebdify.models.dtos.request.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    List<ProductDTO> content;
}
