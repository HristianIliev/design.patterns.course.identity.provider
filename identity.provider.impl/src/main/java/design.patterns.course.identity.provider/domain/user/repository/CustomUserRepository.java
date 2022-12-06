package design.patterns.course.identity.provider.domain.user.repository;

import design.patterns.course.identity.provider.domain.entity.User;

public interface CustomUserRepository {

  public User findByUsername(String username);

  public User findByEmail(String email);

}
