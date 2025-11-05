package FeedApp.FeedApp.repositories;

import FeedApp.FeedApp.model.Poll;
import org.springframework.data.repository.CrudRepository;

public interface PollsRepo extends CrudRepository<Poll, String> {
}
