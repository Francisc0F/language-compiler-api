package francisco.languagecompiler.resource.util.observer;

public interface EventListener<T>  {
    void handleEvent(String eventType, T obj);
}
