package FeedApp.FeedApp.repositories;

import FeedApp.FeedApp.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, String> {
}
