package FeedApp.FeedApp.repositories;

import FeedApp.FeedApp.model.VoteOption;
import org.springframework.data.repository.CrudRepository;

public interface VoteOptionRepo extends CrudRepository<VoteOption, String> {
}
