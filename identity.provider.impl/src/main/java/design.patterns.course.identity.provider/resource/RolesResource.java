package design.patterns.course.identity.provider.resource;

import design.patterns.course.identity.provider.domain.entity.User;
import design.patterns.course.identity.provider.domain.role.service.RoleService;
import design.patterns.course.identity.provider.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import security.annotation.RequiresProtection;

@Controller
@RequestMapping("v1/users")
public class RolesResource {

  @Autowired
  private RoleService roleService;

  @Autowired
  private UserService userService;

  @PostMapping("{username}/roles")
  @RequiresProtection(userIdentifierFrom = "path", userIdentifierParamName = "users", roles = "Administrator")
  public ResponseEntity<String> addRoleToUser(@PathVariable String username, @RequestParam String roleName) {
    User user = userService.retrieveUser(username);
    if (user == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("There is no such user.");
    }

    if (user.hasRole(roleName) == false) {
      roleService.addRoleToUser(roleName, user);
    }

    return ResponseEntity.ok().build();
  }

  @DeleteMapping("{username}/roles")
  @RequiresProtection(userIdentifierFrom = "path", userIdentifierParamName = "users", roles = "Administrator")
  public ResponseEntity<String> deleteRoleOfUser(@PathVariable String username,  @RequestParam String roleName) {
    User user = userService.retrieveUser(username);
    if (user == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("There is no such user.");
    }

    if (user.hasRole(roleName)) {
      roleService.removeRoleFromUser(roleName, user);
    }

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

}
