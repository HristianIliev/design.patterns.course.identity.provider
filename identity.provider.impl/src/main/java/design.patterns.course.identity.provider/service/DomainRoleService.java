package design.patterns.course.identity.provider.service;

import design.patterns.course.identity.provider.domain.entity.Role;
import design.patterns.course.identity.provider.domain.entity.User;
import design.patterns.course.identity.provider.domain.role.repository.RoleRepository;
import design.patterns.course.identity.provider.domain.role.service.RoleService;
import design.patterns.course.identity.provider.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DomainRoleService implements RoleService {

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private UserRepository userRepository;

  @Override
  public void addRoleToUser(String roleName, User user) {
    Role role = new Role();
    role.setName(roleName);
    role.setUser(user);

    roleRepository.save(role);
  }

  @Override
  public void removeRoleFromUser(String roleName, User user) {
    List<Role> roles = user.getRoles();

    int indexOfRoleForDeletion = 0;
    for (int i = 0; i < roles.size(); i++) {
      if (roles.get(i).getName().equals(roleName)) {
        indexOfRoleForDeletion = i;

        break;
      }
    }

    Role role = roles.get(indexOfRoleForDeletion);

    roleRepository.delete(role);
  }

}
