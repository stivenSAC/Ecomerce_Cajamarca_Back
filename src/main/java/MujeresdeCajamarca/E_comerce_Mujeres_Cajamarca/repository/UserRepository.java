package MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.repository;

import MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByCorreo(String correo);
    
    boolean existsByCorreo(String correo);
}