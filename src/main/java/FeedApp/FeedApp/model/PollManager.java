package FeedApp.FeedApp.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import FeedApp.FeedApp.repositories.PollsRepo;
import FeedApp.FeedApp.repositories.UserRepo;
import FeedApp.FeedApp.repositories.VoteOptionRepo;
import FeedApp.FeedApp.repositories.VoteRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class PollManager {
//    private final Map<String, User> users = new HashMap<>();
//    private final Map<String, Poll> polls = new HashMap<>();
//    private final Map<String, Vote> votes = new HashMap<>();
  private final UserRepo userRepo;
  private final VoteRepo voteRepo;
  private final VoteOptionRepo voteOptionRepo;
  private final PollsRepo pollRepo;

  public PollManager(UserRepo userRepo, VoteRepo voteRepo, VoteOptionRepo voteOptionRepo, PollsRepo pollRepo) {
    this.userRepo = userRepo;
    this.voteRepo = voteRepo;
    this.voteOptionRepo = voteOptionRepo;
    this.pollRepo = pollRepo;
  }

  public Iterable<User>  getUsers() {
    return userRepo.findAll();
  }

    // user methods
    public void addUser(String username, User user) {
        userRepo.save(user);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public Optional<User> getUser(String userId) {
        return userRepo.findById(userId);
    }
    public boolean removeUser(String username) {
      userRepo.deleteById(username);
      return true;
    }

    // poll methods
    public void addPoll(Poll poll) {
      pollRepo.save(poll);
    }

    public Iterable<Poll> getPolls() {
    return pollRepo.findAll();
  }

    public Optional<Poll> getPoll(String pollId) {
        return pollRepo.findById(pollId);
    }

    public boolean removePoll(String pollId) {
      pollRepo.deleteById(pollId);
      return true;
    }

    // vote methods
    public void addVote(String pollId, Vote vote, String userIdOrGuestId, String optionId) {
        VoteOption option = voteOptionRepo.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Vote option not found"));

        // Check if poll exists
        Poll poll = pollRepo.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Poll not found"));

        // Check user or guest
        Optional<User> userOpt = userRepo.findById(userIdOrGuestId);
        boolean alreadyVoted;

        if (userOpt.isPresent()) {
            alreadyVoted = voteRepo.existsByUser_IdAndOption_Poll_Id(userOpt.get().getId(), pollId);
            if (alreadyVoted) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User already voted");
            }
            vote.setUser(userOpt.get());
        } else {
            // Treat ID as guest id
            alreadyVoted = voteRepo.existsByGuestIdAndOption_Poll_Id(userIdOrGuestId, pollId);
            if (alreadyVoted) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Guest already voted");
            }
            vote.setGuestId(userIdOrGuestId);
        }

        vote.setVotesOn(option);
        option.setVoteCount(option.getVoteCount() + 1);

        voteRepo.save(vote);
        voteOptionRepo.save(option);
    }


    //TODO: change this method to use the database
//    public void updateVote(String pollId, Vote vote, String username) {
//        this.votes.put(pollId + "-" + username, vote);
//    }

    public Vote getVote(String pollId, String userId) {
        return voteRepo.findById(pollId + "-" + userId).orElse(null);
    }

    public boolean removeVote(String pollId, String username) {
        Vote vote = voteRepo.findById(pollId + "-" + username).orElse(null);
        if (vote == null)  return false;
        voteRepo.deleteById(pollId + "-" + username);
        return true;
    }

    public Collection<Vote> getVotes(String pollId) {
      Poll poll = pollRepo.findById(pollId)
          .orElseThrow(() -> new RuntimeException("Poll not found"));

      return voteRepo.findAllByOption_Poll_Id(pollId);
  }

  public int getVoteCount(String pollId, String optionId) {

    Poll poll = pollRepo.findById(pollId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            "Poll not found"));

    VoteOption option = poll.getOptions().stream()
        .filter(opt -> opt.getId().equals(optionId))
        .findFirst()
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            "Option not found in poll"));

    return option.getVoteCount();
  }

    public void removeVotes(String pollId) {
        voteRepo.deleteById(pollId);
    }
}
