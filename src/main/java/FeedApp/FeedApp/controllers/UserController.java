package FeedApp.FeedApp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import FeedApp.FeedApp.model.PollManager;
import FeedApp.FeedApp.model.User;

import java.util.Collection;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    private final PollManager pollManager;

    public UserController(PollManager pollManager) {
        this.pollManager = pollManager;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return pollManager.getUsers().values();
    }

    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        User user = pollManager.getUser(username);
        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        return user;
    }

    @PostMapping
    public void createUser(@RequestBody User user) {
        pollManager.addUser(user.getUsername(), user);
    }

    @PutMapping("/{username}")
    public void updateUser(@PathVariable String username, @RequestBody User user) {
        pollManager.addUser(username, user);
    }

    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable String username) {
        boolean removed = pollManager.removeUser(username);
        if (!removed) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }
}
