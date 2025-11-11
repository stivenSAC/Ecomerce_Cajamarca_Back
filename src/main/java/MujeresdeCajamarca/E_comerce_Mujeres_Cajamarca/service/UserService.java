package MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.service;

import MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.model.User;
import MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByCorreo(String correo) {
        return userRepository.findByCorreo(correo);
    }

    public User save(User user) {
        if (userRepository.existsByCorreo(user.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado");
        }
        user.setContrasena(passwordEncoder.encode(user.getContrasena()));
        return userRepository.save(user);
    }

    public User update(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Solo actualizar campos que no son null
        if (userDetails.getNombre() != null) {
            user.setNombre(userDetails.getNombre());
        }
        
        if (userDetails.getApellido() != null) {
            user.setApellido(userDetails.getApellido());
        }
        
        if (userDetails.getEdad() != null) {
            user.setEdad(userDetails.getEdad());
        }
        
        if (userDetails.getCorreo() != null) {
            if (!user.getCorreo().equals(userDetails.getCorreo()) && 
                userRepository.existsByCorreo(userDetails.getCorreo())) {
                throw new RuntimeException("El correo ya está registrado");
            }
            user.setCorreo(userDetails.getCorreo());
        }
        
        if (userDetails.getContrasena() != null && !userDetails.getContrasena().isEmpty()) {
            user.setContrasena(passwordEncoder.encode(userDetails.getContrasena()));
        }
        
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        userRepository.deleteById(id);
    }

    public boolean existsByCorreo(String correo) {
        return userRepository.existsByCorreo(correo);
    }
}