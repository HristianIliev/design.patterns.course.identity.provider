package design.patterns.course.identity.provider.domain.user.rule;

import design.patterns.course.identity.provider.domain.entity.UserInformation;
import design.patterns.course.identity.provider.domain.rule.BusinessEvaluationRuleException;
import design.patterns.course.identity.provider.domain.rule.CompositeRule;
import org.springframework.http.HttpStatus;

public class ValidPassword extends CompositeRule<UserInformation> {

  private static final int MIN_PASSWORD_LENGTH = 5;
  private static final int MAX_PASSWORD_LENGTH = 16;

  @Override
  public void evaluate(UserInformation candidate) throws BusinessEvaluationRuleException {
    String password = candidate.getPassword();

    if (password != null) {
      if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
        throw new BusinessEvaluationRuleException("Password does not have correct length.", HttpStatus.BAD_REQUEST.value());
      }

      if (notContainDigit(password) || notContainUppercase(password)) {
        throw new BusinessEvaluationRuleException("Password is not in expected format.", HttpStatus.BAD_REQUEST.value());
      }
    }
  }

  private boolean notContainUppercase(String password) {
    return password.chars().noneMatch(Character::isUpperCase);
  }

  private boolean notContainDigit(String password) {
    return password.chars().noneMatch(Character::isDigit);
  }

}
