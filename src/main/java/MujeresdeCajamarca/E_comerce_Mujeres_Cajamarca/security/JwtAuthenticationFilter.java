package MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.security;

import MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        String path = request.getRequestURI();
        String method = request.getMethod();
        
        // Saltar filtro JWT para rutas públicas
        if (path.startsWith("/api/auth/") || 
            ("GET".equals(method) && (path.equals("/api/products") || 
                                     path.matches("/api/products/\\d+") || 
                                     path.startsWith("/api/products/search")))) {
            filterChain.doFilter(request, response);
            return;
        }
        
        String jwt = parseJwt(request);
        
        if (jwt != null && jwtService.validateJwtToken(jwt)) {
            String correo = jwtService.getCorreoFromJwtToken(jwt);
            Long userId = jwtService.getUserIdFromJwtToken(jwt);
            
            System.out.println("JWT válido - Correo: " + correo + ", UserId: " + userId);
            
            UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(correo, null, new ArrayList<>());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            
            // Agregar userId al contexto
            request.setAttribute("userId", userId);
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            System.out.println("JWT inválido o no encontrado");
        }
        
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        
        return null;
    }
}