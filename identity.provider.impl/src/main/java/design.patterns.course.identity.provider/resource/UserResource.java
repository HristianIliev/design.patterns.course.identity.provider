package design.patterns.course.identity.provider.resource;

import design.patterns.course.identity.provider.domain.entity.User;
import design.patterns.course.identity.provider.domain.entity.UserInformation;
import design.patterns.course.identity.provider.domain.exception.UserException;
import design.patterns.course.identity.provider.domain.rule.BusinessEvaluationRuleException;
import design.patterns.course.identity.provider.domain.user.service.UserService;
import design.patterns.course.identity.provider.event.RegisteredUserEvent;
import design.patterns.course.identity.provider.subscribers.RegisteredUserEventSubscriber;
import event.service.DomainEventHandlingException;
import event.service.DomainEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import security.annotation.RequiresProtection;

import java.util.List;

@Controller
@RequestMapping("v1/users")
public class UserResource {

  @Autowired
  private UserService userService;

  private DomainEventService eventService;

  public UserResource() {
    eventService = new DomainEventService();

    eventService.register(new RegisteredUserEventSubscriber());
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> registerUser(@RequestBody UserInformation userInformation) {
    try {
      userService.evaluateUserRegistrationRules(userInformation);
    } catch (BusinessEvaluationRuleException exception) {
      return ResponseEntity.status(exception.getStatus())
          .body("Validation of request body has failed. Reason: " + exception.getMessage());
    }

    try {
      userService.registerUser(userInformation);
    } catch (UserException exception) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Failed to create user due to: " + exception.getMessage());
    }

    try {
      eventService.publish(new RegisteredUserEvent(userInformation));
    } catch (DomainEventHandlingException exception) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Failed to publish registered user event: " + exception.getMessage());
    }

    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set("Location", "/api/v1/users/" + userInformation.getUsername());

    return ResponseEntity.status(HttpStatus.CREATED)
        .headers(responseHeaders)
        .body("User has been successfully created.");
  }

  @PutMapping(value = "{username}", consumes = MediaType.APPLICATION_JSON_VALUE)
  @RequiresProtection(userIdentifierFrom = "path", userIdentifierParamName = "users")
  public ResponseEntity<String> updateUser(@PathVariable String username, @RequestBody UserInformation userInformation) {
    try {
      userService.evaluateUserUpdateRules(userInformation);
    } catch (BusinessEvaluationRuleException exception) {
      return ResponseEntity.status(exception.getStatus())
          .body("Validation of request body has failed. Reason: " + exception.getMessage());
    }

    User originalUser = userService.retrieveUser(username);
    if (originalUser == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Invalid request");
    }

    try {
      userService.updateUser(originalUser, userInformation);
    } catch (UserException exception) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Failed to update user due to: " + exception.getMessage());
    }

    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set("Location", "/api/v1/users/" + userInformation.getUsername());

    return ResponseEntity.status(HttpStatus.OK)
        .headers(responseHeaders)
        .body("User has been successfully updated.");
  }

  @PostMapping(value = "{username}/pictures", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @RequiresProtection(userIdentifierFrom = "path", userIdentifierParamName = "users")
  public ResponseEntity<String> uploadPicture(@PathVariable String username, @RequestParam MultipartFile picture) {
    User user = userService.retrieveUser(username);
    if (user == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("There is no such user.");
    }

    try {
      userService.addPictureToUser(picture, user);
    } catch (UserException exception) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Could not upload picture to user " + exception.getMessage());
    }

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @DeleteMapping("{username}/pictures")
  @RequiresProtection(userIdentifierFrom = "path", userIdentifierParamName = "users")
  public ResponseEntity<String> deletePicture(@PathVariable String username) {
    User user = userService.retrieveUser(username);
    if (user == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("There is no such user.");
    }

    userService.deletePictureOfUser(user);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @DeleteMapping("{username}")
  @RequiresProtection(userIdentifierFrom = "path", userIdentifierParamName = "users")
  public ResponseEntity<String> deleteUser(@PathVariable String username) {
    userService.deleteUser(username);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping("{username}")
  @RequiresProtection(userIdentifierFrom = "path", userIdentifierParamName = "users")
  public ResponseEntity<User> retrieveUser(@PathVariable String username) {
    User user = userService.retrieveUser(username);
    if (user == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    return ResponseEntity.ok(user);
  }

  @GetMapping()
  @RequiresProtection(userIdentifierFrom = "system")
  public ResponseEntity<List<User>> retrieveUsers(@RequestParam int pageNumber, @RequestParam int pageSize) {
    List<User> users = userService.retrieveUsersPage(pageNumber, pageSize);

    return ResponseEntity.ok(users);
  }

}
