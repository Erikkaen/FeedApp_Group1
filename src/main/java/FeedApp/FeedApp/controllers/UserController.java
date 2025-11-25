package FeedApp.FeedApp.controllers;

import FeedApp.FeedApp.dto.UserRegistration;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import FeedApp.FeedApp.services.PasswordService;
import FeedApp.FeedApp.model.PollManager;
import FeedApp.FeedApp.model.User;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final PollManager pollManager;
    private final PasswordService passwordService;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
  public UserController(PollManager pollManager, PasswordService passwordService) {
    this.pollManager = pollManager;
    this.passwordService = passwordService;
  }

    @GetMapping
    public Iterable<User>  getAllUsers() {
        return pollManager.getUsers();
    }

    @GetMapping("/{username}")
    public Optional<User> getUser(@PathVariable String username) {
        Optional<User> user = pollManager.getUser(username);
        if (user.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        return user;
    }

    // REGISTER USER
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserRegistration userRegistration, HttpServletRequest request) {
        //HASHING PASSWORD HERE
      String encodedPassword = passwordService.encode(userRegistration.getPassword());

      User user = new User(
          userRegistration.getUsername(),
          userRegistration.getEmail(),
          encodedPassword
      );

      pollManager.addUser(user);

      UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
      Authentication auth = new UsernamePasswordAuthenticationToken(
          userDetails, null, userDetails.getAuthorities()
      );

      SecurityContextHolder.getContext().setAuthentication(auth);
      request.getSession(true).setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

      return ResponseEntity.status(HttpStatus.CREATED)
          .body(user);
    }


  @PutMapping("/{username}")
    public void updateUser(@PathVariable String username, @RequestBody User user) {
        pollManager.addUser(user);
    }

    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable String username) {
        boolean removed = pollManager.removeUser(username);
        if (!removed) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }

  @Autowired
  private AuthenticationManager authenticationManager;

  @PostMapping("/login")
  public ResponseEntity<?> login(
      @RequestBody Map<String, String> body,
      HttpServletRequest request
  ) {
    String username = body.get("username");
    String password = body.get("password");
    Authentication auth = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(username, password)
    );

    SecurityContextHolder.getContext().setAuthentication(auth);

    request.getSession(true).setAttribute(
        "SPRING_SECURITY_CONTEXT",
        SecurityContextHolder.getContext()
    );

    return ResponseEntity.ok(Map.of("username", username));
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(HttpServletRequest request) {
    try {
      // invalidate session
      var session = request.getSession(false);
      if (session != null) {
        session.invalidate();
      }
      // clear security context
      SecurityContextHolder.clearContext();
      return ResponseEntity.ok(Map.of("loggedOut", true));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
    }
  }
}
