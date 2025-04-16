package com.ecommerce.ebdify.serviceimpl;

import com.ecommerce.ebdify.exceptions.ResourceNotFoundException;
import com.ecommerce.ebdify.models.dtos.request.ProductDTO;
import com.ecommerce.ebdify.models.dtos.response.ProductResponse;
import com.ecommerce.ebdify.models.entities.Category;
import com.ecommerce.ebdify.models.entities.Product;
import com.ecommerce.ebdify.repository.CategoryRepository;
import com.ecommerce.ebdify.repository.ProductRepository;
import com.ecommerce.ebdify.service.FileService;
import com.ecommerce.ebdify.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    // For images path
    @Value("${project.image}")
    private String path;

    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category", "categoryId", categoryId));

        Product product = modelMapper.map(productDTO, Product.class);

        product.setCategory(category);
        double specialPrice = product.getPrice() - (product.getPrice() * (product.getDiscount() / 100));
        product.setSpecialPrice(specialPrice);
        product.setImage("default.png");

        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOS = products
                .stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category", "categoryId", categoryId));

        List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);
        List<ProductDTO> productDTOS = products
                .stream()
                .map(prod -> modelMapper.map(prod, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductResponse searchProductsByKeyword(String keyword) {
        List<Product> products = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%');
        List<ProductDTO> productDTOS = products
                .stream()
                .map(prod -> modelMapper.map(prod, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
        // First get the product to be updated
        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product", "productId", productId));

        // Conversion from dto to entity
        Product product = modelMapper.map(productDTO, Product.class);

        // Update the product
        productFromDb.setProductName(product.getProductName());
        productFromDb.setDescription(product.getDescription());
        productFromDb.setQuantity(product.getQuantity());
        productFromDb.setPrice(product.getPrice());
        productFromDb.setDiscount(product.getDiscount());

        // just re-calculating the special price and set it in the attribute
        double updatedSpecialPrice = product.getPrice() - (product.getPrice() * (product.getDiscount() / 100));
        productFromDb.setSpecialPrice(updatedSpecialPrice);

        // Save to database
        Product savedProduct = productRepository.save(productFromDb);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product", "productId", productId));
        productRepository.delete(product);
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO uploadProductImage(Long productId, MultipartFile image) throws IOException {
        // Get the product from DB
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product", "productId", productId));

        // Upload the image to the server (codebase) and get the file name of the uploaded image
        String fileName = fileService.uploadImage(path, image);

        // Updating the new file name to the product
        product.setImage(fileName);

        // Save the product
        Product updatedProduct = productRepository.save(product);

        // Return DTO after mapping product to DTO
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }
}
