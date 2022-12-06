package event.service;

import event.DomainEvent;

public interface DomainEventSubscriber {

  public boolean canHandle(DomainEvent event);

  public void handle(DomainEvent event) throws DomainEventHandlingException;

}
