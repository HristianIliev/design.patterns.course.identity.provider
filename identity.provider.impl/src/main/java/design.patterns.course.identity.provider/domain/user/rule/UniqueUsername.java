package design.patterns.course.identity.provider.domain.user.rule;

import design.patterns.course.identity.provider.domain.entity.User;
import design.patterns.course.identity.provider.domain.entity.UserInformation;
import design.patterns.course.identity.provider.domain.user.repository.UserRepository;
import design.patterns.course.identity.provider.domain.rule.BusinessEvaluationRuleException;
import design.patterns.course.identity.provider.domain.rule.CompositeRule;
import org.springframework.http.HttpStatus;

public class UniqueUsername extends CompositeRule<UserInformation> {

  private UserRepository userRepository;

  public UniqueUsername(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void evaluate(UserInformation candidate) throws BusinessEvaluationRuleException {
    if (candidate.getUsername() != null) {
      User user = userRepository.findByUsername(candidate.getUsername());
      if (user != null) {
        throw new BusinessEvaluationRuleException("User with same name already exists.", HttpStatus.CONFLICT.value());
      }
    }
  }

}
