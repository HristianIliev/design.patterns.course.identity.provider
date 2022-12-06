package design.patterns.course.identity.provider.domain.rule;

public abstract class CompositeRule<T> implements Rule<T> {

  @Override
  public Rule<T> and(Rule<T> rule) {
    return new AndRule<>(this, rule);
  }

  @Override
  public Rule<T> or(Rule<T> rule) {
    return new OrRule<>(this, rule);
  }

  private static class AndRule<T> extends CompositeRule<T> {

    private Rule<T> leftOperand;
    private Rule<T> rightOperand;

    public AndRule(Rule<T> leftOperand, Rule<T> rightOperand) {
      this.leftOperand = leftOperand;
      this.rightOperand = rightOperand;
    }

    @Override
    public void evaluate(T candidate) throws BusinessEvaluationRuleException {
      leftOperand.evaluate(candidate);
      rightOperand.evaluate(candidate);
    }

  }

  private static class OrRule<T> extends CompositeRule<T> {

    private Rule<T> leftOperand;
    private Rule<T> rightOperand;

    public OrRule(Rule<T> leftOperand, Rule<T> rightOperand) {
      this.leftOperand = leftOperand;
      this.rightOperand = rightOperand;
    }

    @Override
    public void evaluate(T candidate) throws BusinessEvaluationRuleException {
      try {
        leftOperand.evaluate(candidate);

        return;
      } catch (BusinessEvaluationRuleException ignored) {
      }

      rightOperand.evaluate(candidate);
    }

  }

}
