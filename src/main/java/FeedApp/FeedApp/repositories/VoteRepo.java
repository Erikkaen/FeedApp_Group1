package FeedApp.FeedApp.repositories;

import FeedApp.FeedApp.model.Vote;
import org.springframework.data.repository.CrudRepository;

public interface VoteRepo extends CrudRepository<Vote, String> {
  boolean existsByUser_IdAndOption_Poll_Id(String userId, String pollId);
}
