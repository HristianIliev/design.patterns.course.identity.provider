package design.patterns.course.identity.provider.repository;

import design.patterns.course.identity.provider.domain.entity.User;
import design.patterns.course.identity.provider.domain.user.repository.CustomUserRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class DomainUserRepository implements CustomUserRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public User findByUsername(String username) {
    return (User) entityManager.createQuery("SELECT * FROM user u WHERE u.username = :username")
        .setParameter("username", username)
        .getSingleResult();
  }

  @Override
  public User findByEmail(String email) {
    return (User) entityManager.createQuery("SELECT * FROM user u WHERE u.email = :email")
        .setParameter("email", email)
        .getSingleResult();
  }

}
