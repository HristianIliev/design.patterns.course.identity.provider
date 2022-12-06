package design.patterns.course.identity.provider.domain.role.service;

import design.patterns.course.identity.provider.domain.entity.User;

public interface RoleService {

  public void addRoleToUser(String roleName, User user);

  public void removeRoleFromUser(String roleName, User user);

}
