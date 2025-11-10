package com.sistemas.security.controller;

import com.sistemas.entities.UserEntity;
import com.sistemas.repositories.UserRepository;
import com.sistemas.security.JwtService;
import com.sistemas.security.dto.AuthRequest;
import com.sistemas.security.dto.AuthResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest req) {
        // Autenticamos por email (username)
        Authentication auth = new UsernamePasswordAuthenticationToken(req.email(), req.password());
        authenticationManager.authenticate(auth);

        // Cargamos el usuario completo
        UserDetails userDetails = userDetailsService.loadUserByUsername(req.email());
        UserEntity userEntity = userRepository.findByEmailIgnoreCase(req.email())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Agregamos claims adicionales al token
        Map<String, Object> extraClaims = Map.of(
                "id", userEntity.getId(),
                "name", userEntity.getFirstName() + " " + userEntity.getFirstLastName(),
                "email", userEntity.getEmail()
        );

        // Generamos el token con claims
        String token = jwtService.generateToken(userDetails, extraClaims);

        return ResponseEntity.ok(new AuthResponse(token));
    }
}
