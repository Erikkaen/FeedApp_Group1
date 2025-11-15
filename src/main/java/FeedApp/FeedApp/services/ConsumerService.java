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
import java.util.Optional;

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
    String userIdOrGuestId = voteDetails.userIdOrGuestId;

    // Log the incoming vote details
    System.out.println("Incoming vote message: " + message);
    System.out.println("Looking for VoteOption ID: '" + voteDetails.optionId + "' (length=" + voteDetails.optionId.length() + ")");

    // Trim whitespace just in case
    String optionId = voteDetails.optionId.trim();
    VoteOption option = voteOptionRepo.findById(optionId)
        .orElseThrow(() -> new RuntimeException("VoteOption not found"));

    Poll poll = option.getPoll();
    String pollId = poll.getId();
    System.out.println("Found VoteOption for poll ID: " + pollId);

    Optional<User> userOpt = userRepo.findByUsername(userIdOrGuestId);
    String userId = null;
    if (userOpt.isPresent()) {
      userId = userOpt.get().getId();
    }

    boolean alreadyVoted;
    Vote vote = new Vote();

    if (userId != null) {
      // Registered user
      alreadyVoted = voteRepo.existsByUser_IdAndOption_Poll_Id(userId, pollId);
      if (alreadyVoted) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, "User already voted");
      }
      vote.setUser(userOpt.get());
    } else {
      // Treat as guest
      alreadyVoted = voteRepo.existsByGuestIdAndOption_Poll_Id(userIdOrGuestId, pollId);
      if (alreadyVoted) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Guest already voted");
      }
      vote.setGuestId(userIdOrGuestId);
    }


    vote.setVotesOn(option);
    option.setVoteCount(option.getVoteCount() + 1);
    vote.setPublishedAt(Instant.now());
    voteRepo.save(vote);
    voteOptionRepo.save(option);
    System.out.println("Vote persisted successfully!");
  }


}