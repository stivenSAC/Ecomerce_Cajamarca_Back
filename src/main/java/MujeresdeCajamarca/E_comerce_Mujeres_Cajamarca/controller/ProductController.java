package MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.controller;

import MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.dto.ProductRequest;
import MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.model.Product;
import MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.service.ProductService;
import MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.service.ImageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @PostMapping("/test")
    public ResponseEntity<String> test() {
        System.out.println("=== TEST ENDPOINT FUNCIONANDO ===");
        return ResponseEntity.ok("Test OK");
    }

    @Autowired
    private ProductService productService;
    
    @Autowired
    private ImageService imageService;

    // RUTAS PÚBLICAS - No requieren autenticación
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        try {
            System.out.println("=== GET ALL PRODUCTS - INICIO ===");
            List<Product> products = productService.findAll();
            System.out.println("Productos encontrados: " + products.size());
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            System.out.println("ERROR en getAllProducts: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String nombre) {
        return ResponseEntity.ok(productService.findByNombre(nombre));
    }

    // RUTAS PRIVADAS - Requieren autenticación
    @GetMapping("/my-products")
    public ResponseEntity<List<Product>> getMyProducts(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(productService.findByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequest productRequest, 
                                               HttpServletRequest request) {
        System.out.println("=== LLEGÓ AL MÉTODO createProduct ===");
        try {
            Long userId = (Long) request.getAttribute("userId");
            
            if (userId == null) {
                System.out.println("ERROR: userId es null");
                return ResponseEntity.badRequest().build();
            }
            
            System.out.println("Creando producto para userId: " + userId);
            
            Product product = new Product();
            product.setNombre(productRequest.getNombre());
            product.setDetalle(productRequest.getDetalle());
            
            // Comprimir imagen si se proporciona
            String imagen = productRequest.getImagen();
            if (imagen != null && !imagen.isEmpty() && imagen.startsWith("data:image")) {
                try {
                    imagen = imageService.compressBase64Image(imagen);
                    System.out.println("Imagen comprimida exitosamente");
                } catch (Exception e) {
                    System.out.println("Error al comprimir imagen: " + e.getMessage());
                }
            }
            product.setImagen(imagen);
            
            product.setValor(productRequest.getValor());
            product.setContactoUrl(productRequest.getContactoUrl());
            
            Product savedProduct = productService.save(product, userId);
            return ResponseEntity.ok(savedProduct);
        } catch (RuntimeException e) {
            System.out.println("ERROR al crear producto: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, 
                                               @RequestBody ProductRequest productRequest,
                                               HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            Product productDetails = new Product();
            productDetails.setNombre(productRequest.getNombre());
            productDetails.setDetalle(productRequest.getDetalle());
            
            // Comprimir imagen si se proporciona
            String imagen = productRequest.getImagen();
            if (imagen != null && !imagen.isEmpty() && imagen.startsWith("data:image")) {
                try {
                    imagen = imageService.compressBase64Image(imagen);
                } catch (Exception e) {
                    System.out.println("Error al comprimir imagen: " + e.getMessage());
                }
            }
            productDetails.setImagen(imagen);
            
            productDetails.setValor(productRequest.getValor());
            productDetails.setContactoUrl(productRequest.getContactoUrl());
            
            Product updatedProduct = productService.update(id, productDetails, userId);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            productService.deleteById(id, userId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}