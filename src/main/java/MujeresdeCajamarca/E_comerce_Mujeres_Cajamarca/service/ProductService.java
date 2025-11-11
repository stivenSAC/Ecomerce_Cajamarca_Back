package MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.service;

import MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.model.Product;
import MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.model.User;
import MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.repository.ProductRepository;
import MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;

    public List<Product> findAll() {
        try {
            System.out.println("=== ProductService.findAll() ===");
            List<Product> products = productRepository.findAll();
            System.out.println("Productos obtenidos del repositorio: " + products.size());
            return products;
        } catch (Exception e) {
            System.out.println("ERROR en ProductService.findAll(): " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> findByUserId(Long userId) {
        return productRepository.findByUserId(userId);
    }

    public List<Product> findByNombre(String nombre) {
        return productRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public Product save(Product product, Long userId) {
        System.out.println("=== ProductService.save - userId: " + userId + " ===");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        System.out.println("Usuario encontrado: " + user.getNombre());
        product.setUser(user);
        
        Product savedProduct = productRepository.save(product);
        System.out.println("Producto guardado con ID: " + savedProduct.getId());
        return savedProduct;
    }

    public Product update(Long productId, Product productDetails, Long userId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        if (!product.getUser().getId().equals(userId)) {
            throw new RuntimeException("No tienes permisos para actualizar este producto");
        }
        
        product.setNombre(productDetails.getNombre());
        product.setDetalle(productDetails.getDetalle());
        product.setImagen(productDetails.getImagen());
        product.setValor(productDetails.getValor());
        product.setContactoUrl(productDetails.getContactoUrl());
        
        return productRepository.save(product);
    }

    public void deleteById(Long productId, Long userId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        if (!product.getUser().getId().equals(userId)) {
            throw new RuntimeException("No tienes permisos para eliminar este producto");
        }
        
        productRepository.deleteById(productId);
    }

    public void deleteById(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("Producto no encontrado");
        }
        productRepository.deleteById(productId);
    }
}