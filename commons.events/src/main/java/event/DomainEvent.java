package event;

public interface DomainEvent {

  public Class<?> type();

  public boolean isOfType(Class<?> clazz);

}
