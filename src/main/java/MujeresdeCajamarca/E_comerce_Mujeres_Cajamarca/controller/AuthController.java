package MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.controller;

import MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.dto.JwtResponse;
import MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.dto.LoginRequest;
import MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.dto.RegisterRequest;
import MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            JwtResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register(@RequestBody RegisterRequest registerRequest) {
        try {
            JwtResponse response = authService.register(registerRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}