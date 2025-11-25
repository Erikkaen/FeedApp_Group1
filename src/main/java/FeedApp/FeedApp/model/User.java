package FeedApp.FeedApp.model;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
public class User implements UserDetails {

  public enum Roles implements GrantedAuthority {
    GUEST,
    PRIVILEGED;

    @Override
    public String getAuthority() {
      return "ROLE_" + name();
    }
  }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Roles role;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Poll> created = new LinkedHashSet<>();

    public User() {}

    /**
     * Creates a new User object with given username and email.
     * The id of a new user object gets determined by the database.
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.created = new LinkedHashSet<>();
        this.role = Roles.PRIVILEGED;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      return Arrays.asList(this.role);
    }

    public String getId() {
        return this.id; 
    }

  public String getUsername() {
        return this.username;
    }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }
    public Roles getRole() {
      return role;
    }

    public void setRole(Roles role) {
      this.role = role;
    }
    public void setUsername(String username) {
      this.username = username;
    }
    public void setEmail(String email) {
      this.email = email;
    }
}