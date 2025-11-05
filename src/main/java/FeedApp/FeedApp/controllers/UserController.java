package FeedApp.FeedApp.controllers;

import FeedApp.FeedApp.repositories.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import FeedApp.FeedApp.model.PollManager;
import FeedApp.FeedApp.model.User;

import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    private final PollManager pollManager;
  private final UserRepo userRepo;

  public UserController(PollManager pollManager, UserRepo userRepo) {
        this.pollManager = pollManager;
    this.userRepo = userRepo;
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

//  @GetMapping("/{username}")
//  public User getUser(@PathVariable String userId) {
//    return userRepo.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//  }

    @PostMapping
    public void createUser(@RequestBody User user) {
        pollManager.addUser(user.getUsername(), user);
    }

//  @PostMapping
//  public void createUser(@RequestBody User user) {
//     userRepo.save(user);
//  }

  @PutMapping("/{username}")
    public void updateUser(@PathVariable String username, @RequestBody User user) {
        pollManager.addUser(username, user);
    }

//  @PutMapping("/{username}")
//  public void updateUser(@PathVariable String username, @RequestBody User user) {
//    userRepo.save(user);
//  }

    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable String username) {
        boolean removed = pollManager.removeUser(username);
        if (!removed) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }

//  @DeleteMapping("/{username}")
//  public void deleteUser(@PathVariable String username) {
//    userRepo.deleteById(username);
//  }
}
