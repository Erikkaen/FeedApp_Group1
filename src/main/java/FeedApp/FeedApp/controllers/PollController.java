package FeedApp.FeedApp.controllers;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import FeedApp.FeedApp.model.*;
import FeedApp.FeedApp.repositories.PollsRepo;
import FeedApp.FeedApp.repositories.VoteOptionRepo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/polls")
public class PollController {
  private final PollManager pollManager;
  private final PollsRepo pollsRepo;
  private final VoteOptionRepo voteOptionRepo;

  public PollController(PollManager pollManager, PollsRepo pollsRepo, VoteOptionRepo voteOptionRepo) {
        this.pollManager = pollManager;
    this.pollsRepo = pollsRepo;
    this.voteOptionRepo = voteOptionRepo;
  }

    @GetMapping
    public Iterable<Poll> getAllPolls() {
      return pollManager.getPolls();
    }

    @GetMapping("/{pollId}")
    public Optional<Poll> getPoll(@PathVariable String pollId) {
      Optional<Poll> poll = pollManager.getPoll(pollId);
    if (poll.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll not found");
    return poll;
    }

    /*
    @PostMapping("/{userId}")
    public Poll createPoll(@PathVariable String userId, @RequestBody Poll poll) {
        User user = pollManager.getUser(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        poll.setCreatedBy(user);
        pollManager.addPoll(poll);
        return poll;
    }*/

    @PostMapping("/{username}")
    public Poll createPoll(@PathVariable String username, @RequestBody Poll poll) {
        Optional<User> userOpt = pollManager.getUserByUsername(username);
        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        User user = userOpt.get();
        poll.setCreatedBy(user);
        pollManager.addPoll(poll);
        return poll;
    }

    @PutMapping("/{pollId}")
    public void updatePoll(@PathVariable String pollId, @RequestBody Poll poll) {
        pollManager.addPoll(poll);
    }

    @DeleteMapping("/{pollId}")
    public void deletePoll(@PathVariable String pollId) {
        boolean removed = pollManager.removePoll(pollId);
        if (!removed) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll not found");
        pollManager.removeVotes(pollId);
    }
}
