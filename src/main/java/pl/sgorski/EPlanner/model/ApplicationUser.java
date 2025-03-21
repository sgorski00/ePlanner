package pl.sgorski.EPlanner.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.sgorski.EPlanner.custom.validation.ValidPassword;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ValidPassword
@Entity
@Table(name = "app_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApplicationUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Email
    private String email;

    //Validated in class annotation
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    private String provider = "LOCAL";

    private String providerId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Event> events = new ArrayList<>();

    private static PasswordEncoder passwordEncoder;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.toString().toUpperCase()));
    }

    @PreUpdate
    public void preUpdate() {
        if(this.providerId == null && !this.password.startsWith("$2a$")) {
            this.password = passwordEncoder.encode(this.password);
        }
    }

    @PrePersist
    public void prePersist() {
        if(this.providerId == null && !this.password.startsWith("$2a$")) {
            this.password = passwordEncoder.encode(this.password);
        }
    }

    public static void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        ApplicationUser.passwordEncoder = passwordEncoder;
    }
}
