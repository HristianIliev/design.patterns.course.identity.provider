package design.patterns.course.identity.provider.domain.user.repository;

import design.patterns.course.identity.provider.domain.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long>, CustomUserRepository {

}
