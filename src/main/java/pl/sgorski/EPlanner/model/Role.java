package pl.sgorski.EPlanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "role", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<ApplicationUser> users = new HashSet<>();

    @Override
    public String toString() {
        return this.name.substring(0, 1).toUpperCase() + this.name.substring(1).toLowerCase();
    }
}
