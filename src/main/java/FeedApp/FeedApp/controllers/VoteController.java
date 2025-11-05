package FeedApp.FeedApp.controllers;

import java.util.Collection;

import FeedApp.FeedApp.model.VoteOption;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import FeedApp.FeedApp.model.PollManager;
import FeedApp.FeedApp.model.Vote;

@CrossOrigin
@RestController
@RequestMapping("/votes")
public class VoteController {

    private final PollManager pollManager;

    public VoteController(PollManager pollManager) {
        this.pollManager = pollManager;
    }

    @PostMapping("/{pollId}/{username}")
    public void addVote(@PathVariable String pollId, @PathVariable String username,
                         @RequestBody Vote vote, @RequestBody String voteOption) {

        pollManager.addVote(pollId, vote, username, voteOption);
    }

//    @PutMapping("/{pollId}/{username}")
//    public void updateVote(@PathVariable String pollId, @PathVariable String username,
//                           @RequestBody Vote vote) {
//
//        pollManager.updateVote(pollId, vote, username);
//    }
//
//    @GetMapping
//    public Collection<Vote> getAllVotes() {
//        return pollManager.getVotes().values();
//    }

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
