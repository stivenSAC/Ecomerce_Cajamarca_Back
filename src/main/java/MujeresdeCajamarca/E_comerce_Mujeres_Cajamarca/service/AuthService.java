package MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.service;

import MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.dto.JwtResponse;
import MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.dto.LoginRequest;
import MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.dto.RegisterRequest;
import MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.model.User;
import MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public JwtResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByCorreo(loginRequest.getCorreo())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(loginRequest.getContrasena(), user.getContrasena())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        String jwt = jwtService.generateJwtToken(user.getCorreo(), user.getId());
        return new JwtResponse(jwt, user.getId(), user.getCorreo(), user.getNombre());
    }

    public JwtResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByCorreo(registerRequest.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        User user = new User();
        user.setNombre(registerRequest.getNombre());
        user.setApellido(registerRequest.getApellido());
        user.setEdad(registerRequest.getEdad());
        user.setCorreo(registerRequest.getCorreo());
        user.setContrasena(passwordEncoder.encode(registerRequest.getContrasena()));

        User savedUser = userRepository.save(user);
        String jwt = jwtService.generateJwtToken(savedUser.getCorreo(), savedUser.getId());
        
        return new JwtResponse(jwt, savedUser.getId(), savedUser.getCorreo(), savedUser.getNombre());
    }
}