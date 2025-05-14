package fr.epsi.b3devc1.sylvainmjc.backend.controller;

import fr.epsi.b3devc1.sylvainmjc.backend.dto.AuthResponse;
import fr.epsi.b3devc1.sylvainmjc.backend.dto.LoginRequest;
import fr.epsi.b3devc1.sylvainmjc.backend.dto.RegisterRequest;
import fr.epsi.b3devc1.sylvainmjc.backend.entity.Role;
import fr.epsi.b3devc1.sylvainmjc.backend.entity.Utilisateur;
import fr.epsi.b3devc1.sylvainmjc.backend.repository.UtilisateurRepository;
import fr.epsi.b3devc1.sylvainmjc.backend.security.JwtUtil;
import fr.epsi.b3devc1.sylvainmjc.backend.security.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashSet;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthController(
            AuthenticationManager authenticationManager,
            UtilisateurRepository utilisateurRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            UserDetailsServiceImpl userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            String jwt = jwtUtil.generateToken(userDetails);
            
            Utilisateur utilisateur = utilisateurRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
            
            return ResponseEntity.ok(AuthResponse.builder()
                    .token(jwt)
                    .username(utilisateur.getUsername())
                    .email(utilisateur.getEmail())
                    .roles(utilisateur.getRoles())
                    .build());
                    
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Identifiants invalides");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if (utilisateurRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Ce nom d'utilisateur est déjà utilisé");
        }

        if (utilisateurRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Cet email est déjà utilisé");
        }

        Utilisateur user = Utilisateur.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .roles(new HashSet<>(Collections.singletonList(Role.CANDIDAT)))
                .nom(registerRequest.getNom())
                .prenom(registerRequest.getPrenom())
                .build();

        utilisateurRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String jwt = jwtUtil.generateToken(userDetails);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(AuthResponse.builder()
                .token(jwt)
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build());
    }
} 