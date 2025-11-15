package FeedApp.FeedApp.repositories;

import FeedApp.FeedApp.model.Vote;
import org.springframework.data.repository.CrudRepository;
import java.util.Collection;

public interface VoteRepo extends CrudRepository<Vote, String> {
    Collection<Vote> findAllByOption_Poll_Id(String pollId);

  boolean existsByUser_IdAndOption_Poll_Id(String userId, String pollId);
  boolean existsByGuestIdAndOption_Poll_Id(String guestId, String pollId);

}
