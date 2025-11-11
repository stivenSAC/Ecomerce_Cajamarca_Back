package MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.controller;

import MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.model.User;
import MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return userService.findById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(@RequestBody User userDetails, HttpServletRequest request) {
        System.out.println("=== PUT /api/users/profile - LLEGÓ ===");
        try {
            Long userId = (Long) request.getAttribute("userId");
            System.out.println("UserId: " + userId);
            User updatedUser = userService.update(userId, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            System.out.println("ERROR en updateProfile: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/profile")
    public ResponseEntity<User> updateProfilePartial(@RequestBody User userDetails, HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            User updatedUser = userService.update(userId, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteProfile(HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            userService.deleteById(userId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}