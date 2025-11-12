package FeedApp.FeedApp;

import FeedApp.FeedApp.model.User;
import FeedApp.FeedApp.model.PollManager;
import FeedApp.FeedApp.repositories.PollsRepo;
import FeedApp.FeedApp.repositories.UserRepo;
import FeedApp.FeedApp.repositories.VoteOptionRepo;
import FeedApp.FeedApp.repositories.VoteRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@SpringBootTest
//@Transactional //Med Transactional så blir det ikke lagt til noe i databasen pga transaksjoner READ_COMMITED
public class UserBenchmarkTest {

  @Autowired
  private PollManager pollManager;

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private VoteRepo voteRepo;

  @Autowired
  private VoteOptionRepo voteOptionRepo;

  @Autowired
  private PollsRepo pollRepo;

  @PersistenceContext
  private EntityManager em;

  @BeforeEach
  void cleanDatabase() {
    pollRepo.deleteAll();
    voteOptionRepo.deleteAll();
    voteRepo.deleteAll();
    userRepo.deleteAll();
  }

  //Kan legges til for å slette alt som ligger i databasen etter hver test
  // (Kan være greit for å tømme databasen etter å ha kjørt testene)
//  @AfterEach
//   void cleanAfterDatabase() {
//    pollRepo.deleteAll();
//    voteOptionRepo.deleteAll();
//    voteRepo.deleteAll();
//    userRepo.deleteAll();
//  }


  @Test
  public void benchmarkUserPersistence() {
    int numUsers = 10000;

    long start = System.currentTimeMillis();

    for (int i = 0; i < numUsers; i++) {
      String uniqueSuffix = UUID.randomUUID().toString();
      User user = new User(
          "user" + uniqueSuffix,
          "user" + uniqueSuffix + "@example.com",
          "password123"
      );
      pollManager.addUser(user.getUsername(), user);
    }

    long end = System.currentTimeMillis();
    System.out.println("Time to persist " + numUsers + " users: " + (end - start) + " ms");
  }

  @Test
  public void benchmarkBulkRetrieval() {
    int numUsers = 1000;

    // Insert users first
    for (int i = 0; i < numUsers; i++) {
      String uniqueSuffix = UUID.randomUUID().toString();
      User user = new User(
          "user" + uniqueSuffix,
          "user" + uniqueSuffix + "@example.com",
          "password123"
      );
      pollManager.addUser(user.getUsername(), user);
    }

    // Measure bulk retrieval time
    long start = System.currentTimeMillis();
    Iterable<User> users = pollManager.getUsers(); // retrieve all users at once
    long end = System.currentTimeMillis();

    System.out.println("Time to retrieve all users as Iterable: " + (end - start) + " ms" + " Ant: " + ((Collection<User>) users).size());
  }

  @Test
  public void benchmarkIndividualRetrieval() {
    int numUsers = 1000;
    List<User> users = new ArrayList<>();

    // Insert users first
    for (int i = 0; i < numUsers; i++) {
      String uniqueSuffix = UUID.randomUUID().toString();
      User user = new User(
          "user" + uniqueSuffix,
          "user" + uniqueSuffix + "@example.com",
          "password123"
      );
      pollManager.addUser(user.getUsername(), user);
      users.add(user);
    }

    int count = 0;

    long start = System.currentTimeMillis();
    for (User user : users) {
      pollManager.getUserByUsername(user.getUsername());
      count++;
    }
    long end = System.currentTimeMillis();

    System.out.println("Time to retrieve " + count + " users individually: " + (end - start) + " ms");
  }

}
