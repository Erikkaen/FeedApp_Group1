package FeedApp.FeedApp.model;

import java.util.*;

import FeedApp.FeedApp.messaging.Producer;
import FeedApp.FeedApp.repositories.PollsRepo;
import FeedApp.FeedApp.repositories.UserRepo;
import FeedApp.FeedApp.repositories.VoteRepo;
import FeedApp.FeedApp.messaging.RabbitConsumer;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import redis.clients.jedis.UnifiedJedis;

import javax.management.relation.Role;

@Component
public class PollManager {
  private final UserRepo userRepo;
  private final VoteRepo voteRepo;
  private final PollsRepo pollRepo;
  private final UnifiedJedis jedis;
  private final Producer producer;
  private final RabbitConsumer rabbitConsumer;

  public PollManager(UserRepo userRepo, VoteRepo voteRepo, PollsRepo pollRepo, UnifiedJedis jedis, Producer producer,  RabbitConsumer rabbitConsumer) {
    this.userRepo = userRepo;
    this.voteRepo = voteRepo;
    this.producer = producer;
    this.pollRepo = pollRepo;
    this.jedis = jedis;
    this.rabbitConsumer = rabbitConsumer;
  }

  public Iterable<User>  getUsers() {
    return userRepo.findAll();
  }

    // user methods
    public void addUser(User user) {
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

    public User getOrCreateGuest(String guestId) {
      Optional<User> guestOpt = userRepo.findByUsername(guestId);
      if (guestOpt.isPresent()) {
        return guestOpt.get();
      }

      User guest = new User();
      guest.setUsername(guestId);
      guest.setEmail(guestId + "@guest.local"); // dummy email
      guest.setPassword(""); // no password
      guest.setRole(User.Roles.GUEST);

      return userRepo.save(guest);
    }

    // poll methods
    public void addPoll(Poll poll) throws Exception {
      pollRepo.save(poll);
      initTopic(poll.getId());
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

    public void initTopic(String pollId) throws Exception{
      String exchangeName = "Poll_" + pollId;
      producer.createTopicForPoll(exchangeName);
      rabbitConsumer.createConsumerForPoll(exchangeName);
    }

  @PostConstruct
  public void createOnInit() throws Exception {
    List<Poll> polls = (List<Poll>) pollRepo.findAll();
    for (Poll poll : polls) {
      initTopic(poll.getId());
    }
  }



    // vote methods
  public void voteProduce(String pollId, String optionId, String userIdOrGuestId) {
    String message = "{ \"optionId\": \"" + optionId + "\" , \"userIdOrGuestId\": \"" + userIdOrGuestId + "\"  }";
    producer.Produce(message, pollId);

  }

    public Vote getVote(String pollId, String userId) {
        return voteRepo.findById(pollId + "-" + userId).orElse(null);
    }

    public boolean removeVote(String pollId, String username) {
        Vote vote = voteRepo.findById(pollId + "-" + username).orElse(null);
        if (vote == null)  return false;
        voteRepo.deleteById(pollId + "-" + username);
        return true;
    }

  public int getVoteCount(String pollId, String optionId) {
      String redisKey = "poll:" + pollId + ":results";

      // 1. Try Redis cache
      try {
          String cached = jedis.hget(redisKey, optionId);
          if (cached != null) {
              return Integer.parseInt(cached);
          }
      } catch (Exception e) {
          System.out.println("Redis OFFLINE -> Using database!");
      }

    Poll poll = pollRepo.findById(pollId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll not found"));

    VoteOption option = poll.getOptions().stream()
        .filter(opt -> opt.getId().equals(optionId))
        .findFirst()
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Option not found in poll"));

      int voteCount = option.getVoteCount();

      // 3. Populate cache
      try {
          for (VoteOption opt : poll.getOptions()) {
              jedis.hset(redisKey, opt.getId(), String.valueOf(opt.getVoteCount()));
          }
          jedis.expire(redisKey, 60);
      } catch (Exception e) {
          System.out.println("Redis OFFLINE -> Could not update cache.");
      }

      return voteCount;
  }

    public void removeVotes(String pollId) {
        voteRepo.deleteById(pollId);
    }
}
