package design.patterns.course.identity.provider.domain.rule;

public interface Rule<T> {

  public void evaluate(T candidate) throws BusinessEvaluationRuleException;

  public Rule<T> and(Rule<T> rule);

  public Rule<T> or(Rule<T> rule);

}
