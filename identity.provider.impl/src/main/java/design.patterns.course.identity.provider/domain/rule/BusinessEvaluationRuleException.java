package design.patterns.course.identity.provider.domain.rule;

import lombok.Getter;

@Getter
public class BusinessEvaluationRuleException extends Exception {

  private static final long serialVersionUID = 1L;

  private int status;

  public BusinessEvaluationRuleException(String message, int status) {
    super(message);

    this.status = status;
  }

}
