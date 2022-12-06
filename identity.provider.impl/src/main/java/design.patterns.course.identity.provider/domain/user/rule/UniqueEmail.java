package design.patterns.course.identity.provider.domain.user.rule;

import design.patterns.course.identity.provider.domain.entity.User;
import design.patterns.course.identity.provider.domain.entity.UserInformation;
import design.patterns.course.identity.provider.domain.user.repository.UserRepository;
import design.patterns.course.identity.provider.domain.rule.BusinessEvaluationRuleException;
import design.patterns.course.identity.provider.domain.rule.CompositeRule;
import org.springframework.http.HttpStatus;

public class UniqueEmail extends CompositeRule<UserInformation>  {

  private UserRepository userRepository;

  public UniqueEmail(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void evaluate(UserInformation candidate) throws BusinessEvaluationRuleException {
    if (candidate.getEmail() != null) {
      User user = userRepository.findByEmail(candidate.getEmail());
      if (user != null) {
        throw new BusinessEvaluationRuleException("User with same email already exists.", HttpStatus.CONFLICT.value());
      }
    }
  }

}
