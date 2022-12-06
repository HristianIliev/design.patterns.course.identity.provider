package event.service;

import event.DomainEvent;

import java.util.ArrayList;
import java.util.List;

public class DomainEventService {

  private List<DomainEventSubscriber> subscribers;

  public DomainEventService() {
    this.subscribers = new ArrayList<>();
  }

  public void publish(DomainEvent event) throws DomainEventHandlingException {
    for (DomainEventSubscriber subscriber : subscribers) {
      if (subscriber.canHandle(event)) {
        handleEvent(event, subscriber);
      }
    }
  }

  private void handleEvent(DomainEvent event, DomainEventSubscriber subscriber) throws DomainEventHandlingException {
    subscriber.handle(event);
  }

  public void register(DomainEventSubscriber subscriber) {
    subscribers.add(subscriber);
  }

}
