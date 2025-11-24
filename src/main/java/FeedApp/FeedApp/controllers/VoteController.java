package FeedApp.FeedApp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import FeedApp.FeedApp.dto.VoteRequest;
import FeedApp.FeedApp.model.PollManager;
import FeedApp.FeedApp.model.Vote;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/votes")
public class VoteController {

    private final PollManager pollManager;

    public VoteController(PollManager pollManager) {
        this.pollManager = pollManager;
    }

    @PostMapping("/{pollId}/{username}")
    public void addVote(@PathVariable String pollId, @PathVariable String username,
                        @RequestBody VoteRequest voteRequest) {
      pollManager.voteProduce(pollId, voteRequest.optionId, username);
    }

    @GetMapping("/{pollId}/option/{optionId}")
    public int getVoteCount(@PathVariable String pollId,
                            @PathVariable String optionId) {
        return pollManager.getVoteCount(pollId, optionId);
    }

    @GetMapping("/{pollId}/{username}")
    public Vote getVote(@PathVariable String pollId, @PathVariable String username) {
        Vote vote = pollManager.getVote(pollId, username);
        if (vote == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vote not found");
        }
        return vote;
    }

    @DeleteMapping("/{pollId}/{username}")
    public void deleteVote(@PathVariable String pollId, @PathVariable String username) {
        boolean removed = pollManager.removeVote(pollId, username);
        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vote not found");
        }
    }
}
