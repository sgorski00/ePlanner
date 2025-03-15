package pl.sgorski.EPlanner.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.management.relation.Role;

@Entity
@Table(name = "app_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String password;

    private Role role = Role.USER;

    private String provider = "LOCAL";

    private String providerId;

    private static PasswordEncoder passwordEncoder;

    public enum Role {
        USER,
        ADMIN
    }

    @PreUpdate
    public void preUpdate() {
        if(this.providerId != null && !this.password.startsWith("$2a$")) {
            this.password = passwordEncoder.encode(this.password);
        }
    }

    @PrePersist
    public void prePersist() {
        if(this.providerId != null && !this.password.startsWith("$2a$")) {
            this.password = passwordEncoder.encode(this.password);
        }
    }

    public static void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        ApplicationUser.passwordEncoder = passwordEncoder;
    }
}
