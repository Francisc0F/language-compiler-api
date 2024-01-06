package francisco.languagecompiler.resource.util.observer;


import francisco.languagecompiler.resource.model.Operation;
import francisco.languagecompiler.resource.service.OperationQueueService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 *  Observer based of https://refactoring.guru/design-patterns/observer/java/example
 *
 */
public class OperationPublisher {
    Map<String, List<EventListener>> listeners = new HashMap<>();

    public OperationPublisher(String... eventTypes) {
        for (String eventType : eventTypes) {
            this.listeners.put(eventType, new ArrayList<>());
        }
    }

    private void subscribe(String eventType, EventListener listener) {
        List<EventListener> users = listeners.get(eventType);
        users.add(listener);
    }

    public void subscribeOperationStarted(EventListener list) {
        subscribe("run", list);
    }

    public void unsubscribe(String eventType, EventListener listener) {
        List<EventListener> users = listeners.get(eventType);
        users.remove(listener);
    }

    public void notify(String eventType, Operation operation) {
        List<EventListener> users = listeners.get(eventType);
        for (EventListener listener : users) {
            if (listener != null) {
                listener.handleEvent(eventType, operation);
            }
        }
    }

    public void notifyRun(Operation op) {
        notify("run", op);
    }

    public void subscribeOperationCompleted(OperationQueueService operationQueueService) {
        subscribe("complete", operationQueueService);
    }
}
