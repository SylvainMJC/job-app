package fr.epsi.b3devc1.sylvainmjc.backend.config;

import fr.epsi.b3devc1.sylvainmjc.backend.entity.Role;
import fr.epsi.b3devc1.sylvainmjc.backend.entity.Utilisateur;
import fr.epsi.b3devc1.sylvainmjc.backend.repository.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    @Profile("!test")
    public CommandLineRunner initData(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!utilisateurRepository.existsByUsername("admin")) {
                Set<Role> adminRoles = new HashSet<>();
                adminRoles.add(Role.ADMIN);
                
                Utilisateur admin = Utilisateur.builder()
                        .username("admin")
                        .email("admin@jobapp.com")
                        .password(passwordEncoder.encode("admin123"))
                        .roles(adminRoles)
                        .nom("Admin")
                        .prenom("System")
                        .build();
                
                utilisateurRepository.save(admin);
            }
            
            if (!utilisateurRepository.existsByUsername("recruteur")) {
                Set<Role> recruteurRoles = new HashSet<>();
                recruteurRoles.add(Role.RECRUTEUR);
                
                Utilisateur recruteur = Utilisateur.builder()
                        .username("recruteur")
                        .email("recruteur@jobapp.com")
                        .password(passwordEncoder.encode("recruteur123"))
                        .roles(recruteurRoles)
                        .nom("Dupont")
                        .prenom("Jean")
                        .build();
                
                utilisateurRepository.save(recruteur);
            }
            
            if (!utilisateurRepository.existsByUsername("candidat")) {
                Set<Role> candidatRoles = new HashSet<>();
                candidatRoles.add(Role.CANDIDAT);
                
                Utilisateur candidat = Utilisateur.builder()
                        .username("candidat")
                        .email("candidat@jobapp.com")
                        .password(passwordEncoder.encode("candidat123"))
                        .roles(candidatRoles)
                        .nom("Martin")
                        .prenom("Sophie")
                        .build();
                
                utilisateurRepository.save(candidat);
            }
        };
    }
} 