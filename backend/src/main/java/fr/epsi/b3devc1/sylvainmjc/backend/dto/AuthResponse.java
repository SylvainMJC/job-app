package fr.epsi.b3devc1.sylvainmjc.backend.dto;

import fr.epsi.b3devc1.sylvainmjc.backend.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String username;
    private String email;
    private Set<Role> roles;
} 