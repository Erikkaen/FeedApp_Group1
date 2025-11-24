package FeedApp.FeedApp.controllers;

import java.time.Instant;
import java.util.Optional;

import FeedApp.FeedApp.model.*;
import FeedApp.FeedApp.repositories.PollsRepo;
import FeedApp.FeedApp.repositories.VoteOptionRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@CrossOrigin(origins = "*")
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
    public Poll getPoll(@PathVariable String pollId) {
        return pollManager.getPoll(pollId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll not found"));
    }

    @PostMapping("/{username}")
    public ResponseEntity<Poll> createPoll(@PathVariable String username, @RequestBody Poll poll)
 {
        Optional<User> userOpt = pollManager.getUserByUsername(username);
        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        User user = userOpt.get();
        poll.setCreatedBy(user);
        poll.setPublishedAt(Instant.now());

        for (int i = 0; i < poll.getOptions().size(); i++) {
            VoteOption opt = poll.getOptions().get(i);
            opt.setPoll(poll);
            opt.setPresentationOrder(i);
        }

       try {
         pollManager.addPoll(poll);
       } catch (Exception e) {
         throw new RuntimeException("Failed to create poll: " + e.getMessage(), e);
       }
        return ResponseEntity.status(HttpStatus.CREATED).body(poll);
    }

    @PutMapping("/{pollId}")
    public void updatePoll(@PathVariable String pollId, @RequestBody Poll poll) {
      try {
        pollManager.addPoll(poll);
      } catch (Exception e) {
        throw new RuntimeException();
      }
    }

    @DeleteMapping("/{pollId}")
    public void deletePoll(@PathVariable String pollId) {
        boolean removed = pollManager.removePoll(pollId);
        if (!removed) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll not found");
        pollManager.removeVotes(pollId);
    }
}
