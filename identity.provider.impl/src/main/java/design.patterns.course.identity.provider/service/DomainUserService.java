package design.patterns.course.identity.provider.service;

import design.patterns.course.identity.provider.domain.entity.Role;
import design.patterns.course.identity.provider.domain.entity.User;
import design.patterns.course.identity.provider.domain.entity.UserInformation;
import design.patterns.course.identity.provider.domain.exception.UserException;
import design.patterns.course.identity.provider.domain.role.service.RoleService;
import design.patterns.course.identity.provider.domain.rule.BusinessEvaluationRuleException;
import design.patterns.course.identity.provider.domain.rule.Rule;
import design.patterns.course.identity.provider.domain.user.repository.UserRepository;
import design.patterns.course.identity.provider.domain.user.rule.*;
import design.patterns.course.identity.provider.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
public class DomainUserService implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleService roleService;

  @Override
  public void evaluateUserRegistrationRules(UserInformation userInformation) throws BusinessEvaluationRuleException {
    Rule<UserInformation> rule = new UniqueUsername(userRepository)
        .and(new UniqueEmail(userRepository))
        .and(new ValidPassword())
        .and(new ValidDescription())
        .and(new ProvidedRequiredUserFields());

    rule.evaluate(userInformation);
  }

  @Override
  public void registerUser(UserInformation userInformation) throws UserException {
    String salt;
    String password;

    try {
      salt = generateSalt();
      password = encryptPassword(userInformation.getPassword(), salt);
    } catch (Exception exception) {
      throw new UserException("Failed to generate user password.", exception);
    }

    User user = new User();

    user.setEmail(userInformation.getEmail());
    user.setUsername(userInformation.getUsername());
    user.setDescription(userInformation.getDescription());
    user.setPassword(password);
    user.setSalt(salt);
    user.setCreatedAt(LocalDateTime.now());

    userRepository.save(user);
  }

  @Override
  public void evaluateUserUpdateRules(UserInformation userInformation) throws BusinessEvaluationRuleException {
    Rule<UserInformation> rule = new UniqueUsername(userRepository)
        .and(new UniqueEmail(userRepository))
        .and(new UniqueUsername(userRepository))
        .and(new ValidDescription());

    rule.evaluate(userInformation);
  }

  @Override
  public void updateUser(User originalUser, UserInformation userInformation) throws UserException {
    if (userInformation.getEmail() != null) {
      originalUser.setEmail(userInformation.getEmail());
    }

    if (userInformation.getUsername() != null) {
      originalUser.setUsername(userInformation.getUsername());
    }

    if (userInformation.getDescription() != null) {
      originalUser.setDescription(userInformation.getDescription());
    }

    if (userInformation.getPassword() != null) {
      try {
        String newPassword = encryptPassword(userInformation.getPassword(), originalUser.getSalt());

        originalUser.setPassword(newPassword);
      } catch (Exception exception) {
        throw new UserException("Failed to generate user password.", exception);
      }
    }

    userRepository.save(originalUser);
  }

  @Override
  public User retrieveUser(String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  public void addPictureToUser(MultipartFile picture, User user) throws UserException {
    try {
      user.setPicture(picture.getBytes());
    } catch (IOException exception) {
      throw new UserException("Could not get bytes of picture", exception);
    }

    userRepository.save(user);
  }

  @Override
  public void deleteUser(String username) {
    User user = userRepository.findByUsername(username);

    for (Role role : user.getRoles()) {
      roleService.removeRoleFromUser(role.getName(), user);
    }

    userRepository.delete(user);
  }

  @Override
  public List<User> retrieveUsersPage(int pageNumber, int pageSize) {
    Pageable page = PageRequest.of(pageNumber, pageSize);

    Page<User> users = userRepository.findAll(page);

    return users.getContent();
  }

  @Override
  public void deletePictureOfUser(User user) {
    user.setPicture(new byte[] {});

    userRepository.save(user);
  }

  private String encryptPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
    String algorithm = "PBKDF2WithHmacSHA1";
    int derivedKeyLength = 160;
    int iterations = 20000;

    byte[] saltBytes = Base64.getDecoder().decode(salt);
    KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, iterations, derivedKeyLength);
    SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm);

    byte[] encodedBytes = factory.generateSecret(spec).getEncoded();
    return Base64.getEncoder().encodeToString(encodedBytes);
  }

  private String generateSalt() throws NoSuchAlgorithmException {
    SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

    byte[] salt = new byte[8];

    random.nextBytes(salt);

    return Base64.getEncoder().encodeToString(salt);
  }

}
