package design.patterns.course.identity.provider.domain.user.rule;

import design.patterns.course.identity.provider.domain.entity.UserInformation;
import design.patterns.course.identity.provider.domain.rule.BusinessEvaluationRuleException;
import design.patterns.course.identity.provider.domain.rule.CompositeRule;
import org.springframework.http.HttpStatus;

public class ValidDescription extends CompositeRule<UserInformation> {

  private static final int MAX_DESCRIPTION_LENGTH = 250;

  @Override
  public void evaluate(UserInformation candidate) throws BusinessEvaluationRuleException {
    if (candidate.getDescription() != null && candidate.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
      throw new BusinessEvaluationRuleException("Description of user is not valid.", HttpStatus.BAD_REQUEST.value());
    }
  }

}
