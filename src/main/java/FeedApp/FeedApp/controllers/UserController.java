package FeedApp.FeedApp.controllers;

import FeedApp.FeedApp.dto.UserRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import FeedApp.FeedApp.services.PasswordService;
import FeedApp.FeedApp.model.PollManager;
import FeedApp.FeedApp.model.User;

import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/users")
public class UserController {

    private final PollManager pollManager;
    private final PasswordService passwordService;

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
    public Optional<User> getUser(@PathVariable String userId) {
        Optional<User> user = pollManager.getUser(userId);
        if (user.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        return user;
    }

    // REGISTER USER
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserRegistration userRegistration) {
        //HASHING PASSWORD HERE
      String encodedPassword = passwordService.encode(userRegistration.getPassword());

      User user = new User(
          userRegistration.getUsername(),
          userRegistration.getEmail(),
          encodedPassword
      );

        pollManager.addUser(user);
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

    // LOGIN USER
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        Optional<User> userOpt = pollManager.getUserByUsername(loginRequest.getUsername());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }
        User user = userOpt.get();
        boolean passwordMatches = passwordService.matches(
                loginRequest.getPassword(),
                user.getPassword()
        );

        if (passwordMatches) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

}
