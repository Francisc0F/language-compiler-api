package francisco.languagecompiler.resource.service;

import francisco.languagecompiler.resource.model.BuildResultExecution;
import francisco.languagecompiler.resource.model.Execution;
import francisco.languagecompiler.resource.model.Operation;
import francisco.languagecompiler.resource.util.observer.EventListener;
import org.springframework.stereotype.Service;


@Service
public class ExecutionsService extends BaseService<Execution> implements EventListener<Operation> {

    private OperationQueueService operationQueueService;

    public ExecutionsService(OperationQueueService operationQueueService) {
        Execution a = new Execution();
        items.add(a);
        this.operationQueueService = operationQueueService;
        this.operationQueueService.getNotifier().subscribeOperationCompleted(this);
    }

    @Override
    public void handleEvent(String eventType, Operation obj) {
        BuildResultExecution execution = ((BuildResultExecution)obj.getResult()).clone();
        add(execution);
    }
}
