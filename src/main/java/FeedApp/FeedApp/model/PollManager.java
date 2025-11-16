package FeedApp.FeedApp.model;

import java.util.*;

import FeedApp.FeedApp.messaging.Producer;
import FeedApp.FeedApp.repositories.PollsRepo;
import FeedApp.FeedApp.repositories.UserRepo;
import FeedApp.FeedApp.repositories.VoteOptionRepo;
import FeedApp.FeedApp.repositories.VoteRepo;
import FeedApp.FeedApp.messaging.RabbitConsumer;
import FeedApp.FeedApp.services.ConsumerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import redis.clients.jedis.UnifiedJedis;

@Component
public class PollManager {
//    private final Map<String, User> users = new HashMap<>();
//    private final Map<String, Poll> polls = new HashMap<>();
//    private final Map<String, Vote> votes = new HashMap<>();
  private final UserRepo userRepo;
  private final VoteRepo voteRepo;
  private final VoteOptionRepo voteOptionRepo;
  private final PollsRepo pollRepo;
  private final UnifiedJedis jedis;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private Producer producer;
  private RabbitConsumer rabbitConsumer;
  @Autowired
  private org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory;

  @Autowired
  private ConsumerService consumerService;

  public PollManager(UserRepo userRepo, VoteRepo voteRepo, VoteOptionRepo voteOptionRepo, PollsRepo pollRepo, UnifiedJedis jedis, Producer producer,  RabbitConsumer rabbitConsumer) {
    this.userRepo = userRepo;
    this.voteRepo = voteRepo;
    this.voteOptionRepo = voteOptionRepo;
    this.producer = producer;
    this.pollRepo = pollRepo;
    this.jedis = jedis;
    this.rabbitConsumer = rabbitConsumer;
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
    List<Poll> polls = (List<Poll>) pollRepo.findAll(); // get all existing polls
    for (Poll poll : polls) {
      initTopic(poll.getId());
    }
  }



    // vote methods
//    public void addVote(String pollId, Vote vote, String userIdOrGuestId, String optionId) {
//        VoteOption option = voteOptionRepo.findById(optionId)
//                .orElseThrow(() -> new RuntimeException("Vote option not found"));
//
//        // Check if poll exists
//        Poll poll = pollRepo.findById(pollId)
//                .orElseThrow(() -> new RuntimeException("Poll not found"));
//
//        // Check user or guest
//        Optional<User> userOpt = userRepo.findById(userIdOrGuestId);
//        boolean alreadyVoted;
//
//        if (userOpt.isPresent()) {
//            alreadyVoted = voteRepo.existsByUser_IdAndOption_Poll_Id(userOpt.get().getId(), pollId);
//            if (alreadyVoted) {
//                throw new ResponseStatusException(HttpStatus.CONFLICT, "User already voted");
//            }
//            vote.setUser(userOpt.get());
//        } else {
//            // Treat ID as guest id
//            alreadyVoted = voteRepo.existsByGuestIdAndOption_Poll_Id(userIdOrGuestId, pollId);
//            if (alreadyVoted) {
//                throw new ResponseStatusException(HttpStatus.CONFLICT, "Guest already voted");
//            }
//            vote.setGuestId(userIdOrGuestId);
//        }
//
//        vote.setVotesOn(option);
//        option.setVoteCount(option.getVoteCount() + 1);
//
//        voteRepo.save(vote);
//        voteOptionRepo.save(option);
//    }

  public void voteProduce(String pollId, String optionId, String userIdOrGuestId) {
    String message = "{ \"optionId\": \"" + optionId + "\" , \"userIdOrGuestId\": \"" + userIdOrGuestId + "\"  }";
    producer.Produce(message, pollId);

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
