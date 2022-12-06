package design.patterns.course.identity.provider.event;

import design.patterns.course.identity.provider.domain.entity.UserInformation;
import event.DomainEvent;

public class RegisteredUserEvent implements DomainEvent {

  private UserInformation userInformation;

  public RegisteredUserEvent(UserInformation userInformation) {
    this.userInformation = userInformation;
  }

  @Override
  public Class<?> type() {
    return getClass();
  }

  @Override
  public boolean isOfType(Class<?> clazz) {
    return clazz == getClass();
  }
}
