package ewm.user.repository;

import ewm.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findAllByIdIn(Iterable<Integer> ids, Pageable pageable);
}
