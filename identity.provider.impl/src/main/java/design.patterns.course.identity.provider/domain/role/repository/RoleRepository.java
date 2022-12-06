package design.patterns.course.identity.provider.domain.role.repository;

import design.patterns.course.identity.provider.domain.entity.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
}
