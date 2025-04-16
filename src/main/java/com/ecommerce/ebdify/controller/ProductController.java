package com.ecommerce.ebdify.controller;

import com.ecommerce.ebdify.models.dtos.request.ProductDTO;
import com.ecommerce.ebdify.models.dtos.response.ProductResponse;
import com.ecommerce.ebdify.models.entities.Product;
import com.ecommerce.ebdify.repository.ProductRepository;
import com.ecommerce.ebdify.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts() {
        ProductResponse productResponse = productService.getAllProducts();
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(
            @PathVariable Long categoryId) {
        ProductResponse productResponse = productService.searchByCategory(categoryId);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(
            @PathVariable String keyword) {
        ProductResponse productResponse = productService.searchProductsByKeyword(keyword);
        return new ResponseEntity<>(productResponse, HttpStatus.FOUND);
    }

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO,
                                                 @PathVariable Long categoryId) {
        ProductDTO savedProductDTO = productService.addProduct(productDTO, categoryId);
        return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(
            @RequestBody ProductDTO productDTO,
            @PathVariable Long productId) {
        ProductDTO savedProductDTO = productService.updateProduct(productDTO, productId);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId) {
        ProductDTO productDTO = productService.deleteProduct(productId);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> uploadProductImage(
            @PathVariable Long productId,
            @RequestParam("image") MultipartFile image) throws IOException {
        ProductDTO updatedProduct = productService.uploadProductImage(productId, image);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
}
