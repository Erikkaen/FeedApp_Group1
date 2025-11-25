package FeedApp.FeedApp.services;

import FeedApp.FeedApp.dto.VoteRequest;
import FeedApp.FeedApp.model.Poll;
import FeedApp.FeedApp.model.User;
import FeedApp.FeedApp.model.Vote;
import FeedApp.FeedApp.model.VoteOption;
import FeedApp.FeedApp.repositories.UserRepo;
import FeedApp.FeedApp.repositories.VoteOptionRepo;
import FeedApp.FeedApp.repositories.VoteRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
public class ConsumerService {
  private final VoteRepo voteRepo;
  private final VoteOptionRepo voteOptionRepo;
  private final UserRepo userRepo;

  public ConsumerService(VoteRepo voteRepo, VoteOptionRepo voteOptionRepo,  UserRepo userRepo) {
    this.voteRepo = voteRepo;
    this.voteOptionRepo = voteOptionRepo;
    this.userRepo = userRepo;
  }


  public void votePersist(String message) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    VoteRequest voteDetails = objectMapper.readValue(message, VoteRequest.class);
    String usernameOrGuestId = voteDetails.userIdOrGuestId;

    // Log incoming vote
    System.out.println("Incoming vote message: " + message);

    // Find the vote option
    String optionId = voteDetails.optionId.trim();
    VoteOption option = voteOptionRepo.findById(optionId)
        .orElseThrow(() -> new RuntimeException("VoteOption not found"));

    Poll poll = option.getPoll();
    String pollId = poll.getId();
    System.out.println("Found VoteOption for poll ID: " + pollId);

    // Lookup or create a user (guest or registered)
    User user = userRepo.findByUsername(usernameOrGuestId)
        .orElseGet(() -> {
          // Create a new guest user
          User g = new User(usernameOrGuestId, usernameOrGuestId + "@guest.com", "");
          g.setRole(User.Roles.GUEST);
          userRepo.save(g);
          return g;
        });

    // Check if this user already voted in this poll
    boolean alreadyVoted = voteRepo.existsByUser_IdAndOption_Poll_Id(user.getId(), pollId);
    if (alreadyVoted) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "User already voted");
    }

    // Persist vote
    Vote vote = new Vote();
    vote.setUser(user);
    vote.setVotesOn(option);
    vote.setPublishedAt(Instant.now());

    // Increment vote count
    option.setVoteCount(option.getVoteCount() + 1);

    voteRepo.save(vote);
    voteOptionRepo.save(option);

    System.out.println("Vote persisted successfully for user: " + user.getUsername());
  }


}