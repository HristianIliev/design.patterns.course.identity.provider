package design.patterns.course.identity.provider.domain.user.rule;

import design.patterns.course.identity.provider.domain.entity.UserInformation;
import design.patterns.course.identity.provider.domain.rule.BusinessEvaluationRuleException;
import design.patterns.course.identity.provider.domain.rule.CompositeRule;
import org.springframework.http.HttpStatus;

public class ProvidedRequiredUserFields extends CompositeRule<UserInformation> {

  @Override
  public void evaluate(UserInformation candidate) throws BusinessEvaluationRuleException {
    if (isBlank(candidate.getPassword()) || isBlank(candidate.getUsername()) || isBlank(candidate.getEmail())) {
      throw new BusinessEvaluationRuleException("Required fields are missing from user.", HttpStatus.BAD_REQUEST.value());
    }
  }

  private boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }

}
