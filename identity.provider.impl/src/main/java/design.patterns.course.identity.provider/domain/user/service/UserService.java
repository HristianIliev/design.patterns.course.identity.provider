package design.patterns.course.identity.provider.domain.user.service;

import design.patterns.course.identity.provider.domain.entity.User;
import design.patterns.course.identity.provider.domain.entity.UserInformation;
import design.patterns.course.identity.provider.domain.exception.UserException;
import design.patterns.course.identity.provider.domain.rule.BusinessEvaluationRuleException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

  public void evaluateUserRegistrationRules(UserInformation userInformation) throws BusinessEvaluationRuleException;

  public void registerUser(UserInformation userInformation) throws UserException;

  public void evaluateUserUpdateRules(UserInformation userInformation) throws BusinessEvaluationRuleException;

  public void updateUser(User originalUser, UserInformation userInformation) throws UserException;

  public User retrieveUser(String username);

  public void addPictureToUser(MultipartFile picture, User user) throws UserException;

  public void deleteUser(String username);

  public List<User> retrieveUsersPage(int pageNumber, int pageSize);

  public void deletePictureOfUser(User user);

}
