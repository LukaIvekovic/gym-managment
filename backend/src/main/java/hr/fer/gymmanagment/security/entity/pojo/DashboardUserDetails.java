package hr.fer.gymmanagment.security.entity.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hr.fer.gymmanagment.security.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class DashboardUserDetails implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    @Getter
    private Integer id;

    @Getter
    private String firstName;

    @Getter
    private String lastName;

    @JsonIgnore
    private String password;

    @Getter
    private String email;

    private final Collection<? extends GrantedAuthority> authorities;

    public DashboardUserDetails(Integer id, String firstName, String lastName, String password,
                                Collection<? extends GrantedAuthority> authorities,
                                String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.authorities = authorities;
        this.email = email;
    }

    public static DashboardUserDetails build(User user) {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().getName().name()));

        return new DashboardUserDetails(user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                authorities,
                user.getEmail());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return firstName+" "+lastName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DashboardUserDetails user = (DashboardUserDetails) o;
        return Objects.equals(id, user.id);
    }
}

