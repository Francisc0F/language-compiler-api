package francisco.languagecompiler.resource.service;

import francisco.languagecompiler.resource.model.ExecutableOperation;
import francisco.languagecompiler.resource.model.Operation;
import francisco.languagecompiler.resource.util.observer.EventListener;
import francisco.languagecompiler.resource.util.observer.OperationPublisher;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;


@Service
public class OperationQueueService implements EventListener<Operation> {
    OperationPublisher notifier = new OperationPublisher("run", "complete");
    OperationRunner runner = new OperationRunner(notifier);
    private final Queue<ExecutableOperation> operationsQueue = new LinkedList<>();

    OperationQueueService() {
        notifier.subscribeOperationStarted(runner);
        notifier.subscribeOperationCompleted(this);
    }

    public void addToQueue(ExecutableOperation executableOperation) {
        if (operationsQueue.isEmpty()) {
            runNext(executableOperation);
        }
        operationsQueue.offer(executableOperation);
    }


    synchronized private void runNext(ExecutableOperation op) {
        runner.setExecutableOperation(op);
        notifier.notifyRun((Operation) op);
    }


    /**
     * handleEvent On operation complete
     *
     * @param eventType
     * @param obj
     */
    @Override
    public void handleEvent(String eventType, Operation obj) {
        System.out.println("Event Type: " + eventType + " Op: " + obj);
        onComplete(obj);
    }

    private void onComplete(Operation obj) {
        operationsQueue.remove(obj);
        if (!operationsQueue.isEmpty()) {
            ExecutableOperation nextBuild = operationsQueue.poll();
            runNext(nextBuild);
        }
    }
}
