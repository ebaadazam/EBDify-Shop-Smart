package com.ecommerce.ebdify.serviceimpl;

import com.ecommerce.ebdify.exceptions.APIException;
import com.ecommerce.ebdify.exceptions.ResourceNotFoundException;
import com.ecommerce.ebdify.models.dtos.request.CartDTO;
import com.ecommerce.ebdify.models.dtos.request.ProductDTO;
import com.ecommerce.ebdify.models.dtos.response.ProductResponse;
import com.ecommerce.ebdify.models.entities.Cart;
import com.ecommerce.ebdify.models.entities.Category;
import com.ecommerce.ebdify.models.entities.Product;
import com.ecommerce.ebdify.repository.CartRepository;
import com.ecommerce.ebdify.repository.CategoryRepository;
import com.ecommerce.ebdify.repository.ProductRepository;
import com.ecommerce.ebdify.service.CartService;
import com.ecommerce.ebdify.service.FileService;
import com.ecommerce.ebdify.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    // For images path
    @Value("${project.image}")
    private String path;

    @Value("${image.base.url}")
    private String imageBaseUrl;

    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category", "categoryId", categoryId));

        // Check if product exists in DB
        boolean isProductNotPresent = true;
        List<Product> products = category.getProducts();
        for (Product value : products) {
            if (value.getProductName().equals(productDTO.getProductName())) {
                isProductNotPresent = false;
                break;
            }
        }

        if (isProductNotPresent) {
            Product product = modelMapper.map(productDTO, Product.class);
            product.setCategory(category);
            double specialPrice = product.getPrice() - (product.getPrice() * (product.getDiscount() / 100));
            product.setSpecialPrice(specialPrice);
            product.setImage("default.png");

            Product savedProduct = productRepository.save(product);
            return modelMapper.map(savedProduct, ProductDTO.class);
        } else{
            throw new APIException("Product already exists!");
        }
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortByOrder) {
        // For sorting
        Sort sortByAndOrder = sortByOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // For Pagination
        // Pageable is an interface representing the request for specific page with
        // data from database query result
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        // This will find all the results with respect to entered pageNumber & pageSize
        Page<Product> pageProducts = productRepository.findAll(pageDetails);

        List<Product> products = pageProducts.getContent();

        List<ProductDTO> productDTOS = products
                .stream()
                .map(product -> {
                    ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
                    productDTO.setImage(product.getImage());
                    return productDTO;
                }).toList();

        if (products.isEmpty()) {
            throw new APIException("No products available!");
        }
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());
        return productResponse;
    }

    private String constructImageUrl(String imageName) {
        return imageBaseUrl.endsWith("/") ? imageBaseUrl + imageName
                : imageBaseUrl + "/" + imageName;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortByOrder) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category", "categoryId", categoryId));

        // For sorting
        Sort sortByAndOrder = sortByOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // For Pagination
        // Pageable is an interface representing the request for specific page with
        // data from database query result
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        // This will find all the results with respect to entered pageNumber & pageSize
        Page<Product> pageProducts = productRepository.findByCategoryOrderByPriceAsc(category, pageDetails);



        List<Product> products = pageProducts.getContent();
        List<ProductDTO> productDTOS = products
                .stream()
                .map(prod -> modelMapper.map(prod, ProductDTO.class))
                .toList();

        if (products.isEmpty()) {
            throw new APIException("Product with category " + category.getCategoryName() + " not found!");
        }

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse searchProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortByOrder) {

        // For sorting
        Sort sortByAndOrder = sortByOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // For Pagination
        // Pageable is an interface representing the request for specific page with
        // data from database query result
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        // This will find all the results with respect to entered pageNumber & pageSize
        Page<Product> pageProducts = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%', pageDetails);

        List<Product> products = pageProducts.getContent();
        List<ProductDTO> productDTOS = products
                .stream()
                .map(prod -> modelMapper.map(prod, ProductDTO.class))
                .toList();

        if (products.isEmpty()) {
            throw new APIException("No products found with keyword: " + keyword);
        }

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());
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

        // If we are updating the product in the cart then overall product should also be updated
        List<Cart> carts = cartRepository.findCartsByProductId(productId);
        List<CartDTO> cartDTOs = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
            List<ProductDTO> products = cart.getCartItems().stream()
                    .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());
            cartDTO.setProducts(products);
            return cartDTO;
        }).toList();
        cartDTOs.forEach(cart -> cartService.updateProductInCarts(cart.getCartId(), productId));

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product", "productId", productId));

        // DELETE
        List<Cart> carts = cartRepository.findCartsByProductId(productId);
        carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(), productId));

        productRepository.delete(product);
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO uploadProductImage(Long productId, MultipartFile image) throws IOException {
        // Get the product from DB
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product", "productId", productId));

        // Upload the image to the server (codebase) with path and image and store this in the file name of the uploaded image
        String fileName = fileService.uploadImage(path, image);

        // Updating the new file name to the product
        product.setImage(fileName);

        // Save the product
        Product updatedProduct = productRepository.save(product);

        // Return DTO after mapping product to DTO
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }
}
