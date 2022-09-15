package com.mettle.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.Set;

import static org.hibernate.annotations.FetchMode.JOIN;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USER_GROUP")
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @EqualsAndHashCode.Include
    @ToString.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ToString.Include
    private String firstName;
    @ToString.Include
    private String lastName;
    @ToString.Include
    @NotBlank(message = "Username is required")
    private String userName;
    @NotBlank(message = "Password is required")
    private String password;
    @ToString.Include
    @CreationTimestamp
    private Instant createdAt;

    @Fetch(JOIN)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role_mapping",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<Role> roles;

    @Fetch(JOIN)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_feature_mapping",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "feature_id")}
    )
    private Set<Feature> features;
}
