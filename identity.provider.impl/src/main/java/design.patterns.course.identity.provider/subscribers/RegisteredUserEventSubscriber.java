package design.patterns.course.identity.provider.subscribers;

import design.patterns.course.identity.provider.event.RegisteredUserEvent;
import event.DomainEvent;
import event.service.DomainEventHandlingException;
import event.service.DomainEventSubscriber;

public class RegisteredUserEventSubscriber implements DomainEventSubscriber {

  @Override
  public boolean canHandle(DomainEvent event) {
    return event.isOfType(RegisteredUserEvent.class);
  }

  @Override
  public void handle(DomainEvent domainEvent) throws DomainEventHandlingException {
    //TODO send interests to news team.
  }

}
