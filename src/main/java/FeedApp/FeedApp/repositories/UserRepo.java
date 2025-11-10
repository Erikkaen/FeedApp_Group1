package FeedApp.FeedApp.repositories;

import FeedApp.FeedApp.model.User;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface UserRepo extends CrudRepository<User, String> {
    Optional<User> findByUsername(String username);
}
