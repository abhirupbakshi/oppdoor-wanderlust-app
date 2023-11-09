package com.example.wanderlust.model;

import com.example.wanderlust.model.validation.group.Create;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user")
@NoArgsConstructor(force = true)
@Getter
@Setter
@Accessors(chain = true)
public class User implements UserDetails {

    @JsonProperty(value = "username")
    @NotBlank(message = "Username cannot be empty", groups = {Create.class})
    @Id
    @Column(name = "username")
    private String username;

    @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password cannot be empty", groups = {Create.class})
    @Column(name = "password", nullable = false)
    private String password;

    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "username", nullable = false))
    private List<String> roles;

    @JsonIgnore
    @Getter(value = AccessLevel.NONE)
    @Column(name = "accountNonExpired", nullable = false)
    private boolean accountNonExpired;

    @JsonIgnore
    @Getter(value = AccessLevel.NONE)
    @Column(name = "accountNonLocked", nullable = false)
    private boolean accountNonLocked;

    @JsonIgnore
    @Getter(value = AccessLevel.NONE)
    @Column(name = "credentialsNonExpired", nullable = false)
    private boolean credentialsNonExpired;

    @JsonIgnore
    @Getter(value = AccessLevel.NONE)
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @JsonIgnore
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Destination> destinations;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        if (roles == null) {
            throw new RuntimeException("User roles are null. Cannot create List of GrantedAuthority");
        }

        return roles.stream().map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
